package xyz.neziw.wallet.listener;

import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.manager.DatabaseManager;
import xyz.neziw.wallet.manager.UserManager;
import xyz.neziw.wallet.object.WalletUser;

@AllArgsConstructor
public class PlayerQuitListener implements Listener {

    private final UserManager userManager;
    private final DatabaseManager databaseManager;
    private final WalletPlugin plugin;

    @SuppressWarnings("unused")
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final WalletUser user = this.userManager.getUser(player.getUniqueId());
        if(user == null){
            plugin.getLogger().info("Player " + player.getName() + " not found! Skipping save.");
            return;
        }
        this.databaseManager.saveUser(user);
        this.userManager.removeUser(player.getUniqueId());
    }
}