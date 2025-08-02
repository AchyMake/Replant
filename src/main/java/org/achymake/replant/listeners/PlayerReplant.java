package org.achymake.replant.listeners;

import org.achymake.replant.Replant;
import org.achymake.replant.events.PlayerReplantEvent;
import org.achymake.replant.handlers.BlockHandler;
import org.achymake.replant.handlers.MaterialHandler;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

public class PlayerReplant implements Listener {
    private Replant getInstance() {
        return Replant.getInstance();
    }
    private FileConfiguration getConfig() {
        return getInstance().getConfig();
    }
    private BlockHandler getBlockHandler() {
        return getInstance().getBlockHandler();
    }
    private MaterialHandler getMaterials() {
        return getInstance().getMaterialHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public PlayerReplant() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerReplant(PlayerReplantEvent event) {
        if (event.isCancelled())return;
        var block = event.getClickedBlock();
        if (!getBlockHandler().isEnable(block))return;
        var player = event.getPlayer();
        var heldItem = player.getInventory().getItemInMainHand();
        var damage = getConfig().getInt("blocks." + block.getType() + ".damage");
        if (getMaterials().isHoe(heldItem)) {
            if (getMaterials().isWoodenHoe(heldItem)) {
                if (!player.hasPermission("replant.event.replant.wooden_hoe"))return;
            } else if (getMaterials().isStoneHoe(heldItem)) {
                if (!player.hasPermission("replant.event.replant.stone_hoe"))return;
            } else if (getMaterials().isIronHoe(heldItem)) {
                if (!player.hasPermission("replant.event.replant.iron_hoe"))return;
            } else if (getMaterials().isGoldenHoe(heldItem)) {
                if (!player.hasPermission("replant.event.replant.golden_hoe"))return;
            } else if (getMaterials().isDiamondHoe(heldItem)) {
                if (!player.hasPermission("replant.event.replant.diamond_hoe"))return;
            } else if (getMaterials().isNetheriteHoe(heldItem)) {
                if (!player.hasPermission("replant.event.replant.netherite_hoe"))return;
            }
            if (player.getGameMode().equals(GameMode.SURVIVAL)) {
                getMaterials().addDamage(heldItem, damage);
            }
        } else if (!player.hasPermission("replant.event.replant.hand"))return;
        player.swingMainHand();
        getBlockHandler().playSound(block);
        getBlockHandler().dropItems(block, heldItem);
        getBlockHandler().dropExperience(block);
        getBlockHandler().resetAge(block);
        if (getMaterials().isHoe(heldItem)) {
            if (getMaterials().isDestroyed(heldItem)) {
                getMaterials().breakItem(player.getLocation(), heldItem);
            }
        }
    }
}