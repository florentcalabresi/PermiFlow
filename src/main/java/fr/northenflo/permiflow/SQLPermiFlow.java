package fr.northenflo.permiflow;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class SQLPermiFlow {

    private final static ArrayList<String> listCollections = new ArrayList<String>();

    public SQLPermiFlow(){
        listCollections.add("players");
        listCollections.add("groupes");
    }

    public static void createsTables(){
        for(String collection : listCollections) {
            if(!Main.getInstance().getDatabase().collectionExists(collection)) {
                Main.getInstance().getDatabase().createCollection(collection, new BasicDBObject());
                Main.getInstance().log("INFOS", "Collection "+collection+" created successfully");
            }
        }

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
    }

    public static DBObject getGroupe(String key, String value){
        DBObject r = new BasicDBObject(key, value);
        DBObject found = Main.getInstance().getGroupesCollection().findOne(r);
        return found;
    }

    public static boolean checkPlayerExist(Player player) {
        DBObject r = new BasicDBObject("uuid", player.getUniqueId());
        DBObject found = Main.getInstance().getPlayersCollection().findOne(r);
        return found != null;
    }

    public static void storePlayer(Player player) {
        DBObject obj = new BasicDBObject("uuid", String.valueOf(player.getUniqueId()));
        obj.put("groupe_uuid", getGroupe("name", "default").get("uuid"));
        obj.put("prefix", "");
        obj.put("suffix", "");
        Main.getInstance().getPlayersCollection().insert(obj);
    }

    public static DBObject getPlayer(String key, String value){
        DBObject r = new BasicDBObject(key, value);
        DBObject found = Main.getInstance().getGroupesCollection().findOne(r);
        return found;
    }

    public static DBObject getGroupeForPlayer(String key, String value){
        DBObject r = new BasicDBObject(key, value);
        DBObject foundPlayer = Main.getInstance().getPlayersCollection().findOne(r);
        DBObject foundGroup = getGroupe("uuid", (String) foundPlayer.get("groupe_uuid"));
        return foundGroup;
    }
}
