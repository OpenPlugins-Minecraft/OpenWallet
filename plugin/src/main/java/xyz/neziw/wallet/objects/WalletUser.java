package xyz.neziw.wallet.objects;

import lombok.Setter;
import org.bukkit.entity.Player;
import xyz.neziw.wallet.model.Transaction;
import xyz.neziw.wallet.model.User;

import java.util.List;
import java.util.UUID;

public class WalletUser implements User {

    private final UUID uuid;
    private final String name;
    @Setter private Player player;
    @Setter private double balance;
    @Setter private List<Transaction> transactions;

    public WalletUser(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.player = null;
        this.balance = 0.0D;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public List<Transaction> getTransactions() {
        return this.transactions;
    }
}