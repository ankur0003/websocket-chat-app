plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.kotlinx.serializable)
    kotlin("kapt")
}

android {
    namespace = "com.example.websocketchatapp"
    compileSdk = 34
    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
        }
    }
    defaultConfig {
        applicationId = "com.example.websocketchatapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get() // Make sure to use a compatible version
    }
    configurations.all {
        resolutionStrategy.force("com.squareup:javapoet:1.13.0")
    }


}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.dagger.hilt){
        exclude(group = "com.squareup", module = "javapoet")
    }
    kapt(libs.dagger.hilt.compiler){
        exclude(group = "com.squareup", module = "javapoet")
    }
    implementation(libs.dagger.hilt.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose)
    implementation(libs.kotlin.ktor.client.core)
    implementation(libs.kotlin.ktor.client.cio)
    implementation(libs.kotlin.ktor.client.serialization)
    implementation(libs.kotlin.ktor.client.websockets)
    implementation(libs.kotlin.ktor.client.logging)
    implementation(libs.kotlin.ktor.client.content.negotiation)
//    implementation(libs.ch.logback)
    implementation(libs.kotlinx.serialization)
    implementation(libs.javapoet)
}
