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
    @State private var selectedMonth: Date = Date()
    
    private var isCurrentMonth: Bool {
        Calendar.current.isDate(selectedMonth, equalTo: Date(), toGranularity: .month)
    }
    
    private var monthTitle: String {
        DateFormatter.monthYearFormatter.string(from: selectedMonth)
    }
    
    private var monthTransactions: [Transaction] {
        store.transactions(forMonth: selectedMonth)
    }
    
    private var monthIncome: Double {
        store.totalIncome(forMonth: selectedMonth)
    }
    
    private var monthExpense: Double {
        store.totalSpent(forMonth: selectedMonth)
    }

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("选择月份")) {
                    if !store.availableMonths.isEmpty {
                        Picker("月份", selection: $selectedMonth) {
                            ForEach(store.availableMonths, id: \.self) { month in
                                Text(DateFormatter.monthYearFormatter.string(from: month))
                                    .tag(month)
                            }
                        }
                        .pickerStyle(.menu)
                        .onAppear {
                            // 确保初始选中当前月份或第一个可用月份
                            if !store.availableMonths.contains(selectedMonth) {
                                selectedMonth = store.availableMonths.first ?? Date()
                            }
                        }
                    } else {
                        Text("暂无数据")
                            .foregroundColor(.secondary)
                    }
                }
                
                Section(header: Text(isCurrentMonth ? "本月概览" : monthTitle + "概览")) {
                    if isCurrentMonth {
                        HStack {
                            Text("月薪")
                            Spacer()
                            Text(String(format: "¥ %.2f", store.monthlySalary))
                                .font(.title3)
                                .foregroundColor(.orange)
                        }
                        
                        HStack {
                            Text("已设定存钱目标")
                            Spacer()
                            Text(String(format: "¥ %.2f", store.savingTarget))
                                .font(.title3)
                                .foregroundColor(.blue)
                        }
                    }
                    
                    HStack {
                        Text(isCurrentMonth ? "本月额外收入" : "收入")
                        Spacer()
                        Text(String(format: "¥ %.2f", monthIncome))
                            .font(.title3)
                            .foregroundColor(.green)
                    }
                    
                    HStack {
                        Text(isCurrentMonth ? "本月已花" : "支出")
                        Spacer()
                        Text(String(format: "¥ %.2f", monthExpense))
                            .font(.title3)
                            .foregroundColor(.red)
                    }
                }

                Section(header: Text(isCurrentMonth ? "最近动帐" : monthTitle + "动帐")) {
                    if monthTransactions.isEmpty {
                        Text("暂无动帐记录")
                            .foregroundColor(.secondary)
                    } else {
                        ForEach(monthTransactions) { t in
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
