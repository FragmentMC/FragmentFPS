package xyz.fragmentmc.fragmentfps;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import xyz.fragmentmc.fragmentfps.features.FPS;
import xyz.fragmentmc.fragmentfps.listeners.FPSAdapter;
import xyz.fragmentmc.fragmentfps.listeners.InventoryClickListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class FragmentFPS extends JavaPlugin {
    private ProtocolManager protocolManager;

    @Override
    public void onEnable() {
        Config.initConfig(this);

        FPS fps = new FPS(this.getConfig());
        FPSAdapter adapter = new FPSAdapter(this, fps);
        InventoryClickListener clickgui = new InventoryClickListener(fps, this);

        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(adapter);

        getServer().getPluginManager().registerEvents(clickgui, this);

        getCommand("fps").setExecutor(fps);

        getCommand("reload").setExecutor(new Config(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
