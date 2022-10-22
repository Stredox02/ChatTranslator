package it.stredox02.chattranslator.common.utils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface Utils {

    static String colorize(@Nullable String text){
        if(text == null){
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    static List<String> colorize(@Nullable List<String> list){
        if(list == null){
            return new ArrayList<>();
        }
        List<String> copy = new ArrayList<>();
        for (String line : list) {
            copy.add(colorize(line));
        }
        return copy;
    }

}
