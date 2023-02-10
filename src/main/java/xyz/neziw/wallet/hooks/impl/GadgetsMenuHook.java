package xyz.neziw.wallet.hooks.impl;

import com.yapzhenyie.GadgetsMenu.economy.GEconomyProvider;
import com.yapzhenyie.GadgetsMenu.player.OfflinePlayerManager;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.hooks.IHook;
import xyz.neziw.wallet.managers.DataManager;

public class GadgetsMenuHook extends GEconomyProvider implements IHook {

    private DataManager dataManager;

    public GadgetsMenuHook(WalletPlugin plugin) {
        super(plugin, "OpenWallet-Storage");
    }

    @Override
    public void hook(WalletPlugin plugin) {
        this.dataManager = plugin.getDataManager();
        GEconomyProvider.setMysteryDustStorage(this);
    }

    @Override
    public void unHook() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getMysteryDust(OfflinePlayerManager offlinePlayerManager) {
        return (int) this.dataManager.getBalance(offlinePlayerManager.getName());
    }

    @Override
    public boolean addMysteryDust(OfflinePlayerManager offlinePlayerManager, int i) {
        this.dataManager.depositBalance(offlinePlayerManager.getName(), i);
        return true;
    }

    @Override
    public boolean removeMysteryDust(OfflinePlayerManager offlinePlayerManager, int i) {
        this.dataManager.withDrawBalance(offlinePlayerManager.getName(), i);
        return true;
    }

    @Override
    public boolean setMysteryDust(OfflinePlayerManager offlinePlayerManager, int i) {
        this.dataManager.setBalance(offlinePlayerManager.getName(), i);
        return true;
    }
}