plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.android.library)
    alias(additionals.plugins.jetbrains.compose)
    alias(additionals.plugins.compose.compiler)
    alias(additionals.plugins.kotlin.serialization)
    id("jvmCompat")
    id("iosSimulatorConfiguration")
}

kotlin {
    applyDefaultHierarchyTemplate()

    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    jvm()

    js(IR) {
        browser()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(libs.file.picker)
                implementation(libs.kotlinx.serialization.json)

                api(libs.kmp.auth.google)
                api(libs.kmp.auth.firebase)
                api(libs.kmp.auth.uihelper)
                api(libs.gitlive.firebase.analytics)

                api(project(":shared-design"))
            }
        }

        jsMain {
            dependencies {
                implementation(project(":shared-utils-kmp-buildkonfig"))
            }
        }
    }

    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.get().compilerOptions {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

android {
    namespace = "eu.codlab.blipya.utils"
}
