package fr.northenflo.permiflow;

import com.mongodb.*;
import fr.northenflo.permiflow.commands.ICommandP;
import fr.northenflo.permiflow.commands.PermiFlowCommandManager;
import fr.northenflo.permiflow.events.PlayerEvents;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import fr.northenflo.permiflow.variables.PermissionPermiFlow;
import fr.northenflo.permiflow.variables.PlayerPermiFlow;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class Main extends JavaPlugin {

    static Main instance;

    private static DBCollection players;
    private static DBCollection groupes;
    private static DBCollection permissions;

    private static DB permiFlowDB;
    private static MongoClient client;
    private static String ip = "localhost";
    private static int port = 27017;
    private static SQLPermiFlow sqlPermiFlow;

    private static ArrayList<ICommandP> listCommands = new ArrayList<ICommandP>();
    private static ArrayList<GroupPermiFlow> listGroupes = new ArrayList<GroupPermiFlow>();
    private static ArrayList<PlayerPermiFlow> listPlayers = new ArrayList<PlayerPermiFlow>();
    private static PermiFlowCommandManager commandPermiFlow;

    @Override
    public void onEnable() {
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        ServerAddress addr = new ServerAddress(ip, port);
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(MongoCredential.createCredential("NorthenFlo", "PermiFlow", "password".toCharArray()));


        client = new MongoClient(addr, credentials);

        permiFlowDB = client.getDB("PermiFlow");

        log("INFOS", "Success connecting MySQL Server !");
        log("INFOS", "Checking tables ...");

        sqlPermiFlow = new SQLPermiFlow();
        commandPermiFlow = new PermiFlowCommandManager(this);

        SQLPermiFlow.createsTables();

        setGroupesCollection(getDatabase().getCollection("groupes"));
        setPlayersCollection(getDatabase().getCollection("players"));
        setPermissionsCollection(getDatabase().getCollection("permissions"));

        if (!SQLPermiFlow.checkGroupExist("name", "default"))
            SQLPermiFlow.storeGroup(GroupPermiFlow.defaultGroup.getGroupID(), GroupPermiFlow.defaultGroup.getName(), GroupPermiFlow.defaultGroup.getPrefix(), GroupPermiFlow.defaultGroup.getSuffix());

        this.getArrListPlayer().clear();
        this.getArrListGroup().clear();

        this.log("DEBUG", "Result Perm Check Test "+SQLPermiFlow.checkPermAlreadyGive("permiflow.reload", "7c30da5a-5dc0-493e-92e7-3199673d37bb"));

        Main.getInstance().log("INFOS", "Retrieve Groupes in database...");
        List<DBObject> collectGroupes = SQLPermiFlow.getGroupesAll();
        for (DBObject groupe : collectGroupes)
            Main.getInstance().getArrListGroup().add(new GroupPermiFlow((String) groupe.get("uuid"), (String) groupe.get("name"), (String) groupe.get("prefix"), (String) groupe.get("suffix")));

        Main.getInstance().log("INFOS", "Retrieve Permissions in database...");
        List<DBObject> collectPermissions = SQLPermiFlow.getPermissionsAll();
        for (DBObject perms : collectPermissions)
            for (GroupPermiFlow group : getArrListGroup())
                if(group.getGroupID().equalsIgnoreCase((String) perms.get("groupe_uuid")))
                    group.getListPerms().add(new PermissionPermiFlow((String) perms.get("uuid"), (String) perms.get("node")));

        Main.getInstance().log("INFOS", "Check Player in database...");
        for (Player player : this.getServer().getOnlinePlayers()) {
            if (!SQLPermiFlow.checkPlayerExist(player.getPlayer()))
                SQLPermiFlow.storePlayer(player.getPlayer());
            List<DBObject> collectPlayers = Main.getInstance().getSqlPermiFlow().getPlayersAll();
            for (DBObject playerObj : collectPlayers)
                Main.getInstance().getArrListPlayer().add(new PlayerPermiFlow((String) playerObj.get("uuid"), GroupPermiFlow.getGroupList((String) playerObj.get("groupe_uuid")), player.addAttachment(this), (String) playerObj.get("prefix"), (String) playerObj.get("suffix")));

            removeAllPerms(player);

            for (PlayerPermiFlow PFPlayer : getArrListPlayer()) {
                for (PermissionPermiFlow perms : PFPlayer.getGroupe().getListPerms())
                    PFPlayer.getAttachmentPerm().setPermission(perms.getNodePerm(), true);
            }
        }
    }

    public void removeAllPerms(OfflinePlayer player) {
        if (player.isOnline()) {
            Set<PermissionAttachmentInfo> permissions = new HashSet<PermissionAttachmentInfo>(player.getPlayer().getEffectivePermissions());
            for (PermissionAttachmentInfo permission : permissions)
                if (permission.getAttachment() != null)
                    if (player.isOnline())
                        player.getPlayer().removeAttachment(permission.getAttachment());
        }
    }

    public void reloadConfig(){
        this.getArrListPlayer().clear();
        this.getArrListGroup().clear();

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
        for (Player player : this.getServer().getOnlinePlayers()){
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
    }

    private void setPermissionsCollection(DBCollection newCollect) {
        permissions = newCollect;
    }

    public void setGroupesCollection(DBCollection newCollect) {
        groupes = newCollect;
    }
    public void setPlayersCollection(DBCollection newCollect) {
        players = newCollect;
    }

    public SQLPermiFlow getSqlPermiFlow() { return sqlPermiFlow; }

    public static Main getInstance(){
        return instance;
    }

    public MongoClient getClient(){
        return client;
    }

    public DB getDatabase(){
        return permiFlowDB;
    }

    public DBCollection getPermissionsCollection(){
        return permissions;
    }

    public DBCollection getPlayersCollection(){
        return players;
    }

    public DBCollection getGroupesCollection(){
        return groupes;
    }

    public ArrayList<GroupPermiFlow> getArrListGroup() { return listGroupes; }

    @Override
    public void onDisable(){

    }

    public void log(String LEVEL, String MSG){
        System.out.println("[PERMIFLOW]["+LEVEL+"] "+MSG);
    }

    public ArrayList<PlayerPermiFlow> getArrListPlayer() {
        return listPlayers;
    }

    public void sendMessagePlayer(CommandSender sender, String s) {
        sender.sendMessage(s);
    }

    public ArrayList<ICommandP> getListCommands() { return listCommands; }
}


//VIAUZ EST UN ENORME BG
//PINKIE IS MY HEART
//ZEN IS MY BITCH
//JUMPY OU JP POUR LES INTIMES
//HALGO YT IS A CHANNEL YOUTUBE GAMING