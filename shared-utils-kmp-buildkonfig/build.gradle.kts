import com.codingfeline.buildkonfig.compiler.FieldSpec

plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.multiplatform.buildkonfig)
    id("jvmCompat")
}

kotlin {
    applyDefaultHierarchyTemplate()

    js(IR) {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
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

buildkonfig {
    packageName = "eu.codlab.blipya.utils.kmp.buildconfig"

    defaultConfigs {
        listOf(
            "apiKey" to "APP_BLIPYA_JS_apiKey",
            "authDomain" to "APP_BLIPYA_JS_authDomain",
            "projectId" to "APP_BLIPYA_JS_projectId",
            "storageBucket" to "APP_BLIPYA_JS_storageBucket",
            "gcmSenderId" to "APP_BLIPYA_JS_gcmSenderId",
            "applicationId" to "APP_BLIPYA_JS_applicationId",
            "gaTrackingId" to "APP_BLIPYA_JS_gaTrackingId"
        ).forEach {
            buildConfigField(
                FieldSpec.Type.STRING,
                it.first,
                rootProject.extra[it.second] as String
            )
        }
    }
}
