//
//  IncomeView.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/25.
//

import SwiftUI
import SmartSavingShared

struct IncomeView: View {
    @EnvironmentObject var store: BudgetStore

    var body: some View {
        NavigationView {
            List {
                if store.incomeTransactions.isEmpty {
                    ContentUnavailableView(
                        "暂无收入记录",
                        systemImage: "dollarsign.circle",
                        description: Text("添加收入后，记录会显示在这里")
                    )
                } else {
                    ForEach(store.incomeTransactions) { t in
                        HStack {
                            VStack(alignment: .leading, spacing: 4) {
                                Text(t.note ?? "收入")
                                    .font(.headline)
                                Text(DateFormatter.transactionDateFormatter.string(from: t.date))
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                            Spacer()
                            Text(String(format: "+¥ %.2f", t.amount))
                                .font(.headline)
                                .foregroundColor(.green)
                        }
                        .padding(.vertical, 4)
                    }
                    .onDelete(perform: deleteIncomes)
                }
            }
            .navigationTitle("收入")
            .toolbar {
                if !store.incomeTransactions.isEmpty {
                    ToolbarItem(placement: .navigationBarTrailing) {
                        EditButton()
                    }
                }
            }
        }
    }
    
    func deleteIncomes(at offsets: IndexSet) {
        let incomes = store.incomeTransactions
        let idsToDelete = Set(offsets.map { incomes[$0].id })
        store.transactions.removeAll { idsToDelete.contains($0.id) }
        store.saveAsync()
    }
}

