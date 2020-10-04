package fr.northenflo.permiflow.commands.executors;

import com.mongodb.DBObject;
import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.SQLPermiFlow;
import fr.northenflo.permiflow.commands.ICommandP;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import fr.northenflo.permiflow.variables.PermissionPermiFlow;
import fr.northenflo.permiflow.variables.PlayerPermiFlow;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PReloadExecutor extends ICommandP {

    public PReloadExecutor(Main main, String name, String usage, String description, String permission, boolean onlyPlayer) {
        super(main, name, usage, description, permission, onlyPlayer);
    }

    @Override
    public boolean onCmd(CommandSender sender, String cml, String[] args) {
        SQLPermiFlow.createsTables();

        this.getInstancePlugin().setGroupesCollection(this.getInstancePlugin().getDatabase().getCollection("groupes"));
        this.getInstancePlugin().setPlayersCollection(this.getInstancePlugin().getDatabase().getCollection("players"));


        if (!SQLPermiFlow.checkGroupExist("name", "default"))
            SQLPermiFlow.storeGroup(GroupPermiFlow.defaultGroup.getGroupID(), GroupPermiFlow.defaultGroup.getName(), GroupPermiFlow.defaultGroup.getPrefix(), GroupPermiFlow.defaultGroup.getSuffix());

        this.getInstancePlugin().getArrListPlayer().clear();
        this.getInstancePlugin().getArrListGroup().clear();

        Main.getInstance().log("INFOS", "Retrieve Groupes in database...");
        List<DBObject> collectGroupes = SQLPermiFlow.getGroupesAll();
        for(DBObject groupe : collectGroupes)
            Main.getInstance().getArrListGroup().add(new GroupPermiFlow((String) groupe.get("uuid"), (String) groupe.get("name"), (String) groupe.get("prefix"), (String) groupe.get("suffix")));

        Main.getInstance().log("INFOS", "Retrieve Permissions in database...");
        List<DBObject> collectPermissions = SQLPermiFlow.getPermissionsAll();
        for (DBObject perms : collectPermissions)
            for (GroupPermiFlow group : Main.getInstance().getArrListGroup())
                if(group.getGroupID().equalsIgnoreCase((String) perms.get("groupe_uuid")))
                    group.getListPerms().add(new PermissionPermiFlow((String) perms.get("uuid"), (String) perms.get("node")));

        Main.getInstance().log("INFOS", "Check Player in database...");
        for (Player player : this.getInstancePlugin().getServer().getOnlinePlayers()){
            if (!SQLPermiFlow.checkPlayerExist(player.getPlayer()))
                SQLPermiFlow.storePlayer(player.getPlayer());
            List<DBObject> collectPlayers = Main.getInstance().getSqlPermiFlow().getPlayersAll();
            for(DBObject playerObj : collectPlayers)
                Main.getInstance().getArrListPlayer().add(new PlayerPermiFlow((String) playerObj.get("uuid"), GroupPermiFlow.getGroupList((String) playerObj.get("groupe_uuid")), player.addAttachment(Main.getInstance()), (String) playerObj.get("prefix"), (String) playerObj.get("suffix")));

            Main.getInstance().removeAllPerms(player);

            for (PlayerPermiFlow PFPlayer : Main.getInstance().getArrListPlayer())
                for (PermissionPermiFlow perms : PFPlayer.getGroupe().getListPerms())
                    PFPlayer.getAttachmentPerm().setPermission(perms.getNodePerm(), true);
        }

        this.getInstancePlugin().log("INFOS", "Reload finished");
        this.getInstancePlugin().sendMessagePlayer(this.getSender(), "PermiFlow a bien était rechargé");
        return true;
    }

}
