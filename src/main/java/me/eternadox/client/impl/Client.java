package me.eternadox.client.impl;

import me.eternadox.bus.Bus;
import me.eternadox.client.api.config.ConfigReader;
import me.eternadox.client.impl.manager.CommandManager;
import me.eternadox.client.impl.manager.ModuleManager;
import me.eternadox.client.impl.manager.NotificationManager;
import org.lwjgl.opengl.Display;

import java.io.File;

public enum Client {
    INSTANCE;

    private String name = "blåhajhack";
    private final String version = "0.1";
    private final Bus bus = new Bus();
    private final ModuleManager moduleManager = new ModuleManager();
    private final CommandManager commandManager = new CommandManager();
    private final NotificationManager notificationManager = new NotificationManager();
    private final ConfigReader configReader = new ConfigReader();

    public void initialize(){
        Display.setTitle(name+" version "+version);

        moduleManager.initialize();
        commandManager.initialize();
        notificationManager.initialize();

        if (!(configReader.configExists("current.json"))) {
            System.out.println("No current config exists, creating one.");
            configReader.createConfig("current.json");
        }

        System.out.println("Loading current config");
        configReader.loadConfig("current.json");

    }

    public Bus getBus(){
        return this.bus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public ConfigReader getConfigReader() {
        return configReader;
    }
}
