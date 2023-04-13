package xyz.neziw.wallet.utility;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ColorUtils {

    public static String fix(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> fixList(List<String> messages) {
        return messages.stream().map(ColorUtils::fix).collect(Collectors.toList());
    }
}