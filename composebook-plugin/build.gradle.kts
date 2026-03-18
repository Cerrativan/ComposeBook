plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id("com.gradle.plugin-publish") version "1.3.0"
}

group = "io.github.cerrativan.composebook"
version = "0.1.1"

publishing {
    repositories {
        mavenLocal()
    }
}

gradlePlugin {

    website = "https://github.com/Cerrativan/ComposeBook/tree/master/composebook-plugin"
    vcsUrl = "https://github.com/Cerrativan/ComposeBook.git"

    plugins {
        create("composebookPlugin") {
            id = "io.github.cerrativan.composebook"
            implementationClass = "io.github.cerrativan.composebook.plugin.ComposebookPlugin"
            displayName = "ComposeBook"
            description = "Live component catalog for Jetpack Compose — annotate composables with @Page and browse them on device"
            tags = listOf("compose", "kotlin", "android", "storybook", "catalog", "ui")
        }
    }
}

dependencies {
    implementation(gradleApi())
    compileOnly("com.android.tools.build:gradle:9.0.1")
    implementation("com.google.devtools.ksp:symbol-processing-gradle-plugin:2.0.21-1.0.28")
}