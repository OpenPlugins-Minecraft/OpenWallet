package xyz.neziw.wallet.listener;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.manager.DatabaseManager;
import xyz.neziw.wallet.manager.UserManager;
import xyz.neziw.wallet.object.WalletUser;
import xyz.neziw.wallet.basic.UpdateChecker;
import xyz.neziw.wallet.utility.ColorUtils;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final WalletPlugin plugin;
    private final UserManager userManager;
    private final DatabaseManager databaseManager;
    private final YamlDocument config;
    
    @SuppressWarnings({"deprecation", "unused"})
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        this.userManager.createUser(player.getUniqueId());
        final WalletUser user = this.userManager.getUser(player.getUniqueId());
        user.setName(player.getName());
        this.databaseManager.getExecutor().execute(() -> {
            if (this.databaseManager.exists(player.getName())) {
                this.databaseManager.loadUser(user);
            } else {
                this.databaseManager.registerUser(player.getUniqueId(), player.getName());
                this.databaseManager.loadUser(user);
            }
        });
        if (!player.hasPlayedBefore()) {
            user.setBalance(this.config.getDouble("start-balance"));
        }
        if (this.config.getBoolean("update-checker") && player.isOp()) {
            new UpdateChecker(plugin, 107826).getVersion(version -> {
                if (!(plugin.getDescription().getVersion().equals(version))) {
                    player.sendMessage(ColorUtils.fix("&b[&fOpen&aWallet&b] &bThere is a new update available."));
                    TextComponent message = new TextComponent(ColorUtils.fix("&b[&fOpen&aWallet&b] Your version &c" + plugin.getDescription().getVersion() + "&b new version &c" + version));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.spigotmc.org/resources/107826"));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Copy download link").create()));
                    player.spigot().sendMessage(message);
                }
            });
        }
    }
}