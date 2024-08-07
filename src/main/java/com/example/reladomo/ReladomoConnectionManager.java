package com.example.reladomo;

import com.gs.fw.common.mithra.bulkloader.BulkLoader;
import com.gs.fw.common.mithra.bulkloader.BulkLoaderException;
import com.gs.fw.common.mithra.connectionmanager.SourcelessConnectionManager;
import com.gs.fw.common.mithra.connectionmanager.XAConnectionManager;
import com.gs.fw.common.mithra.databasetype.DatabaseType;
import com.gs.fw.common.mithra.databasetype.H2DatabaseType;
import org.h2.tools.RunScript;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.TimeZone;
import java.util.stream.Stream;

public class ReladomoConnectionManager implements SourcelessConnectionManager {

    private static ReladomoConnectionManager instance;
    private XAConnectionManager xaConnectionManager;

    public static synchronized ReladomoConnectionManager getInstance() {
        if (instance == null) {
            instance = new ReladomoConnectionManager();
        }
        return instance;
    }

    private XAConnectionManager createConnectionManager() {



        xaConnectionManager = new XAConnectionManager();
        xaConnectionManager.setLdapName("localhost");
        xaConnectionManager.setDriverClassName("org.h2.Driver");
        xaConnectionManager.setJdbcConnectionString("jdbc:h2:mem:myDb");
        xaConnectionManager.setJdbcUser("username");
        xaConnectionManager.setJdbcPassword("password");
        xaConnectionManager.setPoolName("connectionpool");
        xaConnectionManager.setInitialSize(1);
        xaConnectionManager.setPoolSize(10);
        xaConnectionManager.initialisePool();
        return xaConnectionManager;
    }

    private ReladomoConnectionManager() {
        this.createConnectionManager();
    }

    @Override
    public BulkLoader createBulkLoader() throws BulkLoaderException {
        return null;
    }

    @Override
    public Connection getConnection() {
        return xaConnectionManager.getConnection();
    }

    @Override
    public DatabaseType getDatabaseType() {
        return H2DatabaseType.getInstance();
    }

    @Override
    public TimeZone getDatabaseTimeZone() {
        return TimeZone.getDefault();
    }

    @Override
    public String getDatabaseIdentifier() {
        return "myDb";
    }

    public void createTables() throws Exception {
        Path ddlPath = Paths.get(ClassLoader.getSystemResource("sql").toURI());
        try (
                Connection conn = xaConnectionManager.getConnection();
                Stream<Path> list = Files.list(ddlPath)) {

            list.forEach(path -> {
                try {
                    RunScript.execute(conn, Files.newBufferedReader(path));
                }
                catch (SQLException | IOException exc){
                    exc.printStackTrace();
                }
            });
        }
    }
}
