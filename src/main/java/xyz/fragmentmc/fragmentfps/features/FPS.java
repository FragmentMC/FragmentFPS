package xyz.fragmentmc.fragmentfps.features;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class FPS implements CommandExecutor {
    public final HashMap<UUID, Boolean> tnt_vis;
    public final HashMap<UUID, Boolean> sand_vis;
    public final HashMap<UUID, Boolean> anvil_vis;
    public final HashMap<UUID, Boolean> concrete_vis;
    public final HashMap<UUID, Boolean> explosion_vis;
    public final HashMap<UUID, Boolean> flash_vis;
    public final HashMap<UUID, Boolean> piston_vis;
    public final HashMap<UUID, Boolean> spawner_vis;

    public static final ItemStack tntitem = new ItemStack(Material.TNT);
    public static final ItemStack sanditem = new ItemStack(Material.SAND);
    public static final ItemStack anvilitem = new ItemStack(Material.ANVIL);
    public static final ItemStack concreteitem = new ItemStack(Material.BLUE_CONCRETE);
    public static final ItemStack explosionitem = new ItemStack(Material.GUNPOWDER);
    public static final ItemStack flashitem = new ItemStack(Material.REDSTONE_LAMP);
    public static final ItemStack pistonitem = new ItemStack(Material.PISTON);
    public static final ItemStack spawneritem = new ItemStack(Material.SPAWNER);

    private static final String nc = "" + ChatColor.RESET + ChatColor.WHITE;

    private static final String on = ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "]" + ChatColor.RESET;
    private static final String off = ChatColor.GRAY + "[" + ChatColor.RED + "-" + ChatColor.GRAY + "]" + ChatColor.RESET;

    private final Configuration cfg;
    private static final String itempath = "fps.menu-bg";

    public FPS(Configuration cfg) {
        this.tnt_vis = new HashMap<>();
        this.sand_vis = new HashMap<>();
        this.anvil_vis = new HashMap<>();
        this.concrete_vis = new HashMap<>();
        this.explosion_vis = new HashMap<>();
        this.flash_vis = new HashMap<>();
        this.piston_vis = new HashMap<>();
        this.spawner_vis = new HashMap<>();


        this.cfg = cfg;
    }

    private ItemStack getGuiItem() {
        try {
            ItemStack ri =new ItemStack(Material.valueOf(cfg.getString(itempath).toUpperCase()));
            ItemMeta rm = ri.getItemMeta();
            rm.setDisplayName(" ");
            ri.setItemMeta(rm);
            return ri;
        } catch(Exception e) {
            return new ItemStack(Material.BARRIER);
        }
    }

    public boolean ValidateInv(Inventory inv, Player player) {
        return this.GetGui(player.getUniqueId(), player.getName()).contains(inv.getItem(11)) && inv.getType().equals(InventoryType.CHEST);
    }

    public Inventory GetGui(UUID uuid, String name) {
        Inventory i = Bukkit.createInventory(null, 45, "FPS Settings (" + name + ")");
        ItemStack[] f = new ItemStack[45];
        Arrays.fill(f, getGuiItem());
        i.setContents(f);

        ItemStack tntitem = this.tntitem.clone();
        ItemStack sanditem = this.sanditem.clone();
        ItemStack anvilitem = this.anvilitem.clone();
        ItemStack concreteitem = this.concreteitem.clone();
        ItemStack explosionitem = this.explosionitem.clone();
        ItemStack flashitem = this.flashitem.clone();
        ItemStack pistonitem = this.pistonitem.clone();
        ItemStack spawneritem = this.spawneritem.clone();

        ItemMeta tm = tntitem.getItemMeta();
        tm.setDisplayName((tnt_vis.getOrDefault(uuid, false) ? off : on) + nc + " TNT");
        tntitem.setItemMeta(tm);

        ItemMeta sm = sanditem.getItemMeta();
        sm.setDisplayName((sand_vis.getOrDefault(uuid, false) ? off : on) + nc + " Sand");
        sanditem.setItemMeta(sm);

        ItemMeta am = anvilitem.getItemMeta();
        am.setDisplayName((anvil_vis.getOrDefault(uuid, false) ? off : on) + nc + " Anvil");
        anvilitem.setItemMeta(am);

        ItemMeta cm = concreteitem.getItemMeta();
        cm.setDisplayName((concrete_vis.getOrDefault(uuid, false) ? off : on) + nc + " Concrete");
        concreteitem.setItemMeta(cm);

        ItemMeta em = explosionitem.getItemMeta();
        em.setDisplayName((explosion_vis.getOrDefault(uuid, false) ? off : on) + nc + " Explosion");
        explosionitem.setItemMeta(em);

        ItemMeta fm = flashitem.getItemMeta();
        fm.setDisplayName((flash_vis.getOrDefault(uuid, false) ? off : on) + nc + " TNT Flash (WIP)");
        flashitem.setItemMeta(fm);

        ItemMeta pm = pistonitem.getItemMeta();
        pm.setDisplayName((piston_vis.getOrDefault(uuid, false) ? off : on) + nc + " Moving Piston");
        pistonitem.setItemMeta(pm);

        ItemMeta spm = spawneritem.getItemMeta();
        spm.setDisplayName((spawner_vis.getOrDefault(uuid, false) ? off : on) + nc + " Spawner");
        spawneritem.setItemMeta(spm);

        i.setItem(11, tntitem);
        i.setItem(21, sanditem);
        i.setItem(13, anvilitem);
        i.setItem(23, concreteitem);
        i.setItem(15, explosionitem);
        i.setItem(25, flashitem);
        i.setItem(29, pistonitem);
        i.setItem(31, spawneritem);
        return i;
    }

    @Override
    public boolean onCommand(CommandSender author, Command cmd, String label, String[] args) {
        if (!(author instanceof Player)) {
            author.sendMessage("Command can only be used by players!");
            return true;
        }

        Player player = ((Player) author);
        UUID uuid = player.getUniqueId();

        //add player to hash maps if not already added
        if (!tnt_vis.containsKey(uuid)) {
            tnt_vis.put(uuid, false);
            sand_vis.put(uuid, false);
            anvil_vis.put(uuid, false);
            concrete_vis.put(uuid, false);
            explosion_vis.put(uuid, false);
            flash_vis.put(uuid, false);
            piston_vis.put(uuid, false);
            spawner_vis.put(uuid, false);
        }

        if (cmd.getName().equalsIgnoreCase("fps")) {
            player.openInventory(GetGui(player.getUniqueId(), player.getName()));
        }

        return true;
    }
}
