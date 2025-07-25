package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.ServerAPI;
import fb.survival.gui.gui.AdminItemsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawn implements CommandExecutor {

    static RanksAPI ra;

    public SetSpawn(RanksAPI ra){
        SetSpawn.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.spawn")){
                ServerAPI.setSpawn(p.getLocation());
                p.sendTitle(HexAPI.hex("#0096fc§lSPAWN"), HexAPI.hex("§fUstawiles #0096fcSpawn"));
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.spawn )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
}
