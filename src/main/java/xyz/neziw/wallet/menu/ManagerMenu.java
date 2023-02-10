package xyz.neziw.wallet.menu;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.basic.PlayerInput;
import xyz.neziw.wallet.loaders.ShopsLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static xyz.neziw.wallet.utilities.ColorUtils.fix;
import static xyz.neziw.wallet.utilities.ColorUtils.fixList;

public class ManagerMenu {

    private final WalletPlugin plugin = WalletPlugin.getPlugin(WalletPlugin.class);
    private final YamlDocument config = plugin.getMainConfig();
    private final ShopsLoader shopsLoader = plugin.getShopsLoader();

    @SuppressWarnings("deprecation")
    public void open(Player player) {
        final Gui gui = Gui.gui()
                .title(Component.text("OpenWallet Configuration"))
                .rows(6)
                .create();
        gui.getInventory().clear();
        gui.setDefaultClickAction(event -> event.setCancelled(true));
        gui.getFiller().fillBorder(Arrays.asList(
                ItemBuilder.from(Material.LIME_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem(),
                ItemBuilder.from(Material.RED_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem(),
                ItemBuilder.from(Material.BLUE_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem(),
                ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem(),
                ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem(),
                ItemBuilder.from(Material.PURPLE_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem(),
                ItemBuilder.from(Material.PINK_STAINED_GLASS_PANE).name(Component.text(" ")).asGuiItem()
        ));
        final GuiItem startBalance = ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjA5Mjk5YTExN2JlZTg4ZDMyNjJmNmFiOTgyMTFmYmEzNDRlY2FlMzliNDdlYzg0ODEyOTcwNmRlZGM4MWU0ZiJ9fX0=")
                .setName(fix("&aStarting balance"))
                .setLore(fixList(Arrays.asList(
                        "&7This is amount of balance that player",
                        "&7who never played will get at first join",
                        "",
                        "&7Current value: &6" + this.config.getDouble("start-balance"),
                        "",
                        "&eClick to change this value!"
                )))
                .asGuiItem(event -> {
                    gui.close(player);
                    new PlayerInput(player, input -> {
                        final double value = Double.parseDouble(input);
                        this.config.set("start-balance", value);
                        try {
                            this.config.save();
                        } catch (IOException ignored) { }
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                        gui.open(player);
                        gui.update();
                    }, input -> {
                        player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 5);
                        gui.open(player);
                        gui.update();
                    });
                });
        gui.setItem(20, startBalance);
        final GuiItem placeholderHook;
        if (this.config.getBoolean("hook-placeholder-api")) {
            placeholderHook = ItemBuilder.skull()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc0NzJkNjA4ODIxZjQ1YTg4MDUzNzZlYzBjNmZmY2I3ODExNzgyOWVhNWY5NjAwNDFjMmEwOWQxMGUwNGNiNCJ9fX0=")
                    .setName(fix("&aPlaceholderAPI hook"))
                    .setLore(fixList(Arrays.asList(
                            "&7This option makes OpenWallet hook into PlaceholderAPI",
                            "&7on startup and implement some placeholders",
                            "",
                            "&7Current value: &aEnabled",
                            "",
                            "&eClick to toggle this option!"
                    )))
                    .asGuiItem(event -> {
                        this.config.set("hook-placeholder-api", false);
                        try {
                            this.config.save();
                        } catch (IOException ignored) { }
                        player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 5);
                        gui.open(player);
                        gui.update();
                    });
            gui.setItem(23, placeholderHook);
        } else {
            placeholderHook = ItemBuilder.skull()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk1M2IxMmEwOTQ2YjYyOWI0YzA4ODlkNDFmZDI2ZWQyNmZiNzI5ZDRkNTE0YjU5NzI3MTI0YzM3YmI3MGQ4ZCJ9fX0=")
                    .setName(fix("&aPlaceholderAPI hook"))
                    .setLore(fixList(Arrays.asList(
                            "&7This option makes OpenWallet hook into PlaceholderAPI",
                            "&7on startup and implement some placeholders",
                            "",
                            "&7Current value: &cDisabled",
                            "",
                            "&eClick to toggle this option!"
                    )))
                    .asGuiItem(event -> {
                        this.config.set("hook-placeholder-api", true);
                        try {
                            this.config.save();
                        } catch (IOException ignored) { }
                        player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 5);
                        //gui.open(player);
                        gui.update();
                    });
            gui.setItem(23, placeholderHook);
        }
        final GuiItem gadgetsMenuHook;
        if (this.config.getBoolean("hook-gadgets-menu")) {
            gadgetsMenuHook = ItemBuilder.skull()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzc0NzJkNjA4ODIxZjQ1YTg4MDUzNzZlYzBjNmZmY2I3ODExNzgyOWVhNWY5NjAwNDFjMmEwOWQxMGUwNGNiNCJ9fX0=")
                    .setName(fix("&aGadgetsMenu hook &cNOT AVAILABLE"))
                    .setLore(fixList(Arrays.asList(
                            "&7This option makes OpenWallet hook into GadgetsMenu",
                            "&7on startup and override it's economy",
                            "",
                            "&7Current value: &aEnabled",
                            "",
                            "&eClick to toggle this option!"
                    )))
                    .asGuiItem(event -> {
                        this.config.set("hook-gadgets-menu", false);
                        try {
                            this.config.save();
                        } catch (IOException ignored) { }
                        player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 5);
                        gui.open(player);
                        gui.update();
                    });
            gui.setItem(24, gadgetsMenuHook);
        } else {
            gadgetsMenuHook = ItemBuilder.skull()
                    .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjk1M2IxMmEwOTQ2YjYyOWI0YzA4ODlkNDFmZDI2ZWQyNmZiNzI5ZDRkNTE0YjU5NzI3MTI0YzM3YmI3MGQ4ZCJ9fX0=")
                    .setName(fix("&aGadgetsMenu hook &cNOT AVAILABLE"))
                    .setLore(fixList(Arrays.asList(
                            "&7This option makes OpenWallet hook into GadgetsMenu",
                            "&7on startup and override it's economy",
                            "",
                            "&7Current value: &cDisabled",
                            "",
                            "&eClick to toggle this option!"
                    )))
                    .asGuiItem(event -> {
                        this.config.set("hook-gadgets-menu", true);
                        try {
                            this.config.save();
                        } catch (IOException ignored) { }
                        player.playSound(player.getLocation(), Sound.BLOCK_IRON_TRAPDOOR_OPEN, 1, 5);
                        gui.open(player);
                        gui.update();
                    });
            gui.setItem(24, gadgetsMenuHook);
        }
        final GuiItem shops = ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQwMDVlYmJmOTgxN2Q2OTI4ZGU4YmM1ZjdkMWMzODkyNzYwMjBhYzg3NjQ3ZDI4YWI4Zjk5ZWIzOWZmZGU3NiJ9fX0=")
                .setName(fix("&aLoaded shops"))
                .setLore(fixList(this.getShopsList()))
                .asGuiItem();
        gui.setItem(29, shops);
        final GuiItem reload = ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTRkNDliYWU5NWM3OTBjM2IxZmY1YjJmMDEwNTJhNzE0ZDYxODU0ODFkNWIxYzg1OTMwYjNmOTlkMjMyMTY3NCJ9fX0=")
                .setName(fix("&aReload configurations"))
                .setLore(fixList(Arrays.asList("", "&eClick to reload configs!")))
                .asGuiItem(event -> {
                    try {
                        gui.close(player);
                        this.plugin.getMessagesConfig().reload();
                        this.config.reload();
                        this.shopsLoader.reload();
                        this.plugin.getConfirmationMenuConfig().reload();
                        player.sendMessage(fix("&aReloaded"));
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 5);
                    } catch (IOException ignored) { }
                });
        gui.setItem(30, reload);
        final GuiItem saveTask = ItemBuilder.skull()
                .texture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ4NmRiOWExNGQ1ODc5ZmEyODExZDMwMWNjYmQ1MjY5OTRmODcxMjQ3YjYyZjJkOWE0ODE4M2U5NjQxYWQ2OSJ9fX0=")
                .setName(fix("&bAuto-save is currently " + (this.config.getBoolean("auto-data-save") ? "&a&lENABLED" : "&c&lDISABLED")))
                .setLore(fixList(Arrays.asList("", "&7Player data will automatically save", "&7every 5 minutes to the database", "")))
                .asGuiItem();
        gui.setItem(21, saveTask);
        gui.open(player);
    }

    private List<String> getShopsList() {
        final List<String> list = new ArrayList<>();
        list.add("&7List of currently loaded shops:");
        list.add("");
        for (String string : this.shopsLoader.getShops().keySet()) {
            list.add("&8- &a" + string);
        }
        list.add("");
        return list;
    }
}