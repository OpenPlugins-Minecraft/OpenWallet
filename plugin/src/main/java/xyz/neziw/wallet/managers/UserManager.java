package xyz.neziw.wallet.managers;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.neziw.wallet.objects.WalletUser;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager implements SimpleUserManager<UUID, WalletUser> {

    @Getter private final Map<UUID, WalletUser> users = new ConcurrentHashMap<>();

    @Override
    public Optional<WalletUser> findOrCreate(@NotNull UUID identifier) {
        return Optional.of(this.users.computeIfAbsent(identifier, WalletUser::new));
    }

    @Override
    public Optional<WalletUser> findUser(@NotNull UUID identifier) {
        return Optional.ofNullable(this.users.get(identifier));
    }

    @Override
    public @Nullable WalletUser getUser(@NotNull UUID identifier) {
        return this.users.get(identifier);
    }
}