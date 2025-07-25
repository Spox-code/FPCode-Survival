package fb.survival.cmds;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.core.api.TabListAPI;
import fb.survival.gui.gui.AdminItemsGUI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ECO implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public ECO(RanksAPI ra){
        ECO.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(ra.hasPermission(p, "fb.eco")){
                if(args.length == 4){
                    String playername = args[2];
                    if(BanAPI.hasPlayerStats(playername)){
                        try{
                            int amount = Integer.parseInt(args[3]);
                            String stat = args[1];
                            String event = args[0];
                            if(stat.equalsIgnoreCase("kills") || stat.equalsIgnoreCase("deaths") || stat.equalsIgnoreCase("money")) {
                                if (event.equalsIgnoreCase("set")) {
                                    if (stat.equalsIgnoreCase("kills")) {
                                        BanAPI.setSurvivalKills(playername, amount);
                                    } else if (stat.equalsIgnoreCase("deaths")) {
                                        BanAPI.setSurvivalDeaths(playername, amount);
                                    } else if (stat.equalsIgnoreCase("money")) {
                                        BanAPI.setSurvivalMoney(playername, amount);
                                    }else{
                                        p.sendMessage("§cUzycie /eco <set/take/add> <money/kills/deaths> <gracz> <ilosc>");
                                    }
                                } else if (event.equalsIgnoreCase("add")) {
                                    if (stat.equalsIgnoreCase("kills")) {
                                        BanAPI.addKills(playername, amount);
                                    } else if (stat.equalsIgnoreCase("deaths")) {
                                        BanAPI.addDeaths(playername, amount);
                                    } else if (stat.equalsIgnoreCase("money")) {
                                        BanAPI.addMoney(playername, amount);
                                    }else{
                                        p.sendMessage("§cUzycie /eco <set/take/add> <money/kills/deaths> <gracz> <ilosc>");
                                    }
                                } else if (event.equalsIgnoreCase("take")) {
                                    if (stat.equalsIgnoreCase("kills")) {
                                        BanAPI.takeKills(playername, amount);
                                    } else if (stat.equalsIgnoreCase("deaths")) {
                                        BanAPI.takeDeaths(playername, amount);
                                    } else if (stat.equalsIgnoreCase("money")) {
                                        BanAPI.takeMoney(playername, amount);
                                    }else{
                                        p.sendMessage("§cUzycie /eco <set/take/add> <money/kills/deaths> <gracz> <ilosc>");
                                    }
                                }else{
                                    p.sendMessage("§cUzycie /eco <set/take/add> <money/kills/deaths> <gracz> <ilosc>");
                                }
                                int newamount = 0;
                                if(stat.equalsIgnoreCase("kills")){
                                    newamount = BanAPI.getPlayerStatKills(playername);
                                }else if(stat.equalsIgnoreCase("deaths")){
                                    newamount = BanAPI.getPlayerStatDeaths(playername);
                                }else if(stat.equalsIgnoreCase("money")){
                                    newamount= BanAPI.getPlayerStatMoney(playername);
                                }
                                Player cel = Bukkit.getPlayerExact(playername);
                                if(cel != null){
                                    TabListAPI.pupdate(cel);
                                }
                                p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fUstawiles statystyke #0096fc"+stat+" §fgraczowi #0096fc"+playername+" na #0096fc"+newamount));
                            }else{
                                p.sendMessage("§cUzycie /eco <set/take/add> <money/kills/deaths> <gracz> <ilosc>");
                            }

                        } catch (NumberFormatException e) {
                            p.sendMessage("§cTo musi byc liczba");
                            throw new RuntimeException(e);
                        }
                    }else{
                        p.sendMessage("§cTen gracz nigdy nie byl na serwerze");
                    }
                }else{
                    p.sendMessage("§cUzycie /eco <set/take/add> <money/kills/deaths> <gracz> <ilosc>");
                }
            }else{
                p.sendMessage("§cNie posiadasz uprawnien ( fb.eco )");
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
            if(ra.hasPermission(p, "fb.eco")){
                if(args.length == 1){
                    tab.add("set");
                    tab.add("take");
                    tab.add("add");
                }else if(args.length == 2){
                    tab.add("money");
                    tab.add("kills");
                    tab.add("deaths");
                }else if(args.length == 3){
                    tab.add("<gracz>");
                    for(Player ps : Bukkit.getOnlinePlayers()){
                        tab.add(ps.getName());
                    }
                }else if(args.length == 4){
                    tab.add("<ilosc>");
                }
            }
        }
        return tab;
    }
}
