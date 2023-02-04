package xyz.neziw.wallet.listeners;

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
import xyz.neziw.wallet.managers.DatabaseManager;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.objects.WalletUser;
import xyz.neziw.wallet.basic.UpdateChecker;
import xyz.neziw.wallet.utilities.ColorUtils;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final UserManager userManager;
    private final DatabaseManager databaseManager;
    private final YamlDocument config;
    private final WalletPlugin plugin;
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final String prefix = "&b[&fOpen&aWallet&b] ";
        final int resourceId = 107826;
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
        if (player.isOp()){
            new UpdateChecker(plugin, resourceId).getVersion(version -> {
                if (!(plugin.getDescription().getVersion().equals(version))) {
                    player.sendMessage(ColorUtils.fix(prefix + "&bThere is a new update available."));
                    TextComponent message = new TextComponent(ColorUtils.fix(prefix + "&bYour version &c" + plugin.getDescription().getVersion() + "&b new version &c" + version));
                    message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, "https://www.spigotmc.org/resources/107826"));
                    message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Copy download link").create()));
                    player.spigot().sendMessage(message);
                }
            });
        }
    }
}