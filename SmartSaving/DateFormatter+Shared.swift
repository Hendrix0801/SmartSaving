//
//  DateFormatter+Shared.swift
//  SmartSaving
//
//  Created by 刘文辉 on 2025/11/25.
//

import Foundation

extension DateFormatter {
    static let transactionDateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        return formatter
    }()
    
    static let monthYearFormatter: DateFormatter = {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy年MM月"
        return formatter
    }()
}

