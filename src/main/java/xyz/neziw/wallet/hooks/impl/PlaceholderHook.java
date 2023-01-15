package xyz.neziw.wallet.hooks.impl;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.hooks.IHook;
import xyz.neziw.wallet.utilities.DataUtils;

public class PlaceholderHook extends PlaceholderExpansion implements IHook {

    private WalletPlugin plugin;

    @Override
    public void hook(WalletPlugin plugin) {
        this.plugin = plugin;
        this.register();
    }

    @Override
    public void unHook() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getIdentifier() {
        return "wallet";
    }

    @Override
    public String getAuthor() {
        return this.plugin.getDescription().getAuthors().toString();
    }

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
            return String.valueOf(DataUtils.getBalance(p.getName()));
        }
        return "UNKNOWN_PLACEHOLDER_OR_PLAYER";
    }
}