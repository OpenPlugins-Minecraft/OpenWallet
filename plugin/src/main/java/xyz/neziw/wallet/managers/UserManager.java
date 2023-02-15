package xyz.neziw.wallet.managers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.neziw.wallet.objects.WalletUser;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager implements SimpleUserManager<UUID, WalletUser> {

    @Getter private final Map<UUID, WalletUser> users = new ConcurrentHashMap<>();

    @Override
    @NotNull
    public CompletableFuture<WalletUser> findOrCreate(@NotNull UUID identifier) {
        return CompletableFuture.supplyAsync(() -> this.users.computeIfAbsent(identifier, WalletUser::new));
    }

    @Override
    @NotNull
    public CompletableFuture<WalletUser> findUser(@NotNull UUID identifier) {
        return CompletableFuture.supplyAsync(() -> this.users.get(identifier));
    }

    @Override
    @NotNull
    public WalletUser getUser(@NotNull UUID identifier) {
        return this.users.get(identifier);
    }
}