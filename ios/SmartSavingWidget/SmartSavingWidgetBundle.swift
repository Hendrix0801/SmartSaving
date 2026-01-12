//
//  SmartSavingWidgetBundle.swift
//  SmartSavingWidget
//
//  Created by 刘文辉 on 2025/11/25.
//

import WidgetKit
import SwiftUI

@main
struct SmartSavingWidgetBundle: WidgetBundle {
    var body: some Widget {
        SmartSavingWidget()
        SmartSavingWidgetControl()
        SmartSavingWidgetLiveActivity()
    }
}
