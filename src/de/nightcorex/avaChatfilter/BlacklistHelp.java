package de.nightcorex.avaChatfilter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BlacklistHelp {

    private Main plugin;
    private final List<String> commands;


    public BlacklistHelp(Main plugin) {
        this.plugin = plugin;

        commands = new ArrayList<>();
        //This command technically does exist but it is called in any scenario,
        // therefore explicitly naming it is unnecessary
//        commands.add("help");
        commands.add("add [word]");
        commands.add("show");
        commands.add("remove [word]");
    }

    public boolean showBlacklistCommands(CommandSender sender) {

        //This method is called whenever the call to any of the other methods fails
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission("command.chatfilter.help")) {
                String usage = String.join(" | ", commands);
                p.sendMessage("§4Benutzung: /chatfilter <" + usage + ">");
                return true;
            } else {
                p.sendMessage("§4§lKeine Rechte");
                return false;
            }
        } else {
            String usage = String.join("|", commands);
            System.out.println("Benutzung: /chatfilter <" + usage + ">");
            return true;
        }
    }
}
