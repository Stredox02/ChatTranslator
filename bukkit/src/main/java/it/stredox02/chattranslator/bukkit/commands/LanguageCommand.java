package it.stredox02.chattranslator.bukkit.commands;

import it.stredox02.chattranslator.bukkit.ChatTranslator;
import it.stredox02.chattranslator.common.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class LanguageCommand implements CommandExecutor {

    private final ChatTranslator enhancedTranslator;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            return false;
        }
        ChatTranslator.getLanguageSelector().open(player);
        player.sendMessage(Utils.colorize(enhancedTranslator.getConfigManager().getString("messages", "gui.translator.open")));
        return false;
    }

}
