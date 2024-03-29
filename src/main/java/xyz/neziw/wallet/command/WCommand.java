package xyz.neziw.wallet.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import xyz.neziw.wallet.WalletPlugin;

import java.util.List;

public abstract class WCommand extends Command implements PluginIdentifiableCommand {

    protected WCommand(String name, String usage, List<String> aliases) {
        super(name, "OpenWallet plugin command", usage, aliases);
    }

    @NotNull
    @Override
    public Plugin getPlugin() {
        return WalletPlugin.getProvidingPlugin(WalletPlugin.class);
    }

    public abstract void exec(CommandSender sender, String[] args);

    public abstract List<String> complete(CommandSender sender, String[] args);

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        exec(sender, args);
        return true;
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return complete(sender, args);
    }
}