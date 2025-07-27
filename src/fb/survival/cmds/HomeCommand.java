package fb.survival.cmds;

import fb.survival.gui.gui.CraftingiGUI;
import fb.survival.gui.gui.HomeGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            HomeGUI.OpenGUI(p);
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
}
