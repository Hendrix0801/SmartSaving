//
//  SmartSavingWidgetLiveActivity.swift
//  SmartSavingWidget
//
//  Created by 刘文辉 on 2025/11/25.
//

import ActivityKit
import WidgetKit
import SwiftUI

struct SmartSavingWidgetAttributes: ActivityAttributes {
    public struct ContentState: Codable, Hashable {
        // Dynamic stateful properties about your activity go here!
        var emoji: String
    }

    // Fixed non-changing properties about your activity go here!
    var name: String
}

struct SmartSavingWidgetLiveActivity: Widget {
    var body: some WidgetConfiguration {
        ActivityConfiguration(for: SmartSavingWidgetAttributes.self) { context in
            // Lock screen/banner UI goes here
            VStack {
                Text("Hello \(context.state.emoji)")
            }
            .activityBackgroundTint(Color.cyan)
            .activitySystemActionForegroundColor(Color.black)

        } dynamicIsland: { context in
            DynamicIsland {
                // Expanded UI goes here.  Compose the expanded UI through
                // various regions, like leading/trailing/center/bottom
                DynamicIslandExpandedRegion(.leading) {
                    Text("Leading")
                }
                DynamicIslandExpandedRegion(.trailing) {
                    Text("Trailing")
                }
                DynamicIslandExpandedRegion(.bottom) {
                    Text("Bottom \(context.state.emoji)")
                    // more content
                }
            } compactLeading: {
                Text("L")
            } compactTrailing: {
                Text("T \(context.state.emoji)")
            } minimal: {
                Text(context.state.emoji)
            }
            .widgetURL(URL(string: "http://www.apple.com"))
            .keylineTint(Color.red)
        }
    }
}

extension SmartSavingWidgetAttributes {
    fileprivate static var preview: SmartSavingWidgetAttributes {
        SmartSavingWidgetAttributes(name: "World")
    }
}

extension SmartSavingWidgetAttributes.ContentState {
    fileprivate static var smiley: SmartSavingWidgetAttributes.ContentState {
        SmartSavingWidgetAttributes.ContentState(emoji: "😀")
     }
     
     fileprivate static var starEyes: SmartSavingWidgetAttributes.ContentState {
         SmartSavingWidgetAttributes.ContentState(emoji: "🤩")
     }
}

#Preview("Notification", as: .content, using: SmartSavingWidgetAttributes.preview) {
   SmartSavingWidgetLiveActivity()
} contentStates: {
    SmartSavingWidgetAttributes.ContentState.smiley
    SmartSavingWidgetAttributes.ContentState.starEyes
}
