package xyz.neziw.wallet.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.object.WalletUser;

@RequiredArgsConstructor
public class DataManager {

    private final UserManager userManager;
    private final DatabaseManager databaseManager;

    public void setBalance(String name, double balance) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = userManager.getUser(player.getUniqueId());
            user.setBalance(balance);
            databaseManager.saveUser(user);
        } else {
            databaseManager.setBalanceByName(name, balance);
        }
    }

    public void depositBalance(String name, double balance) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = userManager.getUser(player.getUniqueId());
            user.setBalance(user.getBalance() + balance);
            databaseManager.saveUser(user);
        } else {
            databaseManager.deposit(name, balance);
        }
    }

    public void withDrawBalance(String name, double balance) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = userManager.getUser(player.getUniqueId());
            user.setBalance(user.getBalance() - balance);
            databaseManager.saveUser(user);
        } else {
            databaseManager.withDraw(name, balance);
        }
    }

    public double getBalance(String name) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = userManager.getUser(player.getUniqueId());
            return user.getBalance();
        } else {
            return databaseManager.getBalanceByName(name);
        }
    }

    public boolean exists(String name) {
        return databaseManager.exists(name);
    }
}