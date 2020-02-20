package de.nightcorex.avaChatfilter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class BlacklistRemove {

    private Main plugin;

    public BlacklistRemove(Main plugin) {
        this.plugin = plugin;
    }

    public boolean removeLine(CommandSender sender, String[] args) {

        String removal = args[1];

        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(p.hasPermission("command.chatfilter.remove")) {
                p.sendMessage(deleteElement(removal) ? ("§a\"" + removal + "\" wurde aus der Blacklist entfernt") :
                        ("§a\"" + removal + "\" existierte nicht in der Liste"));
                //Hier wird true returned, obwohl der Command evtl auch fehlschlägt. Dies soll suggerieren,
                // dass nach diesem Teil das entsprechende Wort DEFINITIV nicht (mehr) in der Liste enthalten ist
                return true;
            } else {
                p.sendMessage("§4§lKeine Rechte");
                return false;
            }
        } else {
            System.out.println(deleteElement(removal) ? ('\"' + removal + "\" wurde aus der Blacklist entfernt") :
                    ('\"' + removal + "\" existierte nicht in der Liste"));
            return true;
        }

    }


    //Diese Methode ist synchronized, um evtlle gleichzeite Zugriffe zu unterbinden. Dies sollte eigentlich keine
    // schwerwiegenden Konsequenzen haben, kann aber zu falschen Rückmeldungen führen
    private synchronized boolean deleteElement(String removal) {
        if(!removal.equals("") && plugin.blacklist.remove(removal)) {
            try (BufferedWriter blacklistWriter = Files.newBufferedWriter(plugin.blacklistPath, StandardOpenOption.TRUNCATE_EXISTING)) {

                //Fetch the data from the list to a StringBuilder
                StringBuilder blacklistContent = new StringBuilder();
                plugin.blacklist.forEach(s -> blacklistContent.append(s).append(System.lineSeparator()));

                blacklistWriter.write(blacklistContent.toString().trim());
                return true;
            } catch (IOException e) {
                throw new RuntimeException("Fehler beim Loeschen eines Eintrags");
            }
        } else {
            return false;
        }
    }
}
