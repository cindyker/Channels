package net.zaiyers.Channels.command;

import com.google.common.collect.ImmutableMap;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.command.ConsoleCommandSender;
import net.zaiyers.Channels.Channel;
import net.zaiyers.Channels.Channels;
import net.zaiyers.bungee.UUIDDB.UUIDDB;

public class ChannelKickCommand extends AbstractCommand {

	public ChannelKickCommand(CommandSender sender, String[] args) {
		super(sender, args);
	}

	public String getPermission() {
		return "channels.kick";
	}

	public void execute() {
		Channel chan = Channels.getInstance().getChannel(args[1]);
		if (chan == null) {
			Channels.notify(sender, "channels.command.channel-not-found", ImmutableMap.of("channel", args[1]));
			return;
		}
		
		if (!(sender instanceof ConsoleCommandSender) && !chan.isMod(((ProxiedPlayer) sender).getUUID()) && sender.hasPermission("channels.kick.foreign")) {
			Channels.notify(sender, "channels.command.channel-no-permission");
			return;
		}
		
		String chatterUUID;
		ProxiedPlayer player = Channels.getInstance().getProxy().getPlayer(args[2]);
		if (player == null) {
			chatterUUID = UUIDDB.getInstance().getUUIDByName(args[2]);
		} else {
			chatterUUID = player.getUUID();
			if (chatterUUID == null) {
				Channels.notify(sender, "channels.command.chatter-not-found", ImmutableMap.of("chatter", args[2]));
				return;
			}
		}
		
		chan.kickChatter(chatterUUID);
	}

	public boolean validateInput() {
		return args.length > 2;
	}
}