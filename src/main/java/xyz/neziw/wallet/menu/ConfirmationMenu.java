package xyz.neziw.wallet.menu;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.objects.Product;
import xyz.neziw.wallet.objects.Shop;
import xyz.neziw.wallet.objects.WalletUser;
import xyz.neziw.wallet.utilities.DataUtils;

import java.util.List;

import static xyz.neziw.wallet.utilities.ColorUtils.fix;
import static xyz.neziw.wallet.utilities.ColorUtils.fixList;

public class ConfirmationMenu {

    private final WalletPlugin plugin = WalletPlugin.getInstance();
    private final YamlDocument menuConfig = plugin.getConfirmationMenuConfig();
    private final YamlDocument messages = plugin.getMessagesConfig();
    @SuppressWarnings("FieldCanBeLocal")
    private final UserManager userManager = plugin.getUserManager();

    private final Product product;
    private final Shop shop;
    private final WalletUser user;

    public ConfirmationMenu(Product product, Shop shop, Player player) {
        this.product = product;
        this.shop = shop;
        this.user = this.userManager.getUser(player.getUniqueId());
        this.open(player);
    }

    @SuppressWarnings("deprecation")
    private void open(Player player) {
        final Gui gui = Gui.gui()
                .title(Component.text(this.menuConfig.getString("menu-settings.gui-title")))
                .rows(this.menuConfig.getInt("menu-settings.gui-size"))
                .create();
        gui.getInventory().clear();
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        final GuiItem acceptItem = ItemBuilder.from(Material.valueOf(this.menuConfig.getString("menu-settings.accept-item")))
                .setName(fix(this.menuConfig.getString("menu-settings.accept-name")))
                .setLore(fixList(this.menuConfig.getStringList("menu-settings.accept-lore")))
                .asGuiItem(event -> {
                    if (player.hasPermission("openwallet.buyonce." + this.product.getProduct())) {
                        player.sendMessage(fix(this.messages.getString("errors.already-owned")));
                        gui.close(player);
                        return;
                    }
                    if (this.user.getBalance() < this.product.getCost()) {
                        player.sendMessage(fix(this.messages.getString("errors.not-enough-balance")));
                    } else {
                        player.sendMessage(fix(this.messages.getString("successfully-purchased")
                                .replace("{PRODUCT}", this.product.getName())
                        ));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 8);
                        DataUtils.withDrawBalance(player.getName(), product.getCost());
                        this.runCommands(this.product.getCommands(), player);
                    }
                });
        gui.setItem(this.menuConfig.getInt("menu-settings.accept-item-slot"), acceptItem);
        final GuiItem cancelItem = ItemBuilder.from(Material.valueOf(this.menuConfig.getString("menu-settings.cancel-item")))
                .setName(fix(this.menuConfig.getString("menu-settings.cancel-name")))
                .setLore(fixList(this.menuConfig.getStringList("menu-settings.cancel-lore")))
                .asGuiItem(event -> {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    new ShopMenu(this.shop, player);
                });
        gui.setItem(this.menuConfig.getInt("menu-settings.cancel-item-slot"), cancelItem);
        gui.open(player);
    }

    private void runCommands(List<String> commands, Player player) {
        this.plugin.getServer().getScheduler().runTask(this.plugin, () -> commands.forEach(command -> Bukkit.dispatchCommand(
                this.plugin.getServer().getConsoleSender(),
                command.replace("{PLAYER}", player.getName())
        )));
    }
}