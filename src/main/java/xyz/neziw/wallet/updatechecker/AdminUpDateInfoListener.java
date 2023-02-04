package xyz.neziw.wallet.updatechecker;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.utilities.MessageApi;

public class AdminUpDateInfoListener implements Listener {

    private final WalletPlugin plugin;
    private final String prefix = "";
    private final int resourceId = 107826;
    public AdminUpDateInfoListener(WalletPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void AdminJoin(final PlayerJoinEvent event){
        final Player player = event.getPlayer();
        if (player.isOp()){
            new UpdateChecker(plugin, resourceId).getVersion(version -> {
                if (!(plugin.getDescription().getVersion().equals(version))) {
                    player.sendMessage(prefix + "§bThere is a new update available.");
                    MessageApi.HoverMessageCopy(player,
                            prefix + "§bYour version " + plugin.getDescription().getVersion() + " new version " + version,
                            "https://www.spigotmc.org/resources/" + resourceId,
                            "Copy download link");
                }
            });
        }
    }
}
