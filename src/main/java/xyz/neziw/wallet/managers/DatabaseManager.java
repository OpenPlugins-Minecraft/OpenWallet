package xyz.neziw.wallet.managers;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import lombok.SneakyThrows;
import xyz.neziw.wallet.WalletPlugin;
import xyz.neziw.wallet.objects.WalletUser;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public class DatabaseManager {

    private Connection connection;
    private final WalletPlugin plugin;
    private final String type;
    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    @Getter
    private final ExecutorService executor;

    public DatabaseManager(WalletPlugin plugin, YamlDocument config) {
        this.plugin = plugin;
        this.type = config.getString("database-settings.type");
        this.host = config.getString("database-settings.host");
        this.port = config.getInt("database-settings.port");
        this.user = config.getString("database-settings.user");
        this.password = config.getString("database-settings.password");
        this.database = config.getString("database-settings.database");
        this.executor = Executors.newSingleThreadExecutor(new ThreadFactoryBuilder().setNameFormat("OpenWallet-Plugin-Pool-%d").build());
        this.createTables();
    }

    @SneakyThrows
    public void connect() {
        if (this.connection == null) {
            String driver = this.type.toLowerCase(Locale.ROOT);
            switch (driver) {
                case "mysql":
                    this.connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
                            user, password
                    );
                    break;
                case "sqlite":
                    this.connection = DriverManager.getConnection("jdbc:sqlite:" + new File(this.plugin.getDataFolder(), "database.db"));
                    break;
            }
        }
    }

    @SneakyThrows
    public void disconnect() {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
    }

    private void createTables() {
        this.executor.execute(() -> {
            try {
                this.connect();
                final PreparedStatement statement;
                statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS wallet_data (uuid VARCHAR(100) PRIMARY KEY, " +
                        "name VARCHAR(16), balance DOUBLE)");
                statement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
                this.disconnect();
            }
        });
    }

    @SneakyThrows
    public boolean exists(String name) {
        this.connect();
        final PreparedStatement statement;
        statement = this.connection.prepareStatement("SELECT * FROM wallet_data WHERE name=?");
        statement.setString(1, name);
        ResultSet result = statement.executeQuery();
        return result.next();
    }

    public void registerUser(UUID uuid, String name) {
        this.executor.execute(() -> {
            try {
                this.connect();
                final PreparedStatement statement;
                statement = this.connection.prepareStatement("INSERT INTO wallet_data (uuid, name, balance) VALUES (?,?,?)");
                statement.setString(1, uuid.toString());
                statement.setString(2, name);
                statement.setDouble(3, 0.0);
                statement.execute();
            } catch (SQLException exception) {
                exception.printStackTrace();
                this.disconnect();
            }
        });
    }

    public void loadUser(WalletUser user) {
        this.executor.execute(() -> {
            try {
                this.connect();
                final PreparedStatement statement;
                statement = this.connection.prepareStatement("SELECT * FROM wallet_data WHERE uuid=?");
                statement.setString(1, user.getUuid().toString());
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    user.setName(result.getString("name"));
                    user.setBalance(result.getDouble("balance"));
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
                this.disconnect();
            }
        });
    }

    public void saveUser(WalletUser user) {
        this.executor.execute(() -> {
            try {
                this.connect();
                final PreparedStatement statement;
                statement = this.connection.prepareStatement("UPDATE wallet_data SET name=?, balance=? WHERE uuid=?");
                statement.setString(1, user.getName());
                statement.setDouble(2, user.getBalance());
                statement.setString(3, user.getUuid().toString());
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
                this.disconnect();
            }
        });
    }

    @SneakyThrows
    public double getBalanceByName(String name) {
        this.connect();
        final PreparedStatement statement;
        statement = this.connection.prepareStatement("SELECT * FROM wallet_data WHERE name=?");
        statement.setString(1, name);
        ResultSet result;
        result = statement.executeQuery();
        return result.getDouble("balance");
    }

    public void setBalanceByName(String name, double balance) {
        this.executor.execute(() -> {
            try {
                this.connect();
                final PreparedStatement statement;
                statement = this.connection.prepareStatement("UPDATE wallet_data SET balance=? WHERE name=?");
                statement.setDouble(1, balance);
                statement.setString(2, name);
                statement.executeUpdate();
            } catch (SQLException exception) {
                exception.printStackTrace();
                this.disconnect();
            }
        });
    }

    public void deposit(String name, double balance) {
        final double current = this.getBalanceByName(name);
        final double newBalance = current + balance;
        this.setBalanceByName(name, newBalance);
    }

    public void withDraw(String name, double balance) {
        final double current = this.getBalanceByName(name);
        final double newBalance = current - balance;
        this.setBalanceByName(name, newBalance);
    }
}