package xyz.neziw.wallet.commands.impl;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.commands.WCommand;
import xyz.neziw.wallet.loaders.ShopsLoader;
import xyz.neziw.wallet.menu.ShopMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static xyz.neziw.wallet.utilities.ColorUtils.fix;

public class ShopCommand extends WCommand {

    private final ShopsLoader shopsLoader;
    private final YamlDocument messages;

    public ShopCommand(ShopsLoader shopsLoader, YamlDocument config, YamlDocument messages) {
        super(config.getString("commands-aliases.shop.command"), "", config.getStringList("commands-aliases.shop.aliases"));
        this.shopsLoader = shopsLoader;
        this.messages = messages;
    }

    @Override
    public void exec(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage(fix(this.messages.getString("errors.incorrect-usage")));
            } else {
                if (this.shopsLoader.getShops().get(args[0]) != null) {
                    new ShopMenu(this.shopsLoader.getShops().get(args[0]), player);
                } else {
                    player.sendMessage(fix(this.messages.getString("errors.shop-not-exists")));
                }
            }
        } else {
            sender.sendMessage(fix(this.messages.getString("errors.player-only")));
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        final List<String> matches = new ArrayList<>();
        final String search = args[0].toLowerCase(Locale.ROOT);
        for (String shop : this.shopsLoader.getShops().keySet()) {
            if (shop.toLowerCase(Locale.ROOT).startsWith(search)) {
                matches.add(shop);
            }
        }
        return matches;
    }
}