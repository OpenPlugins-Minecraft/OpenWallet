package xyz.neziw.wallet.utilities;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ColorUtils {

    public static String fix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> fixList(List<String> messageList) {
        List<String> list = new ArrayList<>();
        for (String string : messageList) {
            list.add(fix(string));
        }
        return list;
    }
}