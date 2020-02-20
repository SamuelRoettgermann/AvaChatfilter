package de.nightcorex.avaChatfilter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class AddToBlacklist {

    private Main plugin;

    public AddToBlacklist(Main plugin) {
        this.plugin = plugin;
    }

    public boolean addToList(CommandSender sender, String[] args) {

        //Wenn dieser Command aufgerufen wird, dann ist bereits auf args == 2 Länge gecheckt

        String addition = args[1];

        if(sender instanceof Player) {
            Player p = (Player) sender;
            if(p.hasPermission("command.chatfilter.add")) {
                synchronized (this) {
                    if(plugin.existsInBlacklist(addition)) {
                        p.sendMessage("§a\"" + addition + "\" oder ein Teil davon ist bereits in der Liste enthalten");
                        return false;
                    } else {
                        //Der blacklistWriter kann hier jedes Mal neu erstellt werden, weil der Befehl sowieso so
                        // selten benutzt wird, dass es besser ist den Writer nicht permanent als Objekt rumliegen zu haben
                        try (BufferedWriter blacklistWriter = Files.newBufferedWriter(plugin.blacklistPath, StandardOpenOption.APPEND)) {
                            blacklistWriter.write(System.lineSeparator() + addition);
                            plugin.blacklist.add(addition);
                            p.sendMessage("§a" + addition + " erfolgreich hinzugefügt");
                            return true;
                        } catch (IOException e) {
                            throw new RuntimeException("Konnte nicht in die blacklist schreiben");
                        }
                    }
                }
            } else {
                p.sendMessage("§4§lKeine Rechte");
                return false;
            }
        } else {
            synchronized (this) {
                if(plugin.existsInBlacklist(addition)) {
                    System.out.println('\"' + addition + "\" oder ein Teil davon ist bereits in der Liste enthalten");
                    return false;
                } else {
                    //Der blacklistWriter kann hier jedes Mal neu erstellt werden, weil der Befehl sowieso so
                    // selten benutzt wird, dass es besser ist den Writer nicht permanent als Objekt rumliegen zu haben
                    try (BufferedWriter blacklistWriter = Files.newBufferedWriter(plugin.blacklistPath, StandardOpenOption.APPEND)) {
                        blacklistWriter.write(System.lineSeparator() + addition);
                        plugin.blacklist.add(addition);
                        System.out.println(addition + " erfolgreich hinzugefuegt");
                        return true;
                    } catch (IOException e) {
                        throw new RuntimeException("Konnte nicht in die blacklist schreiben");
                    }
                }
            }
        }
    }
}
