package xyz.neziw.wallet.hook;

import xyz.neziw.wallet.WalletPlugin;

public interface IHook {

    void hook(WalletPlugin plugin);
    @SuppressWarnings("unused")
    void unHook();
}