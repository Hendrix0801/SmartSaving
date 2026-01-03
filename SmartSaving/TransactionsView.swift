//
//  TransactionsView.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/25.
//

import SwiftUI
import SmartSavingShared

struct TransactionsView: View {
    @EnvironmentObject var store: BudgetStore
    @State private var transactionType: TransactionType = .expense
    @State private var amountText: String = ""
    @State private var noteText: String = ""
    @FocusState private var isAmountFocused: Bool
    @FocusState private var isNoteFocused: Bool
    @State private var buttonScale: CGFloat = 1.0
    
    private var isButtonDisabled: Bool {
        amountText.isEmpty || Double(amountText) == nil || Double(amountText)! <= 0
    }
    
    private var buttonText: String {
        transactionType == .income ? "添加收入" : "添加支出"
    }

    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("新增动帐")) {
                    // 收入/支出选择
                    HStack(spacing: 12) {
                        Button {
                            withAnimation(.easeInOut(duration: 0.2)) {
                                transactionType = .income
                            }
                        } label: {
                            HStack {
                                Spacer()
                                Text("收入")
                                    .foregroundColor(.green)
                                    .fontWeight(transactionType == .income ? .semibold : .regular)
                                Spacer()
                            }
                            .padding(.vertical, 10)
                            .background(transactionType == .income ? Color.green.opacity(0.15) : Color.clear)
                            .cornerRadius(8)
                        }
                        .buttonStyle(.plain)
                        
                        Button {
                            withAnimation(.easeInOut(duration: 0.2)) {
                                transactionType = .expense
                            }
                        } label: {
                            HStack {
                                Spacer()
                                Text("支出")
                                    .foregroundColor(.red)
                                    .fontWeight(transactionType == .expense ? .semibold : .regular)
                                Spacer()
                            }
                            .padding(.vertical, 10)
                            .background(transactionType == .expense ? Color.red.opacity(0.15) : Color.clear)
                            .cornerRadius(8)
                        }
                        .buttonStyle(.plain)
                    }
                    
                    TextField("金额", text: $amountText)
                        .keyboardType(.decimalPad)
                        .focused($isAmountFocused)
                        .submitLabel(.next)
                    
                    TextField("备注（可选）", text: $noteText)
                        .focused($isNoteFocused)
                        .submitLabel(.done)
                        .textInputAutocapitalization(.never)
                        .autocorrectionDisabled()
                        .onSubmit {
                            if !amountText.isEmpty, let amt = Double(amountText), amt > 0 {
                                addTransaction()
                            }
                        }
                }
                
                Section {
                    Button {
                        // 立即触发视觉反馈
                        withAnimation(.easeInOut(duration: 0.1)) {
                            buttonScale = 0.95
                        }
                        // 在主线程执行操作
                        Task { @MainActor in
                            addTransaction()
                            // 恢复按钮状态
                            try? await Task.sleep(nanoseconds: 100_000_000) // 0.1秒
                            withAnimation(.easeInOut(duration: 0.1)) {
                                buttonScale = 1.0
                            }
                        }
                    } label: {
                        HStack {
                            Spacer()
                            Text(buttonText)
                                .fontWeight(.medium)
                            Spacer()
                        }
                        .scaleEffect(buttonScale)
                    }
                    .disabled(isButtonDisabled)
                }
            }
            .navigationTitle("记账")
            .scrollDismissesKeyboard(.interactively)
        }
    }

    @MainActor
    func addTransaction() {
        // 先验证输入
        guard let amt = Double(amountText), amt > 0 else {
            return
        }
        
        // 收起键盘
        isAmountFocused = false
        isNoteFocused = false
        
        // 添加交易
        let note = noteText.trimmingCharacters(in: .whitespacesAndNewlines)
        let defaultNote = transactionType == .income ? "收入" : "支出"
        store.addTransaction(amount: amt, note: note.isEmpty ? defaultNote : note, type: transactionType)
        
        // 清空输入
        amountText = ""
        noteText = ""
    }
}

extension View {
    func hideKeyboard() {
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder),
                                        to: nil, from: nil, for: nil)
    }
}
