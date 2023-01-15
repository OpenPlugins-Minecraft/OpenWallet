package xyz.neziw.wallet.tasks;

import lombok.AllArgsConstructor;
import xyz.neziw.wallet.managers.DatabaseManager;
import xyz.neziw.wallet.managers.UserManager;
import xyz.neziw.wallet.objects.WalletUser;

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