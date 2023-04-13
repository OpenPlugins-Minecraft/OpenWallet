package xyz.neziw.wallet.task;

import lombok.AllArgsConstructor;
import xyz.neziw.wallet.manager.DatabaseManager;
import xyz.neziw.wallet.manager.UserManager;
import xyz.neziw.wallet.object.WalletUser;

@AllArgsConstructor
public class SaveTaskRunnable implements Runnable {

    private final UserManager userManager;
    private final DatabaseManager databaseManager;

    @Override
    public void run() {
        for (WalletUser user : this.userManager.getUsers().values()) {
            this.databaseManager.saveUser(user);
        }
    }
}