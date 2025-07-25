package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.gui.gui.AdminItemsGUI;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminItems implements CommandExecutor {

    static RanksAPI ra;

    public AdminItems(RanksAPI ra){
        AdminItems.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.aitems")){
                AdminItemsGUI.OpenGUI(p);
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.aitems )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
}
