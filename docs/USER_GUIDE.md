# MoreBees User Guide

This guide is for server administrators who want to install, configure, and use the MoreBees plugin.

## Table of Contents

- [Installation](#installation)
- [Commands & Permissions](#commands--permissions)
- [Features](#features)
- [Gameplay Mechanics](#gameplay-mechanics)
- [Tips & Best Practices](#tips--best-practices)
- [Troubleshooting](#troubleshooting)

## Installation

### Requirements

- **Minecraft Version**: 1.21.4 or higher
- **Server Software**: Paper (recommended) or compatible fork
- **Java Version**: 21 or higher

### Required Dependencies

These plugins **must** be installed for MoreBees to work:

1. **[RecipesAPI](https://github.com/Traqueur-dev/RecipesAPI)** - Custom crafting system
2. **[Structura](https://github.com/Traqueur-dev/Structura)** - Configuration framework

### Optional Dependencies

These plugins add extra features but are not required:

- **[ModelEngine](https://mythiccraft.io/index.php?resources/model-engine%E2%80%94ultimate-entity-model-manager-1-16-5-1-20-4.389/)** - 3D bee models
- **[ItemsAdder](https://www.spigotmc.org/resources/itemsadder.73355/)** - Custom items and blocks
- **[Oraxen](https://www.spigotmc.org/resources/oraxen.72448/)** - Custom items and blocks
- **[Nexo](https://www.spigotmc.org/resources/nexo.115641/)** - Custom items and blocks

### Installation Steps

1. Download MoreBees jar from releases
2. Place `MoreBees-x.x.x.jar` in your `plugins/` folder
3. Install required dependencies in `plugins/` folder
4. Restart your server
5. Check console for successful load: `[MoreBees] Plugin enabled`
6. Configuration files will be generated in `plugins/MoreBees/`

### File Structure

After first run, you'll have:

```
plugins/MoreBees/
├── config.yml          # Main configuration (bee types, tools)
├── breeds.yml          # Breeding and mutation rules
├── upgrades.yml        # Beehive upgrade definitions
└── messages.yml        # Translatable messages
```

## Commands & Permissions

### Command Overview

All commands start with `/morebees` (aliases: `/bees`, `/mb`, `/bee`)

| Command                                               | Description             | Permission                 |
|-------------------------------------------------------|-------------------------|----------------------------|
| `/morebees egg <player> <beetype> [amount]`           | Give bee spawn egg      | `morebees.command.egg`     |
| `/morebees spawn <beetype> [baby]`                    | Spawn bee at cursor     | `morebees.command.spawn`   |
| `/morebees honey <player> <beetype> <block> [amount]` | Give honey items        | `morebees.command.honey`   |
| `/morebees tool <player> <tool>`                      | Give bee tool (jar/box) | `morebees.command.tool`    |
| `/morebees upgrade <player> <upgrade>`                | Give beehive upgrade    | `morebees.command.upgrade` |
| `/morebees reload`                                    | Reload configuration    | `morebees.command.reload`  |

### Permission Details

#### Admin Permissions

```yaml
morebees.command.egg: true       # Give spawn eggs
morebees.command.spawn: true     # Spawn bees
morebees.command.honey: true     # Give honey items
morebees.command.tool: true      # Give tools
morebees.command.upgrade: true   # Give upgrades
morebees.command.reload: true    # Reload config
```

#### Wildcard Permission

```yaml
morebees.*: true    # All MoreBees permissions
```

### Command Examples

```bash
# Give a redstone bee egg to Steve
/morebees egg Steve redstone-bee

# Give 10 diamond bee eggs to Alex
/morebees egg Alex diamond-bee 10

# Spawn a baby emerald bee at your location
/morebees spawn emerald-bee true

# Give Steve redstone honeycomb (not block)
/morebees honey Steve redstone-bee false

# Give Alex 5 redstone honey blocks
/morebees honey Alex redstone-bee true 5

# Give a bee jar to Steve
/morebees tool Steve BEE_JAR

# Give a bee box to Alex
/morebees tool Alex BEE_BOX

# Give level-1 upgrade to Steve
/morebees upgrade Steve level-1

# Reload all configuration files
/morebees reload
```

## Features

### 1. Custom Bee Types

MoreBees allows you to create unlimited bee types with unique properties:

- **Custom Names**: MiniMessage color support
- **Custom Foods**: What items players can feed bees
- **Custom Flowers**: What blocks bees pollinate
- **Custom Products**: Honey and additional items
- **Custom Models**: ModelEngine 3D models (optional)

**Example Use Cases:**
- **Redstone Bee**: Pollinates redstone blocks, produces redstone
- **Diamond Bee**: Pollinates diamond blocks, produces diamonds
- **Ender Bee**: Pollinates end stone, produces ender pearls

### 2. Breeding System

Breed two bee types to create offspring:

- Configure parent pairs and child outcomes
- Set probability for special breeds (0.0 = never, 1.0 = always)
- Random parent inheritance as fallback

**Example:**
```yaml
breeds:
  - parents: [redstone-bee, diamond-bee]
    child: emerald-bee
    chance: 0.25  # 25% chance to get emerald-bee
```

If the special breed fails, the baby will be either a redstone-bee or diamond-bee randomly.

### 3. Mutation System

Bees can transform when collecting nectar from specific blocks:

- Trigger mutations by flying over configured blocks with nectar
- Bee drops a special item and resets nectar status
- Block is consumed in the process

**Example:**
```yaml
mutations:
  - parent: redstone-bee
    child: diamond-bee
    blocks:
      - DIAMOND_ORE
      - DEEPSLATE_DIAMOND_ORE
```

When a redstone-bee with nectar flies over diamond ore, it drops a diamond-bee egg and the ore is destroyed.

### 4. Bee Capture Tools

Capture and transport bees while preserving their data:

#### Bee Jar
- Stores **1 bee**
- Displays bee type in lore

#### Bee Box
- Stores **multiple bees** (configurable, default 10)
- Shows all captured bees in lore

**Usage:**
- **Right-click bee** with tool to capture
- **Right-click air** to release one bee
- **Sneak + Right-click air** to release all bees

### 5. Beehive Upgrades

Enhance beehives with upgrade items:

**Benefits:**
- **Increased Capacity**: More bees per hive
- **Production Multiplier**: Produce honey faster
- **Block Production**: Enable honey block creation

**Application:**
1. Craft or obtain upgrade item
2. Right-click beehive with upgrade
3. Visual display appears above hive
4. Upgrade is consumed

**Removal:**
- Sneak + Right-click beehive with empty hand
- Returns upgrade item to player
- Resets to default settings

### 6. Custom Recipes

MoreBees automatically generates recipes for all bee types:

**Auto-Generated Recipes:**
- 4 Honey → 1 Honey Block (2x2 shaped)
- 1 Honey Block → 4 Honey (shapeless)
- 1 Honey → 1 Additional Product (shapeless)

**Example for Redstone Bee:**
- 4 Redstone Honeycomb → 1 Redstone Honeycomb Block
- 1 Redstone Honeycomb Block → 4 Redstone Honeycomb
- 1 Redstone Honeycomb → 1 Redstone Ore

## Gameplay Mechanics

### Breeding Bees

1. Feed two adult bees their favorite food
2. Hearts appear, bees enter love mode
3. After mating, a baby bee spawns
4. Baby bee type is determined by:
   - Configured breed rules (if match + probability succeeds)
   - Random parent type (fallback)

**Tips:**
- Bees must be adults to breed
- Only configured foods work for feeding
- Check `breeds.yml` for possible offspring

### Mutating Bees

1. Let bee pollinate from its flower
2. Bee gets nectar status (particles visible)
3. Guide bee to fly over mutation trigger block
4. Bee drops special egg and loses nectar
5. Trigger block is destroyed

**Tips:**
- Place trigger blocks near flowers for easy mutations
- Multiple bees can mutate on same block type
- Mutation eggs can be collected and hatched

### Using Beehive Upgrades

**Default Beehive Stats:**
- Max Bees: 3
- Production Multiplier: 1.0x
- Produces Blocks: false

**With Upgrade:**
- Increases capacity (e.g., 3 → 5 bees)
- Boosts production (e.g., 1.0x → 1.5x faster)
- Enables honey block creation

**Honey Tracking:**
- Each bee type's honey is tracked separately
- Harvest with bottle to get specific honey type
- Upgraded hives produce faster and more

### Capturing Bees

**Best Practices:**
- Use Bee Jar for single valuable bees
- Use Bee Box for transporting multiple bees
- Captured bees preserve:
  - Bee type and custom name
  - Adult/baby status
  - Nectar status

**Release Behavior:**
- Released bees spawn at cursor location
- All data is restored (type, age, nectar)
- Custom models are re-applied

## Tips & Best Practices

### For Server Owners

1. **Start Small**: Begin with 2-3 bee types, expand later
2. **Balance Production**: Don't make rare resources too easy
3. **Test Breeding**: Verify breed probabilities work as intended
4. **Use Mutations Wisely**: Make them discoverable but not trivial
5. **Upgrade Progression**: Create tiered upgrades for gameplay progression

### For Players

1. **Build Bee Farms**: Organized flower areas for specific bees
2. **Breed Strategically**: Plan parent combinations for rare types
3. **Automate Mutations**: Create mutation stations with trigger blocks
4. **Use Tools**: Always carry a bee box for unexpected finds
5. **Upgrade Hives**: Invest in upgrades for efficient honey production

### Performance Tips

1. **Limit Bee Counts**: Too many bees can lag servers
2. **Optimize Flowers**: Don't create massive flower fields
3. **Use Chunks**: Spread bee farms across multiple chunks
4. **Monitor TPS**: Use `/spark` or similar to check performance

## Troubleshooting

### Common Issues

#### Bees Not Spawning from Eggs
**Problem**: Spawn eggs don't work
**Solution**:
- Check permission: `morebees.command.egg`
- Verify bee type exists in `config.yml`
- Check for conflicting plugins
- Try `/morebees reload`

#### Breeding Not Working
**Problem**: Bees don't enter love mode
**Solution**:
- Verify food item matches config
- Ensure bees are adults (not babies)
- Check `breeds.yml` for parent compatibility
- Look for plugin conflicts (claim plugins, mob limits)

#### Mutations Not Triggering
**Problem**: Bees fly over blocks but don't mutate
**Solution**:
- Bee must have nectar (check particles)
- Block must match `mutations.blocks` list
- Check `breeds.yml` for mutation definition
- Verify parent bee type is correct

#### Beehive Upgrades Not Applying
**Problem**: Right-clicking hive doesn't apply upgrade
**Solution**:
- Verify upgrade exists in `upgrades.yml`
- Check if hive already has an upgrade
- Ensure using correct item (check model-id)
- Try giving upgrade via command first

#### Custom Models Not Showing
**Problem**: ModelEngine models don't appear
**Solution**:
- Install ModelEngine plugin
- Verify model ID in config matches ModelEngine
- Check ModelEngine resource pack is active
- Restart server after model changes

#### Recipe Conflicts
**Problem**: Honey recipes don't work
**Solution**:
- Ensure RecipesAPI is installed and loaded first
- Check for recipe naming conflicts
- Verify custom item IDs are correct
- Try `/morebees reload` to re-register recipes

### Debug Mode

Enable debug logging in `config.yml`:

```yaml
debug: true
```

This will log:
- Bee spawn events
- Breeding calculations
- Mutation triggers
- Tool interactions
- Upgrade applications

Check console for `[MoreBees] [DEBUG]` messages.

### Getting Help

1. **Check Logs**: Look in `logs/latest.log` for errors
2. **Test Vanilla**: Try spawning normal bees first
3. **Isolate Issue**: Disable other plugins to test
4. **Update Dependencies**: Ensure all deps are latest versions
5. **Report Bugs**: Include logs, config, and steps to reproduce

## Configuration Files

For detailed configuration documentation, see [CONFIGURATION.md](CONFIGURATION.md).

Quick links:
- [config.yml](CONFIGURATION.md#configyml) - Bee types and tools
- [breeds.yml](CONFIGURATION.md#breedsyml) - Breeding and mutations
- [upgrades.yml](CONFIGURATION.md#upgradesyml) - Beehive upgrades
- [messages.yml](CONFIGURATION.md#messagesyml) - Translatable text

## Advanced Topics

### Multi-World Support

MoreBees works across all worlds automatically. Bee data is stored in:
- Entity persistent data (travels with bee)
- Item persistent data (stored in eggs/tools)
- Block persistent data (stored in beehives)

### Backups

Important files to backup:
- `plugins/MoreBees/*.yml` - All configuration
- World data (bee entities persist in chunks)
- Player inventories (if they have bee eggs/tools)

### Migration

When updating MoreBees:
1. Backup config files
2. Replace plugin jar
3. Start server (new config options generate)
4. Merge custom configs back
5. Run `/morebees reload`

## Next Steps

- **Configure Bees**: See [CONFIGURATION.md](CONFIGURATION.md)
- **Developer API**: See [API.md](API.md)
- **Integration**: See [HOOKS.md](HOOKS.md)