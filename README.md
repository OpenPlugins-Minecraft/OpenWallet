<div align="center">

![Logo](https://i.imgur.com/SGgWxRi.png)
## OpenWallet - OpenSource Bukkit economy plugin
## Recode IN PROGRESS - V2.0 IS COMING

[![GitHub build](https://img.shields.io/github/actions/workflow/status/neziw/OpenWallet/build.yml?style=for-the-badge)](https://github.com/neziw/OpenWallet/actions)
![Libraries.io dependency status for GitHub repo](https://img.shields.io/librariesio/github/neziw/OpenWallet?style=for-the-badge)
![Last Commit](https://img.shields.io/github/last-commit/neziw/OpenWallet?style=for-the-badge)
 ![GitHub commit activity](https://img.shields.io/github/commit-activity/m/OpenPlugins-Minecraft/OpenWallet?color=brightgreen&style=for-the-badge)
[![Open issues](https://img.shields.io/github/issues/neziw/OpenWallet?style=for-the-badge)](https://github.com/neziw/OpenWallet/issues)
[![Open pull requests](https://img.shields.io/github/issues-pr/neziw/OpenWallet?style=for-the-badge)](https://github.com/neziw/OpenWallet)
[![License](https://img.shields.io/github/license/neziw/OpenWallet?style=for-the-badge)](LICENSE)
[![Contributors](https://img.shields.io/github/contributors/neziw/OpenWallet?color=blue&style=for-the-badge)](https://github.com/neziw/OpenWallet/graphs/contributors)
[![Repo size](https://img.shields.io/github/repo-size/neziw/OpenWallet?style=for-the-badge)](https://github.com/neziw/OpenWallet)
![Latest Tag](https://img.shields.io/github/v/tag/neziw/OpenWallet?label=LATEST%20TAG&style=for-the-badge)
[![Forks](https://img.shields.io/github/forks/neziw/OpenWallet?style=for-the-badge)](https://github.com/neziw/OpenWallet/network/members)
[![GitHub stars](https://img.shields.io/github/stars/neziw/OpenWallet?style=for-the-badge)](https://github.com/neziw/OpenWallet/stargazers)   </br>
[![Discord](https://img.shields.io/discord/1071413512414449666?color=7289da&logo=discord&logoColor=white&style=for-the-badge)](https://discord.gg/k69htTFCVk)

Welcome to the **OpenWallet** plugin! This is a brand new economy plugin with a lot of features (custom shops, hooks etc.)

Don't know how to start? Just check the [wiki](https://github.com/neziw/OpenWallet/wiki) page!

[Join our Discord Server](https://discord.gg/k69htTFCVk)

[![Polymart](https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/available/polymart_vector.svg?raw=true)](https://polymart.org/resource/openwallet-economy-plugin.3374)
[![Modrinth](https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/available/modrinth_vector.svg?raw=true)](https://modrinth.com/mod/openwallet)</br>
![JDK11](https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/built-with/java11_vector.svg?raw=true)
![Gradle](https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/built-with/gradle_vector.svg?raw=true)
</br>
![Bukkit](https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/supported/bukkit_vector.svg?raw=true)
[![Spigot](https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/supported/spigot_vector.svg?raw=true)](https://www.spigotmc.org/resources/openwallet-economy-plugin.107826/)
![Paper](https://github.com/intergrav/devins-badges/blob/v2/assets/cozy/supported/paper_vector.svg?raw=true)
</div>

----
## Features
  * YAML Configuration
  * Fully customizable and unlimited shops
  * Simple management panel (for admins)
  * Admin commands
  * All messages can be translated
  * Confirmation menu
  * Option to make only once-buy items
  * SQLite and MySQL database support
  * Balance command for players
  * Vault and PlaceholderAPI hook
  * Custom aliases for plugin commands

----
## Default configuration files
<details>
  <summary>config.yml</summary>
  
```yaml
# Configuration file version (don't change manually)
config-version: 4

# Whether update checker should be enabled
update-checker: true

# Database configuration
database-settings:
  # Type of database (MySQL/SQLite)
  # Default: SQLite
  type: SQLite
  # Host IP address (use 127.0.0.1 for local)
  # Default: 127.0.0.1
  host: 127.0.0.1
  # Port of database
  # Default: 3306
  port: 3306
  # Name of database user
  # Default: minecraft
  user: minecraft
  # Password of database user
  # Default:
  password: ''
  # Name of database
  # Default: wallet_plugin
  database: wallet_plugin

# Default player balance on first join
# Default: 20.0
start-balance: 20.0

# Whether PlaceholderAPI hook should be enabled
# Default: false
hook-placeholder-api: false

# Whether GadgetsMenu hook should be enabled
# Default: false
hook-gadgets-menu: false

# Whether OpenWallet should override Vault economy
# Default: false
hook-vault-api: false

# Whether save task should run after plugin enable
# This is useful in situations like crashes etc.
# If you have this option disabled - player data like their balance
# will only save in certain situations
# Default: true
auto-data-save: true

# Configuration for commands aliases
commands-aliases:
  # Admin command
  wallet-admin:
    command: 'wadmin'
    aliases:
      - 'walletadmin'
      - 'wa'
  # Balance command
  wallet:
    command: 'wallet'
    aliases:
      - 'balance'
      - 'openwallet'
  # Shop command
  shop:
    command: 'shop'
    aliases:
      - 'shops'
      - 'walletshop'
      - 'store'
```
</details>
<details>
  <summary>messages.yml</summary>
  
```yaml
# Configuration file version (don't change manually)
config-version: 1

# Output messages for /wallet command
wallet-command:
  - ""
  - " &e&lWallet:"
  - " &7Your current balance: &6{BALANCE}"
  - ""

# Output messages for /wadmin command
wadmin-command:
  - "&8&l---------------------------------"
  - "&6/wadmin add <player> <amount> &8- &7gives balance to player"
  - "&6/wadmin set <player> <amount> &8- &7sets balance from player"
  - "&6/wadmin take <player> <amount> &8- &7takes balance from player"
  - "&6/wadmin check <player> &8- &7gets player balance"
  - "&6/wadmin panel &8- &7opens configuration panel"
  - "&8&l---------------------------------"

# Error messages
errors:
  unknown-user: "&cThis player doesn't exist!"
  player-only: "&cYou can't execute this command as console!"
  no-permission: "&cYou don't have access to this command!"
  incorrect-usage: "&cIncorrect usage!"
  invalid-number: "&cArgument must be a number!"
  too-much-value: "&cTarget player doesn't have that much balance!"
  not-enough-balance: "&cYou don't have enough balance to buy this!"
  already-owned: "&cYou can't buy this again!"
  shop-not-exists: "&cInvalid shop provided!"

# Other messages
balance-gave: "&aYou gave &6{BALANCE} &ato &6{PLAYER} &aaccount."
balance-set: "&aYou set &6{BALANCE} &afor &6{PLAYER} &aaccount."
balance-took: "&aYou took &6{BALANCE} &afrom &6{PLAYER} &aaccount."
balance-check: "&aPlayer &6{PLAYER} &abalance is: &6{BALANCE}"
successfully-purchased: "&aSuccessfully purchased {PRODUCT}"
```
</details>
<details>
  <summary>confirmation-menu.yml</summary>
  
```yaml
# Settings for confirmation menu
menu-settings:
  # Title of the menu
  gui-title: "Confirm your purchase"
  # Number of menu rows
  gui-size: 3
  # Accept button material
  accept-item: "GREEN_WOOL"
  # Cancel button material
  cancel-item: "RED_WOOL"
  # Accept button name and lore
  accept-name: "&aConfirm purchase"
  accept-lore:
    - "&7Warning! This action cannot"
    - "&7be undone"
    - " "
    - "&eClick to purchase!"
  # Cancel button name and lore
  cancel-name: "&cCancel purchase"
  cancel-lore:
    - " "
    - "&eClick to cancel!"
  # Slots of items
  accept-item-slot: 11
  cancel-item-slot: 15
```
</details>
<details>
  <summary>example-shop.yml</summary>
  
```yaml
# This is example shop file
name: "example-shop"
# Title of the menu
shop-title: "Example Shop"
# Number of menu rows
shop-size: 5
# Products configuration
products:
  # First product is VIP rank
  rank-vip:
    # Name of the item
    item-name: "&a[VIP] Rank"
    # Lore of the item
    item-lore:
      - "&7Get better perks on our network"
      - "&7with this exclusive rank"
      - ""
      - "&7Cost: &61000"
      - ""
      - "&eClick to purchase!"
    # Slot of the item
    item-slot: 11
    # Material of the item
    item-material: EMERALD_BLOCK
    # Cost of this product
    cost: 1000
    # List of commands to execute after purchase
    # You can use {PLAYER} for player name
    commands:
      # This permission makes that player cannot buy this product again
      - "lp user {PLAYER} permission set openwallet.buyonce.rank-vip"
      # This command will give rank to player
      - "lp user {PLAYER} parent set vip"
```
</details>

----
## Links
* [Discord Server](https://discord.gg/k69htTFCVk)
* [Spigot](https://www.spigotmc.org/resources/openwallet-economy-plugin.107826/)
* [Modrinth](https://modrinth.com/plugin/openwallet)
* [Polymart](https://polymart.org/resource/openwallet-economy-plugin.3374)
* [Wiki](https://github.com/neziw/OpenWallet/wiki)

----
## Other Dependencies usages
  * [BoostedYAML](https://github.com/dejvokep/boosted-yaml) - The Best library 💗 for YAML configs
  * [Lombok](https://projectlombok.org/) - Awesome annotations for Java
  * [Triumph-GUI](https://github.com/TriumphTeam/triumph-gui) - Simple creation of guis
  * [IridiumColorAPI](https://github.com/Iridium-Development/IridiumColorAPI) - Great library for translating RGB messages

----
