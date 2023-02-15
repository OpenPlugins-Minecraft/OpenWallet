package xyz.neziw.wallet.model;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface User {

    UUID getUUID();

    String getName();

    Player getPlayer();

    double getBalance();

    List<Transaction> getTransactions();
}