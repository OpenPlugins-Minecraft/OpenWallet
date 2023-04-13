package xyz.neziw.wallet.menu;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.object.Product;
import xyz.neziw.wallet.object.Shop;

import static xyz.neziw.wallet.utility.ColorUtils.fix;
import static xyz.neziw.wallet.utility.ColorUtils.fixList;

public class ShopMenu {

    private final Shop shop;

    public ShopMenu(Shop shop, Player player) {
        this.shop = shop;
        this.open(player);
    }

    @SuppressWarnings("deprecation")
    private void open(Player player) {
        final Gui gui = Gui.gui()
                .title(Component.text(this.shop.getTitle()))
                .rows(this.shop.getSize())
                .create();
        gui.getInventory().clear();
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        for (Product product : this.shop.getProducts().values()) {
            final GuiItem guiItem;
            guiItem = ItemBuilder.from(product.getMaterial()).setName(fix(product.getName()))
                    .setLore(fixList(product.getLore()))
                    .asGuiItem(event -> new ConfirmationMenu(product, this.shop, player));
            gui.setItem(product.getSlot(), guiItem);
        }
        gui.open(player);
    }
}