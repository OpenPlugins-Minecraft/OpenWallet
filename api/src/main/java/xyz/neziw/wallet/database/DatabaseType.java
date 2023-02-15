package xyz.neziw.wallet.database;

public enum DatabaseType {

    MYSQL("mysql"),
    SQLITE("sqlite"),
    MONGODB("mongodb"),
    YAML("yaml");

    public final String nameCase;

    DatabaseType(String nameCase) {
        this.nameCase = nameCase;
    }
}