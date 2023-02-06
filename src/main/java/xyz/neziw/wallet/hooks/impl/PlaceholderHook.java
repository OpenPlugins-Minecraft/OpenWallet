package xyz.neziw.wallet.hooks.impl;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.hooks.IHook;
import xyz.neziw.wallet.managers.DataManager;

public class PlaceholderHook extends PlaceholderExpansion implements IHook {

    private WalletPlugin plugin;
    private DataManager dataManager;

    @Override
    public void hook(WalletPlugin plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getDataManager();
        this.register();
    }

    @Override
    public void unHook() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @NotNull
    @Override
    public String getIdentifier() {
        return "wallet";
    }

    @NotNull
    @Override
    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

    @NotNull
    @Override
    public String getVersion() {
        return this.plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return this.plugin != null;
    }

    @Override
    public String onRequest(OfflinePlayer p, String params) {
        if (params.equals("balance")) {
            return String.valueOf(this.dataManager.getBalance(p.getName()));
        }
        return "UNKNOWN_PLACEHOLDER_OR_PLAYER";
    }
}