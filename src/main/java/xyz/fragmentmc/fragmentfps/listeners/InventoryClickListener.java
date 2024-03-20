package xyz.fragmentmc.fragmentfps.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.fragmentmc.fragmentfps.FragmentFPS;
import xyz.fragmentmc.fragmentfps.features.FPS;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class InventoryClickListener implements Listener {
    private final FPS fps;
    private final Plugin plugin;

    public InventoryClickListener(FPS fps, Plugin plugin) {
        this.fps = fps;
        this.plugin = plugin;
    }

    @EventHandler
    private void inventoryClickListener(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && fps.ValidateInv(event.getClickedInventory(), (Player) event.getWhoClicked())) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if (event.getCurrentItem().getType().equals(FPS.tntitem.getType())) {
                fps.tnt_vis.put(uuid, !fps.tnt_vis.get(uuid));
            }
            else if (event.getCurrentItem().getType().equals(FPS.sanditem.getType())) {
                fps.sand_vis.put(uuid, !fps.sand_vis.get(uuid));
            }
            else if (event.getCurrentItem().getType().equals(FPS.anvilitem.getType())) {
                fps.anvil_vis.put(uuid, !fps.anvil_vis.get(uuid));
            }
            else if (event.getCurrentItem().getType().equals(FPS.concreteitem.getType())) {
                fps.concrete_vis.put(uuid, !fps.concrete_vis.get(uuid));
            }
            else if (event.getCurrentItem().getType().equals(FPS.explosionitem.getType())) {
                fps.explosion_vis.put(uuid, !fps.explosion_vis.get(uuid));
            }
            else if (event.getCurrentItem().getType().equals(FPS.flashitem.getType())) {
                fps.flash_vis.put(uuid, !fps.flash_vis.get(uuid));
            }
            else if (event.getCurrentItem().getType().equals(FPS.pistonitem.getType())) {
                fps.piston_vis.put(uuid, !fps.piston_vis.get(uuid));
            }
            else if (event.getCurrentItem().getType().equals(FPS.spawneritem.getType())) {
                fps.spawner_vis.put(uuid, !fps.spawner_vis.get(uuid));
                if (fps.spawner_vis.get(uuid)) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (Chunk loadedChunk : player.getWorld().getLoadedChunks()) {
                            for (BlockState tileEntity : loadedChunk.getTileEntities()) {
                                if (tileEntity.getType() == Material.SPAWNER) {
                                    player.sendBlockChange(tileEntity.getLocation(), Material.STONE.createBlockData());
                                }
                            }
                        }
                    });
                } else {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        for (Chunk loadedChunk : player.getWorld().getLoadedChunks()) {
                            for (BlockState tileEntity : loadedChunk.getTileEntities()) {
                                if (tileEntity.getType() == Material.SPAWNER) {
                                    player.sendBlockChange(tileEntity.getLocation(), Material.SPAWNER.createBlockData());
                                }
                            }
                        }
                    });
                }
            }

            // player.closeInventory();
            player.openInventory(fps.GetGui(player.getUniqueId(), player.getName()));
        }
    }
}
