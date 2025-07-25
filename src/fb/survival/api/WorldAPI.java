package fb.survival.api;

import fb.core.api.HexAPI;
import fb.survival.data.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WorldAPI {

    static ServerData cd;

    public WorldAPI(){
        cd = ServerData.getInstance();
    }

    public static void createWorld(Player p, String worldname){
        World existingWorld = Bukkit.getServer().getWorld(worldname);
        if (existingWorld != null) {
            p.sendMessage("§cSwiat o nazwie " + worldname + " już istnieje!");
            return;
        }
        p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fTworzenie swiata #0096FC" + worldname + "!"));
        WorldCreator worldCreator = new WorldCreator(worldname);
        World world = Bukkit.getServer().createWorld(worldCreator);
        p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fSwiat #0096FC" + worldname + " §fzostal stworzony!"));
        if(cd.getData().getStringList("worlds") == null){
            List<String> worlds = new ArrayList<>();
            worlds.add(worldname);
            cd.getData().set("worlds", worlds);
        }else {
            List<String> worlds = cd.getData().getStringList("worlds");
            worlds.add(worldname);
            cd.getData().set("worlds", worlds);
        }
        cd.saveData();
    }
    private static void loadWorld(String worldname){
        WorldCreator worldCreator = new WorldCreator(worldname);
        World world = Bukkit.getServer().createWorld(worldCreator);
    }
    public static void loadWorlds(){
        if(cd.getData().getStringList("world") != null){
            List<String> worlds = cd.getData().getStringList("worlds");
            int size = worlds.size();
            for(int i = 0; i<size; i++){
                loadWorld(worlds.get(i));
            }
        }
    }

    public static void teleportPlayerToWorld(Player p, String worldname) {
        World world = Bukkit.getWorld(worldname);
        if (world != null) {
            Location spawnLocation = world.getSpawnLocation();
            p.teleport(spawnLocation);
            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fZostales teleportowany do swiata #0096FC" + world.getName() + "!"));
        } else {
            p.sendMessage("§cSwiat nie istnieje!");
        }
    }
    public static void deleteWorld(Player p, String worldname) {
        World world = Bukkit.getServer().getWorld(worldname);
        if (world == null) {
            p.sendMessage("§cSwiat o nazwie " + worldname + " nie istnieje!");
            return;
        }

        // Unload the world
        boolean unloaded = Bukkit.getServer().unloadWorld(world, false);
        if (unloaded) {
            // Delete the world folder
            File worldFolder = new File(Bukkit.getWorldContainer(), worldname);
            deleteDirectory(worldFolder);
            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fSwiat #0096FC" + worldname + " §fzostal usuniety!"));
        } else {
            p.sendMessage("§cNie mozna wyladowac swiata " + worldname + "!");
        }
    }

    private static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        directory.delete();
    }
}
