import java.io.File

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}

mavenPublishing {
    coordinates("io.github.cerrativan.composebook", "composebook-annotations", libs.versions.composebook.get())
    publishToMavenCentral()

    signAllPublications()

    pom {
        name = "ComposeBook Annotations"
        description = "Annotations for ComposeBook — a live component catalog for Jetpack Compose"
        url = "https://github.com/cerrativan/ComposeBook"
        licenses {
            license {
                name = "Apache License 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }
        developers {
            developer {
                id = "Cerrativan"
                name = "Ivan Di Sante"
                url = "https://github.com/Cerrativan"
            }
        }
        scm {
            url = "https://github.com/Cerrativan/ComposeBook"
            connection = "scm:git:git://github.com/Cerrativan/ComposeBook.git"
            developerConnection = "scm:git:ssh://git@github.com/Cerrativan/ComposeBook.git"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
