package rolandboss1234.hu.broadcast.utils;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.Listener;
import rolandboss1234.hu.broadcast.Broadcast;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils implements Listener {
    private final Broadcast plugin;

    public ColorUtils(Broadcast plugin) {

        this.plugin = plugin;
    }

    public static String applyHexColors(String message) {
        Pattern gradientPattern = Pattern.compile("<\\#([A-Fa-f0-9]{6})>(.*?)</\\#([A-Fa-f0-9]{6})>");
        Matcher gradientMatcher = gradientPattern.matcher(message);

        StringBuffer result = new StringBuffer();

        while (gradientMatcher.find()) {
            String startHex = gradientMatcher.group(1);
            String text = gradientMatcher.group(2);
            String endHex = gradientMatcher.group(3);

            Color startColor = Color.decode("#" + startHex);
            Color endColor = Color.decode("#" + endHex);

            String gradientText = applyGradientToText(text, startColor, endColor);
            gradientMatcher.appendReplacement(result, gradientText);
        }

        String finalMessage = gradientMatcher.appendTail(result).toString();

        Pattern hexPattern = Pattern.compile("#([A-Fa-f0-9]{6})");
        Matcher hexMatcher = hexPattern.matcher(finalMessage);
        StringBuffer buffer = new StringBuffer();

        while (hexMatcher.find()) {
            String hexColor = hexMatcher.group();
            String colorCode = ChatColor.of(hexColor).toString();
            hexMatcher.appendReplacement(buffer, colorCode);
        }

        hexMatcher.appendTail(buffer);

        return buffer.toString();
    }

    private static String applyGradientToText(String text, Color startColor, Color endColor) {
        StringBuilder gradientText = new StringBuilder();

        int textLength = text.length();
        for (int i = 0; i < textLength; i++) {
            float ratio = (float) i / (textLength - 1);

            int red = (int) (startColor.getRed() * (1 - ratio) + endColor.getRed() * ratio);
            int green = (int) (startColor.getGreen() * (1 - ratio) + endColor.getGreen() * ratio);
            int blue = (int) (startColor.getBlue() * (1 - ratio) + endColor.getBlue() * ratio);

            String hexColor = String.format("#%02X%02X%02X", red, green, blue);
            gradientText.append(ChatColor.of(hexColor)).append(text.charAt(i));
        }

        return gradientText.toString();
    }
}