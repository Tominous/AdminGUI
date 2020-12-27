package com.rabbitcompany.admingui;

import com.rabbitcompany.admingui.commands.Admin;
import com.rabbitcompany.admingui.listeners.InventoryClickListener;
import com.rabbitcompany.admingui.listeners.PlayerDamageListener;
import com.rabbitcompany.admingui.listeners.PlayerJoinListener;
import com.rabbitcompany.admingui.listeners.PlayerLoginListener;
import com.rabbitcompany.admingui.utils.Message;
import com.rabbitcompany.admingui.utils.Updater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class AdminGUI extends JavaPlugin {

    private static AdminGUI instance;

    //VaultAPI
    private static Economy econ = null;
    public static boolean vault = false;

    private File l = null;
    private YamlConfiguration lang = new YamlConfiguration();

    public static SpigotUpdater updater;

    @Override
    public void onEnable() {
        instance = this;
        this.l = new File(getDataFolder(), "language.yml");
        mkdir();
        loadYamls();

        Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &aPlugin is enabled!"));
        info("&aEnabling");

        //bStats
        if(!Bukkit.getVersion().contains("1.8")){
            MetricsLite metricsLite = new MetricsLite(this);
        }

        //Updater
        updater = new SpigotUpdater(this, 71157);

        //VaultAPI
        if(setupEconomy()){
            vault = true;
        }

        //Check for updates
        Updater.sendConsole();

        //Listeners
        new InventoryClickListener(this);
        new PlayerDamageListener(this);
        new PlayerJoinListener(this);
        new PlayerLoginListener(this);

        //Commands
        this.getCommand("admin").setExecutor((CommandExecutor) new Admin());
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(Message.chat("&7[&cAdmin GUI&7] &4Plugin is disabled!"));
        info("&4Disabling");
    }

    //VaultAPI
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    private void mkdir(){
        if (!this.l.exists()) {
            saveResource("language.yml", false);
        }
    }

    private void loadYamls(){
        try {
            this.lang.load(this.l);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getLang() { return this.lang; }

    public void saveLang() {
        try {
            this.lang.save(this.l);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void info(String message){
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8[]=====[" + message + " &cAdminGUI&8]=====[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8| &cInformation:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Name: &bAdminGUI"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Developer: &bBlack1_TV"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Version: &b1.4.5"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8| &cSupport:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Discord: &bCrazy Rabbit#0001"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Mail: &bziga.zajc007@gmail.com"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Discord: &bhttps://discord.gg/hUNymXX"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8[]=====================================[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
    }

    public static AdminGUI getInstance(){
        return instance;
    }
}
