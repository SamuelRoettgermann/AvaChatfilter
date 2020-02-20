package de.nightcorex.avaChatfilter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BlacklistSupervisor implements CommandExecutor {

    private Main plugin;
    private AddToBlacklist add;
    private BlacklistHelp help;
    private BlacklistShow show;
    private BlacklistRemove remove;

    public BlacklistSupervisor(Main plugin) {
        this.plugin = plugin;
        add = new AddToBlacklist(plugin);
        help = new BlacklistHelp(plugin);
        show = new BlacklistShow(plugin);
        remove = new BlacklistRemove(plugin);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if(args.length > 0) {

            if(args[0].equalsIgnoreCase("add")) {
                if(args.length == 2) {
                    return add.addToList(sender, args);
                }
            }

            if(args[0].equalsIgnoreCase("show")) {
                return show.showBlacklistedWords(sender);
            }

            if(args[0].equalsIgnoreCase("remove")) {
                if(args.length == 2) {
                    return remove.removeLine(sender, args);
                }
            }
        }

        return help.showBlacklistCommands(sender);
    }


}
