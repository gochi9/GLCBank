package com.deadshotmdf.GLCBank.Commands;

import com.deadshotmdf.GLCBank.ConfigSettings;
import com.deadshotmdf.GLCBank.GLCB;
import com.deadshotmdf.GLCBank.Managers.BankManager;
import com.deadshotmdf.GLCBank.Objects.Enums.CommandType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {

    public final static List<String> EMPTY = Collections.emptyList();

    private final HashMap<String, SubCommand> subCommands;

    public BankCommand(GLCB main, BankManager bankManager) {
        this.subCommands = new HashMap<>();
        this.subCommands.put("balance", new SeeBalanceSelf(bankManager, "glcbank.balance", CommandType.PLAYER, 0, ConfigSettings.getSeeBalanceSelfHelpMessage(), ""));
        this.subCommands.put("peek", new PeekBalance(bankManager, "glcbank.peek", CommandType.BOTH, 1, ConfigSettings.getPeekBalanceHelpMessage(), ConfigSettings.getInvalidPeekBalanceSyntax()));
        this.subCommands.put("consoledepositwithdraw", new DepositWithdrawForPlayerAsConsole(bankManager, "glcbank.consoledepositwithdraw", CommandType.CONSOLE, 3, ConfigSettings.getConsoleDepositWithdrawHelpMessage(), ConfigSettings.getInvalidConsoleDepositWithdrawSyntax()));
        this.subCommands.put("reload", new ReloadConfig(main, bankManager, "glcbank.reload", CommandType.BOTH, 0, ConfigSettings.getReloadConfigHelpMessage(), ""));
        this.subCommands.put("deposit", new DepositWithdrawCommand(bankManager, "glcbank.depositwithdraw", CommandType.PLAYER, 1, ConfigSettings.getDepositHelpMessage(), ConfigSettings.getInvalidDepositSyntax()));
        this.subCommands.put("withdraw", new DepositWithdrawCommand(bankManager, "glcbank.depositwithdraw", CommandType.PLAYER, 1, ConfigSettings.getWithdrawHelpMessage(), ConfigSettings.getInvalidWithdrawSyntax()));
        this.subCommands.put("openinput", new OpenPlayerInput(bankManager, "glcbank.openinput", CommandType.CONSOLE, 2, ConfigSettings.getOpenPlayerInputHelpMessage(), ConfigSettings.getInvalidOpenPlayerInputSyntax()));
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 1 || args[0].equalsIgnoreCase("help")){
            List<String> allowedCommands = getAllowedCommands(sender);

            if(allowedCommands.isEmpty())
                sender.sendMessage(ConfigSettings.getNoAvailableCommands());
            else
                allowedCommands.forEach(allowedCommand -> sender.sendMessage(subCommands.get(allowedCommand).getCommandHelpMessage()));

            return true;
        }

        SubCommand subCommand = subCommands.get(args[0]);

        if(subCommand == null){
            sender.sendMessage(ConfigSettings.getInvalidCommand());
            return true;
        }

        if(!subCommand.canExecute(sender, args.length, true))
            return true;

        subCommand.execute(sender, args);
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0)
            return EMPTY;

        if(args.length == 1)
            return getAllowedCommands(sender);


        SubCommand subCommand = subCommands.get(args[0]);
        return subCommand != null && subCommand.canExecute(sender, 0, false) ? subCommand.tabCompleter(sender, args) : EMPTY;
    }

    private List<String> getAllowedCommands(CommandSender sender) {
        List<String> allowedCommands = new LinkedList<>();
        subCommands.forEach((k, v) -> {
            if(v.canExecute(sender, 0, false))
                allowedCommands.add(k);
        });

        return allowedCommands;
    }
}
