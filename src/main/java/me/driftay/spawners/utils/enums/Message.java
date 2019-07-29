package me.driftay.spawners.utils.enums;

import me.driftay.spawners.utils.Methods;
import me.driftay.spawners.utils.FileManager.Files;
import org.bukkit.configuration.file.FileConfiguration;
import java.util.HashMap;
import java.util.List;

public enum Message {

    CONFIG_RELOAD("Config-Reload", "&cYou have reloaded the plugin."),
    NO_PERMS("No-Perms","&cYou do not have permission to use this command.");


    private String path;
    private String defaultMessage;
    private List<String> defaultListMessage;

    private Message(String path, String defaultMessage) {
        this.path = path;
        this.defaultMessage = defaultMessage;
    }

    private Message(String path, List<String> defaultListMessage) {
        this.path = path;
        this.defaultListMessage = defaultListMessage;
    }

    public String getMessage() {
        if(isList()) {
            if(exists()) {
                return Methods.pl(convertList(Files.LANG.getFile().getStringList("Messages." + path)));
            }else {
                return Methods.pl(convertList(getDefaultListMessage()));
            }
        }else {
            if(exists()) {
                return Methods.getPrefix(Files.LANG.getFile().getString("Messages." + path));
            }else {
                return Methods.getPrefix(getDefaultMessage());
            }
        }
    }

    public String getMessage(HashMap<String, String> placeholders) {
        String message;
        if(isList()) {
            if(exists()) {
                message = Methods.pl(convertList(Files.LANG.getFile().getStringList("Messages." + path), placeholders));
            }else {
                message = Methods.pl(convertList(getDefaultListMessage(), placeholders));
            }
        }else {
            if(exists()) {
                message = Methods.getPrefix(Files.LANG.getFile().getString("Messages." + path));
            }else {
                message = Methods.getPrefix(getDefaultMessage());
            }
            for(String ph : placeholders.keySet()) {
                if(message.contains(ph)) {
                    message = message.replaceAll(ph, placeholders.get(ph));
                }
            }
        }
        return message;
    }

    public String getMessageNoPrefix() {
        if(isList()) {
            if(exists()) {
                return Methods.pl(convertList(Files.LANG.getFile().getStringList("Messages." + path)));
            }else {
                return Methods.pl(convertList(getDefaultListMessage()));
            }
        }else {
            if(exists()) {
                return Methods.pl(Files.LANG.getFile().getString("Messages." + path));
            }else {
                return Methods.pl(getDefaultMessage());
            }
        }
    }

    public String getMessageNoPrefix(HashMap<String, String> placeholders) {
        String message;
        if(isList()) {
            if(exists()) {
                message = Methods.pl(convertList(Files.LANG.getFile().getStringList("Messages." + path), placeholders));
            }else {
                message = Methods.pl(convertList(getDefaultListMessage(), placeholders));
            }
        }else {
            if(exists()) {
                message = Methods.pl(Files.LANG.getFile().getString("Messages." + path));
            }else {
                message = Methods.pl(getDefaultMessage());
            }
            for(String ph : placeholders.keySet()) {
                if(message.contains(ph)) {
                    message = message.replaceAll(ph, placeholders.get(ph));
                }
            }
        }
        return message;
    }

    public static String convertList(List<String> list) {
        String message = "";
        for(String m : list) {
            message += Methods.pl(m) + "\n";
        }
        return message;
    }

    public static String convertList(List<String> list, HashMap<String, String> placeholders) {
        String message = "";
        for(String m : list) {
            message += Methods.pl(m) + "\n";
        }
        for(String ph : placeholders.keySet()) {
            message = Methods.pl(message.replaceAll(ph, placeholders.get(ph)));
        }
        return message;
    }

    public static void addMissingMessages() {
        FileConfiguration messages = Files.LANG.getFile();
        boolean saveFile = false;
        for(Message message : values()) {
            if(!messages.contains("Messages." + message.getPath())) {
                saveFile = true;
                if(message.getDefaultMessage() != null) {
                    messages.set("Messages." + message.getPath(), message.getDefaultMessage());
                }else {
                    messages.set("Messages." + message.getPath(), message.getDefaultListMessage());
                }
            }
        }
        if(saveFile) {
            Files.LANG.saveFile();
        }
    }

    private Boolean exists() {
        return Files.LANG.getFile().contains("Messages." + path);
    }

    private Boolean isList() {
        if(Files.LANG.getFile().contains("Messages." + path)) {
            return !Files.LANG.getFile().getStringList("Messages." + path).isEmpty();
        }else {
            return defaultMessage == null;
        }
    }

    private String getPath() {
        return path;
    }

    private String getDefaultMessage() {
        return defaultMessage;
    }

    private List<String> getDefaultListMessage() {
        return defaultListMessage;
    }

}
