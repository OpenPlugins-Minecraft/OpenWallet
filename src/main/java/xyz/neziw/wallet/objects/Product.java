package xyz.neziw.wallet.objects;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class Product {

    @Getter
    private final String product;
    @Getter
    private final String name;
    @Getter
    @Setter
    private List<String> lore = new ArrayList<>();
    @Getter
    private final int slot;
    @Getter
    private final Material material;
    @Getter
    private final double cost;
    @Getter
    @Setter
    private List<String> commands = new ArrayList<>();

    public Product(String product, String name, int slot, String material, double cost) {
        this.product = product;
        this.name = name;
        this.slot = slot;
        this.material = Material.valueOf(material);
        this.cost = cost;
    }
}