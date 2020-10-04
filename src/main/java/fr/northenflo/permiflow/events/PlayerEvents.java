package fr.northenflo.permiflow.events;

import com.mongodb.DBObject;
import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.SQLPermiFlow;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import fr.northenflo.permiflow.variables.PermissionPermiFlow;
import fr.northenflo.permiflow.variables.PlayerPermiFlow;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getInstance().log("INFOS", "Check Player in database...");
        if(!SQLPermiFlow.checkPlayerExist(event.getPlayer()))
            SQLPermiFlow.storePlayer(event.getPlayer());

        List<DBObject> collectPlayers = Main.getInstance().getSqlPermiFlow().getPlayersAll();
        for(DBObject playerObj : collectPlayers)
            Main.getInstance().getArrListPlayer().add(new PlayerPermiFlow((String) playerObj.get("uuid"), GroupPermiFlow.getGroupList((String) playerObj.get("groupe_uuid")), event.getPlayer().addAttachment(Main.getInstance()), (String) playerObj.get("prefix"), (String) playerObj.get("suffix")));

        PlayerPermiFlow PFPlayer = PlayerPermiFlow.getPlayerList(event.getPlayer().getUniqueId());
        for (PermissionPermiFlow perms : PFPlayer.getGroupe().getListPerms())
            PFPlayer.getAttachmentPerm().setPermission(perms.getNodePerm(), true);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        Main.getInstance().removeAllPerms(event.getPlayer());
        PlayerPermiFlow.removePlayerList(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event){
        event.setFormat(
                ChatColor.translateAlternateColorCodes('&',  PlayerPermiFlow.getPlayerList(event.getPlayer().getUniqueId()).getGroupe().getPrefix())
                        + ChatColor.translateAlternateColorCodes('&',  PlayerPermiFlow.getPlayerList(event.getPlayer().getUniqueId()).getPrefix())
                        + ChatColor.RESET
                        + "%1$s "
                        + ChatColor.translateAlternateColorCodes('&',  PlayerPermiFlow.getPlayerList(event.getPlayer().getUniqueId()).getGroupe().getSuffix())
                        + ChatColor.translateAlternateColorCodes('&',  PlayerPermiFlow.getPlayerList(event.getPlayer().getUniqueId()).getSuffix())
                        + ChatColor.RESET
                        + ": %2$s");
    }

}
