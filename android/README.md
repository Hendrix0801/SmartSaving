# SmartSaving Android

智能储蓄管理应用的Android版本，使用Kotlin和Jetpack Compose开发。

## 功能特性

- **预算管理**：设置月薪和存钱目标，查看本月支出和每日可用金额
- **记账功能**：快速添加收入和支出记录
- **支出列表**：查看所有支出记录，支持删除操作
- **收入列表**：查看所有收入记录，支持删除操作
- **统计功能**：查看本月概览和最近动帐记录

## 技术栈

- **语言**：Kotlin
- **UI框架**：Jetpack Compose
- **架构模式**：MVVM
- **数据持久化**：DataStore Preferences
- **导航**：Navigation Compose
- **状态管理**：StateFlow + ViewModel

## 项目结构

```
app/
├── src/main/
│   ├── java/com/smartsaving/android/
│   │   ├── data/              # 数据模型和存储
│   │   │   ├── Transaction.kt
│   │   │   ├── BudgetStore.kt
│   │   │   └── PersistenceManager.kt
│   │   ├── viewmodel/         # ViewModel层
│   │   │   └── BudgetViewModel.kt
│   │   ├── ui/                # UI界面
│   │   │   ├── BudgetView.kt
│   │   │   ├── TransactionsView.kt
│   │   │   ├── ExpensesListView.kt
│   │   │   ├── IncomeView.kt
│   │   │   └── StatsView.kt
│   │   ├── ui/theme/          # 主题配置
│   │   │   ├── Color.kt
│   │   │   ├── Theme.kt
│   │   │   └── Type.kt
│   │   ├── util/              # 工具类
│   │   │   └── DateFormatter.kt
│   │   └── MainActivity.kt    # 主Activity
│   └── res/                   # 资源文件
```

## 构建要求

- Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17
- Android SDK 24 或更高版本
- Gradle 8.2 或更高版本

## 构建和运行

1. 克隆项目到本地
2. 使用Android Studio打开项目
3. 等待Gradle同步完成
4. 连接Android设备或启动模拟器
5. 点击运行按钮或使用快捷键 `Shift+F10` (Windows/Linux) 或 `Ctrl+R` (Mac)

## 配置

应用会自动保存数据到本地存储，无需额外配置。

## 特性说明

### 预算管理
- 设置月薪和存钱目标
- 自动计算本月支出总额
- 计算每日可用金额（基于剩余天数）
- 支持清空本月所有记录

### 记账功能
- 快速切换收入和支出类型
- 输入金额和备注（可选）
- 自动保存交易记录

### 数据持久化
- 使用DataStore Preferences保存数据
- JSON序列化存储交易记录
- 自动加载上次保存的数据

## 注意事项

- 应用需要Android 7.0 (API 24) 或更高版本
- 数据存储在本地，卸载应用会清除所有数据
- 建议定期备份重要数据

## 许可

本项目基于原始iOS版本SmartSaving开发。
