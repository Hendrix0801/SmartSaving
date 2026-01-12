//
//  Transaction.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/27.
//

// Transaction.swift
import Foundation

public enum TransactionType: String, Codable {
    case expense = "expense"  // 支出
    case income = "income"     // 收入
}

public struct Transaction: Identifiable, Codable {
    public var id: UUID
    public var date: Date
    public var amount: Double
    public var note: String?
    public var type: TransactionType

    public init(id: UUID = UUID(), date: Date = Date(), amount: Double, note: String? = nil, type: TransactionType = .expense) {
        self.id = id
        self.date = date
        self.amount = amount
        self.note = note
        self.type = type
    }
    
    // 自定义 Codable 实现，确保向后兼容
    enum CodingKeys: String, CodingKey {
        case id, date, amount, note, type
    }
    
    public init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(UUID.self, forKey: .id)
        date = try container.decode(Date.self, forKey: .date)
        amount = try container.decode(Double.self, forKey: .amount)
        note = try container.decodeIfPresent(String.self, forKey: .note)
        // 如果旧数据没有 type 字段，默认为支出
        type = try container.decodeIfPresent(TransactionType.self, forKey: .type) ?? .expense
    }
    
    public func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        try container.encode(date, forKey: .date)
        try container.encode(amount, forKey: .amount)
        try container.encodeIfPresent(note, forKey: .note)
        try container.encode(type, forKey: .type)
    }
}
