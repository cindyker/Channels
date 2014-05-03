package net.zaiyers.Channels.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.zaiyers.Channels.Channels;

public class ChatterYamlConfig extends YamlConfig implements ChatterConfig {

	/**
	 * load configuration from disk
	 * 
	 * @param configFilePath
	 * @throws IOException
	 */
	public ChatterYamlConfig(String configFilePath) throws IOException {
		super(configFilePath);
	}

	/**
	 * load a config instance by player uuid
	 * @param uuid
	 * @return
	 */
	public static ChatterYamlConfig load(String uuid) {
		String configFilePath = Channels.getInstance().getDataFolder()+("/chatters/"+uuid.substring(0,2)+"/"+uuid.substring(2,4)+"/"+uuid+".yml").toLowerCase();
		File cfgFile = new File(configFilePath);
		if (cfgFile.exists()) {
			try {
				return new ChatterYamlConfig(configFilePath);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	public List<UUID> getSubscriptions() {
		ArrayList<UUID> subs = new ArrayList<UUID>();
		for (String sub: cfg.getStringList("subscriptions")) {
			subs.add(UUID.fromString(sub));
		};
		
		return subs;
	}
	
	public boolean isMuted() {
		return cfg.getBoolean("muted", false);
	}

	public List<String> getIgnores() {
		return cfg.getStringList("ignores");
	}

	public String getPrefix() {
		return cfg.getString("prefix", "");
	}

	public String getSuffix() {
		return cfg.getString("suffix", "");
	}
	
	public String getLastSender() {
		return cfg.getString("lastSender", null);
	}

	public String getLastRecipient() {
		return cfg.getString("lastRecipient", null);
	}

	public UUID getChannelUUID() {
		return UUID.fromString(cfg.getString("channelUUID"));
	}

	public void save() {
		try {
			ymlCfg.save(cfg, configFile);
		} catch (IOException e) {
			Channels.getInstance().getLogger().severe("Unable to save chatter configuration '"+configFile.getAbsolutePath()+"'");
			e.printStackTrace();
		}
	}

	public void createDefaultConfig() {
		cfg = ymlCfg.load(
			new InputStreamReader(Channels.getInstance().getResourceAsStream("chatter.yml"))
		);
		
		// set default channel
		cfg.set("channelUUID", Channels.getConfig().getDefaultChannelUUID().toString());
		
		// subscribe to default channel
		ArrayList<UUID> subs = new ArrayList<UUID>();
		subs.add(Channels.getConfig().getDefaultChannelUUID());
		setSubscriptions(subs);
		
		save();
	}

	public void setSubscriptions(List<UUID> subs) {
		List<String> subscriptions = new ArrayList<String>();
		for (UUID u: subs) {
			subscriptions.add(u.toString());
		}
		
		cfg.set("subscriptions", subscriptions);
	}

	public void setMuted(boolean b) {
		cfg.set("muted", b);
	}

	public void setPrefix(String prefix) {
		cfg.set("prefix", prefix);
	}

	public void setSuffix(String suffix) {
		cfg.set("prefix", suffix);
	}

	public void removeIgnore(String ignoreUUID) {
		List<String> ignores = cfg.getStringList("ignores");
		ignores.remove(ignoreUUID);
		
		cfg.set("ignores", ignores);
	}

	public void addIgnore(String uuid) {
		List<String> ignores = cfg.getStringList("ignores");
		ignores.add(uuid);
		
		cfg.set("ignores", ignores);
	}

	public void setDefaultChannel(UUID uuid) {
		cfg.set("channelUUID", uuid.toString());
	}

	public void setLastSender(String uuid) {
		cfg.set("lastSender", uuid);
	}
}
