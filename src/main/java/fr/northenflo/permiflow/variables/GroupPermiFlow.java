package fr.northenflo.permiflow.variables;

import org.bukkit.entity.Player;

import java.util.UUID;

public class GroupPermiFlow {

    private static GroupPermiFlow defaultGroup = new GroupPermiFlow(UUID.fromString("7458AB-01548-32025-AHD120"));


    private UUID groupID;

    public GroupPermiFlow(UUID groupID){
        this.groupID = groupID;
    }

}
