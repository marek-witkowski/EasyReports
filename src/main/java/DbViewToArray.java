import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;


public class DbViewToArray {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbViewToArray.class);

    private String dbServer;
    private String dbUser;
    private String dbPassword;
    private String dbView;

    private String[][] tableView;


    public DbViewToArray(String dbServer, String dbUser, String dbPassword, String dbView) {
        this.dbServer = dbServer;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        this.dbView = dbView;
    }


    String[][] getTableView() {

        LOGGER.info("Rozpoczynam przetwarzanie widoku: " + dbView);

        try {


            Class.forName("oracle.jdbc.driver.OracleDriver");

            Connection con = DriverManager.getConnection(dbServer, dbUser, dbPassword);
            Statement stmt = con.createStatement();

            ResultSet rsCount = stmt.executeQuery("Select count(*) from " + dbView);
            rsCount.next();

            int rows = rsCount.getInt(1);

            LOGGER.debug("Ilosc z bazy " + rsCount.getString(1));

            ResultSet rs = stmt.executeQuery("Select * from " + dbView);
            ResultSetMetaData rsmd = rs.getMetaData();

            int cols = rsmd.getColumnCount();
            int rowsCount = 0;
            tableView = new String[rows][cols];

            while (rs.next()) {

                for (int i = 0; i < cols; i++) {

                    tableView[rowsCount][i] = rs.getString(i + 1);

                }

                rowsCount++;

            }

            LOGGER.debug("Wczytano wszystkie wiersze widoku " + dbView + " do pamięci");

        } catch (ClassNotFoundException | SQLException e) {

            LOGGER.error("Błąd: ", e);


        }

        return tableView;
    }
}
