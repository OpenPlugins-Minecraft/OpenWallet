package xyz.neziw.wallet;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import org.bukkit.Bukkit;
import xyz.neziw.wallet.factory.DataFactory;
import xyz.neziw.wallet.managers.DatabaseManager;
import xyz.neziw.wallet.managers.SimpleUserManager;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.objects.WalletUser;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WalletPlugin extends WalletAPI {

    private YamlDocument mainConfig;
    private YamlDocument commandsConfig;
    private YamlDocument confirmationMenuConfig;
    private YamlDocument creatorCodesConfig;
    private YamlDocument hooksConfig;
    private YamlDocument languageConfig;
    private YamlDocument shopsMenuConfig;
    private YamlDocument vouchersConfig;
    private DatabaseManager databaseManager;
    private UserManager userManager;

    @Getter private ExecutorService executorService;

    @Override
    public void onEnable() {
        this.loadConfigs();

        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactoryBuilder()
                .setNameFormat("OpenWallet-ThreadPool-%d").build());

        this.databaseManager = new DatabaseManager();
        this.userManager = new UserManager();
    }

    @Override
    public void onDisable() {

    }

    private void loadConfigs() {
        final UpdaterSettings settings = UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build();
        try {
            this.mainConfig = YamlDocument.create(new File(this.getDataFolder(), "main-config.yml"), getResource("main-config.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, settings);
            this.commandsConfig = YamlDocument.create(new File(this.getDataFolder(), "commands.yml"), getResource("commands.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, settings);
            this.confirmationMenuConfig = YamlDocument.create(new File(this.getDataFolder(), "confirmation-menu.yml"), getResource("confirmation-menu.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, settings);
            this.creatorCodesConfig = YamlDocument.create(new File(this.getDataFolder(), "creator-codes.yml"), getResource("creator-codes.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, settings);
            this.hooksConfig = YamlDocument.create(new File(this.getDataFolder(), "hooks.yml"), getResource("hooks.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, settings);
            this.shopsMenuConfig = YamlDocument.create(new File(this.getDataFolder(), "shops-menu.yml"), getResource("shops-menu.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, settings);
            this.vouchersConfig = YamlDocument.create(new File(this.getDataFolder(), "vouchers.yml"), getResource("vouchers.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, settings);
            this.languageConfig = YamlDocument.create(new File(this.getDataFolder(), "lang/" + this.mainConfig.getString("language-file")),
                    getResource("lang/language-file"), GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(),
                    DumperSettings.DEFAULT, settings);
        } catch (IOException exception) {
            throw new RuntimeException("failed to load configurations", exception);
        }
    }

    @Override
    public SimpleUserManager<UUID, WalletUser> getUserManager() {
        return this.userManager;
    }

    @Override
    public DataFactory<?> dataFactory() {
        return null;
    }

    @Override
    public YamlDocument getCommandsConfig() {
        return this.commandsConfig;
    }

    @Override
    public YamlDocument getConfirmationMenuConfig() {
        return this.confirmationMenuConfig;
    }

    @Override
    public YamlDocument getCreatorCodesConfig() {
        return this.creatorCodesConfig;
    }

    @Override
    public YamlDocument getHooksConfig() {
        return this.hooksConfig;
    }

    @Override
    public YamlDocument getLanguageConfig() {
        return this.languageConfig;
    }

    @Override
    public YamlDocument getShopsMenuConfig() {
        return this.shopsMenuConfig;
    }

    @Override
    public YamlDocument getVouchersConfig() {
        return this.vouchersConfig;
    }
}