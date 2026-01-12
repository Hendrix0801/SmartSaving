//
//  ExpensesListView.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/25.
//

import SwiftUI
import SmartSavingShared

struct ExpensesListView: View {
    @EnvironmentObject var store: BudgetStore

    var body: some View {
        NavigationView {
            List {
                if store.expenseTransactions.isEmpty {
                    ContentUnavailableView(
                        "暂无支出记录",
                        systemImage: "list.bullet.clipboard",
                        description: Text("添加支出后，记录会显示在这里")
                    )
                } else {
                    ForEach(store.expenseTransactions) { t in
                        HStack {
                            VStack(alignment: .leading, spacing: 4) {
                                Text(t.note ?? "支出")
                                    .font(.headline)
                                Text(DateFormatter.transactionDateFormatter.string(from: t.date))
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                            Spacer()
                            Text(String(format: "-¥ %.2f", t.amount))
                                .font(.headline)
                                .foregroundColor(.red)
                        }
                        .padding(.vertical, 4)
                    }
                    .onDelete(perform: deleteExpenses)
                }
            }
            .navigationTitle("支出")
            .toolbar {
                if !store.expenseTransactions.isEmpty {
                    ToolbarItem(placement: .navigationBarTrailing) {
                        EditButton()
                    }
                }
            }
        }
    }
    
    func deleteExpenses(at offsets: IndexSet) {
        let expenses = store.expenseTransactions
        let idsToDelete = Set(offsets.map { expenses[$0].id })
        store.transactions.removeAll { idsToDelete.contains($0.id) }
        store.saveAsync()
    }
}

