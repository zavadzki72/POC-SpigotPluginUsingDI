package com.marccusz.mm_dependencyinjection.Events;

import com.google.inject.Inject;
import com.marccusz.mm_dependencyinjection.Cache.ICacheManager;
import com.marccusz.mm_dependencyinjection.Database.IMongoDbGenericRepository;
import com.marccusz.mm_dependencyinjection.Database.IMongoDbManager;
import com.marccusz.mm_dependencyinjection.Models.PlacedBlockData;
import dev.morphia.Datastore;
import dev.morphia.query.Query;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;

public class PlayerEvents implements Listener
{
    private final boolean _isUsingMongoDb = true;

    private final ICacheManager _cacheManager;
    private final IMongoDbGenericRepository _mongoDbGenericRepository;
    private final IMongoDbManager _mongoDbManager;

    @Inject
    public PlayerEvents(ICacheManager cacheManager, IMongoDbGenericRepository mongoDbGenericRepository, IMongoDbManager mongoDbManager)
    {
        _cacheManager = cacheManager;
        _mongoDbGenericRepository = mongoDbGenericRepository;
        _mongoDbManager = mongoDbManager;
    }

    @EventHandler
    public void OnBreak(BlockBreakEvent event)
    {
        Player player = event.getPlayer();
        String cacheKey = "CACHE_DROP_"+ player.getUniqueId();

        Boolean dropConfiguration = _cacheManager.GetByKey(cacheKey);

        if(dropConfiguration == null || !dropConfiguration)
        {
            return;
        }

        if(player.getInventory().firstEmpty() == -1)
        {
            player.sendMessage("§cYou are with full inventory!");
            return;
        }

        if(_isUsingMongoDb)
        {
            OnBreakWithMongo(event, player);
        }
        else
        {
            OnBreakWithoutMongo(event, player);
        }
    }

    private void OnBreakWithoutMongo(BlockBreakEvent event, Player player)
    {
        Block block = event.getBlock();

        Collection<ItemStack> dropsOfBlock = block.getDrops();

        block.setType(Material.AIR);

        for (ItemStack item : dropsOfBlock)
        {
            player.getInventory().addItem(item);
        }
    }

    private void OnBreakWithMongo(BlockBreakEvent event, Player player)
    {
        Block block = event.getBlock();
        Collection<ItemStack> dropsOfBlock = block.getDrops();
        Location loc = event.getBlock().getLocation();

        Datastore connection = _mongoDbManager.GetConnection();
        Query<PlacedBlockData> query = connection.createQuery(PlacedBlockData.class);

        query.and(
                query.criteria("X").equal(loc.getBlockX()),
                query.criteria("Y").equal(loc.getBlockY()),
                query.criteria("Z").equal(loc.getBlockZ())
        );

        PlacedBlockData result = query.first();
        block.setType(Material.AIR);

        if(result == null)
        {
            for (ItemStack item : dropsOfBlock)
            {
                player.getInventory().addItem(item);
            }

            return;
        }

        for (ItemStack item : dropsOfBlock)
        {
            ItemMeta itemMeta = item.getItemMeta();
            itemMeta.setDisplayName(result.ItemName);
            itemMeta.setLore(result.ItemLore);
            item.setItemMeta(itemMeta);

            player.getInventory().addItem(item);
        }

        _mongoDbGenericRepository.Delete(result);
    }

    @EventHandler
    public void OnPlace(BlockPlaceEvent event)
    {
        if(!_isUsingMongoDb)
        {
            return;
        }

        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();

        if(!meta.hasDisplayName() && !meta.hasLore())
        {
            return;
        }

        Location loc = event.getBlockPlaced().getLocation();
        PlacedBlockData entity = new PlacedBlockData(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), meta.getDisplayName(), meta.getLore());

        _mongoDbGenericRepository.Create(entity);
    }
}
