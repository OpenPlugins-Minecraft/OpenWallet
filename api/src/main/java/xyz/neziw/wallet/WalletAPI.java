package xyz.neziw.wallet;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.neziw.wallet.factory.DataFactory;
import xyz.neziw.wallet.managers.SimpleUserManager;

public abstract class WalletAPI extends JavaPlugin {

    private static WalletAPI instance;

    public static void setInstance(WalletAPI instance) {
        WalletAPI.instance = instance;
    }

    public static WalletAPI getInstance() {
        return instance;
    }

    public abstract SimpleUserManager<?, ?> getUserManager();
    public abstract DataFactory<?> dataFactory();
    public abstract YamlDocument getLanguageConfig();
    public abstract YamlDocument getConfirmationMenuConfig();
    public abstract YamlDocument getShopsMenuConfig();
    public abstract YamlDocument getCommandsConfig();
    public abstract YamlDocument getCreatorCodesConfig();
    public abstract YamlDocument getHooksConfig();
    public abstract YamlDocument getVouchersConfig();
}