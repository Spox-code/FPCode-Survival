package fb.survival.cmds;

import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.survival.api.BossAPI;
import fb.survival.api.ServerAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Boss implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public Boss(RanksAPI ra){
        Boss.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.boss")){
                if(args.length == 1){
                    if(args[0].equalsIgnoreCase("zombie")){
                        BossAPI.SpawnBossZombie(p.getLocation());
                        p.sendTitle(HexAPI.hex("#0096fc§lBOSS"), HexAPI.hex("§fPojawiles Bossa #0096FCZombie"));
                    }else{
                        p.sendMessage("§cUzycie /boss <nazwa>");
                    }
                }else{
                    p.sendMessage("§cUzycie /boss <nazwa>");
                }
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.boss )");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.boss")){
                tab.add("zombie");
            }
        }
        return tab;
    }
}
