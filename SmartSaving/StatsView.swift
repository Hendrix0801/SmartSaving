//
//  StatsView.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/25.
//

import SwiftUI
import SmartSavingShared

struct StatsView: View {
    @EnvironmentObject var store: BudgetStore

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("本月概览")) {
                    HStack {
                        Text("月薪")
                        Spacer()
                        Text(String(format: "¥ %.2f", store.monthlySalary))
                            .font(.title3)
                            .foregroundColor(.orange)
                    }
                    
                    HStack {
                        Text("本月额外收入")
                        Spacer()
                        Text(String(format: "¥ %.2f", store.totalIncomeThisMonth))
                            .font(.title3)
                            .foregroundColor(.green)
                    }
                    
                    HStack {
                        Text("已设定存钱目标")
                        Spacer()
                        Text(String(format: "¥ %.2f", store.savingTarget))
                            .font(.title3)
                            .foregroundColor(.blue)
                    }
                    
                    HStack {
                        Text("本月已花")
                        Spacer()
                        Text(String(format: "¥ %.2f", store.totalSpentThisMonth))
                            .font(.title3)
                            .foregroundColor(.red)
                    }
                }

                Section(header: Text("最近动帐")) {
                    if store.transactions.isEmpty {
                        Text("暂无动帐记录")
                            .foregroundColor(.secondary)
                    } else {
                        ForEach(store.transactions.prefix(20)) { t in
                            HStack {
                                VStack(alignment: .leading, spacing: 4) {
                                    Text(t.note ?? (t.type == .income ? "收入" : "支出"))
                                        .font(.headline)
                                    Text(DateFormatter.transactionDateFormatter.string(from: t.date))
                                        .font(.caption)
                                        .foregroundColor(.secondary)
                                }
                                Spacer()
                                if t.type == .income {
                                    Text(String(format: "+¥ %.2f", t.amount))
                                        .font(.headline)
                                        .foregroundColor(.green)
                                } else {
                                    Text(String(format: "-¥ %.2f", t.amount))
                                        .font(.headline)
                                        .foregroundColor(.red)
                                }
                            }
                            .padding(.vertical, 4)
                        }
                    }
                }
            }
            .navigationTitle("统计")
        }
    }
}
