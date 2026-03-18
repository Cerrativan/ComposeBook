package io.github.cerrativan.composebook.plugin

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

internal const val VERSION = "0.1.2"

class ComposebookPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        if (!target.plugins.hasPlugin("com.google.devtools.ksp")) {
            throw GradleException(
                "ComposeBook requires the KSP plugin. Add it to your plugins block:\n" +
                "  id(\"com.google.devtools.ksp\") version \"<your-kotlin-version>-1.0.x\""
            )
        }

        target.dependencies.add("kspDebug", "io.github.cerrativan.composebook:composebook-processor:$VERSION")
        target.dependencies.add("debugImplementation", "io.github.cerrativan.composebook:composebook-ui:$VERSION")
        target.dependencies.add("implementation", "io.github.cerrativan.composebook:composebook-annotations:$VERSION")

        target.tasks.register("composebookRun", ComposebookRunTask::class.java) {
            group = "composebook"
            description = "Runs the ComposeBook catalog"
        }
    }
}
