plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.android.library)
    alias(additionals.plugins.jetbrains.compose)
    alias(additionals.plugins.compose.compiler)
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
                implementation(compose.material)
                implementation(compose.material3)
                api(compose.components.resources)

                api(additionals.multiplatform.safearea)
                api(additionals.multiplatform.collapsing.toolbar)
                api(additionals.multiplatform.widgets.compose)
                api(additionals.multiplatform.viewpager)
                api(additionals.multiplatform.viewmodel)
                api(additionals.multiplatform.platform)

                api(additionals.multiplatform.file.access)
                implementation(additionals.androidx.compose.material.icons.core)

                implementation(libs.hotpreview)

                api(libs.koalaplot)
                api(project(":shared-icons"))
                api(libs.kotlinx.datetime)
                api("io.github.epicarchitect:epic-calendar-compose:1.0.8") // includes pager + ranges
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        androidMain {
            dependencies {
                api(additionals.androidx.core)
                api(additionals.androidx.appcompat)
                api(additionals.androidx.activity.compose)
            }
        }

        jvmMain {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }

        jsMain {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
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

compose.resources {
    publicResClass = false
    packageOfResClass = "eu.codlab.blipya.design.res"
    generateResClass = always
}

android {
    namespace = "eu.codlab.blipya.design"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.window)
    implementation(libs.androidx.ui.android)
}
