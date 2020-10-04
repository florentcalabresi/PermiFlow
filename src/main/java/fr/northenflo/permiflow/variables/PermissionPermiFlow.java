package fr.northenflo.permiflow.variables;

public class PermissionPermiFlow {


    private final String permissionID;
    private final String nodePerm;

    public PermissionPermiFlow(String permissionID, String nodePerm){
        this.permissionID = permissionID;
        this.nodePerm = nodePerm;
    }

    public String getPermissionID(){
        return this.permissionID;
    }

    public String getNodePerm() {
        return this.nodePerm;
    }

}
