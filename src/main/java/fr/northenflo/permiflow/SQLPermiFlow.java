package fr.northenflo.permiflow;

import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SQLPermiFlow {

    private static ArrayList<String> listCreatesTables = new ArrayList<String>();

    public static void createsTables(){
        listCreatesTables.add("CREATE TABLE `permiflow_dev`.`groups` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `group_uuid` VARCHAR(255) NULL,\n" +
                "  `group_name` VARCHAR(255) NULL,\n" +
                "  `group_prefix` VARCHAR(255) NULL,\n" +
                "  `group_suffix` VARCHAR(255) NULL,\n" +
                "  PRIMARY KEY (`id`))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;");

        listCreatesTables.add("CREATE TABLE `permiflow_dev`.`users` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `user_uuid` VARCHAR(255) NULL,\n" +
                "  `group_uuid` VARCHAR(255) NULL,\n" +
                "  `user_prefix` VARCHAR(255) NULL,\n" +
                "  `user_suffix` VARCHAR(255) NULL,\n" +
                "  PRIMARY KEY (`id`))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8mb4;");

        for(String ctsql : listCreatesTables){
            createSQL(ctsql);
        }
    }

    public static void createSQL(String sqlStr){
        PreparedStatement stmt = null;
        try {
            stmt = Main.getInstance().getConnection().prepareStatement(sqlStr);
            stmt.executeUpdate();
        } catch (MySQLSyntaxErrorException e) {
            Main.getInstance().log("ERROR", e.getMessage());
        } catch (SQLException e) {
            Main.getInstance().log("ERROR", e.getMessage());
            return;
        }
    }

    public static void insertSQL(Main main, String sqlStr){
        PreparedStatement stmt = null;
        try {
            stmt = Main.getInstance().getConnection().prepareStatement(sqlStr);
            stmt.executeUpdate();
        } catch (MySQLSyntaxErrorException e) {
            Main.getInstance().log("ERROR", e.getMessage());
        } catch (SQLException e) {
            Main.getInstance().log("ERROR", e.getMessage());
            return;
        }
    }

    public static void inserPlayerSQL(Player p){
        PreparedStatement stmt = null;
        String sqlPlayerNew = "INSERT INTO `permiflow_dev`.`users`\n" +
                "(`user_uuid`,\n" +
                "`group_uuid`,\n" +
                "`user_prefix`,\n" +
                "`user_suffix`)\n" +
                "VALUES\n" +
                "(\""+p.getUniqueId()+"\",\n" +
                "\"000-000-000-0000\",\n" +
                "\"\",\n" +
                "\"\");";
        try {
            stmt = Main.getInstance().getConnection().prepareStatement(sqlPlayerNew);
            stmt.executeUpdate();
            Main.getInstance().log("INFOS", "Insert new Player ("+p.getPlayer().getName()+") in database");
        } catch (MySQLSyntaxErrorException e) {
            Main.getInstance().log("ERROR", e.getMessage());
        } catch (SQLException e) {
            Main.getInstance().log("ERROR", e.getMessage());
            return;
        }
    }

    public static boolean checkPlayerExistSQL(Player p){
        String sql = "SELECT * FROM users WHERE user_uuid='"+p.getUniqueId()+"'";
        PreparedStatement stmt = null;
        try {
            stmt = Main.getInstance().getConnection().prepareStatement(sql);
            ResultSet results = null;
            results = stmt.executeQuery();
            if (results.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            return false;
        }
        return false;
    }

}
