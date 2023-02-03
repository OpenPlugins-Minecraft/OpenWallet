package xyz.neziw.wallet.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.hooks.IHook;
import xyz.neziw.wallet.hooks.impl.GadgetsMenuHook;
import xyz.neziw.wallet.hooks.impl.PlaceholderHook;
import xyz.neziw.wallet.hooks.impl.VaultHook;

import java.util.HashMap;
import java.util.Map;

public class HookManager {

    @Getter
    private final Map<Class<?>, IHook> hooks = new HashMap<>();
    private final WalletPlugin plugin;

    public HookManager(WalletPlugin plugin, YamlDocument config) {
        this.plugin = plugin;
        if (config.getBoolean("hook-placeholder-api")) {
            this.hooks.put(PlaceholderHook.class, new PlaceholderHook());
        }
        if (config.getBoolean("hook-gadgets-menu")) {
            this.hooks.put(GadgetsMenuHook.class, new GadgetsMenuHook());
        }
        if (config.getBoolean("hook-vault-api")) {
            final VaultHook vaultHook = new VaultHook(this.plugin);
            this.plugin.getServer().getServicesManager().register(Economy.class, vaultHook, this.plugin, ServicePriority.High);
            this.hooks.put(VaultHook.class, vaultHook);
        }
        this.startHooks();
    }

    private void startHooks() {
        for (IHook hook : this.hooks.values()) {
            hook.hook(this.plugin);
        }
    }
}