# SmartSaving 项目结构重组脚本
# 将 iOS 和 Android 项目分别移动到 ios/ 和 android/ 文件夹

Write-Host "开始重组项目结构..." -ForegroundColor Green
Write-Host ""

# 检查当前目录
$currentDir = Get-Location
Write-Host "当前目录: $currentDir" -ForegroundColor Cyan

# 创建新文件夹
Write-Host "创建 ios/ 和 android/ 文件夹..." -ForegroundColor Yellow
New-Item -ItemType Directory -Path "ios" -Force | Out-Null
New-Item -ItemType Directory -Path "android" -Force | Out-Null

# 移动 iOS 相关文件
Write-Host "移动 iOS 项目文件到 ios/..." -ForegroundColor Yellow
if (Test-Path "SmartSaving") {
    Move-Item -Path "SmartSaving" -Destination "ios\" -Force
    Write-Host "  ✓ SmartSaving" -ForegroundColor Green
}
if (Test-Path "SmartSaving.xcodeproj") {
    Move-Item -Path "SmartSaving.xcodeproj" -Destination "ios\" -Force
    Write-Host "  ✓ SmartSaving.xcodeproj" -ForegroundColor Green
}
if (Test-Path "SmartSavingShared") {
    Move-Item -Path "SmartSavingShared" -Destination "ios\" -Force
    Write-Host "  ✓ SmartSavingShared" -ForegroundColor Green
}
if (Test-Path "SmartSavingWidget") {
    Move-Item -Path "SmartSavingWidget" -Destination "ios\" -Force
    Write-Host "  ✓ SmartSavingWidget" -ForegroundColor Green
}
if (Test-Path "SmartSavingWidgetExtension.entitlements") {
    Move-Item -Path "SmartSavingWidgetExtension.entitlements" -Destination "ios\" -Force
    Write-Host "  ✓ SmartSavingWidgetExtension.entitlements" -ForegroundColor Green
}

# 移动 Android 相关文件
Write-Host "移动 Android 项目文件到 android/..." -ForegroundColor Yellow
if (Test-Path "SmartSavingAndroid") {
    # 将 SmartSavingAndroid 的内容移动到 android/
    Get-ChildItem -Path "SmartSavingAndroid" | ForEach-Object {
        Move-Item -Path $_.FullName -Destination "android\" -Force
    }
    Remove-Item -Path "SmartSavingAndroid" -Force
    Write-Host "  ✓ SmartSavingAndroid (内容已移动到 android/)" -ForegroundColor Green
}

Write-Host ""
Write-Host "项目结构重组完成！" -ForegroundColor Green
Write-Host ""
Write-Host "新结构：" -ForegroundColor Cyan
Write-Host "  ios/" -ForegroundColor White
Write-Host "    ├── SmartSaving/              # iOS 主应用" -ForegroundColor Gray
Write-Host "    ├── SmartSaving.xcodeproj/    # Xcode 项目" -ForegroundColor Gray
Write-Host "    ├── SmartSavingShared/        # 共享框架" -ForegroundColor Gray
Write-Host "    └── SmartSavingWidget/        # Widget 扩展" -ForegroundColor Gray
Write-Host "  android/" -ForegroundColor White
Write-Host "    ├── app/                      # Android 应用" -ForegroundColor Gray
Write-Host "    ├── build.gradle.kts" -ForegroundColor Gray
Write-Host "    └── settings.gradle.kts" -ForegroundColor Gray
Write-Host ""
Write-Host "重要提示：" -ForegroundColor Yellow
Write-Host "1. 请在 Xcode 中重新打开 ios/SmartSaving.xcodeproj" -ForegroundColor Yellow
Write-Host "2. 检查 Xcode 项目中的文件路径引用是否正确" -ForegroundColor Yellow
Write-Host "3. 在 Android Studio 中打开 android/ 文件夹" -ForegroundColor Yellow
