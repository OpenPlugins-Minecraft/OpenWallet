package xyz.neziw.wallet.listeners;

import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.managers.DatabaseManager;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.objects.Product;
import xyz.neziw.wallet.objects.Shop;
import xyz.neziw.wallet.objects.WalletUser;

@AllArgsConstructor
public class PlayerJoinListener implements Listener {

    private final UserManager userManager;
    private final DatabaseManager databaseManager;
    private final YamlDocument config;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        this.userManager.createUser(player.getUniqueId());
        WalletUser user = this.userManager.getUser(player.getUniqueId());
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
    }

    @EventHandler
    public void test(AsyncPlayerChatEvent event) {
        if (event.getMessage().equalsIgnoreCase("debug")) {
            event.setCancelled(true);
            Shop shop = WalletPlugin.getInstance().getShopsLoader().getShops().get("example-shop");
            for (Product product : shop.getProducts().values()) {
                event.getPlayer().sendMessage(product.getLore().toString());
            }
        }
    }
}