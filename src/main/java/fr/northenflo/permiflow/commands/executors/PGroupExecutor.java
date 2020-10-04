package fr.northenflo.permiflow.commands.executors;

import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.SQLPermiFlow;
import fr.northenflo.permiflow.commands.ICommandP;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import fr.northenflo.permiflow.variables.PermissionPermiFlow;
import fr.northenflo.permiflow.variables.PlayerPermiFlow;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            if (sender.hasPermission("permiflow.group.create")) {
                String nameGroup = args[1];
                if (!nameGroup.isEmpty()) {
                    if (!SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                        SQLPermiFlow.storeGroup(String.valueOf(UUID.randomUUID()), nameGroup, "", "");
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le groupe " + nameGroup + " a bien été créée");
                    } else
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Ce nom est déjà pris !");
                    return true;
                } else {
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pfgroup create <name>");
                    return true;
                }
            }else{
                this.getInstancePlugin().sendMessagePlayer(sender, "Vous n'avez pas la permission pour la commande");
                return true;
            }
        }else if(this.action.equalsIgnoreCase("addperm")){
            String nameGroup = args[1];
            String nodePerm = args[2];
            if (sender.hasPermission("permiflow.group.addperm")) {
                if (!nameGroup.isEmpty() && !nodePerm.isEmpty()) {
                    if (SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                        GroupPermiFlow groupGet = GroupPermiFlow.getGroupListWN(nameGroup);
                        if (!SQLPermiFlow.checkPermAlreadyGive(nodePerm, groupGet.getGroupID())) {
                            SQLPermiFlow.storePerms(UUID.randomUUID(), nodePerm, groupGet.getGroupID());
                            Main.getInstance().reloadConfig();
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Permission added !");
                        }else
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Cette permission a déjà était donné !");
                    }else
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Ce groupe n'existe pas !");
                }else{
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pfgroup addperm <group> <node>");
                }
            }else{
                this.getInstancePlugin().sendMessagePlayer(sender, "Vous n'avez pas la permission pour la commande");
                return true;
            }
            return true;
        }else if(this.action.equalsIgnoreCase("delperm")){
            String nameGroup = args[1];
            String nodePerm = args[2];
            if (sender.hasPermission("permiflow.group.delperm")) {
                if (!nameGroup.isEmpty() && !nodePerm.isEmpty()) {
                    if (SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                        GroupPermiFlow groupGet = GroupPermiFlow.getGroupListWN(nameGroup);
                        if (SQLPermiFlow.checkPermAlreadyGive(nodePerm, groupGet.getGroupID())) {
                            SQLPermiFlow.deletePerms(nodePerm, groupGet.getGroupID());
                            Main.getInstance().reloadConfig();
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Permission delete !");
                        }else
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Cette permission n'a jamais était donné !");
                    }else
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Ce groupe n'existe pas !");
                }else{
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pfgroup addperm <group> <node>");
                }
            }else{
                this.getInstancePlugin().sendMessagePlayer(sender, "Vous n'avez pas la permission pour la commande");
                return true;
            }
            return true;
        }else if(this.action.equalsIgnoreCase("setprefix")){
            String nameGroup = args[1];
            if (sender.hasPermission("permiflow.group.setprefix")) {
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
            }else{
                this.getInstancePlugin().sendMessagePlayer(sender, "Vous n'avez pas la permission pour la commande");
                return true;
            }
        }else if(this.action.equalsIgnoreCase("setsuffix")){
            String nameGroup = args[1];
            if (sender.hasPermission("permiflow.group.setsuffix")) {
                if (!nameGroup.isEmpty()) {
                    StringBuilder builder = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        builder.append(args[i]).append(" ");
                    }
                    String suffixGroup = builder.toString();
                    if (!suffixGroup.isEmpty()) {
                        if (SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                            GroupPermiFlow groupeGet = GroupPermiFlow.getGroupListWN(nameGroup);
                            SQLPermiFlow.updateGroup(groupeGet, groupeGet.getName(), groupeGet.getPrefix(), suffixGroup);
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le suffix du groupe a été mis à jour");
                        } else
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le groupe n'existe pas !");
                        return true;
                    } else {
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pgroup setsuffix <group> <suffix>");
                        return true;
                    }
                } else {
                    this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pgroup setsuffix <group> <suffix>");
                    return true;
                }
            }else{
                this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Vous n'avez pas la permission pour la commande");
                return true;
            }
        }
        return false;
    }

}
