package io.github.cerrativan.composebook.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

internal const val VERSION = "0.1.0-SNAPSHOT"

class ComposebookPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply("com.google.devtools.ksp")

        target.dependencies.add("kspDebug", "io.github.cerrativan.composebook:composebook-processor:$VERSION")
        target.dependencies.add("debugImplementation", "io.github.cerrativan.composebook:composebook-ui:$VERSION")
        target.dependencies.add("implementation", "io.github.cerrativan.composebook:composebook-annotations:$VERSION")

        target.tasks.register("composebookRun", ComposebookRunTask::class.java) {
            group = "composebook"
            description = "Runs the ComposeBook catalog"
        }
    }
}
