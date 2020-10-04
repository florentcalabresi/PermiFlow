package fr.northenflo.permiflow;

import com.mongodb.*;
import fr.northenflo.permiflow.commands.ICommandP;
import fr.northenflo.permiflow.commands.PermiFlowCommandManager;
import fr.northenflo.permiflow.events.PlayerEvents;
import fr.northenflo.permiflow.variables.GroupPermiFlow;
import fr.northenflo.permiflow.variables.PlayerPermiFlow;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class Main extends JavaPlugin {

    static Main instance;
    private static DBCollection players;
    private static DBCollection groupes;
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

        if (!SQLPermiFlow.checkGroupExist("name", "default"))
            SQLPermiFlow.storeGroup(GroupPermiFlow.defaultGroup.getGroupID(), GroupPermiFlow.defaultGroup.getName(), GroupPermiFlow.defaultGroup.getPrefix(), GroupPermiFlow.defaultGroup.getSuffix());

        this.getArrListPlayer().clear();
        this.getArrListGroup().clear();

        Main.getInstance().log("INFOS", "Retrieve Groupes in database...");
        List<DBObject> collectGroupes = SQLPermiFlow.getGroupesAll();
        for(DBObject groupe : collectGroupes)
            Main.getInstance().getArrListGroup().add(new GroupPermiFlow((String) groupe.get("uuid"), (String) groupe.get("name"), (String) groupe.get("prefix"), (String) groupe.get("suffix")));

        Main.getInstance().log("INFOS", "Check Player in database...");
        for (Player player : this.getServer().getOnlinePlayers()){
            if (!SQLPermiFlow.checkPlayerExist(player.getPlayer()))
                SQLPermiFlow.storePlayer(player.getPlayer());
            List<DBObject> collectPlayers = Main.getInstance().getSqlPermiFlow().getPlayersAll();
            for(DBObject playerObj : collectPlayers)
                Main.getInstance().getArrListPlayer().add(new PlayerPermiFlow((String) playerObj.get("uuid"), GroupPermiFlow.getGroupList((String) playerObj.get("groupe_uuid")), (String) playerObj.get("prefix"), (String) playerObj.get("suffix")));
        }
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