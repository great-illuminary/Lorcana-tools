import SwiftUI
import GoogleSignIn
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {

  func application(_ application: UIApplication,
                   didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
    FirebaseApp.configure()
    // KMPAuth showed an interesting way to have the same Android's Application effect -> AppInitializer is an object in KMP
    // AppInitializer.shared.onApplicationStart()

    return true
  }

  func application(
        _ app: UIApplication,
        open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]
  ) -> Bool {
    var handled: Bool

    handled = GIDSignIn.sharedInstance.handle(url)
    if handled {
      return true
    }

    // Handle other custom URL types.

    // If not handled by this app, return false.
    return false
  }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate

    var body: some Scene {
        let currentSystemScheme = UITraitCollection.current.userInterfaceStyle
        WindowGroup {
            VStack {
                ContentView()
            }.frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(
                    (currentSystemScheme == .dark ? Color.black : Color.white).ignoresSafeArea()
                )
        }
    }
}
