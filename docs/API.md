# MoreBees API Documentation

Developer documentation for integrating with the MoreBees plugin API.

## Table of Contents

- [Getting Started](#getting-started)
- [Core API](#core-api)
- [Manager System](#manager-system)
- [Data Models](#data-models)
- [Configuration System](#configuration-system)
- [Serialization](#serialization)
- [Hook System](#hook-system)
- [Utilities](#utilities)
- [Examples](#examples)

## Getting Started

### plugin.yml

Declare MoreBees as a dependency:

```yaml
name: YourPlugin
version: 1.0.0
depend: [MoreBees]
api-version: '1.21'
```

### Basic Usage

```java
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.managers.BeeManager;

public class YourPlugin extends JavaPlugin {
    private BeePlugin moreBeesAPI;
    private BeeManager beeManager;

    @Override
    public void onEnable() {
        // Get MoreBees API instance
        moreBeesAPI = JavaPlugin.getPlugin(BeePlugin.class);

        // Get manager
        beeManager = moreBeesAPI.getManager(BeeManager.class);

        getLogger().info("MoreBees API initialized!");
    }
}
```

## Core API

### BeePlugin

Abstract base class providing access to all MoreBees functionality.

**Location:** `fr.traqueur.morebees.api.BeePlugin`

#### Methods

```java
// Get plugin instance
BeePlugin plugin = JavaPlugin.getPlugin(BeePlugin.class);

// Access managers
<T extends Manager> T getManager(Class<T> managerClass)

// Access configuration
<T extends Settings> T getSettings(Class<T> settingsClass)

// Get command manager
PlatformCommandManager<CommandSender> getCommandManager()

// Get recipes API
RecipesAPI getRecipesAPI()

// Register manager (for advanced use)
<I extends Manager, T extends I> void registerManager(Class<I> inter, T instance)

// Register event listener
void registerListener(Listener listener)
```

#### Example

```java
BeePlugin api = JavaPlugin.getPlugin(BeePlugin.class);
BeeManager beeManager = api.getManager(BeeManager.class);
GlobalSettings settings = Settings.getSettings(GlobalSettings.class);
```

### Manager Interface

Base interface for all manager types.

**Location:** `fr.traqueur.morebees.api.Manager`

```java
public interface Manager {
    default BeePlugin getPlugin() { ... }
}
```

All managers implement this interface and provide access to the BeePlugin instance.

## Manager System

MoreBees uses a manager-based architecture for different functional domains.

### BeeManager

Manages bee entities, spawning, breeding, and mutations.

**Location:** `fr.traqueur.morebees.api.managers.BeeManager`

#### Methods

##### Bee Type Retrieval

```java
// Get bee type from spawn egg
Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack)

// Get bee type from entity
Optional<BeeType> getBeeTypeFromEntity(LivingEntity entity)
```

**Example:**
```java
BeeManager beeManager = api.getManager(BeeManager.class);

// Check spawn egg
ItemStack egg = player.getInventory().getItemInMainHand();
Optional<BeeType> type = beeManager.getBeeTypeFromEgg(egg);
if (type.isPresent()) {
    player.sendMessage("This is a " + type.get().displayName() + " egg!");
}

// Check entity
if (entity instanceof Bee bee) {
    Optional<BeeType> beeType = beeManager.getBeeTypeFromEntity(bee);
    beeType.ifPresent(type -> {
        System.out.println("Found " + type.type() + " bee");
    });
}
```

##### Bee Spawning

```java
void spawnBee(
    Location location,
    BeeType beeType,
    CreatureSpawnEvent.SpawnReason reason,
    boolean baby,
    boolean nectar
)
```

**Parameters:**
- `location` - Where to spawn the bee
- `beeType` - Type of bee to spawn
- `reason` - Spawn reason (for events)
- `baby` - `true` for baby bee, `false` for adult
- `nectar` - `true` if bee should have nectar

**Example:**
```java
// Spawn adult redstone bee with nectar
GlobalSettings settings = api.getSettings(GlobalSettings.class);
Optional<BeeType> beeType = settings.getBeeType("redstone-bee");

beeType.ifPresent(type -> {
    beeManager.spawnBee(
        player.getLocation(),
        type,
        CreatureSpawnEvent.SpawnReason.CUSTOM,
        false,  // adult
        true    // has nectar
    );
});
```

##### Bee Modification

```java
void patchBee(Bee bee, BeeType beeType)
```

**Description:** Applies BeeType properties to an existing bee entity.

**What it does:**
- Sets bee type in persistent data
- Applies custom name or model
- Updates AI goals (food, pollination)

**Example:**
```java
// Transform vanilla bee to custom type
if (entity instanceof Bee bee) {
    Optional<BeeType> type = settings.getBeeType("diamond-bee");
    type.ifPresent(beeType -> {
        beeManager.patchBee(bee, beeType);
    });
}
```

##### Breeding

```java
BeeType computeChildType(BeeType mother, BeeType father)
```

**Description:** Calculate offspring bee type based on breeding rules.

**Returns:**
- Configured child type (if breed rule matches and succeeds)
- Random parent type (fallback)

**Example:**
```java
BeeType mother = ...; // redstone-bee
BeeType father = ...; // diamond-bee
BeeType child = beeManager.computeChildType(mother, father);

// Might return emerald-bee (if configured) or one of the parents
System.out.println("Child type: " + child.type());
```

##### Feeding

```java
void feed(Player player, Bee bee)
```

**Description:** Handle bee feeding mechanics.

**Effect:**
- If adult + can breed: Enter love mode
- If baby: Accelerate growth

**Example:**
```java
@EventHandler
public void onBeeInteract(PlayerInteractEntityEvent event) {
    if (event.getRightClicked() instanceof Bee bee) {
        beeManager.feed(event.getPlayer(), bee);
    }
}
```

##### Mutation

```java
void mutate(Bee bee, Mutation mutation, Location location)
```

**Description:** Trigger bee mutation.

**Effect:**
- Remove nectar from bee
- Destroy block at location
- Drop mutation result egg

**Example:**
```java
BreedSettings breedSettings = api.getSettings(BreedSettings.class);
beeManager.getBeeTypeFromEntity(bee).ifPresent(beeType -> {
    Block block = location.getBlock();
    breedSettings.getMutation(beeType, block).ifPresent(mutation -> {
        beeManager.mutate(bee, mutation, location);
    });
});
```

##### Recipe Registration

```java
void registerRecipes()
```

**Description:** Registers all crafting recipes for bee products.

**Note:** Called automatically on plugin enable. Useful for dynamic recipe updates.

---

### BeehiveManager

Manages beehive data storage and upgrades.

**Location:** `fr.traqueur.morebees.api.managers.BeehiveManager`

#### Methods

##### Data Retrieval

```java
// Load beehive data from block
Optional<Beehive> getBeehiveFromBlock(BlockState blockState)

// Load beehive data from item
Optional<Beehive> getBeehiveFromItem(ItemStack itemStack)
```

**Example:**
```java
BeehiveManager beehiveManager = api.getManager(BeehiveManager.class);

// From placed beehive
Block block = player.getTargetBlock(null, 5);
if (block.getState() instanceof Beehive) {
    Optional<Beehive> beehive = beehiveManager.getBeehiveFromBlock(block.getState());
    beehive.ifPresent(hive -> {
        player.sendMessage("Honey count: " + hive.getHoneyCombCounts());
    });
}

// From item in inventory
ItemStack item = player.getInventory().getItemInMainHand();
Optional<Beehive> beehive = beehiveManager.getBeehiveFromItem(item);
```

##### Data Persistence

```java
void saveBeehiveToBlock(Block block, Beehive beehive)
```

**Description:** Save beehive data to block's persistent data.

**Example:**
```java
beehiveManager.getBeehiveFromBlock(block.getState()).ifPresent(hive -> {
    // Modify data
    hive.addHoney(beeType, 5);

    // Save back to block
    beehiveManager.saveBeehiveToBlock(block, hive);
});
```

##### Data Modification

```java
void editBeehive(Block block, Consumer<Beehive> consumer)
```

**Description:** Load, modify, and save beehive data in one call.

**Example:**
```java
// Add honey atomically
beehiveManager.editBeehive(block, beehive -> {
    beehive.addHoney(beeType, 10);
    beehive.setUpgrade(upgrade);
});
```

---

### ToolsManager

Manages bee capture tools (Jar and Box).

**Location:** `fr.traqueur.morebees.api.managers.ToolsManager`

#### Methods

##### Tool Identification

```java
Optional<Tool> getTool(ItemStack itemStack)
```

**Returns:** `Tool.BEE_JAR` or `Tool.BEE_BOX` if item is a tool.

**Example:**
```java
ToolsManager toolsManager = api.getManager(ToolsManager.class);

ItemStack item = player.getInventory().getItemInMainHand();
Optional<Tool> tool = toolsManager.getTool(item);

tool.ifPresent(t -> {
    player.sendMessage("Holding a " + t.name());
});
```

##### Capacity Check

```java
boolean isFull(ItemStack tool)
```

**Returns:** `true` if tool cannot hold more bees.

**Example:**
```java
if (toolsManager.isFull(item)) {
    player.sendMessage("Tool is full!");
    return;
}
```

##### Bee Data Conversion

```java
BeeData toData(Bee bee, BeeType beeType)
```

**Description:** Convert bee entity to storable BeeData.

**Example:**
```java
BeeData data = toolsManager.toData(bee, beeType);
System.out.println("Bee is " + (data.isAdult() ? "adult" : "baby"));
System.out.println("Has nectar: " + data.hasNectar());
```

##### Bee Capture

```java
void catchBee(ItemStack tool, Bee bee, BeeType beeType)
```

**Description:** Store bee in tool and remove bee entity.

**Example:**
```java
@EventHandler
public void onBeeClick(PlayerInteractEntityEvent event) {
    if (event.getRightClicked() instanceof Bee bee) {
        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();

        Optional<Tool> toolType = toolsManager.getTool(tool);
        if (toolType.isPresent() && !toolsManager.isFull(tool)) {
            Optional<BeeType> beeType = beeManager.getBeeTypeFromEntity(bee);
            beeType.ifPresent(type -> {
                toolsManager.catchBee(tool, bee, type);
                event.getPlayer().sendMessage("Caught bee!");
            });
        }
    }
}
```

##### Bee Release

```java
void releaseBee(ItemStack tool, boolean all)
```

**Description:** Release bee(s) from tool at player's cursor location.

**Parameters:**
- `tool` - The tool item
- `all` - `true` to release all, `false` for one

**Example:**
```java
// Release one bee
toolsManager.releaseBee(tool, false);

// Release all bees
toolsManager.releaseBee(tool, true);
```

---

### UpgradesManager

Manages beehive upgrades and visual displays.

**Location:** `fr.traqueur.morebees.api.managers.UpgradesManager`

#### Methods

##### Upgrade Identification

```java
Optional<Upgrade> getUpgradeFromItem(ItemStack itemStack)
```

**Example:**
```java
UpgradesManager upgradesManager = api.getManager(UpgradesManager.class);

ItemStack item = player.getInventory().getItemInMainHand();
Optional<Upgrade> upgrade = upgradesManager.getUpgradeFromItem(item);

upgrade.ifPresent(u -> {
    player.sendMessage("Max bees: " + u.maxBees());
    player.sendMessage("Multiplier: " + u.productionMultiplier() + "x");
});
```

##### Display Management

```java
// Create floating upgrade display
void createUpgradeDisplay(Block beehiveBlock, Upgrade upgrade)

// Remove display entity
void removeUpgradeDisplay(UUID displayId)
```

**Example:**
```java
// Apply upgrade with display
beehiveManager.editBeehive(block, beehive -> {
    beehive.setUpgrade(upgrade);
    upgradesManager.createUpgradeDisplay(block, upgrade);
});

// Remove upgrade display
beehive.getUpgradeId().ifPresent(upgradesManager::removeUpgradeDisplay);
```

##### Beehive Loading

```java
void loadBeehive(Beehive beehiveState, Stream<Entity> nearbyEntities)
```

**Description:** Link upgrade displays to beehive on world load.

**Note:** Called automatically by the plugin.

## Data Models

### BeeType

Immutable record defining bee variant properties.

**Location:** `fr.traqueur.morebees.api.models.BeeType`

```java
public record BeeType(
    String type,
    Integer modelId,
    String displayName,
    List<String> foods,
    List<String> flowers,
    Product product,
    String model
) {
    public static final BeeType NORMAL = ...;

    // Methods
    ItemStack egg();
    ItemStack productItem();
    ItemStack honey(int amount, boolean block);
    boolean isFood(ItemStack item);
    boolean isFlower(Block block);
}
```

#### Fields

- `type` - Unique identifier (e.g., "redstone-bee")
- `modelId` - CustomModelData for spawn egg
- `displayName` - MiniMessage formatted name
- `foods` - List of food material/item IDs
- `flowers` - List of flower material/block IDs
- `product` - Honey and additional products
- `model` - ModelEngine model ID

#### Methods

```java
BeeType beeType = ...;

// Generate spawn egg
ItemStack egg = beeType.egg();

// Get product item
ItemStack product = beeType.productItem();

// Get honey (single or block)
ItemStack honey = beeType.honey(1, false);  // 1 honeycomb
ItemStack block = beeType.honey(1, true);   // 1 honey block

// Check if item is food
boolean canEat = beeType.isFood(itemStack);

// Check if block is flower
boolean canPollinate = beeType.isFlower(block);
```

#### Product Record

```java
public record Product(
    ItemStackWrapper honey,
    ItemStackWrapper honeyBlock,
    ItemStackWrapper additional
) {}
```

---

### BeeData

Interface representing storable bee state.

**Location:** `fr.traqueur.morebees.api.models.BeeData`

```java
public interface BeeData {
    BeeType type();
    boolean hasNectar();
    boolean isAdult();
}
```

**Usage:**
```java
BeeData data = toolsManager.toData(bee, beeType);

String ageStatus = data.isAdult() ? "Adult" : "Baby";
String nectarStatus = data.hasNectar() ? "Has Nectar" : "No Nectar";
System.out.println(data.type().displayName() + " - " + ageStatus + ", " + nectarStatus);
```

---

### Beehive

Interface representing beehive state.

**Location:** `fr.traqueur.morebees.api.models.Beehive`

```java
public interface Beehive {
    // Upgrade management
    Optional<Upgrade> getUpgrade();
    void setUpgrade(Upgrade upgrade);

    // Display entity ID
    Optional<UUID> getUpgradeId();
    void setUpgradeId(UUID id);

    // Honey tracking
    Map<BeeType, Integer> getHoneyCombCounts();
    int getHoneyCombCount(BeeType beeType);
    void addHoney(BeeType beeType, int amount);
    void removeHoney(BeeType beeType, int amount);

    // Item patching
    void patch(ItemStack beehiveItem);
}
```

**Example:**
```java
beehiveManager.getBeehiveFromBlock(block.getState()).ifPresent(beehive -> {
    // Check upgrade
    Optional<Upgrade> upgrade = beehive.getUpgrade();
    upgrade.ifPresent(u -> {
        System.out.println("Upgrade: " + u.id());
    });

    // Manage honey
    int honeyCount = beehive.getHoneyCombCount(beeType);
    beehive.addHoney(beeType, 5);

    // Apply to item
    ItemStack item = new ItemStack(Material.BEEHIVE);
    beehive.patch(item);
});
```

---

### Breed

Record defining breeding outcome.

**Location:** `fr.traqueur.morebees.api.models.Breed`

```java
public record Breed(
    List<String> parents,
    String child,
    double chance
) {}
```

**Example:**
```java
BreedSettings settings = api.getSettings(BreedSettings.class);

for (Breed breed : settings.breeds()) {
    System.out.println(breed.parents() + " -> " + breed.child() +
                      " (" + (breed.chance() * 100) + "%)");
}
```

---

### Mutation

Record defining block-triggered mutation.

**Location:** `fr.traqueur.morebees.api.models.Mutation`

```java
public record Mutation(
    String parent,
    String child,
    List<String> blocks
) {
    boolean canMutate(Block block);
}
```

**Example:**
```java
BreedSettings settings = api.getSettings(BreedSettings.class);

for (Mutation mutation : settings.mutations()) {
    if (mutation.canMutate(block)) {
        System.out.println("This block triggers: " +
                          mutation.parent() + " -> " + mutation.child());
    }
}
```

---

### Tool

Enum defining bee capture tools.

**Location:** `fr.traqueur.morebees.api.models.Tool`

```java
public enum Tool {
    BEE_JAR,
    BEE_BOX;

    int maxBees();
    ItemStack itemStack(List<BeeData> bees);
    List<Component> lore(List<BeeData> bees);
}
```

**Example:**
```java
Tool tool = Tool.BEE_JAR;

int capacity = tool.maxBees();  // 1
ItemStack item = tool.itemStack(Collections.emptyList());
```

---

### Upgrade

Record defining beehive upgrade.

**Location:** `fr.traqueur.morebees.api.models.Upgrade`

```java
public record Upgrade(
    String id,
    ItemStackWrapper item,
    int maxBees,
    double productionMultiplier,
    boolean produceBlocks
) {
    public static final Upgrade NONE = ...;

    ItemStack build();
}
```

**Example:**
```java
UpgradeSettings settings = api.getSettings(UpgradeSettings.class);

settings.getUpgrade("level-1").ifPresent(upgrade -> {
    System.out.println("Max bees: " + upgrade.maxBees());
    System.out.println("Multiplier: " + upgrade.productionMultiplier());
    System.out.println("Produces blocks: " + upgrade.produceBlocks());

    ItemStack item = upgrade.build();
});
```

## Configuration System

### Settings Interface

Marker interface for all configuration classes.

**Location:** `fr.traqueur.morebees.api.settings.Settings`

All settings classes implement this interface and extend `Loadable` from Structura.

### GlobalSettings

Main plugin configuration.

**Location:** `fr.traqueur.morebees.api.settings.GlobalSettings`

```java
public record GlobalSettings(
    boolean debug,
    String flyAnimation,
    List<BeeType> bees,
    ItemStackWrapper beeBox,
    int beeBoxSize,
    ItemStackWrapper beeJar,
    List<String> beehiveLore
) implements Settings {
    Optional<BeeType> getBeeType(String type);
    boolean contains(String... type);
}
```

**Example:**
```java
GlobalSettings settings = api.getSettings(GlobalSettings.class);

// Get bee type
Optional<BeeType> beeType = settings.getBeeType("redstone-bee");

// Check if types exist
if (settings.contains("redstone-bee", "diamond-bee")) {
    System.out.println("Both types exist!");
}

// Get all bees
List<BeeType> allBees = settings.bees();

// Tool settings
int boxSize = settings.beeBoxSize();
ItemStack jar = settings.beeJar().build();
```

### BreedSettings

Breeding and mutation configuration.

**Location:** `fr.traqueur.morebees.api.settings.BreedSettings`

```java
public record BreedSettings(
    List<Breed> breeds,
    List<Mutation> mutations
) implements Settings {
    Optional<Mutation> getMutation(BeeType beeType, Block block);
}
```

**Example:**
```java
BreedSettings settings = api.getSettings(BreedSettings.class);

// Get all breeds
List<Breed> breeds = settings.breeds();

// Check for mutation
Optional<Mutation> mutation = settings.getMutation(beeType, block);
mutation.ifPresent(m -> {
    System.out.println("Mutation to: " + m.child());
});
```

### UpgradeSettings

Upgrade definitions.

**Location:** `fr.traqueur.morebees.api.settings.UpgradeSettings`

```java
public record UpgradeSettings(
    List<Upgrade> upgrades
) implements Settings {
    Optional<Upgrade> getUpgrade(String id);
}
```

**Example:**
```java
UpgradeSettings settings = api.getSettings(UpgradeSettings.class);

// Get specific upgrade
Optional<Upgrade> upgrade = settings.getUpgrade("level-1");

// Get all upgrades
List<Upgrade> allUpgrades = settings.upgrades();
```

## Serialization

### Keys Enum

Enum of persistent data keys for NBT storage.

**Location:** `fr.traqueur.morebees.api.serialization.Keys`

```java
public enum Keys {
    BEE_TYPE,
    BEEHIVE,
    TOOL_ID,
    BEES,
    UPGRADE_ID;

    <T> Optional<T> get(PersistentDataContainer container, PersistentDataType<?,T> type);
    <T> T get(PersistentDataContainer container, PersistentDataType<?,T> type, T def);
    <T> void set(PersistentDataContainer container, PersistentDataType<?,T> type, T value);
}
```

**Example:**
```java
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.BeeTypeDataType;

PersistentDataContainer pdc = entity.getPersistentDataContainer();

// Set bee type
Keys.BEE_TYPE.set(pdc, BeeTypeDataType.INSTANCE, beeType);

// Get bee type with default
BeeType type = Keys.BEE_TYPE.get(pdc, BeeTypeDataType.INSTANCE, BeeType.NORMAL);

// Get bee type as Optional
Optional<BeeType> optType = Keys.BEE_TYPE.get(pdc, BeeTypeDataType.INSTANCE);
```

### Data Type Classes

Custom `PersistentDataType` implementations for complex objects.

**Location:** `fr.traqueur.morebees.api.serialization.datas.*`

Available data types:
- `BeeTypeDataType.INSTANCE` - Serialize BeeType
- `BeeDataDataType.INSTANCE` - Serialize BeeData
- `BeehiveDataType.INSTANCE` - Serialize Beehive
- `ToolDataType.INSTANCE` - Serialize Tool enum
- `UpgradeDataType.INSTANCE` - Serialize Upgrade

**Example:**
```java
import fr.traqueur.morebees.api.serialization.datas.*;

// Store bee type in item
ItemMeta meta = item.getItemMeta();
PersistentDataContainer pdc = meta.getPersistentDataContainer();
Keys.BEE_TYPE.set(pdc, BeeTypeDataType.INSTANCE, beeType);
item.setItemMeta(meta);

// Retrieve bee type
BeeType type = Keys.BEE_TYPE.get(pdc, BeeTypeDataType.INSTANCE, BeeType.NORMAL);
```

## Hook System

Extend MoreBees with custom integrations.

### Hook Interface

Base interface for plugin hooks.

**Location:** `fr.traqueur.morebees.api.hooks.Hook`

```java
public interface Hook {
    void onEnable(BeePlugin plugin);

    static Set<Hook> getHooks();
    static <T extends Hook> Optional<T> getByClass(Class<T> clazz);
    static void register(Hook hook);
}
```

**Example:**
```java
public class MyCustomHook implements Hook {
    @Override
    public void onEnable(BeePlugin plugin) {
        plugin.getLogger().info("MyCustomHook enabled!");
    }
}

// Register hook
Hook.register(new MyCustomHook());
```

### ItemProviderHook

Interface for custom item system integration.

**Location:** `fr.traqueur.morebees.api.hooks.ItemProviderHook`

```java
public interface ItemProviderHook extends Hook {
    String getItemName(ItemStack item);
    String getBlockName(Block block);
    ItemStack getItemFromId(String id);
}
```

**Example:**
```java
public class MyItemsHook implements ItemProviderHook {
    @Override
    public void onEnable(BeePlugin plugin) {
        // Initialize integration
    }

    @Override
    public String getItemName(ItemStack item) {
        // Return custom item ID or null
        return MyItemsAPI.getItemId(item);
    }

    @Override
    public String getBlockName(Block block) {
        // Return custom block ID or null
        return MyItemsAPI.getBlockId(block);
    }

    @Override
    public ItemStack getItemFromId(String id) {
        // Create ItemStack from ID or null
        return MyItemsAPI.createItem(id);
    }
}
```

## Utilities

### Logger

Static logging utility with color support.

**Location:** `fr.traqueur.morebees.api.Logger`

```java
// Initialize (done by MoreBees)
Logger.init(slf4jLogger, debugMode);

// Log messages
Logger.info("Plugin message");
Logger.success("<green>Operation completed!");
Logger.warning("<yellow>Warning message");
Logger.severe("<red>Error occurred");
Logger.debug("Debug message (requires debug mode)");

// With formatting
Logger.info("Player %s has %d bees", playerName, beeCount);
```

### Formatter

Dynamic text replacement system.

**Location:** `fr.traqueur.morebees.api.util.Formatter`

```java
// Format text
String result = Formatter.format("Hello %player%!",
    Formatter.format("%player%", player.getName())
);

// Multiple formatters
String text = Formatter.format(
    "Bee: %type%, Count: %count%",
    Formatter.format("%type%", beeType.displayName()),
    Formatter.format("%count%", String.valueOf(count))
);

// With suppliers (lazy evaluation)
Formatter.format("Time: %time%",
    Formatter.format("%time%", () -> String.valueOf(System.currentTimeMillis()))
);
```

### MiniMessageHelper

Convert MiniMessage strings to Adventure components.

**Location:** `fr.traqueur.morebees.api.util.MiniMessageHelper`

```java
import net.kyori.adventure.text.Component;

Component component = MiniMessageHelper.parse("<red>Hello <bold>World</bold>!");
player.sendMessage(component);
```

## Examples

### Example 1: Custom Bee Spawner

```java
public class CustomBeeSpawner extends JavaPlugin implements Listener {
    private BeePlugin moreBeesAPI;
    private BeeManager beeManager;

    @Override
    public void onEnable() {
        moreBeesAPI = JavaPlugin.getPlugin(BeePlugin.class);
        beeManager = moreBeesAPI.getManager(BeeManager.class);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onSpawnerActivate(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.SPAWNER) {
            Player player = event.getPlayer();

            // Spawn custom bee
            GlobalSettings settings = moreBeesAPI.getSettings(GlobalSettings.class);
            settings.getBeeType("redstone-bee").ifPresent(beeType -> {
                Location loc = block.getLocation().add(0.5, 1, 0.5);
                beeManager.spawnBee(loc, beeType,
                    CreatureSpawnEvent.SpawnReason.SPAWNER, false, false);
                player.sendMessage("Spawned " + beeType.displayName());
            });
        }
    }
}
```

### Example 2: Bee Statistics Tracker

```java
public class BeeStatTracker extends JavaPlugin implements Listener {
    private BeeManager beeManager;
    private Map<BeeType, Integer> beeStats = new HashMap<>();

    @Override
    public void onEnable() {
        BeePlugin api = JavaPlugin.getPlugin(BeePlugin.class);
        beeManager = api.getManager(BeeManager.class);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBeeBreed(EntityBreedEvent event) {
        if (event.getEntity() instanceof Bee baby) {
            Optional<BeeType> type = beeManager.getBeeTypeFromEntity(baby);
            type.ifPresent(beeType -> {
                beeStats.merge(beeType, 1, Integer::sum);
                getLogger().info("Bred: " + beeType.type() +
                               " (Total: " + beeStats.get(beeType) + ")");
            });
        }
    }

    public void printStats() {
        beeStats.forEach((type, count) -> {
            getLogger().info(type.displayName() + ": " + count + " bees");
        });
    }
}
```

### Example 3: Custom Beehive GUI

```java
public class BeehiveGUI extends JavaPlugin implements Listener {
    private BeehiveManager beehiveManager;

    @Override
    public void onEnable() {
        BeePlugin api = JavaPlugin.getPlugin(BeePlugin.class);
        beehiveManager = api.getManager(BeehiveManager.class);

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onBeehiveClick(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && block.getType() == Material.BEEHIVE) {
            event.setCancelled(true);
            openBeehiveGUI(event.getPlayer(), block);
        }
    }

    private void openBeehiveGUI(Player player, Block block) {
        beehiveManager.getBeehiveFromBlock(block.getState()).ifPresent(beehive -> {
            Inventory gui = Bukkit.createInventory(null, 27, "Beehive Stats");

            // Show upgrade
            beehive.getUpgrade().ifPresent(upgrade -> {
                ItemStack upgradeItem = upgrade.build();
                gui.setItem(13, upgradeItem);
            });

            // Show honey counts
            Map<BeeType, Integer> honeyCounts = beehive.getHoneyCombCounts();
            int slot = 0;
            for (Map.Entry<BeeType, Integer> entry : honeyCounts.entrySet()) {
                ItemStack honey = entry.getKey().honey(entry.getValue(), false);
                gui.setItem(slot++, honey);
            }

            player.openInventory(gui);
        });
    }
}
```

### Example 4: Bee Migration System

```java
public class BeeMigration extends JavaPlugin {
    private BeeManager beeManager;

    @Override
    public void onEnable() {
        BeePlugin api = JavaPlugin.getPlugin(BeePlugin.class);
        beeManager = api.getManager(BeeManager.class);

        // Start migration task
        Bukkit.getScheduler().runTaskTimer(this, this::migrateBees, 20L * 60, 20L * 300);
    }

    private void migrateBees() {
        for (World world : Bukkit.getWorlds()) {
            for (Bee bee : world.getEntitiesByClass(Bee.class)) {
                beeManager.getBeeTypeFromEntity(bee).ifPresent(beeType -> {
                    if (Math.random() < 0.1) {  // 10% chance
                        Location newLocation = findNearbyFlower(bee.getLocation(), beeType);
                        if (newLocation != null) {
                            bee.teleport(newLocation);
                            getLogger().info("Migrated " + beeType.type() + " to new location");
                        }
                    }
                });
            }
        }
    }

    private Location findNearbyFlower(Location origin, BeeType beeType) {
        // Search for flower blocks in 50 block radius
        for (int x = -50; x <= 50; x++) {
            for (int z = -50; z <= 50; z++) {
                Block block = origin.clone().add(x, 0, z).getBlock();
                if (beeType.isFlower(block)) {
                    return block.getLocation().add(0.5, 1, 0.5);
                }
            }
        }
        return null;
    }
}
```

## Best Practices

1. **Always use Optional**: Handle missing data gracefully
2. **Check dependencies**: Verify MoreBees is loaded before accessing API
3. **Use managers**: Don't bypass the manager system
4. **Persistent data**: Use Keys enum for consistent data access
5. **Thread safety**: Most operations are not thread-safe, use Bukkit scheduler
6. **Event priority**: Use `MONITOR` priority for stat tracking, `NORMAL` for modifications
7. **Performance**: Cache manager references, don't fetch on every call
8. **Null checks**: Always validate entities and items before processing

## Troubleshooting

### ClassNotFoundException
**Problem:** `fr.traqueur.morebees.api.BeePlugin not found`
**Solution:** Add MoreBees as `depend` or `softdepend` in plugin.yml

### NullPointerException
**Problem:** Manager returns null
**Solution:** Ensure MoreBees is enabled before accessing API

### NoSuchElementException
**Problem:** `Optional.get()` called on empty Optional
**Solution:** Use `ifPresent()` or `orElse()` instead

### UnsupportedOperationException
**Problem:** Modifying immutable collections
**Solution:** Records return unmodifiable lists, create new list if modification needed

## Support

- **API Changes**: Check changelog for breaking changes
- **Backwards Compatibility**: API maintains compatibility within major versions
- **Deprecation**: Deprecated methods include migration instructions
- **Questions**: Join Discord or open GitHub issue

## Next Steps

- **Explore Hooks**: See [HOOKS.md](HOOKS.md) for integration examples
- **Configuration**: Read [CONFIGURATION.md](CONFIGURATION.md) for settings
- **Examples**: Check GitHub repository for more examples