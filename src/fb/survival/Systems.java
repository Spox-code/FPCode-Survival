package fb.survival;

import fb.actionbar.api.ActionBar;
import fb.core.api.HexAPI;
import fb.survival.api.BossAPI;
import fb.survival.api.DropManager;
import fb.survival.api.PlayerAPI;
import fb.survival.api.ServerAPI;
import fb.survival.cmds.Spawn;
import fb.survival.cmds.Sprawdzanie;
import fb.survival.cmds.Vanish;
import fb.survival.cmds.Zrzut;
import fb.survival.events.EntityDamageByEntity;
import fb.survival.gui.gui.HomeGUI;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Systems {

    static Main plugin;
    private static boolean messageSentToday = false;
    private static Random random = new Random();
    private static long globalTickCounter = 0; // Własny licznik ticków serwera

    // Mapa do śledzenia, czy dany boss już się zregenerował (jeśli chcesz mieć taką mechanikę)
    private static Map<UUID, Boolean> bossRegenerationStatus = new HashMap<>();


    public Systems(Main m){
        plugin = m;
    }

    public static void start(){
        // NOWE ZADANIE: Inkrementacja globalnego licznika ticków
        new BukkitRunnable() {
            @Override
            public void run() {
                globalTickCounter++;
            }
        }.runTaskTimer(plugin, 0L, 1L); // Działa co 1 tick


        // Istniejące zadanie dla ActionBara i innych mechanik (co 1 sekundę)
        new BukkitRunnable(){
            @Override
            public void run() {
                for(Player ps : Bukkit.getOnlinePlayers()){
                    List<String> messages = new ArrayList<>();

                    if(EntityDamageByEntity.antylogoutstatus.getOrDefault(ps, false)){
                        messages.add(HexAPI.hex("§9§lANTYLOGOUT §f" + EntityDamageByEntity.antylogouttime.get(ps) + "sek"));
                        EntityDamageByEntity.antylogouttime.put(ps, EntityDamageByEntity.antylogouttime.get(ps)-1);
                        if(EntityDamageByEntity.antylogouttime.get(ps) == 0){
                            ps.sendMessage("§aNie jestes juz podczas walki");
                            EntityDamageByEntity.antylogouttime.put(ps, 30);
                            EntityDamageByEntity.antylogoutstatus.put(ps, false);
                        }
                    }

                    if(Sprawdzanie.playerisCheck.getOrDefault(ps, false)){
                        ps.sendTitle(HexAPI.hex("#0096fc§lSPRAWDZANIE"), HexAPI.hex("§fJestes sprawdzany przez #0096fcAdministratora!"), 0, 25, 10);
                    }

                    if(Spawn.teleportstatus.getOrDefault(ps, false)){
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
                    if(HomeGUI.isteleport.getOrDefault(ps, false)){
                        if(HomeGUI.teleporttime.get(ps) == 0){
                            ps.teleport(PlayerAPI.getHome(ps, HomeGUI.teleporthome.get(ps)));
                            ps.sendTitle(HexAPI.hex("#0096fc§lTELEPORT"), HexAPI.hex("§fZostales przeteleportowany na #0096fcHome #" + HomeGUI.teleporthome.get(ps)));
                            HomeGUI.teleporttime.put(ps, 5);
                            HomeGUI.isteleport.put(ps, false);
                        }else {
                            ps.sendTitle(HexAPI.hex("#0096fc§lTELEPORT"), HexAPI.hex("§fZostaniesz przeteleportowany za #0096fc" + HomeGUI.teleporttime.get(ps) + "sek"), 0, 25, 10);
                            HomeGUI.teleporttime.put(ps, HomeGUI.teleporttime.get(ps) - 1);
                        }
                    }

                    boolean isInVanish = Vanish.vanishlist.getOrDefault(ps, false);
                    boolean isNearSpawnBorder = false;

                    World playerWorld = ps.getLocation().getWorld();
                    if(playerWorld != null && playerWorld.getName().equalsIgnoreCase("spawn")){
                        WorldBorder worldBorder = playerWorld.getWorldBorder();
                        Location playerLocation = ps.getLocation();

                        if (isNearWorldBorder(playerLocation, worldBorder, 10)) {
                            isNearSpawnBorder = true;
                        }
                    }

                    if (isInVanish) {
                        messages.add(HexAPI.hex("§fJestes teraz §bniewidoczny"));
                    }
                    if (isNearSpawnBorder) {
                        messages.add(HexAPI.hex("§cJestes blisko granicy spawna"));
                    }

                    // Użycie funkcji getNearestBoss
                    Zombie nearestBoss = BossAPI.getNearestBoss(ps.getLocation(), 20.0);
                    if (nearestBoss != null) {
                        double health = nearestBoss.getHealth();
                        double maxHealth = nearestBoss.getMaxHealth();
                        int percentage = (int) ((health / maxHealth) * 100);

                        String healthBar = createHealthBar(percentage);
                        messages.add(HexAPI.hex("§6§lBOSS ZOMBIE §fHP: " + healthBar + " §a" + (int)health + "§7/§c" + (int)maxHealth));
                    }

                    if (!messages.isEmpty()) {
                        String finalMessage = String.join(HexAPI.hex(" §f| "), messages);
                        ActionBar.sendMessage(ps, finalMessage);
                    } else {
                        ActionBar.sendMessage(ps, "");
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L); // Co 1 sekundę (20 ticków)


        // ZADANIE: Teleportacja Bossów (co 15 sekund)
        new BukkitRunnable(){
            @Override
            public void run() {
                // Używamy kopii, aby uniknąć ConcurrentModificationException
                for (UUID bossId : new ArrayList<>(BossAPI.getActiveBosses().keySet())) {
                    Zombie boss = BossAPI.getActiveBosses().get(bossId);

                    if (boss != null && !boss.isDead()) {
                        Location oldBossLoc = boss.getLocation();
                        World world = oldBossLoc.getWorld();

                        if (world == null) {
                            plugin.getLogger().warning("Boss found in an unloaded world. Skipping teleportation.");
                            continue;
                        }

                        // Logika teleportacji
                        boolean teleported = false;
                        for (int i = 0; i < 10; i++) { // Próby teleportacji
                            double randomX = oldBossLoc.getX() + (random.nextDouble() * 40) - 20; // -20 do 20 od bossa
                            double randomZ = oldBossLoc.getZ() + (random.nextDouble() * 40) - 20; // -20 do 20 od bossa

                            // Sprawdź, czy chunk jest załadowany przed próbą pobrania highestY
                            if (!world.isChunkLoaded((int) randomX >> 4, (int) randomZ >> 4)) {
                                continue;
                            }

                            int highestY = world.getHighestBlockYAt((int) randomX, (int) randomZ);
                            // +1 aby boss nie był w bloku ani zakopany w ziemi
                            Location targetLoc = new Location(world, randomX, highestY + 1, randomZ);

                            Block blockAtTarget = targetLoc.getBlock();
                            Block blockAboveTarget = targetLoc.clone().add(0, 1, 0).getBlock(); // Blok nad celem

                            // Sprawdź, czy miejsce jest bezpieczne do teleportacji
                            if (blockAtTarget.getType().isSolid() && // Blok pod spodem jest solidny
                                    blockAboveTarget.getType() == Material.AIR && // Blok nad spodem jest powietrzem
                                    blockAtTarget.getType() != Material.LAVA && // Nie teleportujemy się do lawy
                                    blockAtTarget.getType() != Material.WATER) { // Nie teleportujemy się do wody

                                boss.teleport(targetLoc);
                                boss.getWorld().spawnParticle(Particle.PORTAL, oldBossLoc, 50);
                                boss.getWorld().playSound(oldBossLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                teleported = true;
                                break; // Teleportacja udana, przerywamy pętlę prób
                            }
                        }

                        // NOWOŚĆ: Dodano brakującą logikę wysyłania wiadomości po teleportacji
                        if (teleported) {
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.getLocation().getWorld().equals(boss.getWorld()) &&
                                        p.getLocation().distance(boss.getLocation()) <= 20) {
                                    ActionBar.sendMessage(p, HexAPI.hex("§bBOSS ZOMBIE §fteleportowal sie!"));
                                }
                            }
                        }

                        // === NOWA LOGIKA: Regeneracja życia bossa po spadku HP (przeniesiona tutaj) ===
                        double healthPercentage = (boss.getHealth() / boss.getMaxHealth()) * 100;
                        boolean hasRegenerated = bossRegenerationStatus.getOrDefault(boss.getUniqueId(), false);

                        // Jeśli zdrowie bossa spadło poniżej 30% i jeszcze się nie zregenerował
                        if (healthPercentage <= 30 && !hasRegenerated) {
                            boss.setHealth(boss.getMaxHealth()); // Pełna regeneracja
                            bossRegenerationStatus.put(boss.getUniqueId(), true); // Ustawiamy flagę, że się zregenerował

                            // Efekty wizualne i dźwiękowe
                            boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_ILLUSIONER_PREPARE_BLINDNESS, 1.0f, 0.5f);
                            boss.getWorld().spawnParticle(Particle.HEART, boss.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5);

                            // Wiadomość dla graczy w pobliżu
                            for (Player p : boss.getWorld().getPlayers()) {
                                if (p.getLocation().getWorld().equals(boss.getWorld()) &&
                                        p.getLocation().distance(boss.getLocation()) <= 50) {
                                    p.sendMessage(HexAPI.hex("§bBOSS ZOMBIE §frozjuszyl sie i #0096fcodzyskali pelne zycie§f!"));
                                }
                            }
                        }
                        // === KONIEC LOGIKI REGENERACJI ===

                    } else if (boss != null && boss.isDead()) {
                        BossAPI.removeBoss(bossId);
                        // Kiedy boss umiera, resetujemy jego status regeneracji
                        bossRegenerationStatus.remove(bossId);
                    }
                }
            }
        }.runTaskTimer(plugin, 20L * 15, 20L * 15); // Uruchamia się co 15 sekund (20*15 ticków)


        // NOWE ZADANIE: Spawnowanie minionów (co 2 minuty)
        new BukkitRunnable() {
            @Override
            public void run() {
                // Używamy kopii, aby uniknąć ConcurrentModificationException
                for (UUID bossId : new ArrayList<>(BossAPI.getActiveBosses().keySet())) {
                    Zombie boss = BossAPI.getActiveBosses().get(bossId);
                    if (boss != null && !boss.isDead()) {
                        // Sprawdzamy, czy minęło 2 minuty (120 sekund = 20 * 120 ticków)
                        // Używamy globalTickCounter
                            BossAPI.spawnMinions(boss, 10, 5.0);
                            boss.getWorld().playSound(boss.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CONVERTED, 1.0f, 1.0f);
                            for (Player p : Bukkit.getOnlinePlayers()) {
                                if (p.getLocation().getWorld().equals(boss.getWorld()) &&
                                        p.getLocation().distance(boss.getLocation()) <= 50) {
                                    p.sendMessage(HexAPI.hex("§bBOSS ZOMBIE §fprzywolal swoich pomagierow!"));
                                }
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20*120L); // Działa co sekundę, żeby sprawdzać tickCounter co tick, a nie co 2 minuty!


        new BukkitRunnable() {
            @Override
            public void run() {
                if(ServerAPI.isBoss()){
                    int max = 5000;
                    int randomx = (int) (Math.random()*10000-max);
                    int randomz = (int) (Math.random()*10000-max);
                    World world = Bukkit.getWorld("world");
                    int maxy = world.getHighestBlockYAt(randomx, randomz);
                    Location randomloc = new Location(world, randomx, maxy, randomz);
                    BossAPI.SpawnBossZombie(randomloc);
                }
            }
        }.runTaskTimer(plugin, 0L, 20*60*60*6L);

        // ZADANIE: Zrzut (co minutę, ale z logiką tylko raz dziennie)
        new BukkitRunnable(){
            @Override
            public void run() {
                LocalTime now = LocalTime.now();

                // Sprawdzamy, czy jest godzina 18:00 i czy wiadomość nie została jeszcze wysłana dzisiaj
                if (now.getHour() == 18 && now.getMinute() == 0 && !messageSentToday) {

                    int max = 5000;
                    int randomx = (int) (Math.random()*10000-max);
                    int randomz = (int) (Math.random()*10000-max);
                    World world = Bukkit.getWorld("world"); // Upewnij się, że "world" to prawidłowa nazwa świata

                    if (world == null) {
                        plugin.getLogger().warning("World 'world' not found for Zrzut event.");
                        return; // Zatrzymaj wykonywanie, jeśli świat nie istnieje
                    }

                    int maxy = world.getHighestBlockYAt(randomx, randomz);
                    Location playerLoc = new Location(world, randomx, maxy, randomz); // Użyj zmiennej 'world'

                    // Zmniejsz promień, jeśli jest za duży, aby uniknąć problemów z wydajnością
                    int radius = 4;

                    // Logika zapisu bloków do przywrócenia
                    for (int x = -radius; x <= radius; x++) {
                        for (int y = -radius; y <= radius; y++) {
                            for (int z = -radius; z <= radius; z++) { // Poprawiono z <= z na z <= radius
                                Location blockLoc = playerLoc.clone().add(x, y, z);
                                Block block = blockLoc.getBlock();

                                if (block.getType() != Material.AIR && block.getType() != Material.BEDROCK) {
                                    Zrzut.blocksToRestore.put(blockLoc.clone(), block.getType());
                                }
                            }
                        }
                    }

                    playerLoc.getWorld().createExplosion(playerLoc, 4.0f, true, true);

                    Block blockBelowPlayer = playerLoc.clone().subtract(0, 1, 0).getBlock();
                    // Znajdź pierwszą solidną powierzchnię pod miejscem zrzutu
                    if (blockBelowPlayer.getType() == Material.AIR || blockBelowPlayer.getType() == Material.WATER || blockBelowPlayer.getType() == Material.LAVA) {
                        Location tempLoc = playerLoc.clone().subtract(0, 1, 0);
                        while ((tempLoc.getBlock().getType() == Material.AIR || tempLoc.getBlock().getType() == Material.WATER || tempLoc.getBlock().getType() == Material.LAVA) && tempLoc.getY() > world.getMinHeight()) {
                            tempLoc.subtract(0, 1, 0);
                        }
                        blockBelowPlayer = tempLoc.add(0, 1,0).getBlock(); // Dodaj 1, aby skrzynia była na powierzchni
                    }

                    blockBelowPlayer.setType(Material.CHEST);

                    Zrzut.zrzutChestsWithDestroyedBlocks.put(blockBelowPlayer.getLocation(), Zrzut.blocksToRestore);

                    List<String> hologramLines = Arrays.asList(
                            HexAPI.hex("#0096fc&lNOWY ZRZUT!"),
                            "&b&lKliknij skrzynkę, aby otworzyć!",
                            "&7&o(Teren zostanie przywrócony po otwarciu)"
                    );
                    List<String> newhologram = hologramLines.reversed(); // Odwraca listę
                    double spacing = 0.25;
                    double initialYOffset = 1.0;
                    DropManager.createDropHologram(blockBelowPlayer.getLocation(), newhologram, spacing, initialYOffset);

                    for(Player ps : Bukkit.getOnlinePlayers()){
                        ps.sendMessage("");
                        ps.sendMessage(HexAPI.hex("   #0096fc§lZRZUT"));
                        ps.sendMessage(HexAPI.hex("   §8» §fPojawil sie #0096fcZrzut"));
                        ps.sendMessage(HexAPI.hex("   §8» §fNa kordynatach #0096FCX: §f" + randomx + " #0096FCZ: §f" + randomz));
                        ps.sendMessage("");
                    }
                    messageSentToday = true; // Oznacz, że wiadomość została wysłana dzisiaj
                } else if (now.getHour() == 18 && now.getMinute() == 1) { // Resetuj flagę po minucie od 18:00
                    messageSentToday = false;
                }
            }
        }.runTaskTimer(plugin, 0L, 20L * 60); // Działa co 1 minutę (20 ticków/sek * 60 sek/min)
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

        return locX <= minX + distance || locX >= maxX - distance ||
                locZ <= minZ + distance || locZ >= maxZ - distance;
    }

    private static String createHealthBar(int percentage) {
        int totalBars = 20;
        int greenBars = (int) Math.round((double) percentage / 100 * totalBars);
        int redBars = totalBars - greenBars;

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < greenBars; i++) {
            bar.append("§a█");
        }
        for (int i = 0; i < redBars; i++) {
            bar.append("§c█");
        }
        return bar.toString();
    }
}