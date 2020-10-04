package fr.northenflo.permiflow.variables;

import fr.northenflo.permiflow.Main;

import java.util.UUID;

public class PlayerPermiFlow {

    private String userID;
    private GroupPermiFlow groupe;
    private String prefix;
    private String suffix;

    public PlayerPermiFlow(String userID, GroupPermiFlow groupe, String prefix, String suffix){
        this.userID = userID;
        this.groupe = groupe;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getUserID(){
        return this.userID;
    }

    public GroupPermiFlow getGroupe(){
        return this.groupe;
    }

    public void setGroupe(GroupPermiFlow newGroupe){
        this.groupe = newGroupe;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public static PlayerPermiFlow getPlayerList(UUID uuid) {
        for(PlayerPermiFlow player : Main.getInstance().getArrListPlayer()) {
            if (player.getUserID().equalsIgnoreCase(String.valueOf(uuid)))
                return player;
        }
        return null;
    }

    public static void updatePlayerGroup(UUID pID, GroupPermiFlow groupe) {
        for(PlayerPermiFlow player : Main.getInstance().getArrListPlayer())
            if (player.getUserID().equalsIgnoreCase(String.valueOf(pID)))
                player.setGroupe(groupe);
    }

    public static void updateAllPlayerGroupSpecific(GroupPermiFlow gID) {
        for(PlayerPermiFlow player : Main.getInstance().getArrListPlayer())
            if (player.getGroupe().getGroupID().equalsIgnoreCase(gID.getGroupID()))
                player.setGroupe(gID);
    }

    public static void removePlayerList(UUID uuid) {
        int count = 0;
        while (Main.getInstance().getArrListPlayer().size() > count) {
            if(Main.getInstance().getArrListPlayer().get(count).getUserID().equalsIgnoreCase(String.valueOf(uuid)))
                Main.getInstance().getArrListPlayer().remove(count);
            count++;
        }
    }

}
