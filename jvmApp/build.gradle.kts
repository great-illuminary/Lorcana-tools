import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(additionals.plugins.kotlin.jvm)
    alias(additionals.plugins.jetbrains.compose)
    alias(additionals.plugins.compose.compiler)
    id("jvmCompat")
}

group = "eu.codlab.blipya"
version = "1.0"

tasks.register("buildAndNotarizeDmg") {
    dependsOn("packageReleaseDmg", "notarizeDmg")
}

dependencies {
    api(project(":shared"))
    api(libs.kotlinx.coroutines.jvm)
    api(compose.desktop.currentOs)
}

compose.desktop {
    application {
        buildTypes.release {
            proguard {
                configurationFiles.from("proguard-rules.pro")
            }
        }

        mainClass = "MainApplicationKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "eu.codlab.blipya"
            packageVersion = "1.0.0"

            windows {
                menu = true
                shortcut = true
                perUserInstall = true
                menuGroup = "Blipya"
                iconFile.set(project.file("icon.ico"))
            }

            linux {
                iconFile.set(project.file("icon.png"))
            }

            macOS {
                iconFile.set(project.file("icon.icns"))

                //https://github.com/JetBrains/compose-jb/blob/master/tutorials/Signing_and_notarization_on_macOS/README.md
                if (rootProject.ext.get("MacOSSigningSign") as Boolean) {
                    System.out.println("signing for macos ${rootProject.ext.get("MacOSNotarizationAppleID") as String}")
                    bundleID = rootProject.ext.get("MacOSbundleID") as String

                    signing {
                        sign.set(rootProject.ext.get("MacOSSigningSign") as Boolean)
                        identity.set(rootProject.ext.get("MacOSSigningIdentity") as String)

                        if (null != rootProject.ext.get("MacOSSigningKeychain")) {
                            keychain.set(rootProject.ext.get("MacOSSigningKeychain") as String)
                        }
                    }

                    appStore = rootProject.ext.get("MacOSAppStore") as Boolean

                    notarization {
                        appleID.set(rootProject.ext.get("MacOSNotarizationAppleID") as String)
                        password.set(rootProject.ext.get("MacOSNotarizationPassword") as String)

                        // optional
                        //teamId.set(rootProject.ext.get("MacOSNotarizationTeamID") as String)
                    }
                }

                infoPlist {
                    extraKeysRawXml = macExtraPlistKeys
                }
            }
        }
    }
}
val macExtraPlistKeys: String
    get() = """
<key>CFBundleLocalizations</key>
<array>
    <string>en</string>
    <string>fr</string>
</array>
<key>CFBundleURLTypes</key>
<array>
  <dict>
    <key>CFBundleURLSchemes</key>
    <array>
        <string>decktool</string>
    </array>
    <key>CFBundleURLName</key>
    <string></string>
  </dict>
</array>
"""
