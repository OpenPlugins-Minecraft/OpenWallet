package xyz.neziw.wallet.managers;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.model.Transaction;
import xyz.neziw.wallet.model.User;
import xyz.neziw.wallet.objects.WalletUser;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class DatabaseManager implements SimpleDatabaseManager<WalletUser> {

    private Connection connection;
    private final WalletPlugin plugin;
    private final String type;
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    public DatabaseManager(WalletPlugin plugin, YamlDocument mainConfig) {
        this.plugin = plugin;
        this.type = mainConfig.getString("database-settings.type");
        this.host = mainConfig.getString("database-settings.host");
        this.port = mainConfig.getInt("database-settings.port");
        this.user = mainConfig.getString("database-settings.user");
        this.password = mainConfig.getString("database-settings.passsword");
        this.database = mainConfig.getString("database-settings.database");
        this.createTables();
    }

    private void connect() {
        if (this.connection != null) return;
        final String databaseType = this.type.toLowerCase(Locale.ROOT);
        try {
            switch (databaseType) {
                case "mysql":
                    this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
                            user, password);
                    break;
                case "sqlite":
                default:
                    this.connection = DriverManager.getConnection("jdbc:sqlite:" + new File(this.plugin.getDataFolder(), "openwallet.db"));
            }
        } catch (SQLException exception) {
            // TODO
        }
    }

    /* TODO
    private void disconnect() {

    }
     */

    private void createTables() {
        this.connect();
        try {
            PreparedStatement statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + "users(uuid VARCHAR(36) NOT NULL, " // TODO - complete this
                    + "name VARCHAR(50) NOT NULL, balance DOUBLE NOT NULL PRIMARY KEY (uuid))");
            statement.execute();
            statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS "
                    + "transactions(id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(36) NOT NULL, "
                    + "product VARCHAR(50) NOT NULL, date DATE NOT NULL, balance DOUBLE NOT NULL, PRIMARY KEY (id))"
            );
            statement.execute();
        } catch (SQLException exception) {

        }
    }

    public CompletableFuture<WalletUser> loadUser(@NotNull UUID uuid) {
        this.connect();
        return CompletableFuture.supplyAsync(() -> {
            WalletUser user = null;
            try {
                final PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM users WHERE uuid=?");
                statement.setString(1, uuid.toString());
                final ResultSet result = statement.executeQuery();
                if (result.next()) {
                    final String name = result.getString("name");
                    final double balance = result.getDouble("double");
                    final String transactionsString = result.getString("transactions");
                    final List<Transaction> transactions = this.getTransactionsByIds(transactionsString.split("=")).get();
                    user = new WalletUser(uuid, name);
                    user.setBalance(balance);
                    user.setTransactions(transactions);
                }
            } catch (SQLException | InterruptedException | ExecutionException exception) {
                return null;
            }
            return user;
        });
    }

    private CompletableFuture<List<Transaction>> getTransactionsByIds(String[] transactionsIds) {
        this.connect();
        return CompletableFuture.supplyAsync(() -> {
            final List<Transaction> transactions = new ArrayList<>();
            try {
                final String query = String.format("SELECT * FROM transactions WHERE id IN (%s)", String.join(",", transactionsIds));
                final Statement statement = connection.createStatement();
                final ResultSet result = statement.executeQuery(query);
                while (result.next()) {
                    final UUID uuid = UUID.fromString(result.getString("uuid"));
                    final String product = result.getString("product");
                    final Date date = result.getDate("date");
                    final double balance = result.getDouble("balance");
                    transactions.add(new Transaction(uuid, product, date, balance));
                }
            } catch (SQLException exception) {
                return null;
            }
            return transactions;
        });
    }
}