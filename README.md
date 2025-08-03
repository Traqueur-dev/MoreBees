# 🐝 MoreBees

**A comprehensive Minecraft plugin that adds custom bee types, advanced beehive mechanics, and powerful bee management tools.**

## ✨ Features

### 🐝 Custom Bee Types
- **Multiple bee variants**: Redstone, Emerald, Diamond, Gold, Iron bees, and what you create!
- **Unique behaviors**: Each bee type has specific food preferences and flower requirements
- **Custom models**: Support for ModelEngine integration with custom 3D models
- **Breeding system**: Cross-breed different bee types to create new variants
- **Mutation mechanics**: Bees can mutate when flying over specific blocks

### 🏠 Advanced Beehive System
- **Upgradeable beehives**: Three upgrade levels with different capacities and multipliers
- **Visual upgrade displays**: See your upgrades as 3D items on beehives
- **Custom honey production**: Each bee type produces unique honey and resources
- **Automatic resource conversion**: Honey can be converted to blocks and resources

### 🛠️ Bee Management Tools
- **Bee Jar** 🍯: Capture and transport individual bees
- **Bee Box** 📦: Store up to 10 bees in a portable container
- **Easy release system**: Right-click to release bees, sneak+right-click to release all

## 🔧 Installation

1. **Download** the latest release from [GitHub Releases](https://github.com/Traqueur-Dev/MoreBees/releases) or [SpigotMC]().
2. **Place** the JAR file in your server's `plugins/` folder
3. **Restart** your server
4. **Configure** the plugin using the generated config files

## 📋 Requirements

- **Minecraft**: 1.21+
- **Server Software**: Paper, Purpur, or other Paper-based servers
- **Java**: 17+

## 🔌 Soft Dependencies

- **[ModelEngine](https://mythiccraft.io/index.php?resources/model-engine%E2%80%94ultimate-entity-model-manager-1-16-5-1-20-4.389/)**: For custom 3D bee models
- **[ItemsAdder](https://www.spigotmc.org/resources/itemsadder.73355/)**: Custom item integration
- **[Oraxen](https://www.spigotmc.org/resources/oraxen.72448/)**: Custom item integration
- **[Nexo](https://polymart.org/product/6901/nexo)**: Custom item integration

## ⚙️ Configuration

### ItemStackWrapper 

When you want to configure an itemstack in the plugin, you can use the `ItemStackWrapper` class. This class allows you to define items with custom names, lore, and other properties in a clean and structured way.
```yaml
<key>:
  material: #any material from Material enum or id from depend plugins
  name: # a custom name for the item [Optional]
  lore: # a list of strings for the item's lore [Optional]
  model-id: # Custom model ID to link with other textures [Optional]
```

In some place, custom internal placeholders will provide dynamic values.

### Main Configuration (`config.yml`)

```yaml
debug: true
fly-animation: flying # For ModelEngine, set this model in ModelEngine bees

bees:
  - type: redstone-bee
    display-name: <red>Redstone Bee
    foods: [REDSTONE]
    flowers: [REDSTONE_BLOCK]
    product:
      additional:
        material: REDSTONE_ORE
      honey:
        material: HONEYCOMB
        name: Redstone Honeycomb
        lore:
          - This is a redstone honeycomb
      honey-block:
        material: HONEYCOMB_BLOCK
        name: Redstone Honey Block
        lore:
          - This is a redstone honey block
    model: redstone-bee  # Optional: ModelEngine model
  # ... more bee types

bee-box:
  material: PAPER
  name: Bee box
  lore:
    - This is a beebox
    - '%bees%' # List of bees in the box

bee-box-size: 10 # Maximum number of bees in a bee box

bee-jar:
  material: GLASS_BOTTLE
  name: Bee jar
  lore:
    - This is a bee jar
    - '%bee%' # The bee inside the jar

beehive-lore:
  - This is a beehive patch
  - You can use it to patch your beehive
  - It will add a new lore to your beehive
  - 'production multiplier: %production-multiplier%' # production multiplier from upgrades
  - 'produce-blocks: %produce-blocks%' # whether the beehive produces blocks return the value from messages.yml
  - 'max-bees: %max-bees%' # maximum number of bees in the beehive

```

### Breeding Configuration (`breeds.yml`)

```yaml
breeds:
  - parents: [redstone-bee, diamond-bee]
    child: emerald-bee
    chance: 1.0

mutations:
  - parent: redstone-bee
    child: emerald-bee
    blocks: [REDSTONE_ORE]
```

### Upgrade Configuration (`upgrades.yml`)

```yaml
upgrades:
  - id: level-1
    max-bees: 3
    production-multiplier: 1.5
    produce-blocks: false
    item:
      material: COPPER_INGOT
      name: Level 1 Upgrade
      lore:
        - Upgrade your beehive to level 1
        - 'Max bees: %max-bees%' # Maximum number of bees
        - 'Production multiplier: %production-multiplier%' # Production multiplier
        - 'Produce blocks: %produce-blocks%' # Whether the beehive produces blocks return messages from messages.yml
```

## 🎮 Commands

| Command | Permission | Description |
|---------|------------|-------------|
| `/morebees` | `morebees.command.help` | Show help menu |
| `/morebees reload` | `morebees.command.reload` | Reload configuration |
| `/morebees egg <player> <beetype> [amount]` | `morebees.command.egg` | Give bee spawn eggs |
| `/morebees spawn <beetype> [baby]` | `morebees.command.spawn` | Spawn a bee |
| `/morebees honey <player> <beetype> <block> [amount]` | `morebees.command.honey` | Give honey items |
| `/morebees tool <player> <tool>` | `morebees.command.tool` | Give bee tools |
| `/morebees upgrade <player> <upgrade>` | `morebees.command.upgrade` | Give beehive upgrades |

**Aliases**: `/bees`, `/mb`, `/bee`

## 🎯 How to Use

### Getting Started
1. **Spawn bees** using `/morebees egg <player> <beetype>`
2. **Feed bees** with their preferred foods to breed them
3. **Place beehives** and let bees populate them
4. **Upgrade beehives** by right-clicking with upgrade items
5. **Harvest honey** using shears when beehives are full

### Bee Management
- **Capture bees**: Right-click with a Bee Jar or Bee Box
- **Release bees**: Right-click to release, sneak+right-click to release all
- **Breed bees**: Feed two adult bees to make them breed
- **Create mutations**: Let bees with nectar fly over specific blocks

### Beehive Upgrades
- **Apply upgrades**: Right-click beehive with upgrade item
- **Remove upgrades**: Sneak+right-click to retrieve upgrade
- **Visual feedback**: Upgrades appear as 3D items on beehives

## 📘 Recipes Usage: How It Works

All custom recipes in this plugin are powered by [**RecipesAPI**](https://github.com/Traqueur-Dev/RecipesAPI) — a lightweight and flexible library purpose-built to simplify and centralize the crafting system in Minecraft.

This API was developed in-house to streamline how recipes are created, registered, and managed. It supports two ways to define recipes:

* **YAML files** – perfect for server owners and developers who prefer clean, file-based configuration.
* **Java code** – ideal for dynamic recipe generation or advanced programmatic control.

With **RecipesAPI**, you can:

* Easily add, modify, or remove recipes without dealing with low-level Minecraft internals.
* Automatically validate crafting inputs and prevent invalid crafts.
* Use custom items, tags, and even advanced conditions in your recipes.

🔍 For full documentation, visit the [**GitHub Wiki**](https://github.com/Traqueur-Dev/RecipesAPI/wiki). It includes everything you need — from YAML format examples to Java integration guides — to get started and make the most of RecipesAPI.

## 🚀 Performance Optimization: Spiral Search Algorithm

Our custom bee AI uses an optimized spiral search algorithm that reduces block scanning by up to **90%**.

**[🎬 View Interactive Demo](https://traqueur-dev.github.io/MoreBees/)**

### Key Benefits:
- ⚡ **10x faster** than traditional cube scanning
- 🎯 **Early termination** when target found
- 📈 **Scales perfectly** with multiple bees
- 🛡️ **TPS-friendly** for production servers

## 🎨 ModelEngine Integration

When ModelEngine is installed:
- **Custom 3D models** replace default bee textures
- **Animated bees** with flying animations
- **Automatic scaling** for baby bees

## 🛠️ API for Developers

MoreBees provides a comprehensive API for developers:

```java
// Get the BeeManager
BeeManager beeManager = BeePlugin.getPlugin().getManager(BeeManager.class);
// or use the ServiceManager from spigot

// Spawn a custom bee
beeManager.spawnBee(location, beeType, SpawnReason.CUSTOM, false, false);

// Get bee type from entity
Optional<BeeType> beeType = beeManager.getBeeTypeFromEntity(bee);
```

### Debug Mode
Enable debug logging in `config.yml`:
```yaml
debug: true
```

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **[RecipesAPI](https://github.com/Traqueur-Dev/RecipesAPI)** - Recipe management system
- **[Structura](https://github.com/Traqueur-Dev/Structura)** - Configuration framework
- **ModelEngine** - 3D model support

**Made with ❤️ by [Traqueur_](https://github.com/Traqueur-Dev)**