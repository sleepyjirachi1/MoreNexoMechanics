package org.Autumn.moreNexoMechanics;

import org.Autumn.moreNexoMechanics.mechanics.factories.SwappableEnchantsFactory;
import org.Autumn.moreNexoMechanics.mechanics.listeners.SwappableEnchantsListener;
import org.Autumn.moreNexoMechanics.mechanics.factories.PotionAurasFactory;
import org.Autumn.moreNexoMechanics.mechanics.runnables.PotionAurasTask;

import com.nexomc.nexo.api.events.NexoMechanicsRegisteredEvent;
import com.nexomc.nexo.mechanics.MechanicsManager;
import com.nexomc.nexo.utils.logs.Logs;

import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoreNexoMechanics extends JavaPlugin {

    private SwappableEnchantsFactory swappableEnchantsFactory;
    private PotionAurasFactory potionAurasFactory;

    @Override
    public void onEnable() {
        swappableEnchantsFactory = new SwappableEnchantsFactory();
        potionAurasFactory = new PotionAurasFactory();
        registerListeners();
        registerCommands();
        hookIntoNexoMechanics();

        // Runnables
        new PotionAurasTask(this, potionAurasFactory).start();
    }

    private void registerListeners() {
        registerListener(new SwappableEnchantsListener(this, swappableEnchantsFactory));
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void registerCommands() {
        // None Implemented
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalStateException("Command '" + name + "' is not registered!");
        }
        command.setExecutor(executor);
    }

    private void hookIntoNexoMechanics() {
        registerListener(new Listener() {
            @EventHandler
            public void nRegister(NexoMechanicsRegisteredEvent event) {
                // Register factories here
                MechanicsManager.INSTANCE.registerMechanicFactory(swappableEnchantsFactory, true);
                MechanicsManager.INSTANCE.registerMechanicFactory(potionAurasFactory, true);
                Logs.logInfo("Registered Swappable Enchants");
            }
        });
    }

}
