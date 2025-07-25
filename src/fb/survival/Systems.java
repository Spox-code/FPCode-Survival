package fb.survival;

import fb.actionbar.api.ActionBar;
import fb.core.api.HexAPI;
import fb.survival.api.ServerAPI;
import fb.survival.cmds.Spawn;
import fb.survival.cmds.Vanish;
import fb.survival.events.EntityDamageByEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Systems {

    static Main plugin;

    public Systems(Main m){
        plugin = m;
    }
    public static void start(){
        new BukkitRunnable(){

            @Override
            public void run() {
                for(Player ps : Bukkit.getOnlinePlayers()){
                    if(EntityDamageByEntity.antylogoutstatus.get(ps)){
                        ActionBar.sendMessage(ps, "§9§lANTYLOGOUT §f" + EntityDamageByEntity.antylogouttime.get(ps) + "sek");
                        EntityDamageByEntity.antylogouttime.put(ps, EntityDamageByEntity.antylogouttime.get(ps)-1);
                        if(EntityDamageByEntity.antylogouttime.get(ps) == 0){
                            ps.sendMessage("§aNie jestes juz podczas walki");
                            EntityDamageByEntity.antylogouttime.put(ps, 30);
                            EntityDamageByEntity.antylogoutstatus.put(ps, false);
                            ActionBar.sendMessage(ps, "§9§lANTYLOGOUT §fNie jestes juz podczas walki");
                        }
                    }
                    if(Spawn.teleportstatus.get(ps)){
                        if(Spawn.teleport.get(ps) == 0){
                            ps.teleport(ServerAPI.getSpawn());
                            ps.sendTitle(HexAPI.hex("#0096fc§lTELEPORT"), HexAPI.hex("§fZostales przeteleportowany na #0096fcSpawn"));
                            Spawn.teleport.put(ps, 5);
                            Spawn.teleportstatus.put(ps, false);
                        }else {
                            ps.sendTitle(HexAPI.hex("#0096fc§lTELEPORT"), HexAPI.hex("§fZostaniesz przeteleportowany za #0096fc" + Spawn.teleport.get(ps) + "sek"), 0, 25, 10);
                            Spawn.teleport.put(ps, Spawn.teleport.get(ps) - 1);
                        }
                    }

                    boolean isInVanish = Vanish.vanishlist.getOrDefault(ps, false);
                    boolean isNearSpawnBorder = false; // Domyślnie false

                    World playerWorld = ps.getLocation().getWorld();
                    if(playerWorld.getName().equalsIgnoreCase("spawn")){
                        WorldBorder worldBorder = playerWorld.getWorldBorder();
                        Location playerLocation = ps.getLocation();

                        if (isNearWorldBorder(playerLocation, worldBorder, 10)) {
                            isNearSpawnBorder = true;
                        }
                    }
                    if (isInVanish && isNearSpawnBorder) {
                        ActionBar.sendMessage(ps, HexAPI.hex("§fJestes teraz §bniewidoczny §f| §cJestes blisko granicy spawna"));
                    } else if (isInVanish) {
                        ActionBar.sendMessage(ps, HexAPI.hex("§fJestes teraz §bniewidoczny"));
                    } else if (isNearSpawnBorder) {
                        ActionBar.sendMessage(ps, "§cJestes blisko granicy spawna");
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public static boolean isNearWorldBorder(Location location, WorldBorder border, int distance) {
        double locX = location.getX();
        double locZ = location.getZ();

        double centerX = border.getCenter().getX();
        double centerZ = border.getCenter().getZ();
        double size = border.getSize() / 2.0;

        double minX = centerX - size;
        double maxX = centerX + size;
        double minZ = centerZ - size;
        double maxZ = centerZ + size;

        if (locX <= minX + distance || locX >= maxX - distance ||
                locZ <= minZ + distance || locZ >= maxZ - distance) {
            return true;
        }
        return false;
    }
}