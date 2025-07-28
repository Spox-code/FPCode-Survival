package fb.survival.cmds;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.core.api.TabListAPI;
import fb.survival.api.ServerAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Settings implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public Settings(RanksAPI ra){
        Settings.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.settings")){
                if(args.length == 2){
                    String status = args[0];
                    String setting = args[1];
                    if(setting.equalsIgnoreCase("nether")){
                        if(status.equalsIgnoreCase("start")){
                            ServerAPI.setNether(true);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fWlaczyles #0096fcNether"));
                            for(Player ps : Bukkit.getOnlinePlayers()){
                                ps.sendMessage("");
                                ps.sendMessage(HexAPI.hex("   #0096fcFPCode §8-#0096fc Ogloszenie"));
                                ps.sendMessage(HexAPI.hex("   §8» §fWlasnie zostal wlaczony #0096fcNether"));
                                ps.sendMessage("");
                            }
                        }else if(status.equalsIgnoreCase("off")){
                            ServerAPI.setNether(false);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fWylaczyles #0096fcNether"));
                            for(Player ps : Bukkit.getOnlinePlayers()){
                                ps.sendMessage("");
                                ps.sendMessage(HexAPI.hex("   #0096fcFPCode §8-#0096fc Ogloszenie"));
                                ps.sendMessage(HexAPI.hex("   §8» §fWlasnie zostal wylaczony #0096fcNether"));
                                ps.sendMessage("");
                                if(ps.getLocation().getWorld().getName().equalsIgnoreCase("world_nether")){

                                }
                            }
                        }else{
                            p.sendMessage("§cUzycie /settings <start/off> <usluga>");
                        }
                    }else if(setting.equalsIgnoreCase("kity")){
                        if(status.equalsIgnoreCase("start")){
                            ServerAPI.setKits(true);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fWlaczyles #0096fcKity"));
                            for(Player ps : Bukkit.getOnlinePlayers()){
                                ps.sendMessage("");
                                ps.sendMessage(HexAPI.hex("   #0096fcFPCode §8-#0096fc Ogloszenie"));
                                ps.sendMessage(HexAPI.hex("   §8» §fWlasnie zostaly wlaczone #0096fcKity"));
                                ps.sendMessage("");
                            }
                        }else if(status.equalsIgnoreCase("off")){
                            ServerAPI.setKits(false);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fWylaczyles #0096fcKity"));
                            for(Player ps : Bukkit.getOnlinePlayers()){
                                ps.sendMessage("");
                                ps.sendMessage(HexAPI.hex("   #0096fcFPCode §8-#0096fc Ogloszenie"));
                                ps.sendMessage(HexAPI.hex("   §8» §fWlasnie zostaly wylaczone #0096fcKity"));
                                ps.sendMessage("");
                            }
                        }else{
                            p.sendMessage("§cUzycie /settings <start/off> <usluga>");
                        }
                    }else if(setting.equalsIgnoreCase("boss")){
                        if(status.equalsIgnoreCase("start")){
                            ServerAPI.setBoss(true);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fWlaczyles #0096fcBossy"));
                            for(Player ps : Bukkit.getOnlinePlayers()){
                                ps.sendMessage("");
                                ps.sendMessage(HexAPI.hex("   #0096fcFPCode §8-#0096fc Ogloszenie"));
                                ps.sendMessage(HexAPI.hex("   §8» §fWlasnie zostaly wlaczone #0096fcBossy"));
                                ps.sendMessage("");
                            }
                        }else if(status.equalsIgnoreCase("off")){
                            ServerAPI.setBoss(false);
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fWylaczyles #0096fcBossy"));
                            for(Player ps : Bukkit.getOnlinePlayers()){
                                ps.sendMessage("");
                                ps.sendMessage(HexAPI.hex("   #0096fcFPCode §8-#0096fc Ogloszenie"));
                                ps.sendMessage(HexAPI.hex("   §8» §fWlasnie zostaly wylaczone #0096fcBossy"));
                                ps.sendMessage("");
                            }
                        }else{
                            p.sendMessage("§cUzycie /settings <start/off> <usluga>");
                        }
                    }else{
                        p.sendMessage("§cUzycie /settings <start/off> <usluga>");
                    }
                }else{
                    p.sendMessage("§cUzycie /settings <start/off> <usluga>");
                }
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.settings )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        List<String> tab = new ArrayList<>();
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.settings")){
                if(args.length == 1){
                    tab.add("start");
                    tab.add("off");
                }else if(args.length == 2){
                    tab.add("nether");
                    tab.add("kity");
                    tab.add("boss");
                }
            }
        }
        return tab;
    }
}
