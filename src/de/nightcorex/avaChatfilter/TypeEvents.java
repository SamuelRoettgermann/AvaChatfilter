package de.nightcorex.avaChatfilter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;

public class TypeEvents implements Listener {

    private Main plugin;

    public TypeEvents(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(plugin.existsInBlacklist(event.getMessage())){
            //Mach jetzt eine genauere Analyse des Satzes um die "bösen" Stellen zu finden.
            // Sollte es performance Probleme mit diesem Ansatz geben, so kann man einfach die Message gar nicht erst ausgeben.
            String[] splitSentence = event.getMessage().split("\\s");
            StringBuilder correctedSentence = new StringBuilder();

            Arrays.stream(splitSentence).forEachOrdered(s -> {
                correctedSentence.append(" ");
                if(plugin.existsInBlacklist(s))
                    for(int i = 0; i < s.length(); i++){
                        correctedSentence.append("*");
                    }
                else
                    correctedSentence.append(s);
            });

            event.setMessage(correctedSentence.toString().trim());
        }
    }
}
