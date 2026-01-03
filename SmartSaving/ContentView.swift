//
//  ContentView.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/25.
//

import SwiftUI
import SmartSavingShared

struct ContentView: View {
    var body: some View {
        TabView {
            BudgetView()
                .tabItem { Label("预算", systemImage: "chart.pie.fill") }

            TransactionsView()
                .tabItem { Label("记账", systemImage: "minus.circle.fill") }
            
            ExpensesListView()
                .tabItem { Label("支出", systemImage: "arrow.down.circle.fill") }
            
            IncomeView()
                .tabItem { Label("收入", systemImage: "arrow.up.circle.fill") }

            StatsView()
                .tabItem { Label("统计", systemImage: "list.bullet") }
        }
    }
}
