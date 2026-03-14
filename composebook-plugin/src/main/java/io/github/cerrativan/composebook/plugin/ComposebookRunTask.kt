package io.github.cerrativan.composebook.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import javax.inject.Inject

abstract class ComposebookRunTask : DefaultTask() {

    @get:Inject
    abstract val execOperations: ExecOperations

    @TaskAction
    fun run() {
        val isWindows = System.getProperty("os.name").lowercase().contains("windows")
        val gradlew = project.rootProject.projectDir.resolve(
            if (isWindows) "gradlew.bat" else "gradlew"
        )

        if (hasSourceChanged()) {
            execOperations.exec {
                commandLine(gradlew.absolutePath, ":${project.name}:installDebug")
                workingDir(project.rootProject.projectDir)
            }
        } else {
            println("Composebook: no changes detected, relaunching existing APK")
        }

        val outputMetadata = project.layout.buildDirectory.get().asFile
            .resolve("outputs/apk/debug/output-metadata.json")
        val appId = Regex(""""applicationId"\s*:\s*"([^"]+)"""")
            .find(outputMetadata.readText())
            ?.groupValues?.get(1)
            ?: throw GradleException("Could not determine applicationId from output-metadata.json")

        val adbName = if (isWindows) "adb.exe" else "adb"
        val localProperties = java.util.Properties()
        val localPropertiesFile = project.rootProject.file("local.properties")
        if (localPropertiesFile.exists()) localProperties.load(localPropertiesFile.inputStream())
        val sdkDir = localProperties.getProperty("sdk.dir")?.let { java.io.File(it) }
            ?: System.getenv("ANDROID_HOME")?.let { java.io.File(it) }
            ?: System.getenv("ANDROID_SDK_ROOT")?.let { java.io.File(it) }
            ?: throw GradleException("Android SDK not found. Add sdk.dir to local.properties.")
        val adb = sdkDir.resolve("platform-tools/$adbName")

        val connectedDevices = java.io.ByteArrayOutputStream().also { output ->
            execOperations.exec {
                commandLine(adb.absolutePath, "devices")
                standardOutput = output
            }
        }.toString().lines().drop(1).filter { it.isNotBlank() }

        if (connectedDevices.isEmpty()) {
            throw GradleException(
                "Composebook: no device or emulator connected. " +
                "Start an emulator or connect a device and try again."
            )
        }

        execOperations.exec {
            commandLine(
                adb.absolutePath, "shell", "am", "start",
                "-n", "$appId/io.github.cerrativan.composebook.ComposebookActivity"
            )
        }
    }

    private fun hasSourceChanged(): Boolean {
        val hashFile = project.layout.buildDirectory.get().asFile.resolve("composebook.hash")

        val currentHash = (project.fileTree(project.projectDir)
            .filter { it.extension == "kt" && it.readText().contains("@Page") }
            .map { it.readText() }
            .joinToString() + VERSION)
            .hashCode()
            .toString()

        val previousHash = if (hashFile.exists()) hashFile.readText() else ""

        return if (currentHash != previousHash) {
            hashFile.parentFile.mkdirs()
            hashFile.writeText(currentHash)
            true
        } else {
            false
        }
    }
}
