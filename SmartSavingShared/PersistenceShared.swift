//
//  PersistenceShared.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/27.
//

// PersistenceShared.swift
import Foundation

public enum PersistenceShared {
    public static let appGroup = "group.smartsaving.shared"
    public static let storeKey = "BudgetStore_v1"

    public static func sharedDefaults() -> UserDefaults {
        guard let ud = UserDefaults(suiteName: appGroup) else {
            // 兜底（不推荐生产），但确保不会 crash
            return UserDefaults.standard
        }
        return ud
    }

    public static func saveEncodable<T: Encodable>(_ value: T, forKey key: String) {
        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .iso8601
        if let data = try? encoder.encode(value) {
            sharedDefaults().set(data, forKey: key)
        }
    }

    public static func loadDecodable<T: Decodable>(_ type: T.Type, forKey key: String) -> T? {
        guard let data = sharedDefaults().data(forKey: key) else { return nil }
        let decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .iso8601
        return try? decoder.decode(T.self, from: data)
    }
}
