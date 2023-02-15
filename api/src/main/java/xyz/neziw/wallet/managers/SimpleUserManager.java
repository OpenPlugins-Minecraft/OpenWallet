package xyz.neziw.wallet.managers;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

/**
 * This interface provides a simple user management system that can be used to manage users of any type.
 *
 * @param <I> The type of identifier used to identify users.
 * @param <V> The type of value stored for each user.
 */
public interface SimpleUserManager<I, V> {

    /**
     * Finds or creates a user with the specified identifier.
     *
     * @param identifier The identifier of the user to find or create.
     * @return A CompletableFuture that will complete with the value of the user with the specified identifier.
     */
    @NotNull
    CompletableFuture<V> findOrCreate(@NotNull I identifier);

    /**
     * Finds a user with the specified identifier.
     *
     * @param identifier The identifier of the user to find.
     * @return A CompletableFuture that will complete with the value of the user with the specified identifier, or null if the user was not found.
     */
    @NotNull
    CompletableFuture<V> findUser(@NotNull I identifier);

    /**
     * Gets the user with the specified identifier.
     *
     * @param identifier The identifier of the user to get.
     * @return The value of the user with the specified identifier, or null if the user was not found.
     */
    @NotNull
    V getUser(@NotNull I identifier);
}