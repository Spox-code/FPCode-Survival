package fb.survival.api;

import fb.survival.data.ServerData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ServerAPI {

    static ServerData sd;

    public ServerAPI(){
        sd = ServerData.getInstance();
    }
    public static boolean isNether(){
        return sd.getData().getBoolean("options.nether");
    }
    public static boolean isBoss(){
        return sd.getData().getBoolean("options.boss");
    }
    public static boolean isKits(){
        return sd.getData().getBoolean("options.kits");
    }
    public static void setNether(boolean status){
        sd.getData().set("options.nether", status);
        sd.saveData();
    }
    public static void setKits(boolean status){
        sd.getData().set("options.kits", status);
        sd.saveData();
    }
    public static void setBoss(boolean status){
        sd.getData().set("options.boss", status);
        sd.saveData();
    }
    public static void setSpawn(Location loc){
        sd.getData().set("spawn.world", loc.getWorld().getName());
        sd.getData().set("spawn.x", loc.getX());
        sd.getData().set("spawn.y", loc.getY());
        sd.getData().set("spawn.z", loc.getZ());
        sd.getData().set("spawn.yaw", loc.getYaw());
        sd.getData().set("spawn.pitch", loc.getPitch());
        sd.saveData();
    }
    public static Location getSpawn(){
        World world = Bukkit.getWorld(sd.getData().getString("spawn.world"));
        return new Location(world, sd.getData().getDouble("spawn.x"), sd.getData().getDouble("spawn.y"), sd.getData().getDouble("spawn.z"), (float) sd.getData().getDouble("spawn.yaw"), (float) sd.getData().getDouble("spawn.pitch"));
    }
    public static void setSprawdzanie(Location loc){
        sd.getData().set("sprawdzanie.world", loc.getWorld().getName());
        sd.getData().set("sprawdzanie.x", loc.getX());
        sd.getData().set("sprawdzanie.y", loc.getY());
        sd.getData().set("sprawdzanie.z", loc.getZ());
        sd.getData().set("sprawdzanie.yaw", loc.getYaw());
        sd.getData().set("sprawdzanie.pitch", loc.getPitch());
        sd.saveData();
    }
    public static Location getSprawdzanie(){
        World world = Bukkit.getWorld(sd.getData().getString("sprawdzanie.world"));
        return new Location(world, sd.getData().getDouble("sprawdzanie.x"), sd.getData().getDouble("sprawdzanie.y"), sd.getData().getDouble("sprawdzanie.z"), (float) sd.getData().getDouble("sprawdzanie.yaw"), (float) sd.getData().getDouble("sprawdzanie.pitch"));
    }
    public static void randomTeleport(Player p){
        int max = 5000;
        int randomx = (int) (Math.random()*10000-max);
        int randomz = (int) (Math.random()*10000-max);
        World world = Bukkit.getWorld("world");
        int y = world.getHighestBlockYAt(randomx, randomz);
        p.teleport(new Location(world, randomx, y, randomz));
    }

}
