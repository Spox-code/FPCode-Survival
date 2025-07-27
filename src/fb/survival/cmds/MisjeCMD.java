package fb.survival.cmds;

import fb.survival.gui.gui.CraftingiGUI;
import fb.survival.gui.gui.MisjeGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MisjeCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            MisjeGUI.OpenGUI(p);
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }
}
