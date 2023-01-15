package xyz.neziw.wallet.managers;

import lombok.SneakyThrows;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.SimplePluginManager;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.commands.WCommand;

import java.lang.reflect.Field;

public class CommandManager {

    private final WalletPlugin plugin;
    private final SimpleCommandMap commandMap;
    @SuppressWarnings("FieldCanBeLocal")
    private final SimplePluginManager pluginManager;

    @SneakyThrows
    public CommandManager(WalletPlugin plugin) {
        this.plugin = plugin;
        this.pluginManager = (SimplePluginManager) plugin.getServer().getPluginManager();
        Field field = SimplePluginManager.class.getDeclaredField("commandMap");
        field.setAccessible(true);
        this.commandMap = (SimpleCommandMap) field.get(pluginManager);
    }

    public void registerCommand(WCommand command) {
        this.commandMap.register(this.plugin.getDescription().getName(), command);
    }
}