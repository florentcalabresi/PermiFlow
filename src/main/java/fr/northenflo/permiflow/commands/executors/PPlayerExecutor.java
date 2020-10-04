package fr.northenflo.permiflow.commands.executors;

import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.SQLPermiFlow;
import fr.northenflo.permiflow.commands.ICommandP;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import fr.northenflo.permiflow.variables.PlayerPermiFlow;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import java.util.UUID;

public class PPlayerExecutor extends ICommandP {

    public PPlayerExecutor(Main instancePlugin, String name, String usage, String description, String permission, boolean onlyPlayer, int lengthMinArgs) {
        super(instancePlugin, name, usage, description, permission, onlyPlayer, lengthMinArgs);
    }

    @Override
    public boolean onCmd(CommandSender sender, String cml, String[] args) {
        if(args[0].equalsIgnoreCase("promote")){
            String memberPromote = args[1];
            String nameGroup = args[2];
            if (!memberPromote.isEmpty() && !nameGroup.isEmpty()) {
                try {
                    OfflinePlayer playerOff = this.getInstancePlugin().getServer().getOfflinePlayer(memberPromote);
                    if (SQLPermiFlow.checkGroupExist("name", nameGroup)) {
                        GroupPermiFlow groupeGet = GroupPermiFlow.getGroupListWN(nameGroup);
                        PlayerPermiFlow playerPermiFlow = PlayerPermiFlow.getPlayerList(playerOff.getUniqueId());
                        if(!playerPermiFlow.getGroupe().getGroupID().equalsIgnoreCase(groupeGet.getGroupID())) {
                            SQLPermiFlow.updatePlayer(this.getInstancePlugin(),  playerOff.getPlayer(), UUID.fromString(groupeGet.getGroupID()), playerPermiFlow.getPrefix(), playerPermiFlow.getSuffix());
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le joueur a été promote dans le groupe " + nameGroup);
                        }else {
                            this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Le joueur est déjà dans le groupe " + nameGroup);
                        }
                    }else {
                        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Ce groupe n'existe pas !");
                    }
                    return true;
                }catch (NullArgumentException e){
                    this.getInstancePlugin().sendMessagePlayer(this.getPlayerSender(), "Joueur introuvable.");
                    return true;
                }
            }else{
                this.getInstancePlugin().sendMessagePlayer(this.getSender(), "Use: /pplayer promote <player> <group>");
            }
            return true;
        }
        return false;
    }

}
