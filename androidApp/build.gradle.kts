@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.application")
    kotlin("android")
    alias(additionals.plugins.jetbrains.compose)
    alias(additionals.plugins.compose.compiler)
    id("jvmCompat")
    alias(libs.plugins.android.google.services)
}


val originalVersion: String by rootProject.ext
val projectVersionCode: Int by rootProject.ext

android {
    namespace = "eu.codlab.blipya"

    defaultConfig {
        applicationId = "eu.codlab.blipya"
        versionCode = projectVersionCode
        versionName = originalVersion
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/previous-compilation-data.bin"
        }
    }
    signingConfigs {
        create("release") {
            storeFile = File(project.projectDir, "release.jks")
            storePassword = "lorcana"
            keyAlias = "lorcana"
            keyPassword = "lorcana"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles.add(getDefaultProguardFile("proguard-android.txt"))
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    api(project(":shared"))
    implementation(additionals.androidx.appcompat)
    implementation(additionals.androidx.activity.compose)
    implementation(additionals.multiplatform.permissions)

    implementation(platform("androidx.compose:compose-bom:2023.01.00"))
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.animation:animation")
    implementation("androidx.compose.material:material-icons-core")
}