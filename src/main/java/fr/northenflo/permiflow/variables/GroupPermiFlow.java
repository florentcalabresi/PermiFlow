package fr.northenflo.permiflow.variables;

import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupPermiFlow {

    public static GroupPermiFlow defaultGroup = new GroupPermiFlow(String.valueOf(UUID.randomUUID()), "default", "Default", "");

    private String groupID;
    private String name;
    private String prefix;
    private String suffix;

    public GroupPermiFlow(String groupID, String name, String prefix, String suffix){
        this.groupID = groupID;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
    }

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
}
