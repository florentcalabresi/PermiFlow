package fr.northenflo.permiflow.commands;

import fr.northenflo.permiflow.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class ICommandP implements CommandExecutor {

    private String name, description, perms, usage;
    private boolean onlyPlayer;
    private CommandSender sender;
    private Player playerSender;
    private boolean continueCMD = true;
    private int lengthMinArgs;
    private Main instancePlugin;

    public ICommandP(Main instancePlugin, String name, String usage, String description, String permission, boolean onlyPlayer){
        this.instancePlugin = instancePlugin;
        this.name = name;
        this.perms = permission;
        this.description = description;
        this.usage = usage;
        this.onlyPlayer = onlyPlayer;
    }

    public ICommandP(Main instancePlugin, String name, String usage, String description, String permission, boolean onlyPlayer, int lengthMinArgs){
        this.instancePlugin = instancePlugin;
        this.name = name;
        this.perms = permission;
        this.description = description;
        this.usage = usage;
        this.onlyPlayer = onlyPlayer;
        this.lengthMinArgs = lengthMinArgs;
    }

    public abstract boolean onCmd(CommandSender sender, String cml, String[] args);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        this.sender = sender;
        if(this.onlyPlayer){
            if (!(sender instanceof Player)) {
                Main.getInstance().sendMessagePlayer(sender, "Vous n'avez pas la permission pour la commande, "+commandLabel);
                return true;
            }else
                this.playerSender = (Player) sender;
        }

        if (!(sender.hasPermission(this.perms))){
            Main.getInstance().sendMessagePlayer(sender, "Vous n'avez pas la permission pour la commande, "+commandLabel);
            return true;
        }

        if (!(args.length>=this.getLengthMinArgs())){
            Main.getInstance().sendMessagePlayer(sender, "Usage: " + usage);
            return true;
        }


        if (this.continueCMD) {
            if (!(onCmd(sender, commandLabel, args))) {
                Main.getInstance().sendMessagePlayer(sender, "Usage: " + usage);
                return true;
            }
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public CommandSender getSender() {
        return this.sender;
    }

    public Player getPlayerSender() {
        return this.playerSender;
    }

    public int getLengthMinArgs() {
        return this.lengthMinArgs;
    }

    public String getUsage() {
        return this.usage;
    }

    public String getDescription() {
        return this.description;
    }

    public Main getInstancePlugin(){
        return this.instancePlugin;
    }

}
