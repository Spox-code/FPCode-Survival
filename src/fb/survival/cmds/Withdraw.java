package fb.survival.cmds;

import fb.core.api.BanAPI;
import fb.core.api.HexAPI;
import fb.core.api.RanksAPI;
import fb.core.api.TabListAPI;
import fb.survival.items.BankNote;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Withdraw implements CommandExecutor, TabExecutor {

    static RanksAPI ra;

    public Withdraw(RanksAPI ra){
        fb.survival.cmds.Withdraw.ra = ra;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player p){
            if(args.length == 1){
                try{
                    int amount = Integer.parseInt(args[0]);
                    if(amount > 10){
                        if(BanAPI.getPlayerStatMoney(p.getName()) >= amount){
                            p.getInventory().addItem(BankNote.getitem(amount));
                            p.sendMessage(HexAPI.hex("§8[#0096FC⚡§8] §fWyplaciles #0096fc" + amount + "$"));
                            p.sendTitle(HexAPI.hex("#0096fc§lBANK"), HexAPI.hex("§fWyplaciles #0096fc" + amount + "$"));
                            BanAPI.takeMoney(p.getName(), amount);
                            TabListAPI.pupdate(p);
                        }else{
                            p.sendMessage("§cNie posidasz " + amount + "$");
                        }
                    }else{
                        p.sendMessage("§cKwota nie moze byc mniejsza niz 10$");
                    }
                } catch (NumberFormatException e) {
                    p.sendMessage("§cMusisz podac liczbe!");
                    throw new RuntimeException(e);
                }
            }else{
                p.sendMessage("§cUzycie /withdraw <kwota>");
            }
        }else{
            sender.sendMessage("§cTa komenda jest tylko dla graczy");
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        tab.add("<kwota>");
        return tab;
    }
}
