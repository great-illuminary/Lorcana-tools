plugins {
    alias(additionals.plugins.kotlin.multiplatform)
    alias(additionals.plugins.jetbrains.compose)
    alias(additionals.plugins.compose.compiler)
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.runtime)
                implementation(compose.html.core)
                implementation(compose.ui)
                implementation(compose.material)
                implementation(libs.skiko)
                api(additionals.multiplatform.safearea)
            }
        }
    }
}

compose.experimental {
    web.application {}
}
