//
//  SmartSavingWidget.swift
//  SmartSavingWidget
//
//  Created by 刘文辉 on 2025/11/25.
//

import WidgetKit
import SwiftUI
import Combine
import Foundation
import SmartSavingShared

final class BudgetStoreShared {
    static let shared: BudgetStore = {
        return BudgetStore.load()
    }()
}

struct Provider: TimelineProvider {
    func placeholder(in context: Context) -> BudgetEntry {
        BudgetEntry(date: Date(), dailyAvailable: 100, totalSpent: 50, remainingDays: 10, progress: 0.5)
    }

    func getSnapshot(in context: Context, completion: @escaping (BudgetEntry) -> ()) {
        let entry = BudgetEntry(date: Date(), dailyAvailable: 100, totalSpent: 50, remainingDays: 10, progress: 0.5)
        completion(entry)
    }

    func getTimeline(in context: Context, completion: @escaping (Timeline<BudgetEntry>) -> ()) {
        var entries: [BudgetEntry] = []
        let currentDate = Date()
        
        let store = BudgetStoreShared.shared
        let daily = store.dailyAvailable
        let spent = store.totalSpentThisMonth
        let remaining = store.daysRemainingInMonth
        let progress = min(1.0, spent / max(1, store.monthlySalary - store.savingTarget))
        
        for offset in 0..<5 {
            let entryDate = Calendar.current.date(byAdding: .minute, value: offset * 30, to: currentDate)!
            let entry = BudgetEntry(date: entryDate,
                                    dailyAvailable: daily,
                                    totalSpent: spent,
                                    remainingDays: remaining,
                                    progress: progress)
            entries.append(entry)
        }

        let timeline = Timeline(entries: entries, policy: .atEnd)
        completion(timeline)
    }
}

struct BudgetEntry: TimelineEntry {
    let date: Date
    let dailyAvailable: Double
    let totalSpent: Double
    let remainingDays: Int
    let progress: Double // 已花 / (月薪-存钱目标)
}

struct SmartSavingWidgetEntryView: View {
    var entry: BudgetEntry
    
    // 根据进度选择颜色
    private var progressColor: Color {
        if entry.progress < 0.5 {
            return .green
        } else if entry.progress < 0.8 {
            return .orange
        } else {
            return .red
        }
    }

    var body: some View {
        Gauge(value: entry.progress) {
            // 中心内容
            VStack(spacing: 1) {
                // 每日可用金额（主要信息）
                Text("¥\(Int(entry.dailyAvailable))")
                    .font(.system(size: 15, weight: .bold, design: .rounded))
                    .foregroundColor(.primary)
                    .minimumScaleFactor(0.6)
                    .lineLimit(1)
                
                // 剩余天数
                Text("\(entry.remainingDays)天")
                    .font(.system(size: 9, weight: .medium, design: .rounded))
                    .foregroundColor(.secondary)
                    .minimumScaleFactor(0.7)
                    .lineLimit(1)
            }
        }
        .gaugeStyle(.accessoryCircular)
        .tint(progressColor)
    }
}

struct SmartSavingWidget: Widget {
    let kind: String = "SmartSavingWidget"

    var body: some WidgetConfiguration {
        StaticConfiguration(kind: kind, provider: Provider()) { entry in
            if #available(iOS 17.0, *) {
                SmartSavingWidgetEntryView(entry: entry)
                    .containerBackground(.fill.tertiary, for: .widget)
            } else {
                SmartSavingWidgetEntryView(entry: entry)
                    .padding()
                    .background()
            }
        }
        .configurationDisplayName("Smart Saving")
        .description("显示每日可用金额与预算进度")
        .supportedFamilies([.accessoryCircular])
    }
}

#Preview("SmartSavingWidget", as: .accessoryCircular) {
    SmartSavingWidget()
} timeline: {
    BudgetEntry(date: .now, dailyAvailable: 100, totalSpent: 50, remainingDays: 10, progress: 0.5)
    BudgetEntry(date: .now, dailyAvailable: 120, totalSpent: 60, remainingDays: 9, progress: 0.6)
}
