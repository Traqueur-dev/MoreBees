# MoreBees Integration Guide

Documentation for integrating MoreBees with custom item plugins and ModelEngine.

## Table of Contents

- [Overview](#overview)
- [ModelEngine Integration](#modelengine-integration)
- [ItemsAdder Integration](#itemsadder-integration)
- [Oraxen Integration](#oraxen-integration)
- [Nexo Integration](#nexo-integration)
- [Creating Custom Hooks](#creating-custom-hooks)
- [Troubleshooting](#troubleshooting)

## Overview

MoreBees supports integration with popular custom item and model plugins through its hook system. Hooks are automatically detected and enabled when the respective plugins are present.

### Supported Plugins

| Plugin          | Purpose             | Auto-Detected |
|-----------------|---------------------|---------------|
| **ModelEngine** | 3D bee models       | Yes           |
| **ItemsAdder**  | Custom items/blocks | Yes           |
| **Oraxen**      | Custom items/blocks | Yes           |
| **Nexo**        | Custom items/blocks | Yes           |

### How Hooks Work

1. **Auto-Detection**: MoreBees checks if integration plugins are installed
2. **Initialization**: Enabled hooks are initialized on plugin startup
3. **Registration**: Hook systems register with MoreBees API
4. **Runtime**: Hooks translate custom item/block IDs to ItemStacks

## ModelEngine Integration

### Overview

ModelEngine allows you to replace bee entities with custom 3D models, providing a unique visual experience for your custom bee types.

### Requirements

- **ModelEngine R4.0.8+** installed on server
- Valid ModelEngine models registered
- Resource pack with models sent to players

### Configuration

#### 1. Global Animation Setting

In `config.yml`, set the flying animation:

```yaml
fly-animation: "fly"
```

This animation will be applied to all bees with ModelEngine models.

#### 2. Bee Type Model

In `config.yml`, specify the model for each bee type:

```yaml
bees:
  - type: "redstone-bee"
    display-name: "<red>Redstone Bee"
    model: "redstone_bee"    # ModelEngine model ID
    # ... other fields
```

**Important:** The `model` field must match the model ID registered in ModelEngine.

### Model Requirements

Your ModelEngine models should have:

- **Base Model**: Bee-shaped model (recommended: use vanilla bee as base)
- **Animations**:
  - `fly` - Flying/idle animation (required)
  - Additional animations can be added but won't be used by default
- **Hitbox**: Small hitbox matching bee size
- **Mount Point**: Not required (bees are not rideable)

### Example ModelEngine Configuration

**models/redstone_bee.yml:**
```yaml
redstone_bee:
  base_entity: BEE
  model: redstone_bee
  hitbox:
    width: 0.7
    height: 0.6
  animations:
    fly:
      loop: true
      speed: 1.0
```

### Testing

1. Register your model in ModelEngine
2. Reload ModelEngine: `/meg reload`
3. Spawn bee: `/morebees spawn redstone-bee`
4. Verify 3D model appears instead of vanilla bee

### Troubleshooting

**Model not showing:**
- Check ModelEngine is installed and loaded
- Verify model ID matches exactly (case-sensitive)
- Ensure resource pack is active on client
- Check console for ModelEngine errors

**Bee shows vanilla texture:**
- Model might not be registered in ModelEngine
- Animation name might be wrong
- Resource pack might not be loaded

**Animation not playing:**
- Check `fly-animation` setting in config.yml
- Verify animation exists in model file
- Try different animation names

## ItemsAdder Integration

### Overview

ItemsAdder integration allows you to use custom items and blocks from ItemsAdder in MoreBees configurations.

### Requirements

- **ItemsAdder 4.0.10+** installed on server
- Custom items/blocks registered in ItemsAdder

### Configuration

#### Using ItemsAdder Items

**Format:** `namespace:item_id`

**Example - Custom Food:**
```yaml
bees:
  - type: "ruby-bee"
    foods:
      - "ruby"
      - "ruby_block"
    flowers:
      - "ruby_ore"
```

**Example - Custom Product:**
```yaml
bees:
  - type: "ruby-bee"
    product:
      honey:
        material: "ruby_honey"
        name: "<red>Ruby Honey"
      additional:
        material: "ruby"
```

### ItemsAdder Item IDs

To find ItemsAdder item IDs:

1. **In-game**: `/iaget <item>` and check item NBT
2. **Config**: Check ItemsAdder's `items_packs/*.yml` files

### Examples

#### Ruby Bee with ItemsAdder Items

```yaml
bees:
  - type: "ruby-bee"
    display-name: "<dark_red>Ruby Bee"
    model-id: 5001
    foods:
      - "ruby"
      - "REDSTONE"
    flowers:
      - "ruby_ore"
      - "ruby_block"
    product:
      honey:
        material: "HONEYCOMB"
        name: "<dark_red>Ruby Honeycomb"
        model-id: 5001
      honey-block:
        material: "HONEYCOMB_BLOCK"
        name: "<dark_red>Ruby Honeycomb Block"
        model-id: 5002
      additional:
        material: "ruby"
```

#### Amethyst Bee

```yaml
bees:
  - type: "amethyst-bee"
    display-name: "<light_purple>Amethyst Bee"
    foods:
      - "amethyst_shard"
    flowers:
      - "AMETHYST_CLUSTER"
      - "amethyst_flower"
    product:
      honey:
        material: "amethyst_honey"
      additional:
        material: "AMETHYST_SHARD"
```

### Troubleshooting

**Items not recognized:**
- Verify ItemsAdder is installed and loaded before MoreBees
- Check item ID format: `namespace:item_id`
- Ensure item exists in ItemsAdder configs
- Try `/iaget <item>` to verify item ID

**Blocks not triggering:**
- ItemsAdder blocks might use different IDs
- Check ItemsAdder block format
- Test with vanilla blocks first

## Oraxen Integration

### Overview

Oraxen integration enables using Oraxen custom items and blocks in MoreBees.

### Requirements

- **Oraxen 1.191.0+** installed on server
- Custom items registered in Oraxen

### Configuration

#### Using Oraxen Items

**Format:** `item_id` (auto-detected, no prefix needed)

**Example:**
```yaml
bees:
  - type: "sapphire-bee"
    foods:
      - "sapphire"
    flowers:
      - "sapphire_ore"
    product:
      additional:
        material: "sapphire"
```

### Finding Oraxen Item IDs

1. **In-game**: `/o give <player> <item>`
2. **Config**: Check Oraxen's `items/` folder
3. **List**: `/o list` shows all Oraxen items

### Examples

#### Sapphire Bee with Oraxen

```yaml
bees:
  - type: "sapphire-bee"
    display-name: "<blue>Sapphire Bee"
    foods:
      - "sapphire"
      - "LAPIS_LAZULI"
    flowers:
      - "sapphire_ore"
      - "sapphire_block"
    product:
      honey:
        material: "HONEYCOMB"
        name: "<blue>Sapphire Honeycomb"
        model-id: 6001
      additional:
        material: "sapphire"
```

#### Custom Ore Bee

```yaml
bees:
  - type: "titanium-bee"
    display-name: "<gray>Titanium Bee"
    foods:
      - "titanium_ingot"
    flowers:
      - "titanium_ore"
    product:
      additional:
        material: "titanium_ingot"
```

### Troubleshooting

**Items not working:**
- Verify Oraxen is loaded
- Items are auto-detected, no prefix needed
- Use `/o list` to verify item exists
- Restart server after Oraxen config changes

**Recipes broken:**
- Oraxen items in recipes require exact match
- Check RecipesAPI is installed
- Verify custom model data matches

## Nexo Integration

### Overview

Nexo (formerly known as NexoItems) integration allows using Nexo custom items in MoreBees.

### Requirements

- **Nexo 1.9.0+** installed on server
- Custom items registered in Nexo

### Configuration

#### Using Nexo Items

**Format:** `item_id` (auto-detected, no prefix needed)

**Example:**
```yaml
bees:
  - type: "mythril-bee"
    foods:
      - "mythril"
    flowers:
      - "mythril_ore"
    product:
      additional:
        material: "mythril"
```

### Finding Nexo Item IDs

1. **In-game**: `/nexo give <player> <item>`
2. **Config**: Check Nexo's configuration files
3. **List**: `/nexo list` shows registered items

### Examples

#### Mythril Bee with Nexo

```yaml
bees:
  - type: "mythril-bee"
    display-name: "<aqua>Mythril Bee"
    foods:
      - "mythril"
    flowers:
      - "mythril_ore"
      - "mythril_block"
    product:
      honey:
        material: "HONEYCOMB"
        name: "<aqua>Mythril Honeycomb"
        model-id: 7001
      additional:
        material: "mythril"
```

### Troubleshooting

**Items not recognized:**
- Ensure Nexo is installed and loaded
- Items are auto-detected, no prefix needed
- Verify item exists in Nexo configs
- Restart after Nexo config changes

## Creating Custom Hooks

You can create your own hooks to integrate additional plugins with MoreBees.

### Basic Hook Implementation

```java
import fr.traqueur.morebees.api.hooks.Hook;
import fr.traqueur.morebees.api.BeePlugin;

public class MyCustomHook implements Hook {

    @Override
    public void onEnable(BeePlugin plugin) {
        plugin.getLogger().info("MyCustomHook enabled!");

        // Initialize your integration here
    }
}
```

### ItemProvider Hook Implementation

For custom item plugins, implement `ItemProviderHook`:

```java
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import fr.traqueur.morebees.api.BeePlugin;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class MyItemsHook implements ItemProviderHook {

    private MyItemsAPI itemsAPI;

    @Override
    public void onEnable(BeePlugin plugin) {
        // Get your plugin's API
        Plugin myPlugin = Bukkit.getPluginManager().getPlugin("MyItems");
        if (myPlugin != null) {
            itemsAPI = ((MyItemsPlugin) myPlugin).getAPI();
            plugin.getLogger().info("MyItems integration enabled!");
        }
    }

    @Override
    public String getItemName(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }

        // Extract custom item ID from your plugin
        String customId = itemsAPI.getItemId(item);
        return customId != null ? "myitems:" + customId : null;
    }

    @Override
    public String getBlockName(Block block) {
        if (block == null) {
            return null;
        }

        // Extract custom block ID from your plugin
        String customId = itemsAPI.getBlockId(block);
        return customId != null ? "myitems:" + customId : null;
    }

    @Override
    public ItemStack getItemFromId(String id) {
        // Format: "myitems:item_id"
        if (!id.startsWith("myitems:")) {
            return null;
        }

        String itemId = id.substring("myitems:".length());
        return itemsAPI.createItem(itemId);
    }
}
```

### Registering Custom Hook

#### Option 1: Auto-Registration (Recommended)

Add your hook to MoreBees' service loader:

**resources/META-INF/services/fr.traqueur.morebees.api.hooks.Hook:**
```
com.yourplugin.MyCustomHook
```

#### Option 2: Manual Registration

Register in your plugin's `onEnable()`:

```java
import fr.traqueur.morebees.api.hooks.Hook;

@Override
public void onEnable() {
    if (Bukkit.getPluginManager().isPluginEnabled("MoreBees")) {
        Hook.register(new MyCustomHook());
    }
}
```

### Hook Best Practices

1. **Check Plugin Availability**: Verify target plugin is loaded
2. **Handle Null**: Always return null for non-custom items
3. **Use Namespaces**: Prefix IDs with your plugin name
4. **Log Initialization**: Confirm hook loaded successfully
5. **Thread Safety**: Hooks may be called from async contexts
6. **Performance**: Cache API references, minimize lookups

### Example: Complete Custom Hook

```java
public class GemstoneHook implements ItemProviderHook {
    private GemstonesPlugin gemstonesPlugin;
    private boolean enabled = false;

    @Override
    public void onEnable(BeePlugin plugin) {
        Plugin target = Bukkit.getPluginManager().getPlugin("Gemstones");

        if (target == null) {
            plugin.getLogger().warning("Gemstones plugin not found!");
            return;
        }

        if (!(target instanceof GemstonesPlugin)) {
            plugin.getLogger().warning("Invalid Gemstones plugin version!");
            return;
        }

        this.gemstonesPlugin = (GemstonesPlugin) target;
        this.enabled = true;

        plugin.getLogger().info("Gemstones integration enabled!");
    }

    @Override
    public String getItemName(ItemStack item) {
        if (!enabled || item == null) return null;

        Optional<Gemstone> gemstone = gemstonesPlugin.getGemstone(item);
        return gemstone.map(g -> "gemstones:" + g.getId()).orElse(null);
    }

    @Override
    public String getBlockName(Block block) {
        if (!enabled || block == null) return null;

        Optional<GemstoneBlock> gemBlock = gemstonesPlugin.getGemstoneBlock(block);
        return gemBlock.map(g -> "gemstones:" + g.getId()).orElse(null);
    }

    @Override
    public ItemStack getItemFromId(String id) {
        if (!enabled || !id.startsWith("gemstones:")) return null;

        String gemstoneId = id.substring("gemstones:".length());
        return gemstonesPlugin.createGemstone(gemstoneId)
                .map(Gemstone::getItemStack)
                .orElse(null);
    }

    public boolean isEnabled() {
        return enabled;
    }
}
```

## Multi-Plugin Examples

### Combining Hooks

You can mix and match items from different plugins (all auto-detected):

```yaml
bees:
  - type: "ultimate-bee"
    display-name: "<rainbow>Ultimate Bee"
    model: "ultimate_bee"  # ModelEngine
    foods:
      - "ruby"           # ItemsAdder
      - "sapphire"       # Oraxen
      - "mythril"        # Nexo
      - DIAMOND          # Vanilla
    flowers:
      - "ruby_ore"       # ItemsAdder
      - "sapphire_ore"   # Oraxen
      - "mythril_ore"    # Nexo
    product:
      honey:
        material: "ultimate_honey"  # ItemsAdder
        name: "<rainbow>Ultimate Honey"
      additional:
        material: NETHER_STAR  # Vanilla
```

**Note:** Items are automatically detected by their plugin hooks - no prefixes needed!

### Resource Pack Example

For ModelEngine + Custom Items:

1. **Create bee model** in Blockbench
2. **Export model** to resource pack
3. **Register in ModelEngine**
4. **Create custom items** with CustomModelData
5. **Configure bee** with both model and custom items

## Troubleshooting

### General Issues

**Hook not loading:**
```
[MoreBees] ItemsAdder integration enabled!  ✓ Good
[MoreBees] Oraxen not detected              ✓ Expected
```

Check console on startup for hook status messages.

**Items not recognized:**
1. Verify plugin is installed and loaded
2. Check item ID format is correct
3. Test with vanilla items first
4. Enable debug mode: `debug: true` in config.yml

**Recipes failing:**
- Custom items require RecipesAPI
- Ensure item IDs are exact matches
- Check CustomModelData values

### Plugin-Specific Issues

**ModelEngine:**
- Model not showing → Check resource pack
- Animation not playing → Verify animation name
- Bee invisible → Check hitbox size

**ItemsAdder:**
- Items not working → Verify namespace format
- Blocks not triggering → Check block IDs
- Recipes broken → Update ItemsAdder

**Oraxen:**
- Items missing → Verify item registered
- Wrong texture → Check CustomModelData
- Blocks not working → Verify block format

**Nexo:**
- Items not found → Check item ID exists
- Integration disabled → Verify Nexo version
- Recipes failing → Check item matching

### Debug Mode

Enable debug logging to troubleshoot:

```yaml
debug: true
```

Check console for:
```
[MoreBees] [DEBUG] Loading item: itemsadder:ruby
[MoreBees] [DEBUG] Hook resolved: ruby_item
[MoreBees] [DEBUG] Creating recipe: ruby-bee_honey
```

### Common Mistakes

❌ **Using vanilla material for custom item:**
```yaml
material: "DIAMOND"  # This will use vanilla diamond, not custom
```

✅ **Use the custom item ID:**
```yaml
material: "ruby"  # ItemsAdder/Oraxen/Nexo will auto-detect
```

❌ **Typo in item ID:**
```yaml
material: "rubie"  # Typo - item doesn't exist
```

✅ **Correct spelling:**
```yaml
material: "ruby"  # Exact match from plugin config
```

**Important Notes:**
- **ItemsAdder**: Items are auto-detected, just use the item ID
- **Oraxen**: Items are auto-detected, no `oraxen:` prefix needed
- **Nexo**: Items are auto-detected, no `nexo:` prefix needed
- Vanilla items use uppercase Material names (e.g., `DIAMOND`, `EMERALD`)

## Performance Considerations

**Hook Performance:**
- Item lookups are cached where possible
- Block checks are optimized for vanilla first
- Custom item checks only run if hooks enabled

**Best Practices:**
- Limit custom items per bee (5-10 recommended)
- Use vanilla items when possible
- Test with `/timings` for performance impact

**ModelEngine:**
- Models add minimal overhead
- Animation playback is efficient
- Resource pack size matters more than model count

## Next Steps

- **Configuration**: See [CONFIGURATION.md](CONFIGURATION.md) for setup
- **API Integration**: Read [API.md](API.md) for programmatic access
- **Examples**: Check community configs on Discord
- **Support**: Join Discord for integration help