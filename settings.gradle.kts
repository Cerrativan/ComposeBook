pluginManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "ComposeBook"
include(":composebook-annotations")
include(":composebook-processor")
include(":sampleapp")
include(":composebook-ui")
//include(":composebook-plugin")

