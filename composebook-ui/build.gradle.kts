import java.io.File

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.vanniktech.maven.publish)
}

mavenPublishing {
    configure(com.vanniktech.maven.publish.AndroidSingleVariantLibrary("debug"))
    coordinates("io.github.cerrativan.composebook", "composebook-ui", libs.versions.composebook.get())
    publishToMavenCentral()

    signAllPublications()

    pom {
        name = "ComposeBook UI"
        description = "UI library for ComposeBook — a live component catalog for Jetpack Compose"
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

android {
    namespace = "io.github.cerrativan.composebook.ui"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
