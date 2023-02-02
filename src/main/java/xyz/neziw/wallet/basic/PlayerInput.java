package xyz.neziw.wallet.basic;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import xyz.neziw.wallet.WalletPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static xyz.neziw.wallet.utilities.ColorUtils.fix;

public class PlayerInput {

    private static final Map<UUID, PlayerInput> inputs = new HashMap<>();
    private final UUID uuid;
    private final InputRunnable runGo;
    private final InputRunnable runCancel;
    private final BukkitTask taskId;

    public PlayerInput(Player player, InputRunnable correct, InputRunnable cancel) {
        this.uuid = player.getUniqueId();
        this.runGo = correct;
        this.runCancel = cancel;
        this.taskId = new BukkitRunnable() {
            @Override
            public void run() {
                player.sendTitle(fix("&a&lType value"), fix("&7on the chat!"), 0, 21, 0);
            }
        }.runTaskTimer(WalletPlugin.getInstance(), 0L, 20L);
        this.register();
    }

    private void register() {
        inputs.put(this.uuid, this);
    }

    private void unRegister() {
        inputs.remove(this.uuid);
    }

    public static Listener handle() {
        return new Listener() {
            @EventHandler
            public void onChatInput(AsyncPlayerChatEvent event) {
                final Player player = event.getPlayer();
                final String input = event.getMessage();
                final UUID uuid = player.getUniqueId();
                if (inputs.containsKey(uuid)) {
                    final PlayerInput current = inputs.get(uuid);
                    event.setCancelled(true);
                    if (input.equalsIgnoreCase("cancel")) {
                        player.sendMessage(fix("&cCancelled input!"));
                        current.taskId.cancel();
                        player.sendTitle("", "", 0, 5, 0);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(WalletPlugin.getInstance(), () -> current.runCancel.run(input), 3);
                        current.unRegister();
                        return;
                    }
                    current.taskId.cancel();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(WalletPlugin.getInstance(), () -> current.runGo.run(input), 3);
                    player.sendTitle("", "", 0, 5, 0);
                    current.unRegister();
                }
            }
        };
    }
}