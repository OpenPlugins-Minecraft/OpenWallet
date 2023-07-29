package xyz.neziw.wallet;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.neziw.wallet.basic.Metrics;
import xyz.neziw.wallet.basic.PlayerInput;
import xyz.neziw.wallet.basic.UpdateChecker;
import xyz.neziw.wallet.command.impl.ShopCommand;
import xyz.neziw.wallet.command.impl.WalletAdminCommand;
import xyz.neziw.wallet.command.impl.WalletCommand;
import xyz.neziw.wallet.listener.PlayerJoinListener;
import xyz.neziw.wallet.listener.PlayerQuitListener;
import xyz.neziw.wallet.loader.ShopsLoader;
import xyz.neziw.wallet.manager.CommandManager;
import xyz.neziw.wallet.manager.DatabaseManager;
import xyz.neziw.wallet.manager.HookManager;
import xyz.neziw.wallet.manager.UserManager;
import xyz.neziw.wallet.task.SaveTaskRunnable;
import xyz.neziw.wallet.manager.DataManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class WalletPlugin extends JavaPlugin {

    @Getter
    private YamlDocument mainConfig;
    @Getter
    private YamlDocument messagesConfig;
    @Getter
    private YamlDocument confirmationMenuConfig;
    @Getter
    private UserManager userManager;
    @Getter
    private DatabaseManager databaseManager;
    @Getter
    private HookManager hookManager;
    @Getter
    private ShopsLoader shopsLoader;
    @Getter
    private DataManager dataManager;

    @SuppressWarnings("ConstantConditions")
    @SneakyThrows
    @Override
    public void onEnable() {
        this.mainConfig = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                GeneralSettings.builder().setUseDefaults(false).build(),
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
        );
        this.messagesConfig = YamlDocument.create(new File(getDataFolder(), "messages.yml"), getResource("messages.yml"),
                GeneralSettings.builder().setUseDefaults(false).build(),
                LoaderSettings.builder().setAutoUpdate(true).build(),
                DumperSettings.DEFAULT,
                UpdaterSettings.builder().setVersioning(new BasicVersioning("config-version")).build()
        );
        this.confirmationMenuConfig = YamlDocument.create(new File(getDataFolder(), "confirmation-menu.yml"), getResource("confirmation-menu.yml"));
        this.userManager = new UserManager();
        this.databaseManager = new DatabaseManager(this, this.mainConfig);
        this.dataManager = new DataManager(this.userManager, this.databaseManager);
        this.hookManager = new HookManager(this, this.mainConfig);
        this.shopsLoader = new ShopsLoader(this);
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerQuitListener(this.userManager, this.databaseManager, this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this ,this.userManager, this.databaseManager, this.mainConfig), this);
        pluginManager.registerEvents(PlayerInput.handle(), this);
        final CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommand(new WalletCommand(this.userManager, this.mainConfig, this.messagesConfig));
        commandManager.registerCommand(new WalletAdminCommand(this));
        commandManager.registerCommand(new ShopCommand(this.shopsLoader, this.mainConfig, this.messagesConfig));
        if (this.mainConfig.getBoolean("auto-data-save")) {
            getServer().getScheduler().runTaskTimer(this, new SaveTaskRunnable(this.userManager, this.databaseManager),
                    0L, 300 * 20L
            );
        }
        new Metrics(this, 17633);
        this.exampleShop();
        this.shopsLoader.loadShops();
        updateCheck();
    }

    @Override
    public void onDisable() {
        if (this.userManager != null && this.databaseManager != null) {
            this.databaseManager.getExecutor().shutdown();
            this.userManager.getUsers().values().forEach(this.databaseManager::saveUser);
        }
    }

    @SuppressWarnings({"DataFlowIssue", "ResultOfMethodCallIgnored"})
    private void exampleShop() {
        final File file = new File(getDataFolder(), "shops");
        if (!file.exists()) file.mkdirs();
        if (!file.isDirectory()) throw new UnsupportedOperationException("Shops folder is not directory");
        final File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            try (InputStream inputStream = getClass().getResourceAsStream("/shops/example-shop.yml")) {
                final File exampleShop = new File(file, "example-shop.yml");
                Files.copy(inputStream, exampleShop.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    private void updateCheck() {
        if (!this.mainConfig.getBoolean("update-checker")) return;
        new UpdateChecker(this, 107826).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                this.getLogger().info("There is not a new update available.");
            } else {
                this.getLogger().info("There is a new update available.");
                this.getLogger().info("Your version " + this.getDescription().getVersion() + " new version " + version);
            }
        });
    }
}