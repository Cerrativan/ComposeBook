import java.io.File

plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.vanniktech.maven.publish)
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

mavenPublishing {
    coordinates("io.github.cerrativan.composebook", "composebook-processor", libs.versions.composebook.get())
    publishToMavenCentral()

    signAllPublications()

    pom {
        name = "ComposeBook Processor"
        description = "KSP annotation processor for ComposeBook — a live component catalog for Jetpack Compose"
        url = "https://github.com/Cerrativan/ComposeBook"
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
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    implementation(libs.ksp.symbol.processing.api)
    implementation(libs.kotlinpoet)
    implementation(libs.kotlinpoet.ksp)
    implementation(libs.composebook.annotations)
    implementation(project(":composebook-annotations"))
}
