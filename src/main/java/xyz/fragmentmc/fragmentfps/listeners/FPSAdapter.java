package xyz.fragmentmc.fragmentfps.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import xyz.fragmentmc.fragmentfps.features.FPS;
import org.bukkit.plugin.Plugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FPSAdapter extends  PacketAdapter {
    private final FPS fps;
    private final Plugin p;

    public FPSAdapter(Plugin p, FPS fps) {
        super(p, ListenerPriority.NORMAL,
                PacketType.Play.Server.SPAWN_ENTITY,
                PacketType.Play.Server.EXPLOSION,
                PacketType.Play.Server.BLOCK_ACTION,
                PacketType.Play.Server.MULTI_BLOCK_CHANGE,
                PacketType.Play.Server.BLOCK_CHANGE,
                PacketType.Login.Server.SUCCESS
                );
        this.fps = fps;
        this.p = p;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        UUID uuid = event.getPlayer().getUniqueId();
        PacketType type = packet.getType();
        if (type.equals(PacketType.Play.Server.SPAWN_ENTITY)) {
            EntityType e = packet.getEntityTypeModifier().read(0);
            if (fps.tnt_vis.getOrDefault(uuid, false) && e == EntityType.PRIMED_TNT) {
                event.setCancelled(true);
            } else if (fps.sand_vis.getOrDefault(uuid, false) && e == EntityType.FALLING_BLOCK && packet.getIntegers().read(4) < 8000) {
                event.setCancelled(true);
            } else if (fps.anvil_vis.getOrDefault(uuid, false) && e == EntityType.FALLING_BLOCK && packet.getIntegers().read(4) > 8000 && packet.getIntegers().read(4) < 12000) {
                event.setCancelled(true);
            } else if (fps.concrete_vis.getOrDefault(uuid, false) && e == EntityType.FALLING_BLOCK && packet.getIntegers().read(4) > 12000 && packet.getIntegers().read(4) < 18000) {
                event.setCancelled(true);
            }
        } else if (type.equals(PacketType.Play.Server.EXPLOSION)) {
            if (fps.explosion_vis.getOrDefault(uuid, false)) {
                event.setCancelled(true);
            }
        } else if (type.equals(PacketType.Play.Server.BLOCK_ACTION)) {
            if (fps.piston_vis.getOrDefault(uuid, false) && (packet.getBlocks().read(0).equals(Material.PISTON) || packet.getBlocks().read(0).equals(Material.STICKY_PISTON))) {
                event.setCancelled(true);
            }
        } else if (type.equals(PacketType.Play.Server.BLOCK_CHANGE)) {
            if (fps.spawner_vis.getOrDefault(uuid, false) && packet.getBlockData().read(0).getType() == Material.SPAWNER) {
                packet.getBlockData().write(0, WrappedBlockData.createData(Material.STONE));
            }
        }
        else if (type.equals(PacketType.Play.Server.MULTI_BLOCK_CHANGE)) {
            Player player = event.getPlayer();
            if (fps.spawner_vis.getOrDefault(uuid, false)) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for (Chunk loadedChunk : player.getWorld().getLoadedChunks()) {
                        for (BlockState tileEntity : loadedChunk.getTileEntities()) {
                            if (tileEntity.getType() == Material.SPAWNER) {
                                player.sendBlockChange(tileEntity.getLocation(), Material.STONE.createBlockData());
                            }
                        }
                    }
                }, 5);
            }
        }
        else if (type.equals(PacketType.Login.Server.SUCCESS)) {
            Player player = event.getPlayer();
            if (fps.spawner_vis.getOrDefault(uuid, false)) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    for (Chunk loadedChunk : player.getWorld().getLoadedChunks()) {
                        for (BlockState tileEntity : loadedChunk.getTileEntities()) {
                            if (tileEntity.getType() == Material.SPAWNER) {
                                player.sendBlockChange(tileEntity.getLocation(), Material.STONE.createBlockData());
                            }
                        }
                    }
                }, 20);
            }
        }
    }
}