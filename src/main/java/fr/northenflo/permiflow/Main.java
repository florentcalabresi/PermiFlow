package fr.northenflo.permiflow;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class Main extends JavaPlugin {

    @Override
    public void onEnable(){
        HashMap<UUID, PermissionAttachment> perms = new HashMap<UUID, PermissionAttachment>();
        Player player = this.getServer().getPlayer("NorthenFlo");
        PermissionAttachment attachment = player.addAttachment(this);
        perms.put(player.getUniqueId(), attachment);

        PermissionAttachment pperms = perms.get(player.getUniqueId());
        pperms.setPermission("permission.here", true);

        perms.get(player.getUniqueId()).unsetPermission("permission.here");
    }

    @Override
    public void onDisable(){

    }

}
