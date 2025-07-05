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
        val commonMain by getting {
            dependencies {
                api(additionals.multiplatform.http.client)
                api(additionals.kotlinx.serialization.json)
                api(additionals.multiplatform.file.access)
                api(libs.mapcompose)
                api(project(":shared-utils"))
            }
        }

        val commonTest by getting {
            dependencies {
                api(kotlin("test"))
            }
        }

        val jsMain by getting

        val androidMain by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val jvmMain by getting

        val commonCache by creating {
            dependsOn(commonMain)
            androidMain.dependsOn(this)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            jvmMain.dependsOn(this)

            dependencies {
                implementation("com.mayakapps.kache:file-kache:2.1.1")
            }
        }

        val commonCacheStub by creating {
            dependsOn(commonMain)
            jsMain.dependsOn(this)
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
    namespace = "eu.codlab.maps"

    buildFeatures {
        compose = true
    }
}
