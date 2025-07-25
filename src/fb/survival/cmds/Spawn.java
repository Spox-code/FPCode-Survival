package fb.survival.cmds;

import fb.core.api.RanksAPI;
import fb.survival.gui.gui.AdminItemsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Spawn implements CommandExecutor {

    public static Map<Player, Integer> teleport = new HashMap<>();
    public static Map<Player, Boolean> teleportstatus = new HashMap<>();
    static RanksAPI ra;

    public Spawn(RanksAPI ra){
        Spawn.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.spawn")){
                teleport.put(p, 0);
            }else {
                teleport.put(p, 5);
            }
            teleportstatus.put(p, true);
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
}
