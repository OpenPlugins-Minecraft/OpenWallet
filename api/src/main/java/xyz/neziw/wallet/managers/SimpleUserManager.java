package xyz.neziw.wallet.managers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * An interface for managing user objects.
 * @param <I> the type of the user identifier
 * @param <V> the type of the user object
 */
public interface SimpleUserManager<I, V> {

    /**
     * Finds an existing user object based on the given identifier,
     * or creates a new user object if one does not already exist.
     * @param identifier the user identifier to search for
     * @return an optional containing the existing or newly created user object,
     * or an empty optional if no user object could be found or created
     */
    Optional<V> findOrCreate(@NotNull I identifier);

    /**
     * Finds an existing user object based on the given identifier.
     * @param identifier the user identifier to search for
     * @return an optional containing the existing user object,
     * or an empty optional if no user object could be found
     */
    Optional<V> findUser(@NotNull I identifier);

    /**
     * Gets the user object based on the given identifier.
     * @param identifier the user identifier to search for
     * @return the user object associated with the given identifier,
     * or null if no user object could be found
     */
    @Nullable V getUser(@NotNull I identifier);
}