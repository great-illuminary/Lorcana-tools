import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.android.library)
    alias(additionals.plugins.kotlin.cocoapods)
    alias(additionals.plugins.jetbrains.compose)
    alias(additionals.plugins.compose.compiler)
    alias(additionals.plugins.kotlin.serialization)
    alias(libs.plugins.libraries.report)
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

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    cocoapods {
        summary = "Shared implementation"
        homepage = "todo"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
            linkerOpts("-ld_classic")
        }
        pod("GoogleSignIn") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("Sentry")
        pod("FirebaseAuth") {
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                api(compose.components.resources)
                api(libs.kotlinx.serialization.json)

                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)
                implementation(libs.moko.viewmodel)
                implementation(libs.moko.viewmodel.compose)
                implementation(libs.file.picker)
                implementation(additionals.multiplatform.moko.resources.ext)

                implementation(additionals.libraries.report)

                api(additionals.multiplatform.precompose)
                api(additionals.multiplatform.safearea)
                api(additionals.multiplatform.collapsing.toolbar)
                api(additionals.multiplatform.widgets.compose)
                api(additionals.multiplatform.viewpager)
                api(additionals.multiplatform.viewmodel)
                api(additionals.multiplatform.platform)
                // api(libs.kamel.image)
                api(libs.kamel.image.default)
                //api(additionals.multiplatform.kamel.image.ext)
                api(additionals.multiplatform.file.access)
                api(additionals.multiplatform.http.client)
                api(libs.koalaplot)

                api(libs.bignum)
                api(libs.lorcana)

                api(libs.kmp.auth.google)
                api(libs.kmp.auth.firebase)
                api(libs.kmp.auth.uihelper)

                api(libs.hotpreview)

                api(additionals.multiplatform.sentry)

                api(project(":shared-design"))
                api(project(":shared-utils"))
                api(project(":shared-buildkonfig"))
                api(project(":shared-math"))
                api(project(":shared-dispatchers"))
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
                // and issue exists in the 1.7.2-alpha01 so using 03 on the web
                // but the 03 is faulty in non js env -> we can then just override how it works
                api(additionals.multiplatform.precompose.web)
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
    publicResClass = true
    packageOfResClass = "eu.codlab.blipya.res"
    generateResClass = always
}

android {
    namespace = "eu.codlab.blipya.shared"

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.window)
    implementation(libs.androidx.ui.android)
}

aboutLibraries {
    registerAndroidTasks = false
    prettyPrint = true
}

val licenseCopy by tasks.registering(Copy::class) {
    dependsOn("exportLibraryDefinitions")
    from(layout.buildDirectory.file("generated/aboutLibraries/aboutLibraries.json"))
    into(layout.projectDirectory.file("src/commonMain/composeResources/files/"))

    tasks.matching { it.name.startsWith("process") && it.name.endsWith("JavaRes") }
        .forEach { it.dependsOn(this) }
}
