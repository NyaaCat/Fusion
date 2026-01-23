# Fusion

一个具有直观 GUI 系统的自定义合成配方插件，适用于 Paper/Spigot Minecraft 服务器。

## 概述

Fusion 允许服务器管理员通过交互式 GUI 系统创建和管理自定义合成配方。它支持原版 Minecraft 物品和 RPGItems 作为配方原料和产物。玩家可以合成自定义配方、通过交互式 UI 浏览可用配方，并按材料或产物搜索配方。

## 功能特性

- **自定义无序配方**：使用任意物品组合创建配方
- **交互式 GUI 系统**：三种 UI 模式用于合成和浏览
- **配方浏览器**：带搜索功能的分页列表视图
- **RPGItems 集成**：支持 RPGItems 作为原料和产物
- **灵活的物品匹配**：配置原料匹配的严格程度
- **热重载**：无需重启服务器即可更新配方
- **多语言支持**：内置英文和中文本地化
- **方块快捷方式**：可选的直接点击工作台打开 UI

## 系统要求

- Paper/Spigot 1.21.11+
- Java 21+
- [NyaaCore](https://github.com/NyaaCat/NyaaCore) v9.10+
- LangUtils
- （可选）[RPGItems](https://github.com/NyaaCat/RPGItems-reloaded) v3.8+ 以支持 RPGItem

## 安装

1. 从发布页面下载最新版本
2. 将 `Fusion.jar` 放入服务器的 `plugins/` 文件夹
3. 确保已安装 NyaaCore 和 LangUtils
4. （可选）安装 RPGItems 以支持 RPGItem 配方
5. 重启服务器
6. 在 `plugins/Fusion/config.yml` 中配置插件

## 命令

### 主命令：`/fusion`（别名：`/fus`）

| 子命令 | 权限 | 描述 |
|--------|------|------|
| `craft` | `fusion.user` | 打开自定义工作台 GUI |
| `list` | `fusion.user` | 打开配方浏览器浏览所有配方 |
| `add <名称>` | `fusion.admin` | 从物品栏布局创建新配方 |
| `remove <名称>` | `fusion.admin` | 按名称删除配方 |
| `inspect <名称>` | `fusion.admin` | 打开详情 UI 查看特定配方 |
| `reload` | `fusion.admin` | 重新加载所有配方和配置 |

## 权限

| 权限 | 默认值 | 描述 |
|------|--------|------|
| `fusion.command` | true | 访问主命令 `/fusion` |
| `fusion.user` | true | 用户访问合成和列表命令 |
| `fusion.admin` | op | 管理员访问配方管理（添加、删除、重载、查看） |

## 配置

### config.yml

```yaml
# 语言设置（en_US 或 zh_CN）
language: "zh_CN"

# 启用/禁用插件功能
enabled: true

# GUI 快捷方式配置
gui:
  shortcut:
    enabled: true              # 点击工作台是否打开 Fusion UI
    block: CRAFTING_TABLE      # 触发 UI 的方块类型
    enabled_world:             # GUI 快捷方式生效的世界列表
      - "world"

# 如何在列表模式中显示配方
listMode: ALL                  # ALL = 显示所有配方，NONE = 默认不显示配方
```

## 工作原理

### 创建配方

1. **准备材料**：在物品栏的前 3x3 格子区域排列原料
2. **放置产物**：将产物物品放在特定的物品栏格子中
3. **执行命令**：使用 `/fusion add <配方名称>` 保存配方
4. **完成**：配方现在对所有玩家可用

### 合成

1. **打开合成 UI**：使用 `/fusion craft` 或右键点击工作台（如果启用）
2. **放置原料**：将物品放入 3x3 合成格
3. **查看产物**：如果原料匹配某个配方，产物会出现在输出格
4. **合成**：点击产物进行合成（原料会被消耗）

### 浏览配方

1. **打开浏览器**：使用 `/fusion list` 打开配方浏览器
2. **导航**：使用翻页按钮浏览配方
3. **搜索**：按输入材料或输出物品筛选配方
4. **查看详情**：点击配方查看完整详情

## UI 系统

### 工作台 UI

一个 3x3 格子界面用于放置原料，带有产物格。实时配方验证显示您的原料是否匹配任何配方。

### 配方浏览器 UI

分页列表，每页显示 15 个配方（3x5 格子）。使用上一页/下一页按钮导航。支持以下搜索方式：
- **材料模式**：查找使用特定原料的配方
- **产物模式**：查找产出特定物品的配方

### 配方详情 UI

显示单个配方的完整详情：
- 显示所有所需原料
- 显示产物物品
- 配方间导航
- OP 用户可在创造模式下复制物品（右键点击）

## 元素系统

Fusion 使用可扩展的元素系统来处理不同的物品类型：

### 原版元素

标准 Minecraft 物品通过灵活的基于 NBT 的匹配处理。物品被序列化以持久化，并根据可配置的条件进行匹配。

### RPGItem 元素

当安装了 RPGItems 时，基于 RPGItem 的物品可以用于配方。物品通过其唯一 UID 识别，允许自定义 RPG 武器、护甲和物品作为配方原料或产物。

## 配方存储

配方以单独的 YAML 文件存储在 `plugins/Fusion/recipes/` 中：

```
plugins/Fusion/
├── config.yml
├── recipes/
│   ├── my_recipe.yml
│   ├── another_recipe.yml
│   └── ...
└── lang/
    ├── en_US.yml
    └── zh_CN.yml
```

每个配方文件包含：
- 配方名称/标识符
- 带 NBT 数据的原料列表
- 带 NBT 数据的产物物品

## 高级功能

### 查询系统

异步搜索配方：
- **输入材料**：合成需要哪些物品
- **输出物品**：配方产出哪些物品

### 物品匹配模式

配置原料匹配的严格程度：
- 完整 NBT 比较的精确匹配
- 更灵活合成的模糊匹配

### OP 物品复制

服务器管理员可以在创造模式下或通过右键点击直接从详情 UI 复制配方产物物品。

## 构建

```bash
./gradlew build
```

编译后的 jar 文件将位于 `build/libs/` 目录。

## 许可证

本项目是 NyaaCat 插件生态系统的一部分。

## 链接

- [NyaaCat GitHub](https://github.com/NyaaCat)
- [NyaaCore](https://github.com/NyaaCat/NyaaCore)
- [RPGItems](https://github.com/NyaaCat/RPGItems-reloaded)
