buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath(kotlin("gradle-plugin", version = "1.5.21"))
    }
}

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.hackgt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hackgt"
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
}

dependencies {
    //  implementation (libs.sceneform.android.sdk)
    implementation(libs.play.services.fitness)
    implementation(libs.play.services.location)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation (libs.core)
    implementation (libs.sceneform.ux)
    // implementation("com.google.ar.sceneform.ux:sceneform-ux:1.16.0")
    // implementation("com.gorisse.thomas.sceneform:sceneform:1.23.0")
    implementation("de.javagl:obj:0.2.1")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("com.google.ar:core:1.45.0")
    implementation(libs.assets)
    implementation(libs.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}