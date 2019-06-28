package me.driftay.spawners.utils;

import java.util.List;

public enum Message {
    COMMAND_FORMAT("command-format", "&c&l[!] &7Try /dspawner reload"),
    RELOADED("reloaded", "&c&l[!] &2Configuration Reloaded Successfully!"),
    NO_PERMISSION("no-permission", "&c&l[!] &7You do not have permission.");

    String config, message;
    String[] messages;

    Message(String config, String message) {
        this.config = config;
        this.message = message;
    }

    Message(String config, String[] messages) {
        this.config = config;
        this.messages = messages;
    }

    public String getConfig() {
        return config;
    }

    public String getMessage() {
        return message;
    }

    public String[] getMessages() {
        return this.messages;
    }

    public void setMessages(List<String> list) {
        this.messages = list.toArray(new String[0]);
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

