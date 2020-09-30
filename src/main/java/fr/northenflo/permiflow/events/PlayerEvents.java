package fr.northenflo.permiflow.events;

import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.SQLPermiFlow;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getInstance().log("INFOS", "Check Player in database...");
        if(!SQLPermiFlow.checkPlayerExist(event.getPlayer()))
            SQLPermiFlow.storePlayer(event.getPlayer());
    }

    @EventHandler
    public void playerChat(AsyncPlayerChatEvent event){
        event.setFormat(ChatColor.translateAlternateColorCodes('&',  (String) SQLPermiFlow.getGroupeForPlayer("uuid", String.valueOf(event.getPlayer().getUniqueId())).get("prefix"))+ "" + ChatColor.RESET + "%1$s: %2$s");
    }

}
