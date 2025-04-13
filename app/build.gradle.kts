import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    id("kotlin-kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "tech.movies.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "tech.movies.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProps = Properties().apply {
            val file = rootProject.file("local.properties")
            if (file.exists()) load(file.inputStream())
        }
        val apiKey = requireNotNull(localProps["API_KEY"] as? String) {
            "API_KEY is missing in local.properties, generate your api key from tmdb.org"
        }
        buildConfigField("String", "API_KEY", "\"${apiKey}\"")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {

    // AndroidX Core Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Jetpack Compose Dependencies
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Compose Testing & Debugging
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit for Networking
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.http.logging)

    // Coil for Image Loading in Compose
    implementation(libs.coil.compose)

    // Kotlin Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Dependency Injection with Dagger
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // compose-navigation
    implementation(libs.androidx.navigation.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.kotlinx.metadata.jvm)
}