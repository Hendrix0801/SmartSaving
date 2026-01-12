# 项目结构重组方案

## 当前结构
```
SmartSaving/
├── SmartSaving/              # iOS 主应用
├── SmartSaving.xcodeproj/     # Xcode 项目
├── SmartSavingShared/         # iOS 共享框架
├── SmartSavingWidget/        # iOS Widget
├── SmartSavingAndroid/        # Android 项目
│   ├── app/
│   ├── build.gradle.kts
│   └── settings.gradle.kts
└── .gitignore
```

## 建议的新结构
```
SmartSaving/
├── ios/                       # iOS 项目目录
│   ├── SmartSaving/          # iOS 主应用
│   ├── SmartSaving.xcodeproj/ # Xcode 项目
│   ├── SmartSavingShared/     # iOS 共享框架
│   └── SmartSavingWidget/    # iOS Widget
├── android/                   # Android 项目目录
│   ├── app/                   # Android 应用
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── README.md
├── README.md                  # 项目总说明
└── .gitignore
```

## 重组步骤

### 方法一：使用 PowerShell 脚本（推荐）
1. 在项目根目录运行：
   ```powershell
   .\reorganize_structure.ps1
   ```

### 方法二：手动重组
1. 创建 `ios/` 和 `android/` 文件夹
2. 移动 iOS 相关文件到 `ios/`：
   - SmartSaving/
   - SmartSaving.xcodeproj/
   - SmartSavingShared/
   - SmartSavingWidget/
   - SmartSavingWidgetExtension.entitlements
3. 移动 Android 相关文件到 `android/`：
   - 将 SmartSavingAndroid/ 内的所有内容移动到 android/

## 重组后的操作

### iOS 项目
1. 在 Xcode 中打开 `ios/SmartSaving.xcodeproj`
2. 检查文件路径引用是否正确
3. 如果路径有问题，在 Xcode 中重新添加文件引用

### Android 项目
1. 在 Android Studio 中打开 `android/` 文件夹
2. 等待 Gradle 同步完成
3. 检查 `settings.gradle.kts` 中的路径是否正确

## 优势

✅ **清晰的组织结构**：iOS 和 Android 代码分离，便于管理  
✅ **统一的仓库**：保持单仓库，便于同步开发和版本管理  
✅ **易于扩展**：未来可以添加 shared/ 文件夹存放共享文档、设计资源等  
✅ **符合常见实践**：大多数跨平台项目都采用这种结构

## 注意事项

⚠️ **Xcode 项目**：移动后需要在 Xcode 中重新打开项目，检查文件引用  
⚠️ **Git 历史**：如果使用 Git，文件移动会被识别为删除+新增，可以使用 `git mv` 保留历史  
⚠️ **构建路径**：确保所有构建脚本和 CI/CD 配置中的路径都已更新
