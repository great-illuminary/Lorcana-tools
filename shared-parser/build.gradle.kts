plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.android.library)
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
                api(libs.kotlinx.serialization.json)
                api(libs.lorcana)

                implementation(libs.kotlin.grammar.lixy)
                implementation(libs.kotlin.grammar.pangoro)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(additionals.kotlinx.coroutines.test)
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
    namespace = "eu.codlab.blipya.parser"

    buildFeatures {
        compose = true
    }
}
