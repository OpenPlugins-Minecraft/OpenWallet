package xyz.neziw.wallet.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.managers.DatabaseManager;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.objects.WalletUser;

@UtilityClass
public class DataUtils {

    private static final WalletPlugin plugin = WalletPlugin.getInstance();
    private static final UserManager userManager = plugin.getUserManager();
    private static final DatabaseManager databaseManager = plugin.getDatabaseManager();

    public static void setBalance(String name, double balance) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            WalletUser user = userManager.getUser(player.getUniqueId());
            user.setBalance(balance);
            databaseManager.saveUser(user);
        } else {
            databaseManager.setBalanceByName(name, balance);
        }
    }

    public static void depositBalance(String name, double balance) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            WalletUser user = userManager.getUser(player.getUniqueId());
            user.setBalance(user.getBalance() + balance);
            databaseManager.saveUser(user);
        } else {
            databaseManager.deposit(name, balance);
        }
    }

    public static void withDrawBalance(String name, double balance) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            WalletUser user = userManager.getUser(player.getUniqueId());
            user.setBalance(user.getBalance() - balance);
            databaseManager.saveUser(user);
        } else {
            databaseManager.withDraw(name, balance);
        }
    }

    public static double getBalance(String name) {
        Player player = Bukkit.getPlayer(name);
        if (player != null) {
            WalletUser user = userManager.getUser(player.getUniqueId());
            return user.getBalance();
        } else {
            return databaseManager.getBalanceByName(name);
        }
    }

    public static boolean exists(String name) {
        return databaseManager.exists(name);
    }
}