package xyz.neziw.wallet.objects;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Shop {

    @Getter
    private final String name;
    @Getter
    private final String title;
    @Getter
    private final int size;
    @Getter
    private final FileConfiguration config;
    @Getter
    private final Map<String, Product> products = new HashMap<>();

    public Shop(String name, String title, int size, FileConfiguration config) {
        this.name = name;
        this.title = title;
        this.size = size;
        this.config = config;
    }
}