package fr.northenflo.permiflow.commands.executors;

import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.SQLPermiFlow;
import fr.northenflo.permiflow.commands.ICommandP;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class PGroupExecutor extends ICommandP {

    private String action;

    public PGroupExecutor(Main instancePlugin, String name, String usage, String description, String permission, boolean onlyPlayer, int lengthMinArgs) {
        super(instancePlugin, name, usage, description, permission, onlyPlayer, lengthMinArgs);
    }

    @Override
    public boolean onCmd(CommandSender sender, String cml, String[] args) {
        this.action = args[0];
        if(this.action.equalsIgnoreCase("create")){
            String nameGroup = args[1];
            if (!nameGroup.isEmpty()) {
                if (!SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                    SQLPermiFlow.storeGroup(String.valueOf(UUID.randomUUID()), nameGroup, "", "");
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le groupe "+nameGroup+" a bien été créée");
                }else
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Ce nom est déjà pris !");
                return true;
            }else{
                this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /gcreate <name>");
                return true;
            }
        }else if(this.action.equalsIgnoreCase("setprefix")){
            String nameGroup = args[1];
            if (!nameGroup.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }
                String prefixGroup = builder.toString();
                if (!prefixGroup.isEmpty()){
                    if (SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                        GroupPermiFlow groupeGet = GroupPermiFlow.getGroupListWN(nameGroup);
                        SQLPermiFlow.updateGroup(groupeGet, groupeGet.getName(), prefixGroup, groupeGet.getSuffix());
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le prefix du groupe a été mis à jour");
                    }else
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le groupe n'existe pas !");
                    return true;
                }else{
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pgroup setprefix <group> <prefix>");
                    return true;
                }
            }else{
                this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pgroup setprefix <group> <prefix>");
                return true;
            }
        }else if(this.action.equalsIgnoreCase("setsuffix")){
            String nameGroup = args[1];
            if (!nameGroup.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    builder.append(args[i]).append(" ");
                }
                String suffixGroup = builder.toString();
                if (!suffixGroup.isEmpty()){
                    if (SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                        GroupPermiFlow groupeGet = GroupPermiFlow.getGroupListWN(nameGroup);
                        SQLPermiFlow.updateGroup(groupeGet, groupeGet.getName(), groupeGet.getPrefix(), suffixGroup);
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le suffix du groupe a été mis à jour");
                    }else
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le groupe n'existe pas !");
                    return true;
                }else{
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pgroup setsuffix <group> <suffix>");
                    return true;
                }
            }else{
                this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pgroup setsuffix <group> <suffix>");
                return true;
            }
        }
        return false;
    }

}
