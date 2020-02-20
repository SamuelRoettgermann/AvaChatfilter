package de.nightcorex.avaChatfilter;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This program will create a folder with this plugin's respective name in the directory where the .jar is located
 * This folder contains an empty .csv file which can be edited either manually or by using the in-game commands
 * "/chatfilter add [word]" and "/chatfilter remove [word]".
 * <p>
 * Phrases are saved in all lower-case letters in the blacklist. Input therefore is not case sensitive,
 * neither during chatting nor adding
 * <p>
 * This feature is designed to be used explicitly by team members as its use from outside people could either completely
 * censor the chat or even crash the server, as the "/chatfilter remove [word]" command is completely copying the
 * whole blacklist.
 * <p>
 * Note that this plugin may crash if it receives some unexpected input such as emojis or any letters not included
 * in UTF-8. If it crashes however all data is safe and will be up even after the plugin was reloaded.
 * Blacklist safety when:
 *      + The plugin crashes
 *      + The plugin gets closed abrupt
 *      + The plugin gets deleted/changed
 *      - The .csv file or the folder with this plugins name is deleted
 *      - The .csv file's name or the folder's name, which originally was the one of this plugin, is edited
 *      - The .csv file is edited during downtime
 *      ~ The .csv file is edited during runtime
 * <p>
 * This command can be used by either a player with the specific permission or by the console.
 * <p>
 * Whenever this plugin is called but with incorrect parameters it displays ALL possible commands. So for example if
 * "/chatfilter add" is called it returns {@link BlacklistHelp#showBlacklistCommands}
 * instead of just "/chatfilter add [word]"
 * <p>
 * Possible additions in the future:
 *      * In-game documentation of how to use this command if entered incorrectly -> ADDED {@link BlacklistSupervisor#onCommand}
 *      * compatibility with phrases instead of just word
 *      * not always return the general method usage but make it more method specific (see line 41 ff. in this)
 *
 * @throws RuntimeException When an error occurs with accessing, reading or writing from the blacklist.
 *         This may occur for example if the blacklist.csv or AvaChatfilter folder is read-only
 *
 * @effect Censor the chat by changing letters of words containing blacklisted words to '*'.
 *         Note that this method checks if a word contains a blacklisted word and not for equality so adding for
 *         example an 'e' to the blacklist would also censor the word "Hello".
 *
 * @permissions command.chatfilter.help / command.chatfilter.show / command.chatfilter.add / command.chatfilter.remove
 */
public class Main extends JavaPlugin {

    File blacklistFile;
    Path blacklistPath;
    List<String> blacklist;

    public Main() throws IOException {
        blacklistFile = new File(getDataFolder(), "/blacklist.csv");
        blacklistPath = blacklistFile.toPath();

        //Create the file and folder if they do not exist, yet
        blacklistFile.getParentFile().mkdirs();
        blacklistFile.createNewFile();

        blacklist = Files.lines(blacklistPath).collect(Collectors.toList());
    }

    @Override
    public void onEnable() {
        registerCommands();
        registerListeners();

        System.out.println("AvaChatfilter loaded");
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new TypeEvents(this), this);
    }

    private void registerCommands() {
        this.getCommand("chatfilter").setExecutor(new BlacklistSupervisor(this));
    }

    @Override
    public void onDisable() {

    }

    public boolean existsInBlacklist(String phrase) {
        phrase = phrase.toLowerCase();
        for(String bl : blacklist){
            if(phrase.contains(bl))
                return true;
        }

        return false;
    }


    /**
     * @deprecated Method is deprecated because it is highly inefficien as it creates an array over which a stream
     * is created which then calls another method multiple times... for no reason!
     */
    @Deprecated
    public boolean sentenceIsOk(String sentence) {
        //Hier muss die Negation angegeben werden, da sonst der Methodenname keinen Sinn macht
        return !Arrays.stream(sentence.split("\\s")).parallel().anyMatch(this::existsInBlacklist);
    }

}
