import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.info("--== Start aplikacji ==--");

        ConfigTable configTable;

       configTable = ReportsTable.init("ConfigReports.json");

        for (int i = 0; i < configTable.configList.size(); i++) {

            LOGGER.info("Przetwarzam raport: " + configTable.configList.get(i).getConnectionName());

            DbViewToArray dbViewToArray = new DbViewToArray(
                    configTable.configList.get(i).getDbServer(),
                    configTable.configList.get(i).getDbUser(),
                    configTable.configList.get(i).getDbPassword(),
                    configTable.configList.get(i).getDbView()
                    );

            dbViewToArray.getTableView();








        }




        LOGGER.info("--== Koniec pracy ==--");

    }

}
