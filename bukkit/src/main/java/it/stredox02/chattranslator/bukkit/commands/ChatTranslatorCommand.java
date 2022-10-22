package it.stredox02.chattranslator.bukkit.commands;

import it.stredox02.chattranslator.bukkit.ChatTranslator;
import it.stredox02.chattranslator.bukkit.exceptions.ConfigReloadErrorException;
import it.stredox02.chattranslator.common.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class ChatTranslatorCommand implements CommandExecutor {

    private final ChatTranslator enhancedTranslator;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chattranslator.admin")) {
            sender.sendMessage(Utils.colorize(enhancedTranslator.getConfigManager().getString("messages", "nopermission")));
            return false;
        }
        if (args.length == 0) {
            sender.sendMessage(Utils.colorize(enhancedTranslator.getConfigManager().getString("messages", "usage")));
            return false;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                try {
                    enhancedTranslator.getConfigManager().fullReload();
                } catch (ConfigReloadErrorException e) {
                    sender.sendMessage(Utils.colorize(enhancedTranslator.getConfigManager().getString("messages", "config.reload.error")));
                    e.printStackTrace();
                    return false;
                }
                sender.sendMessage(Utils.colorize(enhancedTranslator.getConfigManager().getString("messages", "config.reload.success")));
                return false;
            }
            if (args[0].equalsIgnoreCase("version")) {
                sender.sendMessage(Utils.colorize("&aThe server is running ChatTranslator version: &2" + enhancedTranslator.getDescription().getVersion()));
                return false;
            }
        }
        return false;
    }

}
