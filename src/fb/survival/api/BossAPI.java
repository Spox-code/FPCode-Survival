package fb.survival.api;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class BossAPI {

    private static Map<UUID, Zombie> activeBosses = new HashMap<>();

    public static void SpawnBossZombie(Location loc) {
        Zombie boss = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        boss.setCustomNameVisible(true);
        boss.setCustomName("§6§lBOSS ZOMBIE");
        double healt = 500.00;
        boss.setMaxHealth(healt);
        boss.setHealth(healt);
        boss.setPersistent(true);
        boss.setCanPickupItems(false);

        if (boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            boss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(15.0);
        }
        if (boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED) != null) {
            boss.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.3);
        }
        if (boss.getAttribute(Attribute.GENERIC_ARMOR) != null) {
            boss.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(20.0);
        }
        if (boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE) != null) {
            boss.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0.8);
        }

        boss.getEquipment().setHelmet(new ItemStack(Material.NETHERITE_HELMET));
        boss.getEquipment().setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
        boss.getEquipment().setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
        boss.getEquipment().setBoots(new ItemStack(Material.NETHERITE_BOOTS));
        boss.getEquipment().setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
        // boss.getEquipment().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING)); // USUNIĘTO TOTEM!

        boss.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 2));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Integer.MAX_VALUE, 1));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60 * 20, 0));
        boss.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0));

        loc.getWorld().strikeLightningEffect(loc);

        activeBosses.put(boss.getUniqueId(), boss);
    }

    /**
     * Zwraca mapę wszystkich aktywnych bossów.
     * @return Mapa UUID do obiektów Zombie (bossów).
     */
    public static Map<UUID, Zombie> getActiveBosses() {
        return new HashMap<>(activeBosses);
    }

    /**
     * Usuwa bossa z mapy aktywnych bossów.
     * Używaj, gdy boss umiera.
     * @param bossId UUID bossa do usunięcia.
     */
    public static void removeBoss(UUID bossId) {
        activeBosses.remove(bossId);
    }

    /**
     * Sprawdza, czy dana encja jest jednym z naszych aktywnych bossów.
     * @param entity Sprawdzana encja.
     * @return True, jeśli encja jest aktywnym bossem, w przeciwnym razie false.
     */
    public static boolean isOurBoss(Zombie entity) {
        return activeBosses.containsKey(entity.getUniqueId());
    }

    /**
     * Znajduje najbliższego bossa w określonym promieniu od danej lokalizacji.
     * @param location Lokalizacja, od której szukamy bossa.
     * @param maxDistance Maksymalna odległość, w której boss musi się znajdować.
     * @return Najbliższy obiekt Zombie (boss), lub null jeśli nie znaleziono bossa w zasięgu.
     */
    public static Zombie getNearestBoss(Location location, double maxDistance) {
        Zombie nearestBoss = null;
        double minDistance = maxDistance;

        for (Zombie boss : activeBosses.values()) {
            if (boss != null && !boss.isDead() && location.getWorld().equals(boss.getWorld())) {
                double distance = location.distance(boss.getLocation());

                if (distance < minDistance) {
                    minDistance = distance;
                    nearestBoss = boss;
                }
            }
        }
        return nearestBoss;
    }

    /**
     * Spawnuje zombiaków wokół bossa.
     * @param boss Boss, wokół którego mają zostać zespawnowane zombie.
     * @param count Liczba zombiaków do zespawnowania.
     * @param radius Promień, w którym mają się pojawić.
     */
    public static void spawnMinions(Zombie boss, int count, double radius) {
        if (boss == null || boss.isDead()) {
            return;
        }

        Location bossLoc = boss.getLocation();
        World world = bossLoc.getWorld();
        Random random = new Random();

        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * 2 * Math.PI;
            double offsetX = Math.cos(angle) * (random.nextDouble() * radius);
            double offsetZ = Math.sin(angle) * (random.nextDouble() * radius);

            Location spawnLoc = bossLoc.clone().add(offsetX, 0, offsetZ);

            int highestY = world.getHighestBlockYAt(spawnLoc.getBlockX(), spawnLoc.getBlockZ());
            spawnLoc.setY(highestY + 1);

            if (spawnLoc.getBlock().getType() == Material.AIR &&
                    spawnLoc.clone().add(0, 1, 0).getBlock().getType() == Material.AIR &&
                    spawnLoc.clone().subtract(0, 1, 0).getBlock().getType().isSolid() &&
                    spawnLoc.clone().subtract(0, 1, 0).getBlock().getType() != Material.LAVA &&
                    spawnLoc.clone().subtract(0, 1, 0).getBlock().getType() != Material.WATER) {

                Zombie minion = (Zombie) world.spawnEntity(spawnLoc, EntityType.ZOMBIE);
                minion.setCustomNameVisible(true);
                minion.setCustomName("§7Zombie Minion");
                minion.setMaxHealth(20.0);
                minion.setHealth(20.0);
                minion.setPersistent(false);

                minion.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
                minion.getEquipment().setItemInMainHand(new ItemStack(Material.STONE_SWORD));
            }
        }
    }
}