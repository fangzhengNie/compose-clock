# Android 闹钟应用

这是一个使用 Kotlin 和 Jetpack Compose 编写的 Android 闹钟应用，支持以下功能：

- 实时显示当前时间与日期
- 设置精确闹钟并通过通知提醒
- 查看所有已设置的闹钟
- 删除不再需要的闹钟记录

## 功能展示

- ⏰ 实时时钟界面，使用渐变背景美化
- 📅 可选择时间设置闹钟
- 🔔 使用系统通知提醒用户
- 📋 单独页面展示已设置的闹钟列表
- 🗑️ 支持一键删除任意闹钟

## 截图
![9fbdbb7453ffab0601fe531a8ccbe7d5](https://github.com/user-attachments/assets/6848a802-a86b-486c-98e9-3e941d3276f2)

![6aac5cc0637c235f5e42e942050df93d](https://github.com/user-attachments/assets/228781f8-31fe-4664-a101-68daacea3144)

![edffd9a774d8d99171bc1ef384113832](https://github.com/user-attachments/assets/30423580-5ffa-4447-bb9b-faee6396958e)





## 技术栈

- **语言**：Kotlin
- **界面**：Jetpack Compose
- **通知与闹钟**：
  - `AlarmManager` 设置精确闹钟
  - `BroadcastReceiver` 接收闹钟广播
  - `NotificationCompat` 创建系统通知
- **数据存储**：SharedPreferences 保存闹钟时间

## 项目结构

```plaintext
.
├── MainActivity.kt          // 主界面，展示时钟与设置按钮
├── AlarmReceiver.kt         // 广播接收器，触发闹钟通知
├── AlarmListActivity.kt     // 显示所有已设置闹钟，可删除
├── AlarmUtils.kt            // 保存/读取/删除闹钟的辅助函数
├── AndroidManifest.xml      // 权限与组件声明
└── res/                     // 图标与资源文件
