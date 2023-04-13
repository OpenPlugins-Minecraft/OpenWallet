package xyz.neziw.wallet.object;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class WalletUser {

    @Getter
    private final UUID uuid;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private double balance;

    public WalletUser(UUID uuid) {
        this.uuid = uuid;
        this.name = null;
        this.balance = 0.0;
    }
}