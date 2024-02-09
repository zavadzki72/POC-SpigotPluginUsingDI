package com.marccusz.mm_dependencyinjection;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.marccusz.mm_dependencyinjection.Commands.DropCommand;
import com.marccusz.mm_dependencyinjection.Database.MongoDbManager;
import com.marccusz.mm_dependencyinjection.Events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MM_DependencyInjection extends JavaPlugin {

    @Inject private DropCommand _dropCommand;
    @Inject private PlayerEvents _playerEvents;
    @Inject private MongoDbManager _mongoDbManager;

    @Override
    public void onEnable()
    {
        MM_DependencyInjectionIoC module = new MM_DependencyInjectionIoC(this);
        Injector injector = module.createInjector();
        injector.injectMembers(this);

        boolean isUsingMongoDb = true;
        if(isUsingMongoDb)
        {
            _mongoDbManager.SetupConnection();
        }

        this.getCommand("drop").setExecutor(_dropCommand);

        this.getServer().getPluginManager().registerEvents(_playerEvents, this);

        Bukkit.getConsoleSender().sendMessage("§6 Plugin on!");
    }

    @Override
    public void onDisable()
    {
        Bukkit.getConsoleSender().sendMessage("§4 Plugin off!");
    }
}
