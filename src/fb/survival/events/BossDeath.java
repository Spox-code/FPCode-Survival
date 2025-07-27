package fb.survival.events;

import fb.core.api.HexAPI;
import fb.survival.api.BossAPI;
import fb.survival.items.Zwoj;
import org.bukkit.Material; // Pozostaw, jeśli używasz gdzieś indziej
import org.bukkit.Particle; // Pozostaw, jeśli używasz gdzieś indziej
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
// import org.bukkit.event.entity.EntityResurrectEvent; // USUNIĘTO - nie potrzebne
import org.bukkit.inventory.ItemStack;
// import org.bukkit.inventory.EntityEquipment; // USUNIĘTO - nie potrzebne

public class BossDeath implements Listener {

    @EventHandler
    public void onBossDeath(EntityDeathEvent e){
        LivingEntity entity = e.getEntity();

        if (entity.getType() == EntityType.ZOMBIE) {
            Zombie bossZombie = (Zombie) entity;

            if (BossAPI.isOurBoss(bossZombie)) {
                e.getDrops().clear();
                ItemStack drop = Zwoj.getItem();
                e.getDrops().add(drop);

                // Usuwamy bossa z mapy aktywnych bossów. To jest ostateczna śmierć.
                BossAPI.removeBoss(bossZombie.getUniqueId());

                // Upewnij się, że nie ma tu żadnych innych operacji na martwym bossie
                // (np. wcześniejsze, błędne .damage(20000);)

                e.getEntity().getWorld().strikeLightningEffect(entity.getLocation());
                if (e.getEntity().getKiller() != null) {
                    e.getEntity().getKiller().sendTitle(HexAPI.hex("#0096fc§lBOSS"), HexAPI.hex("§fZabiles bossa #0096fcGratulacje!"));
                }
            }
        }
    }

}