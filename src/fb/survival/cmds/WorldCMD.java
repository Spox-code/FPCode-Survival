package fb.survival.cmds;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.core.api.TabListAPI;
import fb.survival.api.WorldAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WorldCMD implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public WorldCMD(RanksAPI ra){
        WorldCMD.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.world")){
                if(args.length == 2){
                    String name = args[1];
                    if(args[0].equalsIgnoreCase("create")){
                        p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fTworzenie swiata #0096fc" + name));
                        WorldAPI.createWorld(p, name);
                        p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fSwiat #0096fc" + name + " §fzostal stworzony"));
                    }else if(args[0].equalsIgnoreCase("teleport")){
                        WorldAPI.teleportPlayerToWorld(p, name);
                        p.sendTitle(HexAPI.hex("#0096fc§lWORLD"), HexAPI.hex("§fZostales przeniesiony na swiat #0096fc" + name));
                    }else if(args[0].equalsIgnoreCase("delete")){
                        p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fUsuwanie swiata #0096fc" + name));
                        WorldAPI.deleteWorld(p, name);
                        p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fSwiat #0096fc" + name + " §fzostal usuniety"));
                    }else{
                        p.sendMessage("§cUzycie /world <teleport/create/delete> <nazwa swiata>");
                    }
                }else{
                    p.sendMessage("§cUzycie /world <teleport/create/delete> <nazwa swiata>");
                }
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.world )");
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
            if(ra.hasPermission(p, "fb.world")){
                if(args.length == 1){
                    tab.add("create");
                    tab.add("teleport");
                    tab.add("delete");
                }else if(args.length == 2){
                    for(World world : Bukkit.getWorlds()){
                        tab.add(world.getName());
                    }
                }
            }
        }
        return tab;
    }
}
