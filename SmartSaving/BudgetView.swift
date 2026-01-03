//
//  BudgetView.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/25.
//

import SwiftUI
import SmartSavingShared

struct BudgetView: View {
    @EnvironmentObject var store: BudgetStore
    @State private var salaryText: String = ""
    @State private var savingText: String = ""
    @State private var saveTask: Task<Void, Never>?
    @State private var showClearConfirmation = false
    @State private var buttonScale: CGFloat = 1.0
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("本月设置")) {
                    HStack {
                        Text("月薪")
                        Spacer()
                        TextField("0", text: $salaryText)
                            .multilineTextAlignment(.trailing)
                            .keyboardType(.decimalPad)
                            .onChange(of: salaryText) {
                                // 防抖：取消之前的保存任务
                                saveTask?.cancel()
                                // 延迟保存，避免频繁写入
                                saveTask = Task {
                                    try? await Task.sleep(nanoseconds: 500_000_000) // 0.5秒
                                    if !Task.isCancelled, let v = Double(salaryText) {
                                        await MainActor.run {
                                            store.monthlySalary = v
                                            store.saveAsync()
                                        }
                                    }
                                }
                            }
                    }

                    HStack {
                        Text("存钱目标")
                        Spacer()
                        TextField("0", text: $savingText)
                            .multilineTextAlignment(.trailing)
                            .keyboardType(.decimalPad)
                            .onChange(of: savingText) {
                                // 防抖：取消之前的保存任务
                                saveTask?.cancel()
                                // 延迟保存，避免频繁写入
                                saveTask = Task {
                                    try? await Task.sleep(nanoseconds: 500_000_000) // 0.5秒
                                    if !Task.isCancelled, let v = Double(savingText) {
                                        await MainActor.run {
                                            store.savingTarget = v
                                            store.saveAsync()
                                        }
                                    }
                                }
                            }
                    }
                }

                Section(header: Text("今日与本月概览")) {
                    HStack {
                        VStack(alignment: .leading) {
                            Text("已花（本月）").font(.caption)
                            Text(String(format: "¥ %.2f", store.totalSpentThisMonth))
                                .font(.title2)
                        }
                        Spacer()
                        VStack(alignment: .trailing) {
                            Text("每日可用").font(.caption)
                            Text(String(format: "¥ %.2f", store.dailyAvailable))
                                .font(.title2)
                        }
                    }
                    Text("剩余天数：\(store.daysRemainingInMonth)")
                        .font(.footnote)
                        .foregroundColor(.secondary)
                }

                Section {
                    Button {
                        // 立即触发视觉反馈
                        withAnimation(.easeInOut(duration: 0.1)) {
                            buttonScale = 0.95
                        }
                        // 显示确认对话框
                        showClearConfirmation = true
                        // 恢复按钮状态
                        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
                            withAnimation(.easeInOut(duration: 0.1)) {
                                buttonScale = 1.0
                            }
                        }
                    } label: {
                        HStack {
                            Spacer()
                            Text("清空本月记录")
                                .fontWeight(.medium)
                                .foregroundColor(.red)
                            Spacer()
                        }
                        .scaleEffect(buttonScale)
                    }
                    .buttonStyle(.plain)
                }
            }
            .alert("确认清空", isPresented: $showClearConfirmation) {
                Button("取消", role: .cancel) { }
                Button("清空", role: .destructive) {
                    clearAllTransactions()
                }
            } message: {
                Text("确定要清空所有交易记录吗？此操作无法撤销。")
            }
            .navigationTitle("预算")
            .scrollDismissesKeyboard(.interactively)
            .onAppear {
                salaryText = store.monthlySalary > 0 ? String(format: "%.0f", store.monthlySalary) : ""
                savingText = store.savingTarget > 0 ? String(format: "%.0f", store.savingTarget) : ""
            }
        }
    }
    
    @MainActor
    private func clearAllTransactions() {
        // 确保在主线程执行
        store.removeAllTransactions()
    }
}
