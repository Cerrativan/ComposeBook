package io.github.cerrativan.composebook.processor

import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.buildCodeBlock
import com.squareup.kotlinpoet.ksp.writeTo

class ComposebookProcessor(val environment: SymbolProcessorEnvironment): SymbolProcessor {

    private var isProcessed = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        if (isProcessed) return emptyList()
        isProcessed = true
        val pages = resolver.getSymbolsWithAnnotation("io.github.cerrativan.composebook.annotations.Page")
            .filterIsInstance<KSFunctionDeclaration>()

        val pagesList: MutableList<PageModel> = mutableListOf()

        pages.forEach { fn ->
            if (fn.parameters.isNotEmpty()) {
                environment.logger.error(
                    "@Page on '${fn.simpleName.asString()}' must have no parameters. " +
                    "Create a preview wrapper instead:\n\n" +
                    "  @Page(name = \"...\", group = \"...\")\n" +
                    "  fun ${fn.simpleName.asString()}Preview() {\n" +
                    "      ${fn.simpleName.asString()}(/* your data */)\n" +
                    "  }",
                    fn
                )
                return@forEach
            }
            val page = fn.annotations.first { it.shortName.asString() == "Page" }
            val name = page.arguments.first { it.name?.asString() == "name" }.value as String
            val group = page.arguments.first { it.name?.asString() == "group" }.value as String
            val content = fn.simpleName.asString()
            environment.logger.info("Found page: name:$name, group: $group, content: $content")
            pagesList.add(
                PageModel(
                    name = name,
                    group = group,
                    content = content,
                    packageName = fn.packageName.asString()
                )
            )
        }

        if (pagesList.isEmpty()) return emptyList()

        generatePagesFile(pages = pagesList.toList(), environment)
        generateActivityFile(environment)
        return emptyList()
    }

}

class ComposebookProcessorProvider: SymbolProcessorProvider {
    override fun create(
        environment: SymbolProcessorEnvironment
    ): SymbolProcessor {
        return ComposebookProcessor(environment)
    }
}

private fun generatePagesFile(pages: List<PageModel>, environment: SymbolProcessorEnvironment) {

    val pageClassName = ClassName("io.github.cerrativan.composebook.ui.model", "Page")

    val fileSpec = FileSpec.builder("io.github.cerrativan.composebook.generated", "PagesRegistry")
        .apply {
            pages.forEach { addImport(it.packageName, it.content) }
        }
        .addType(
            TypeSpec.objectBuilder("PagesRegistry")
                .addProperty(
                    PropertySpec.builder("allPages", List::class.asClassName().parameterizedBy(pageClassName))
                        .initializer(buildCodeBlock {
                            addStatement("listOf(")
                            pages.forEach {
                                addStatement("Page(name = %S, group = %S, content = { %L() }),", it.name, it.group, it.content)
                            }
                            addStatement(")")
                        }).build()
                ).build()
        ).build()

    fileSpec.writeTo(environment.codeGenerator, Dependencies.ALL_FILES)
}

private fun generateActivityFile(environment: SymbolProcessorEnvironment) {
    val file = environment.codeGenerator.createNewFile(
        Dependencies.ALL_FILES,
        "io.github.cerrativan.composebook",
        "ComposebookActivity",
        "kt"
    )
    file.bufferedWriter().use {
        it.write("""
            package io.github.cerrativan.composebook

            import android.os.Bundle
            import androidx.activity.ComponentActivity
            import androidx.activity.compose.setContent
            import io.github.cerrativan.composebook.ui.ComposeBookApp
            import io.github.cerrativan.composebook.generated.PagesRegistry

            class ComposebookActivity : ComponentActivity() {
                override fun onCreate(savedInstanceState: Bundle?) {
                    super.onCreate(savedInstanceState)
                    setContent { ComposeBookApp(PagesRegistry.allPages) }
                }
            }
        """.trimIndent())
    }
}

data class PageModel(
    val name: String,
    val group: String,
    val content: String,
    val packageName: String
)