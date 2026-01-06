# MoreBees Documentation

Welcome to the MoreBees plugin documentation! MoreBees is a Minecraft plugin that adds customizable bee types with unique behaviors, breeding systems, mutations, and beehive upgrades.

## Documentation Structure

- **[User Guide](USER_GUIDE.md)** - For server administrators who want to install and use MoreBees
- **[Configuration Guide](CONFIGURATION.md)** - Detailed configuration file reference with examples
- **[API Documentation](API.md)** - For plugin developers who want to integrate with MoreBees
- **[Hooks & Integration](HOOKS.md)** - Integration with custom item plugins (ItemsAdder, Oraxen, Nexo, ModelEngine)

## Quick Start

### Installation

1. Download the latest MoreBees jar from releases
2. Place it in your server's `plugins/` folder
3. Install required dependencies:
   - [RecipesAPI](https://github.com/Traqueur-dev/RecipesAPI)
   - [Structura](https://github.com/Traqueur-dev/Structura)
4. Restart your server
5. Configure your bees in `plugins/MoreBees/config.yml`

### Basic Commands

```
/morebees egg <player> <beetype> [amount]    - Give a bee spawn egg
/morebees spawn <beetype> [baby]             - Spawn a bee at your location
/morebees honey <player> <beetype> <block>   - Give honey items
/morebees tool <player> <tool>               - Give bee capture tools
/morebees upgrade <player> <upgrade>         - Give beehive upgrades
/morebees reload                             - Reload configuration
```

## Features Overview

### Custom Bee Types
- Define unlimited bee types with unique properties
- Custom names, models, foods, and flowers
- Unique honey and product items
- ModelEngine integration for 3D models

### Breeding System
- Breed two bee types to create offspring
- Configurable breeding outcomes with probability
- Automatic child type calculation based on parents

### Mutation System
- Bees can mutate when collecting nectar from specific blocks
- Configure block-triggered transformations
- Drop special items when mutations occur

### Bee Capture Tools
- **Bee Jar**: Store 1 bee with its data
- **Bee Box**: Store multiple bees (configurable capacity)
- Preserve bee type, age, and nectar status

### Beehive Upgrades
- Increase beehive capacity
- Boost honey production with multipliers
- Enable honey block production
- Visual upgrade displays on beehives

### Integration Support
- **ModelEngine**: 3D bee models
- **ItemsAdder**: Custom items and blocks
- **Oraxen**: Custom items and blocks
- **Nexo**: Custom items and blocks
- **RecipesAPI**: Custom crafting recipes

## Project Information

- **Version**: 1.1.0
- **Minecraft Version**: 1.21.4+
- **Platform**: Paper (Bukkit/Spigot compatible)
- **Java Version**: 21
- **Author**: Traqueur
- **License**: Proprietary

## Architecture

MoreBees is built with a clean separation between API and implementation:

- **API Module** (`morebees-api.jar`) - Public interfaces for plugin developers
- **Main Module** (`MoreBees.jar`) - Complete plugin with implementation

This allows other plugins to depend on the API without including the full implementation.

## Support & Contributions

- **Issues**: Report bugs and request features on GitHub
- **Discord**: Join our community server for support
- **Wiki**: Additional tutorials and examples

## Example Configuration

Here's a simple bee type configuration:

```yaml
bees:
  - type: redstone-bee
    display-name: "<red>Redstone Bee"
    foods:
      - REDSTONE
    flowers:
      - REDSTONE_BLOCK
    product:
      honey:
        material: HONEYCOMB
        name: "<red>Redstone Honeycomb"
        model-id: 1001
      additional:
        material: REDSTONE
```

See the [Configuration Guide](CONFIGURATION.md) for complete examples and detailed explanations.

## Next Steps

- **Server Admins**: Start with the [User Guide](USER_GUIDE.md)
- **Config Help**: Check the [Configuration Guide](CONFIGURATION.md)
- **Plugin Developers**: Read the [API Documentation](API.md)
- **Custom Items**: See [Hooks & Integration](HOOKS.md)