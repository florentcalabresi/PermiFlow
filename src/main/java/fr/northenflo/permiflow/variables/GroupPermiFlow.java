package fr.northenflo.permiflow.variables;

import fr.northenflo.permiflow.Main;

import java.util.ArrayList;
import java.util.UUID;

public class GroupPermiFlow {

    public static GroupPermiFlow defaultGroup = new GroupPermiFlow(String.valueOf(UUID.randomUUID()), "default", "Default", "");

    private final String groupID;
    private final String name;
    private final String prefix;
    private final String suffix;
    private final ArrayList<PermissionPermiFlow> listPerms = new ArrayList<PermissionPermiFlow>();

    public GroupPermiFlow(String groupID, String name, String prefix, String suffix){
        this.groupID = groupID;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public ArrayList<PermissionPermiFlow> getListPerms(){ return this.listPerms; }

    public String getGroupID(){
        return this.groupID;
    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public static GroupPermiFlow getGroupList(String uuid) {
        for(GroupPermiFlow groupe : Main.getInstance().getArrListGroup()) {
            if (groupe.getGroupID().equalsIgnoreCase(uuid))
                return groupe;
        }
        return null;
    }

    public static GroupPermiFlow getGroupListWN(String name) {
        for(GroupPermiFlow groupe : Main.getInstance().getArrListGroup()) {
            if (groupe.getName().equalsIgnoreCase(name))
                return groupe;
        }
        return null;
    }

    public static void removeGroupList(UUID uuid) {
        int count = 0;
        while (Main.getInstance().getArrListGroup().size() > count) {
            if(Main.getInstance().getArrListGroup().get(count).getGroupID().equalsIgnoreCase(String.valueOf(uuid)))
                Main.getInstance().getArrListGroup().remove(count);
            count++;
        }
    }
}
