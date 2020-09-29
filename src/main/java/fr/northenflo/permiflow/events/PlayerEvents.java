package fr.northenflo.permiflow.events;

import fr.northenflo.permiflow.Main;
import fr.northenflo.permiflow.SQLPermiFlow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Main.getInstance().log("INFOS", "Check Player in database...");
        if(!SQLPermiFlow.checkPlayerExistSQL(event.getPlayer()))
            SQLPermiFlow.inserPlayerSQL(event.getPlayer());
    }

}
