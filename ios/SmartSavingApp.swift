import SwiftUI
import Combine
import SmartSavingShared

@main
struct SmartSavingApp: App {
    @StateObject private var store = BudgetStore.load()

    var body: some Scene {
        WindowGroup {
            ContentView()
                .environmentObject(store)
        }
    }
}
