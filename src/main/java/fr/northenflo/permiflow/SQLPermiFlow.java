package fr.northenflo.permiflow;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import fr.northenflo.permiflow.variables.PermissionPermiFlow;
import fr.northenflo.permiflow.variables.PlayerPermiFlow;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SQLPermiFlow {

    private final static ArrayList<String> listCollections = new ArrayList<String>();

    public SQLPermiFlow(){
        listCollections.add("players");
        listCollections.add("groupes");
        listCollections.add("permissions");
    }

    public static void createsTables(){
        for(String collection : listCollections) {
            if(!Main.getInstance().getDatabase().collectionExists(collection)) {
                Main.getInstance().getDatabase().createCollection(collection, new BasicDBObject());
                Main.getInstance().log("INFOS", "Collection "+collection+" created successfully");
            }
        }
    }

    public static boolean checkPermAlreadyGive(String node, String groupeID) {
        DBObject r = new BasicDBObject("groupe_uuid", groupeID);
        r.put("node", node);
        DBObject found = Main.getInstance().getPermissionsCollection().findOne(r);
        return found != null;
    }

    public static boolean checkGroupExist(String key, String value) {
        DBObject r = new BasicDBObject(key, value);
        DBObject found = Main.getInstance().getGroupesCollection().findOne(r);
        return found != null;
    }

    public static void storeGroup(String groupID, String name, String prefix, String suffix) {
        DBObject obj = new BasicDBObject("uuid", groupID);
        obj.put("name", name);
        obj.put("prefix", prefix);
        obj.put("suffix", suffix);
        Main.getInstance().getGroupesCollection().insert(obj);
        Main.getInstance().getArrListGroup().add(new GroupPermiFlow(groupID, name, prefix, suffix));
    }

    public static void storePerms(UUID uuid, String node, String idGroup) {
        DBObject obj = new BasicDBObject("uuid", String.valueOf(uuid));
        obj.put("node", node);
        obj.put("groupe_uuid", idGroup);
        Main.getInstance().getPermissionsCollection().insert(obj);
        GroupPermiFlow.getGroupList(idGroup).getListPerms().add(new PermissionPermiFlow(String.valueOf(uuid), node));
    }

    public static void deletePerms(String nodePerm, String groupID) {
        DBObject r = new BasicDBObject("groupe_uuid", groupID);
        r.put("node", nodePerm);
        Main.getInstance().getPermissionsCollection().remove(r);
    }

    public DBObject getGroupe(String key, String value){
        DBObject r = new BasicDBObject(key, value);
        DBObject found = Main.getInstance().getGroupesCollection().findOne(r);
        return found;
    }

    public static List<DBObject> getPermissionsAll() { return Main.getInstance().getPermissionsCollection().find().toArray(); }

    public static List<DBObject> getGroupesAll(){
         return Main.getInstance().getGroupesCollection().find().toArray();
    }

    public static List<DBObject> getPlayersAll() {
        return Main.getInstance().getPlayersCollection().find().toArray();
    }

    public static boolean checkPlayerExist(Player player) {
        DBObject r = new BasicDBObject("uuid", String.valueOf(player.getUniqueId()));
        DBObject found = Main.getInstance().getPlayersCollection().findOne(r);
        return found != null;
    }

    public static void storePlayer(Player player) {
        DBObject obj = new BasicDBObject("uuid", String.valueOf(player.getUniqueId()));
        obj.put("groupe_uuid", Main.getInstance().getSqlPermiFlow().getGroupe("name", "default").get("uuid"));
        obj.put("prefix", "");
        obj.put("suffix", "");
        Main.getInstance().getPlayersCollection().insert(obj);
        Main.getInstance().getArrListPlayer().add(new PlayerPermiFlow(String.valueOf(player.getUniqueId()), GroupPermiFlow.getGroupList(String.valueOf(Main.getInstance().getSqlPermiFlow().getGroupe("name", "default").get("uuid"))), player.addAttachment(Main.getInstance()), "", ""));
    }

    public static void updateGroup(GroupPermiFlow group, String name, String prefix, String suffix) {
        DBObject r = new BasicDBObject("uuid", String.valueOf(group.getGroupID()));
        DBObject found = Main.getInstance().getGroupesCollection().findOne(r);
        if (found == null)
            return;
        DBObject obj = new BasicDBObject("uuid", String.valueOf(group.getGroupID()));
        obj.put("name", name);
        obj.put("prefix", prefix);
        obj.put("suffix", suffix);
        Main.getInstance().getGroupesCollection().update(found, obj);
        GroupPermiFlow.removeGroupList(UUID.fromString(group.getGroupID()));
        Main.getInstance().getArrListGroup().add(new GroupPermiFlow(String.valueOf(group.getGroupID()), name, prefix, suffix));
        PlayerPermiFlow.updateAllPlayerGroupSpecific(GroupPermiFlow.getGroupList(group.getGroupID()));
    }

    public static DBObject getPlayer(String key, String value){
        DBObject r = new BasicDBObject(key, value);
        DBObject found = Main.getInstance().getPlayersCollection().findOne(r);
        return found;
    }

    public static void updatePlayer(Main instancePL, Player player, UUID groupID, String prefix, String suffix) {
        DBObject r = new BasicDBObject("uuid", String.valueOf(player.getUniqueId()));
        DBObject found = Main.getInstance().getPlayersCollection().findOne(r);
        if (found == null)
            return;
        DBObject obj = new BasicDBObject("uuid", String.valueOf(player.getUniqueId()));
        obj.put("groupe_uuid", String.valueOf(groupID));
        obj.put("prefix", prefix);
        obj.put("suffix", suffix);
        Main.getInstance().getPlayersCollection().update(found, obj);
        PlayerPermiFlow.removePlayerList(player.getUniqueId());
        Main.getInstance().getArrListPlayer().add(new PlayerPermiFlow(String.valueOf(player.getUniqueId()), GroupPermiFlow.getGroupList(String.valueOf(groupID)), player.addAttachment(instancePL), prefix, suffix));
    }

    public static DBObject getGroupeForPlayer(String key, String value){
        DBObject r = new BasicDBObject(key, value);
        DBObject foundPlayer = Main.getInstance().getPlayersCollection().findOne(r);
        DBObject foundGroup = Main.getInstance().getSqlPermiFlow().getGroupe("uuid", (String) foundPlayer.get("groupe_uuid"));
        return foundGroup;
    }
}
