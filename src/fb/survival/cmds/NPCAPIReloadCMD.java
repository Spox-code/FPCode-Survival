package fb.survival.cmds;

import fb.core.api.RanksAPI;
import fb.survival.api.NPCAPI;
import fb.survival.gui.gui.AdminItemsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NPCAPIReloadCMD implements CommandExecutor {

    static RanksAPI ra;

    public NPCAPIReloadCMD(RanksAPI ra){
        NPCAPIReloadCMD.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.npcapireload")){
                NPCAPI.reloadNpcAndHologramData();
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.npcapireload )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
}
