package xyz.neziw.wallet.hooks.impl;

import net.milkbowl.vault.economy.Economy;

public class VaultHook /*extends Economy*/ {

    //@Override
    public boolean hasBankSupport() {
        return false;
    }

}