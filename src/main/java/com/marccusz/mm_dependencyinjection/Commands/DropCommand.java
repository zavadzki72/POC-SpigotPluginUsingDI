package com.marccusz.mm_dependencyinjection.Commands;

import com.google.inject.Inject;
import com.marccusz.mm_dependencyinjection.Cache.ICacheManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class DropCommand implements CommandExecutor
{
    private final ICacheManager _cacheManager;

    @Inject
    public DropCommand(ICacheManager cacheManager)
    {
        _cacheManager = cacheManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String aliases, String[] strings)
    {
        if(!(commandSender instanceof Player))
        {
            return false;
        }

        Player player = (Player)commandSender;
        String cacheKey = "CACHE_DROP_"+ player.getUniqueId();

        if(strings.length == 1)
        {
            String arg = strings[0];
            Boolean value = GetDropBoolByArgs(arg);

            if(value == null){
                player.sendMessage("§cInvalid arguments. Give §2<on> §cto turn on, or §4<off> §cto turn off");
                return  false;
            }

            _cacheManager.Set(cacheKey, value);

            player.sendMessage("§aThe drop configuration was updated!");
            return true;
        }

        Boolean actualValue = _cacheManager.GetByKey(cacheKey);

        if(actualValue == null)
        {
            actualValue = false;
        }

        _cacheManager.Set(cacheKey, !actualValue);

        ItemStack itemTest = new ItemStack(Material.CARPET);
        ItemMeta itemMetaTest = itemTest.getItemMeta();

        itemMetaTest.setDisplayName("§dTESTING");
        itemMetaTest.setLore(Collections.singletonList("§aThis is just a test!"));

        itemTest.setItemMeta(itemMetaTest);

        player.getInventory().addItem(itemTest);
        player.sendMessage("§aThe drop configuration was updated!");
        return true;
    }

    private Boolean GetDropBoolByArgs(String arg)
    {
        if(arg.equalsIgnoreCase("on"))
        {
            return true;
        }
        else if(arg.equalsIgnoreCase("off"))
        {
            return false;
        }

        return null;
    }
}
