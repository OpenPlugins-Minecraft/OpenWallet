package xyz.neziw.wallet.loaders;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.objects.Product;
import xyz.neziw.wallet.objects.Shop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopsLoader {

    private final WalletPlugin plugin;
    @Getter
    private final HashMap<String, Shop> shops = new HashMap<>();

    public ShopsLoader(WalletPlugin plugin) {
        this.plugin = plugin;
        this.loadShops();
    }

    @SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
    public void loadShops() {
        File dataFolder = new File(this.plugin.getDataFolder(), "shops");
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
        List<FileConfiguration> configs = new ArrayList<>();
        for (File file : dataFolder.listFiles()) {
            configs.add(YamlConfiguration.loadConfiguration(file));
        }
        for (FileConfiguration shopConfigs : configs) {
            Shop shop = new Shop(shopConfigs.getString("name"), shopConfigs.getString("shop-title"),
                    shopConfigs.getInt("shop-size"), shopConfigs
            );
            for (String string : shopConfigs.getConfigurationSection("products").getKeys(false)) {
                System.out.println(string);
                Product product = new Product(string, shopConfigs.getString("products." + string + ".item-name"),
                        shopConfigs.getInt("products." + string + ".item-slot"),
                        shopConfigs.getString("products." + string + ".item-material"),
                        shopConfigs.getDouble("products." + string + ".cost")
                        );
                product.setLore(shopConfigs.getStringList("products." + string + ".item-lore"));
                product.setCommands(shopConfigs.getStringList("products." + string + ".commands"));
                shop.getProducts().put(string, product);
            }
            this.shops.put(shopConfigs.getString("name"), shop);
        }
    }

    public void reload() {
        this.unloadShops();
        this.loadShops();
    }

    private void unloadShops() {
        this.shops.clear();
    }
}