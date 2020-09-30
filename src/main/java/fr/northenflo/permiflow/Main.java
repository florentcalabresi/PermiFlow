package fr.northenflo.permiflow;

import com.mongodb.*;
import fr.northenflo.permiflow.events.PlayerEvents;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {

    static Main instance;
    private static DBCollection players;
    private static DBCollection groupes;
    private static DB permiFlowDB;
    private static MongoClient client;
    private static String ip = "localhost";
    private static int port = 27017;
    private static SQLPermiFlow sqlPermiFlow;

    @Override
    public void onEnable(){
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);

        ServerAddress addr = new ServerAddress("localhost", port);
        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
        credentials.add(MongoCredential.createCredential("NorthenFlo", "PermiFlow", "#0801Flo62150".toCharArray()));


        client = new MongoClient(addr);

        permiFlowDB = client.getDB("PermiFlow");

        log("INFOS", "Success connecting MySQL Server !");
        log("INFOS", "Checking tables ...");

        sqlPermiFlow = new SQLPermiFlow();

        SQLPermiFlow.createsTables();

        setGroupesCollection(getDatabase().getCollection("groupes"));
        setPlayersCollection(getDatabase().getCollection("players"));

        if(!SQLPermiFlow.checkGroupExist("name", "default"))
            SQLPermiFlow.storeGroup(GroupPermiFlow.defaultGroup.getGroupID(), GroupPermiFlow.defaultGroup.getName(), GroupPermiFlow.defaultGroup.getPrefix(), GroupPermiFlow.defaultGroup.getSuffix());

        Main.getInstance().log("INFOS", "Check Player in database...");
        for(Player player : this.getServer().getOnlinePlayers())
            if (!SQLPermiFlow.checkPlayerExist(player.getPlayer()))
                SQLPermiFlow.storePlayer(player.getPlayer());
    }

    private void setGroupesCollection(DBCollection newCollect) {
        groupes = newCollect;
    }
    private void setPlayersCollection(DBCollection newCollect) {
        players = newCollect;
    }

    public static Main getInstance(){
        return instance;
    }

    public MongoClient getClient(){
        return client;
    }

    public DB getDatabase(){
        return permiFlowDB;
    }

    public DBCollection getPlayersCollection(){
        return players;
    }

    public DBCollection getGroupesCollection(){
        return groupes;
    }

    @Override
    public void onDisable(){

    }

    public void log(String LEVEL, String MSG){
        System.out.println("[PERMIFLOW]["+LEVEL+"] "+MSG);
    }

}


//VIAUZ EST UN ENORME BG
//IL GERE EN GRAPHISME.