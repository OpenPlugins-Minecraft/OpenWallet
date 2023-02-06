package xyz.neziw.wallet.commands.impl;

import com.google.common.collect.ImmutableList;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.commands.WCommand;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.menu.ManagerMenu;
import xyz.neziw.wallet.managers.DataManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static xyz.neziw.wallet.utilities.ColorUtils.fix;

public class WalletAdminCommand extends WCommand {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final UserManager userManager;
    private final YamlDocument messages;
    private final DataManager dataManager;

    public WalletAdminCommand(UserManager userManager, YamlDocument config, YamlDocument messages, DataManager dataManager) {
        super(config.getString("commands-aliases.wallet-admin.command"), "", config.getStringList("commands-aliases.wallet-admin.aliases"));
        this.userManager = userManager;
        this.messages = messages;
        this.dataManager = dataManager;
    }

    @Override
    public void exec(CommandSender sender, String[] args) {
        if (sender.hasPermission("openwallet.admin")) {
            if (args.length == 0) {
                for (String string : this.messages.getStringList("wadmin-command")) {
                    sender.sendMessage(fix(string));
                }
            } else if (args.length == 1 && args[0].equals("panel")) {
                if (sender instanceof Player) {
                    new ManagerMenu().open((Player) sender);
                } else {
                    sender.sendMessage(fix(this.messages.getString("errors.player-only")));
                }
            } else if (args.length == 2 && args[0].equals("check")) {
                final String target = args[1];
                if (this.dataManager.exists(target)) {
                    sender.sendMessage(fix(this.messages.getString("balance-check")
                            .replace("{PLAYER}", target)
                            .replace("{BALANCE}", String.valueOf(this.dataManager.getBalance(target)))
                    ));
                } else {
                    sender.sendMessage(fix(this.messages.getString("errors.unknown-user")));
                }
            } else if (args.length >= 3) {
                final String target = args[1];
                switch (args[0]) {
                    case "add":
                        if (this.dataManager.exists(target)) {
                            try {
                                final double balance = Double.parseDouble(args[2]);
                                this.dataManager.depositBalance(target, balance);
                                sender.sendMessage(fix(this.messages.getString("balance-gave")
                                        .replace("{PLAYER}", target)
                                        .replace("{BALANCE}", String.valueOf(balance))
                                ));
                            } catch (NumberFormatException ex) {
                                sender.sendMessage(fix(this.messages.getString("errors.invalid-number")));
                            }
                        } else {
                            sender.sendMessage(fix(this.messages.getString("errors.unknown-user")));
                        }
                        break;
                    case "set":
                        if (this.dataManager.exists(target)) {
                            try {
                                final double balance = Double.parseDouble(args[2]);
                                this.dataManager.setBalance(target, balance);
                                sender.sendMessage(fix(this.messages.getString("balance-set")
                                        .replace("{PLAYER}", target)
                                        .replace("{BALANCE}", String.valueOf(balance))
                                ));
                            } catch (NumberFormatException ex) {
                                sender.sendMessage(fix(this.messages.getString("errors.invalid-number")));
                            }
                        } else {
                            sender.sendMessage(fix(this.messages.getString("errors.unknown-user")));
                        }
                        break;
                    case "take":
                        if (this.dataManager.exists(target)) {
                            try {
                                final double balance = Double.parseDouble(args[2]);
                                final double current = this.dataManager.getBalance(target);
                                if (current < balance) {
                                    sender.sendMessage(fix(this.messages.getString("errors.too-much-value")));
                                } else {
                                    this.dataManager.withDrawBalance(target, balance);
                                    sender.sendMessage(fix(this.messages.getString("balance-took")
                                            .replace("{PLAYER}", target)
                                            .replace("{BALANCE}", String.valueOf(balance))
                                    ));
                                }
                            } catch (NumberFormatException ex) {
                                sender.sendMessage(fix(this.messages.getString("errors.invalid-number")));
                            }
                        } else {
                            sender.sendMessage(fix(this.messages.getString("errors.unknown-user")));
                        }
                        break;
                    default:
                        sender.sendMessage(fix(this.messages.getString("errors.incorrect-usage")));
                        break;
                }
            } else {
                sender.sendMessage(fix(this.messages.getString("errors.incorrect-usage")));
            }
        } else {
            sender.sendMessage(fix(this.messages.getString("errors.no-permission")));
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (!sender.hasPermission("openwallet.admin")) return ImmutableList.of();
        if (args.length == 0 || args.length > 3) {
            return Collections.emptyList();
        }
        final List<String> matches = new ArrayList<>();
        if (args.length == 1) {
            String search = args[0].toLowerCase(Locale.ROOT);
            if ("panel".startsWith(search)) {
                matches.add("panel");
            }
            if ("check".startsWith(search)) {
                matches.add("check");
            }
            if ("add".startsWith(search)) {
                matches.add("add");
            }
            if ("take".startsWith(search)) {
                matches.add("take");
            }
            if ("set".startsWith(search)) {
                matches.add("set");
            }
        }
        if (args.length == 2 && !args[0].equals("panel")) {
            final String search = args[1].toLowerCase(Locale.ROOT);
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase(Locale.ROOT).startsWith(search)) {
                    matches.add(player.getName());
                }
            }
        }
        return matches;
    }
}