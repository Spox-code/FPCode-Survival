package fb.survival.events;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.core.api.TabListAPI;
import fb.survival.Main;
import fb.survival.api.DropManager;
import fb.survival.items.BankNote;
import fb.survival.items.EmblematZycia;
import fb.survival.items.TotemUlaskawienia;
import fb.survival.items.Zwoj;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import fb.survival.cmds.Zrzut;

// Importy dla BlockData
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Powerable;
import org.bukkit.block.data.Attachable; // To jest to, czego nam brakowało dla przycisków!
import org.bukkit.block.data.type.WallSign; // Do pobierania kierunku i face dla tabliczek
import org.bukkit.block.data.type.Switch; // Przyciski implementują Switch, który ma Facing i Face

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class ClickEvent implements Listener {

    static RanksAPI ra;
    static Main plugin;

    public ClickEvent(RanksAPI ra, Main m){
        ClickEvent.ra = ra;
        plugin = m;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        ItemStack mainitem = p.getInventory().getItemInMainHand();

        if(a == Action.RIGHT_CLICK_BLOCK){
            Block clickedBlock = e.getClickedBlock();




            if (clickedBlock != null && clickedBlock.getType() == Material.CHEST && Zrzut.zrzutChestsWithDestroyedBlocks.containsKey(clickedBlock.getLocation())) {
                if (e.hasItem() && e.getPlayer().isSneaking()) {
                    return;
                }

                Map<Location, Material> blocksToRestoreMap = Zrzut.zrzutChestsWithDestroyedBlocks.get(clickedBlock.getLocation());
                LinkedList<Entry<Location, Material>> blocksToRestoreList = new LinkedList<>(blocksToRestoreMap.entrySet());

                clickedBlock.setType(Material.AIR);
                Zrzut.zrzutChestsWithDestroyedBlocks.remove(clickedBlock.getLocation());
                DropManager.removeDropHologram(clickedBlock.getLocation());

                try {
                    clickedBlock.getLocation().getWorld().dropItemNaturally(clickedBlock.getLocation(), Zwoj.getItem());
                } catch (NoClassDefFoundError | Exception ex) {
                    plugin.getLogger().severe("Could not drop Zwoj item. Error: " + ex.getMessage());
                }

                new BukkitRunnable() {
                    private Iterator<Entry<Location, Material>> iterator = blocksToRestoreList.iterator();
                    private int restoreCounter = 0;

                    @Override
                    public void run() {
                        if (iterator.hasNext() && restoreCounter < 5) {
                            Entry<Location, Material> entry = iterator.next();
                            Location loc = entry.getKey();
                            Material originalMaterial = entry.getValue();

                            if (loc.getBlock().getType() == Material.AIR || loc.getBlock().getType() == Material.FIRE || loc.getBlock().getType().isBurnable()) {
                                loc.getBlock().setType(originalMaterial);
                            }
                            restoreCounter++;
                        } else if (iterator.hasNext()) {
                            restoreCounter = 0;
                        } else {
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 2L);

                e.setCancelled(true);
                p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fSkrzynka ze zrzutu zostala otwarta! Teren bedzie przywracany."));
                for(Player ps : Bukkit.getOnlinePlayers()){
                    ps.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fZrzut zostal znaleziony przez #0096fc" + p.getName()));
                }
                return;
            }

            if(mainitem.hasItemMeta() && mainitem.getItemMeta().hasCustomModelData() && mainitem.getItemMeta().getCustomModelData() == TotemUlaskawienia.iditem){
                e.setCancelled(true);
            }
            if(p.getLocation().getWorld().getName().equalsIgnoreCase("spawn")){
                if(!ra.hasPermission(p, "fb.spawn")) {
                    e.setCancelled(true);
                }
                if(e.getClickedBlock().getType() == Material.ANVIL){
                    e.setCancelled(true);
                    if(p.getLevel() >= 5){
                        if(mainitem.getType() != Material.AIR) {
                            mainitem.setDurability((short) 0);
                            p.setLevel(p.getLevel() - 5);
                            p.sendTitle(HexAPI.hex("#0096fc§lKOWADLO"), HexAPI.hex("§fNaprawiles przedmiot #0096fc" + mainitem.getType().toString().toUpperCase()));
                        }
                    }
                }
            }
        }
        if(a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR){
            if(mainitem.hasItemMeta() && mainitem.getItemMeta().hasCustomModelData() && mainitem.getItemMeta().getCustomModelData() == EmblematZycia.iditem){
                if (mainitem.getAmount() > 1) {
                    mainitem.setAmount(mainitem.getAmount() - 1);
                } else {
                    p.getInventory().setItemInMainHand(null);
                }
                p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 254));
                p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5);
                p.sendTitle(HexAPI.hex("#0096fc§l❤ §b§lEmblemat Życia #0096fc§l❤"), HexAPI.hex("§fTwoje zycie zostalo #0096fculeczone"));
            }else if(mainitem.hasItemMeta() && mainitem.getItemMeta().hasCustomModelData() && mainitem.getItemMeta().getCustomModelData() == BankNote.iditem){
                String lore = mainitem.getItemMeta().getLore().get(1).replace(HexAPI.hex("§7Banknot o wartości: §b§l"), "").replace("$", "");
                try{
                    int amount = Integer.parseInt(lore);
                    BanAPI.addMoney(p.getName(), amount);
                    if (mainitem.getAmount() > 1) {
                        mainitem.setAmount(mainitem.getAmount() - 1);
                    } else {
                        p.getInventory().setItemInMainHand(null);
                    }
                    p.sendTitle(HexAPI.hex("#0096fc§lBANK"), HexAPI.hex("§fWplaciles #0096fc" + amount + "$"));
                    TabListAPI.pupdate(p);
                } catch (NumberFormatException ex) {
                    p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §cNiepoprawny format banknotu!"));
                    plugin.getLogger().severe("Could not parse banknote amount for player " + p.getName() + ": " + lore + " Error: " + ex.getMessage());
                }
            }
        }
    }
}