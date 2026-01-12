//
//  BudgetStore.swift
//  SmartSavingShared
//
//  Created by 刘文辉 on 2025/11/27.
//

import Foundation
import SwiftUI
import Combine
#if canImport(WidgetKit)
import WidgetKit
#endif

public final class BudgetStore: ObservableObject, Codable {
    @Published public var monthlySalary: Double = 0
    @Published public var savingTarget: Double = 0
    @Published public var transactions: [Transaction] = []

    public init() {}

    // 本月支出总额
    public var totalSpentThisMonth: Double {
        let calendar = Calendar.current
        return transactions.filter { 
            $0.type == .expense && calendar.isDate($0.date, equalTo: Date(), toGranularity: .month)
        }
        .reduce(0) { $0 + $1.amount }
    }
    
    // 本月收入总额
    public var totalIncomeThisMonth: Double {
        let calendar = Calendar.current
        return transactions.filter { 
            $0.type == .income && calendar.isDate($0.date, equalTo: Date(), toGranularity: .month)
        }
        .reduce(0) { $0 + $1.amount }
    }
    
    // 所有支出交易
    public var expenseTransactions: [Transaction] {
        transactions.filter { $0.type == .expense }
    }
    
    // 所有收入交易
    public var incomeTransactions: [Transaction] {
        transactions.filter { $0.type == .income }
    }
    
    // 获取指定月份的支出总额
    public func totalSpent(forMonth date: Date) -> Double {
        let calendar = Calendar.current
        return transactions.filter {
            $0.type == .expense && calendar.isDate($0.date, equalTo: date, toGranularity: .month)
        }
        .reduce(0) { $0 + $1.amount }
    }
    
    // 获取指定月份的收入总额
    public func totalIncome(forMonth date: Date) -> Double {
        let calendar = Calendar.current
        return transactions.filter {
            $0.type == .income && calendar.isDate($0.date, equalTo: date, toGranularity: .month)
        }
        .reduce(0) { $0 + $1.amount }
    }
    
    // 获取指定月份的所有交易
    public func transactions(forMonth date: Date) -> [Transaction] {
        let calendar = Calendar.current
        return transactions.filter {
            calendar.isDate($0.date, equalTo: date, toGranularity: .month)
        }
    }
    
    // 获取所有有交易记录的月份（按时间倒序）
    public var availableMonths: [Date] {
        let calendar = Calendar.current
        let monthSet = Set(transactions.map { transaction in
            calendar.date(from: calendar.dateComponents([.year, .month], from: transaction.date))!
        })
        return Array(monthSet).sorted(by: >)
    }

    public var daysRemainingInMonth: Int {
        let calendar = Calendar.current
        let today = Date()
        guard let range = calendar.range(of: .day, in: .month, for: today),
              let last = range.last else { return 1 }
        let day = calendar.component(.day, from: today)
        // 剩余天数不包括今天，从明天开始计算
        return max(1, last - day)
    }

    public var dailyAvailable: Double {
        // 可用资金 = 月薪 + 本月收入 - 存钱目标 - 本月支出
        let availablePool = monthlySalary + totalIncomeThisMonth - savingTarget - totalSpentThisMonth
        let days = Double(max(1, daysRemainingInMonth))
        return max(0, availablePool / days)
    }

    public func addTransaction(amount: Double, note: String?, type: TransactionType = .expense) {
        let t = Transaction(date: Date(), amount: amount, note: note, type: type)
        transactions.insert(t, at: 0)
        // @Published 会自动触发更新，不需要手动调用 objectWillChange
        saveAsync()
    }

    public func removeTransactions(at offsets: IndexSet) {
        transactions.remove(atOffsets: offsets)
        // @Published 会自动触发更新
        saveAsync()
    }

    public func removeAllTransactions() {
        // 直接在主线程更新（通常已经在主线程）
        transactions.removeAll()
        // @Published 会自动触发更新
        saveAsync()
    }

    // MARK: - Codable
    enum CodingKeys: String, CodingKey {
        case monthlySalary, savingTarget, transactions
    }

    public func encode(to encoder: Encoder) throws {
        var c = encoder.container(keyedBy: CodingKeys.self)
        try c.encode(monthlySalary, forKey: .monthlySalary)
        try c.encode(savingTarget, forKey: .savingTarget)
        try c.encode(transactions, forKey: .transactions)
    }

    public required convenience init(from decoder: Decoder) throws {
        let c = try decoder.container(keyedBy: CodingKeys.self)
        let monthlySalary = try c.decode(Double.self, forKey: .monthlySalary)
        let savingTarget = try c.decode(Double.self, forKey: .savingTarget)
        let transactions = try c.decode([Transaction].self, forKey: .transactions)
        self.init()
        self.monthlySalary = monthlySalary
        self.savingTarget = savingTarget
        self.transactions = transactions
    }

    // MARK: - Persistence
    public func save() {
        PersistenceShared.saveEncodable(self, forKey: PersistenceShared.storeKey)
#if canImport(WidgetKit)
        WidgetCenter.shared.reloadTimelines(ofKind: "SmartSavingWidget")
#endif
    }
    
    // 异步保存方法（供外部调用）
    public func saveAsync() {
        DispatchQueue.global(qos: .utility).async { [weak self] in
            guard let self = self else { return }
            self.save()
        }
    }

    public static func load() -> BudgetStore {
        if let store: BudgetStore = PersistenceShared.loadDecodable(BudgetStore.self, forKey: PersistenceShared.storeKey) {
            return store
        }
        let new = BudgetStore()
        new.save()
        return new
    }
}
