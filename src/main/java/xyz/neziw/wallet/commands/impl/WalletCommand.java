package xyz.neziw.wallet.commands.impl;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.commands.WCommand;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.objects.WalletUser;

import java.util.Collections;
import java.util.List;

import static xyz.neziw.wallet.utilities.ColorUtils.fix;

public class WalletCommand extends WCommand {

    private final UserManager userManager;
    private final YamlDocument messages;

    public WalletCommand(UserManager userManager, YamlDocument config, YamlDocument messages) {
        super(config.getString("commands-aliases.wallet.command"), "", config.getStringList("commands-aliases.wallet.aliases"));
        this.userManager = userManager;
        this.messages = messages;
    }

    @Override
    public void exec(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            final Player player = (Player) sender;
            final WalletUser user = this.userManager.getUser(player.getUniqueId());
            for (String string : this.messages.getStringList("wallet-command")) {
                player.sendMessage(fix(string).replace("{BALANCE}", String.valueOf(user.getBalance())));
            }
        } else {
            sender.sendMessage(fix(this.messages.getString("errors.player-only")));
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
}