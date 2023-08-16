package xyz.neziw.wallet.manager;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.object.WalletUser;
import xyz.neziw.wallet.utility.FormatUtil;

@RequiredArgsConstructor
public class DataManager {

    private final UserManager userManager;
    private final DatabaseManager databaseManager;

    public void setBalance(final String name, final double balance) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = this.userManager.getUser(player.getUniqueId());
            user.setBalance(balance);
            this.databaseManager.saveUser(user);
        } else {
            this.databaseManager.setBalanceByName(name, balance);
        }
    }

    public void depositBalance(final String name, final double balance) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = this.userManager.getUser(player.getUniqueId());
            user.setBalance(user.getBalance() + balance);
            this.databaseManager.saveUser(user);
        } else {
            this.databaseManager.deposit(name, balance);
        }
    }

    public void withDrawBalance(final String name, final double balance) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = this.userManager.getUser(player.getUniqueId());
            user.setBalance(user.getBalance() - balance);
            this.databaseManager.saveUser(user);
        } else {
            this.databaseManager.withDraw(name, balance);
        }
    }

    public double getBalance(final String name) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null) {
            final WalletUser user = this.userManager.getUser(player.getUniqueId());
            return FormatUtil.format(user.getBalance() , 2);
        } else {
            return FormatUtil.format(this.databaseManager.getBalanceByName(name) , 2);
        }
    }

    public boolean exists(final String name) {
        return this.databaseManager.exists(name);
    }
}