package me.driftay.spawners.file.impl;

import me.driftay.spawners.file.CustomFile;
import me.driftay.spawners.utils.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageFile extends CustomFile {

	private JavaPlugin instance;

	public MessageFile(JavaPlugin instance) {
		super(instance, "", "messages");
		this.instance = instance;
		for (Message message : Message.values()) {
			if (message.getMessages() != null) {
				for (String string : message.getMessages()) {
					ChatColor.translateAlternateColorCodes('&', string);
					getConfig().addDefault(message.getConfig(), message.getMessages());
				}
			} else {
				getConfig().addDefault(message.getConfig(), message.getMessage());
			}
		}
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void init() {
		this.reloadConfig();
		for (Message message : Message.values()) {
			if (message.getMessages() == null) {
				message.setMessage(getConfig().getString(message.getConfig()));
			} else {
				message.setMessages(getConfig().getStringList(message.getConfig()));
			}
		}
	}
}
