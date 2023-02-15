package xyz.neziw.wallet.objects;

import org.bukkit.entity.Player;
import xyz.neziw.wallet.model.User;

import java.util.List;
import java.util.UUID;

public class WalletUser implements User {

    private final UUID uuid;
    private String name;
    private Player player;
    private double balance;
    private final List<>

    public WalletUser(UUID uuid) {
        this.uuid = uuid;
        this.name = null;
        this.player = null;
        this.balance = 0.0D;
    }

    @Override
    public UUID getUUID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}