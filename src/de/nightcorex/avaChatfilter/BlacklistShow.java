package de.nightcorex.avaChatfilter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BlacklistShow {

    private Main plugin;

    public BlacklistShow(Main plugin) {
        this.plugin = plugin;
    }

    public boolean showBlacklistedWords(CommandSender sender) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission("command.chatfilter.show")) {
                String blacklistedPhrases = String.join(", ", plugin.blacklist);
                p.sendMessage("§dBlacklist: " + blacklistedPhrases);
                return true;
            } else {
                p.sendMessage("§4§lKeine Rechte");
                return false;
            }
        } else {
            String blacklistedPhrases = String.join(", ", plugin.blacklist);
            System.out.println("Blacklist: " + blacklistedPhrases);
            return true;
        }
    }
}
