package xyz.neziw.wallet.model;

import java.util.Date;
import java.util.UUID;


public class Transaction {

    private UUID uuid;
    private String product;
    private Date date;
    private double balance;

    public Transaction(UUID uuid, String product, Date date, double balance) {
        this.uuid = uuid;
        this.product = product;
        this.date = date;
        this.balance = balance;
    }
}