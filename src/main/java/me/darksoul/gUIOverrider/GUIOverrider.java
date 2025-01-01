package me.darksoul.gUIOverrider;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class GUIOverrider extends JavaPlugin {
    private ProtocolManager protocolManager;
    private Map<String, String> GUITitles;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfigValues();

        protocolManager = ProtocolLibrary.getProtocolManager();

        protocolManager.addPacketListener(new PacketAdapter(this, PacketType.Play.Server.OPEN_WINDOW) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                String oTitle = packet.getChatComponents().read(0).getJson();
                for (String key : GUITitles.keySet()) {
                    if (oTitle.contains(key)) {
                        String nTitle = GUITitles.get(key);
                        packet.getChatComponents().write(0, WrappedChatComponent.fromText(nTitle));
                    }
                }
            }
        });
        getCommand("guioverrider").setExecutor(this);
    }

    private void loadConfigValues() {
        GUITitles = new HashMap<>();
        if (getConfig().isConfigurationSection("titles")) {
            Set<String> keys = getConfig().getConfigurationSection("titles").getKeys(false);
            for (String key : keys) {
                String value = getConfig().getString("titles." + key);
                if (value != null) {
                    GUITitles.put(key, value);
                }
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("guioverrider")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                GUITitles.clear();
                loadConfigValues();
                sender.sendMessage("§aGUIOverrider configuration reloaded successfully.");
                return true;
            }
        }
        sender.sendMessage("§cUsage: /guioverrider reload");
        return false;
    }

    @Override
    public void onDisable() {
        if (protocolManager != null) {
            protocolManager.removePacketListeners(this);
        }
    }

}
