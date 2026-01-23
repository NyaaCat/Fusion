# Fusion

A custom recipe plugin for Paper/Spigot Minecraft servers with an intuitive GUI system.

## Overview

Fusion allows server administrators to create and manage custom crafting recipes through an interactive GUI system. It supports both vanilla Minecraft items and RPGItems as recipe ingredients and results. Players can craft custom recipes, browse available recipes through an interactive UI, and search for recipes by materials or results.

## Features

- **Custom Shapeless Recipes**: Create recipes with any combination of items
- **Interactive GUI System**: Three UI modes for crafting and browsing
- **Recipe Browser**: Paginated list view with search functionality
- **RPGItems Integration**: Support for RPGItems as ingredients and results
- **Flexible Item Matching**: Configure how strictly ingredients must match
- **Hot Reload**: Update recipes without server restart
- **Multi-Language Support**: English and Chinese localizations included
- **Block Shortcut**: Optional direct crafting table click to open UI

## Requirements

- Paper/Spigot 1.21.11+
- Java 21+
- [NyaaCore](https://github.com/NyaaCat/NyaaCore) v9.10+
- LangUtils
- (Optional) [RPGItems](https://github.com/NyaaCat/RPGItems-reloaded) v3.8+ for RPGItem support

## Installation

1. Download the latest release from the releases page
2. Place `Fusion.jar` in your server's `plugins/` folder
3. Ensure NyaaCore and LangUtils are installed
4. (Optional) Install RPGItems for RPGItem recipe support
5. Restart your server
6. Configure the plugin in `plugins/Fusion/config.yml`

## Commands

### Main Command: `/fusion` (alias: `/fus`)

| Subcommand | Permission | Description |
|------------|------------|-------------|
| `craft` | `fusion.user` | Open the custom crafting table GUI |
| `list` | `fusion.user` | Open the recipe browser to browse all recipes |
| `add <name>` | `fusion.admin` | Create a new recipe from inventory layout |
| `remove <name>` | `fusion.admin` | Delete a recipe by name |
| `inspect <name>` | `fusion.admin` | Open detail UI showing a specific recipe |
| `reload` | `fusion.admin` | Reload all recipes and configuration |

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `fusion.command` | true | Access to the main `/fusion` command |
| `fusion.user` | true | User access to craft and list commands |
| `fusion.admin` | op | Admin access to recipe management (add, remove, reload, inspect) |

## Configuration

### config.yml

```yaml
# Language setting (en_US or zh_CN)
language: "en_US"

# Enable/disable plugin functionality
enabled: true

# GUI Shortcut Configuration
gui:
  shortcut:
    enabled: true              # Whether clicking crafting table opens Fusion UI
    block: CRAFTING_TABLE      # Block type that triggers the UI
    enabled_world:             # List of worlds where GUI shortcut works
      - "world"

# How to display recipes in list mode
listMode: ALL                  # ALL = show all recipes, NONE = show no recipes by default
```

## How It Works

### Creating Recipes

1. **Prepare Materials**: Arrange ingredients in your inventory's first 3x3 grid area
2. **Place Result**: Put the result item in a specific inventory slot
3. **Run Command**: Use `/fusion add <recipe_name>` to save the recipe
4. **Done**: The recipe is now available for all players

### Crafting

1. **Open Crafting UI**: Use `/fusion craft` or right-click a crafting table (if enabled)
2. **Place Ingredients**: Put items in the 3x3 crafting grid
3. **View Result**: If ingredients match a recipe, the result appears in the output slot
4. **Craft**: Click the result to craft the item (ingredients are consumed)

### Browsing Recipes

1. **Open Browser**: Use `/fusion list` to open the recipe browser
2. **Navigate**: Use page buttons to browse through recipes
3. **Search**: Filter recipes by input materials or output items
4. **View Details**: Click a recipe to see its full details

## UI System

### Crafting Table UI

A 3x3 grid interface for placing ingredients with a result slot. Real-time recipe validation shows if your ingredients match any recipe.

### Recipe Browser UI

A paginated list showing 15 recipes per page (3x5 grid). Navigate with Previous/Next page buttons. Supports searching by:
- **Material Mode**: Find recipes that use specific ingredients
- **Result Mode**: Find recipes that produce specific items

### Recipe Detail UI

Shows a single recipe's full details:
- All required ingredients displayed
- Result item shown
- Navigation between recipes
- OP users can clone items in creative mode (right-click)

## Element System

Fusion uses an extensible element system to handle different item types:

### Vanilla Elements

Standard Minecraft items are handled with flexible NBT-based matching. Items are serialized for persistence and matched based on configurable criteria.

### RPGItem Elements

When RPGItems is installed, RPGItem-based items can be used in recipes. Items are identified by their unique UID, allowing custom RPG weapons, armor, and items to be recipe ingredients or results.

## Recipe Storage

Recipes are stored as individual YAML files in `plugins/Fusion/recipes/`:

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

Each recipe file contains:
- Recipe name/identifier
- List of ingredients with NBT data
- Result item with NBT data

## Advanced Features

### Query System

Search for recipes asynchronously by:
- **Input materials**: What items are needed to craft
- **Output items**: What items the recipe produces

### Item Matching Modes

Configure how strictly ingredients must match:
- Exact matching with full NBT comparison
- Fuzzy matching for more flexible crafting

### OP Item Cloning

Server operators can clone recipe result items directly from the detail UI when in creative mode or by right-clicking.

## Building

```bash
./gradlew build
```

The compiled jar will be in `build/libs/`.

## License

This project is part of the NyaaCat plugin ecosystem.

## Links

- [NyaaCat GitHub](https://github.com/NyaaCat)
- [NyaaCore](https://github.com/NyaaCat/NyaaCore)
- [RPGItems](https://github.com/NyaaCat/RPGItems-reloaded)
