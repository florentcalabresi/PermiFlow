package fr.northenflo.permiflow;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import fr.northenflo.permiflow.events.PlayerEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main extends JavaPlugin {

    static Main instance;
    final String username = "root";
    final String password = "root";
    final String url = "jdbc:mysql://localhost:3306/permiflow_dev";
    static Connection connection = null;

    @Override
    public void onEnable(){
        instance = this;
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.err.println("jdbc driver unavailable!");
            return;
        }
        try {
            connection = DriverManager.getConnection(url,username,password);
            log("INFOS", "Success connecting MySQL Server !");
        } catch (SQLException e) {
            e.printStackTrace();
            log("ERROR", "Error connecting MySQL Server ! \n "+e.getMessage());
            return;
        }

        log("INFOS", "Checking tables ...");
        SQLPermiFlow.createsTables();
    }

    public static Main getInstance(){
        return instance;
    }

    public static Connection getConnection(){
        return connection;
    }

    @Override
    public void onDisable(){
        try {
            if (connection!=null && !connection.isClosed())
                connection.close();
        } catch(Exception e) {
            e.printStackTrace();
            log("ERROR", "Error close connection MySQL Server ! \n "+e.getMessage());
        }
    }

    public void log(String LEVEL, String MSG){
        System.out.println("[PERMIFLOW]["+LEVEL+"] "+MSG);
    }

}
