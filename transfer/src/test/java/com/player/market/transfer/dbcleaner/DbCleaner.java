package com.player.market.transfer.dbcleaner;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class DbCleaner {
    public static void clearDatabase() throws SQLException {
        JdbcDataSource ds = new JdbcDataSource();
        ds.setURL("jdbc:h2:mem:testdb"); ds.setUser("sa"); ds.setPassword("password");
        Connection conn = ds.getConnection();
        Statement s = conn.createStatement();
        // Disable FK
        s.execute("SET REFERENTIAL_INTEGRITY FALSE");
        // Find all tables and truncate them
        Set<String> tables = new HashSet<String>();
        ResultSet rs = s.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'");
        while (rs.next()) {
            tables.add(rs.getString(1));
        }
        rs.close();
        for (String table : tables) {
            s.executeUpdate("TRUNCATE TABLE " + table);
        }
    }
}
