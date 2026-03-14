plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "io.github.cerrativan.composebook"
version = "0.1.0-SNAPSHOT"

publishing {
    repositories {
        mavenLocal()
    }
}

gradlePlugin {
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
}