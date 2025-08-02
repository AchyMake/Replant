package org.achymake.replant.listeners;

import org.achymake.replant.Replant;
import org.achymake.replant.handlers.BlockHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.PluginManager;

public class BlockBreak implements Listener {
    private Replant getInstance() {
        return Replant.getInstance();
    }
    private BlockHandler getBlockHandler() {
        return getInstance().getBlockHandler();
    }
    private PluginManager getPluginManager() {
        return getInstance().getPluginManager();
    }
    public BlockBreak() {
        getPluginManager().registerEvents(this, getInstance());
    }
    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled())return;
        var block = event.getBlock();
        if (!getBlockHandler().isEnable(block))return;
        if (!getBlockHandler().isRightAge(block))return;
        getBlockHandler().dropExperience(block);
    }
}