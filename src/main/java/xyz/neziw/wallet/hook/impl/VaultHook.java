package xyz.neziw.wallet.hook.impl;

import lombok.AllArgsConstructor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.ServicePriority;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.hook.IHook;
import xyz.neziw.wallet.manager.DataManager;
import xyz.neziw.wallet.manager.DatabaseManager;
import xyz.neziw.wallet.manager.UserManager;
import xyz.neziw.wallet.object.WalletUser;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class VaultHook implements IHook, Economy {

    private WalletPlugin plugin;
    private DataManager dataManager;
    private DatabaseManager databaseManager;
    private UserManager userManager;

    @Override
    public void hook(WalletPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getServicesManager().register(Economy.class, this, this.plugin, ServicePriority.High);
    }

    @Override
    public void unHook() {
        throw new UnsupportedOperationException("Not supported yet."); // TODO : maybe do something with this ? !
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        throw new NotImplementedException("\"bankBalance(String name)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
         throw new NotImplementedException("\"bankDeposit(String name, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
         throw new NotImplementedException("\"bankHas(String name, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
         throw new NotImplementedException("\"bankWithdraw(String name, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
         throw new NotImplementedException("\"createBank(String name, OfflinePlayer player)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
         throw new NotImplementedException("\"createBank(String name, String player)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (this.databaseManager.exists(player.getName())) {
            return false;
        } else {
            this.databaseManager.registerUser(player.getUniqueId(), player.getName());
            this.userManager.createUser(player.getUniqueId());
            final WalletUser user = this.userManager.getUser(player.getUniqueId());
            if (this.databaseManager.exists(player.getName())) {
                this.databaseManager.loadUser(user);
                return false;
            } else {
                this.databaseManager.registerUser(player.getUniqueId(), player.getName());
                this.databaseManager.loadUser(user);
                return true;
            }
        }
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        throw new NotImplementedException("\"createPlayerAccount(OfflinePlayer player, String worldName)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        if (this.databaseManager.exists(playerName)) {
            return false;
        } else {
            this.databaseManager.registerUser(Bukkit.getOfflinePlayer(playerName).getUniqueId(), playerName);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
            this.userManager.createUser(offlinePlayer.getUniqueId());
            final WalletUser user = this.userManager.getUser(offlinePlayer.getUniqueId());
            user.setName(playerName);
            if (this.databaseManager.exists(playerName)) {
                this.databaseManager.loadUser(user);
                return false;
            } else {
                this.databaseManager.registerUser(offlinePlayer.getUniqueId(), playerName);
                this.databaseManager.loadUser(user);
                return true;
            }
        }
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        throw new NotImplementedException("\"createPlayerAccount(String playerName, String worldName)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public String currencyNamePlural() {
        return "Balance"; // TODO - maybe configurable ? !
    }

    @Override
    public String currencyNameSingular() {
        return "Balance;"; // TODO - change this to something better
    }

    @Override
    public EconomyResponse deleteBank(String name) {
         throw new NotImplementedException("\"deleteBank(String name)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return this.depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        this.dataManager.depositBalance(player.getName(), amount);
        return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Success");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
         throw new NotImplementedException("\"depositPlayer(OfflinePlayer player, String worldName, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
         throw new NotImplementedException("\"depositPlayer(String playerName, String worldName, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public String format(double amount) {
        return String.format("%." + fractionalDigits() + "f", amount);
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return this.dataManager.getBalance(player.getName());
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return this.dataManager.getBalance(player.getName());
    }

    @Override
    public double getBalance(String playerName) {
        if(this.dataManager.exists(playerName)){
            return this.dataManager.getBalance(playerName);
        }
        else{
            return 0.0D;
        }
    }

    @Override
    public double getBalance(String playerName, String world) {
        throw new NotImplementedException("\"getBalance(String playerName, String world)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public List<String> getBanks() {
         throw new NotImplementedException("\"getBanks()\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean has(String playerName, double amount) {
         throw new NotImplementedException("\"has(String playerName, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return this.dataManager.getBalance(player.getName()) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
         throw new NotImplementedException("\"has(OfflinePlayer player, String worldName, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
         throw new NotImplementedException("\"has(String playerName, String worldName, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return this.databaseManager.exists(player.getName());
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
         throw new NotImplementedException("\"hasAccount(OfflinePlayer player, String worldName)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean hasAccount(String playerName) {
        return this.databaseManager.exists(playerName);
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
         throw new NotImplementedException("\"hasAccount(String playerName, String worldName)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean hasBankSupport() {
         return false;
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
         throw new NotImplementedException("\"isBankMember(String name, OfflinePlayer player)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
         throw new NotImplementedException("\"isBankOwner(String name, OfflinePlayer player)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
         throw new NotImplementedException("\"isBankMember(String name, String playerName)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
         throw new NotImplementedException("\"isBankOwner(String name, String playerName)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public boolean isEnabled() {
        return this.plugin != null;
    }

    @Override
    public String getName() {
        return this.plugin.getName();
    }

    @Override
    public EconomyResponse withdrawPlayer(String string, double amount) {
        if(getBalance(string) >= amount){
            this.dataManager.withDrawBalance(string, amount);
            return new EconomyResponse(amount, getBalance(string), EconomyResponse.ResponseType.SUCCESS, "Success");
        }
        else {
            return new EconomyResponse(amount, getBalance(string), EconomyResponse.ResponseType.FAILURE, "Error");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if(getBalance(player) >= amount){
            this.dataManager.withDrawBalance(player.getName(), amount);
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Success");
        }
        else {
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Error");
        }
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
         throw new NotImplementedException("\"withdrawPlayer(String playerName, String worldName, double amount)\" is not implemented. Report this to " + this.plugin.getName() +" developer!");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        if(getBalance(player) >= amount){
            this.dataManager.withDrawBalance(player.getName(), amount);
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.SUCCESS, "Success");
        }
        else {
            return new EconomyResponse(amount, getBalance(player), EconomyResponse.ResponseType.FAILURE, "Error");
        }
    }
}