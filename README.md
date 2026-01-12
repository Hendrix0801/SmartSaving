# SmartSaving - 智能储蓄管理应用

一个跨平台的智能储蓄管理应用，帮助您管理月薪、设定存钱目标，并追踪每日的收支情况。

## 📱 平台支持

- **iOS**: 使用 Swift 和 SwiftUI 开发
- **Android**: 使用 Kotlin 和 Jetpack Compose 开发

## ✨ 功能特性

- **预算管理**：设置月薪和存钱目标，查看本月支出和每日可用金额
- **记账功能**：快速添加收入和支出记录
- **支出列表**：查看所有支出记录，支持删除操作
- **收入列表**：查看所有收入记录，支持删除操作
- **统计功能**：查看本月概览和历史月份数据
- **历史查询**：支持查看任意历史月份的动账数据

## 📁 项目结构

```
SmartSaving/
├── ios/              # iOS 项目
│   ├── SmartSaving/              # 主应用
│   ├── SmartSaving.xcodeproj/   # Xcode 项目
│   ├── SmartSavingShared/       # 共享框架
│   └── SmartSavingWidget/       # Widget 扩展
├── android/         # Android 项目
│   ├── app/                     # Android 应用
│   ├── build.gradle.kts
│   └── settings.gradle.kts
└── README.md        # 本文件
```

## 🚀 快速开始

### iOS 开发

1. 打开 `ios/SmartSaving.xcodeproj`
2. 选择目标设备或模拟器
3. 运行项目 (⌘R)

### Android 开发

1. 使用 Android Studio 打开 `android/` 文件夹
2. 等待 Gradle 同步完成
3. 连接设备或启动模拟器
4. 运行项目

## 🛠 技术栈

### iOS
- **语言**: Swift
- **UI框架**: SwiftUI
- **数据持久化**: UserDefaults (通过 PersistenceShared)
- **Widget**: WidgetKit

### Android
- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构模式**: MVVM
- **数据持久化**: DataStore Preferences
- **导航**: Navigation Compose
- **状态管理**: StateFlow + ViewModel

## 📝 开发说明

### 数据模型

两个平台使用相同的数据模型：
- `Transaction`: 交易记录（收入/支出）
- `BudgetStore`: 预算存储和业务逻辑

### 数据持久化

- **iOS**: 使用 `PersistenceShared` 进行数据持久化
- **Android**: 使用 `DataStore Preferences` 和 JSON 序列化

## 📄 许可证

本项目基于原始 iOS 版本 SmartSaving 开发。

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！
