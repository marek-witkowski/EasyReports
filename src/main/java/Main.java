import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {


    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.info("--== Start aplikacji ==--");

        ConfigTable configTable;
        String[][] temporaryDataTable;
        String fileName;
        String configFile = "ConfigReports.json";

        configTable = ReportsTable.init(configFile);

        for (int i = 0; i < configTable.configList.size(); i++) {

            LOGGER.info("Przetwarzam raport: " + configTable.configList.get(i).getConnectionName());

            DbViewToArray dbViewToArray = new DbViewToArray(
                    configTable.configList.get(i).getDbServer(),
                    configTable.configList.get(i).getDbUser(),
                    configTable.configList.get(i).getDbPassword(),
                    configTable.configList.get(i).getDbView()
            );

            temporaryDataTable = dbViewToArray.getTableView();

            DataSaver ds = new DataSaver();

            fileName = ds.asFile(temporaryDataTable,
                    configTable.configList.get(i).getFileType(),
                    configTable.configList.get(i).getFileName(),
                    configTable.configList.get(i).getPeriodOfReport()
            );

            if (!fileName.equals("")) {

                LOGGER.debug("Nazwa pliku: " + fileName + " Przekazano plik do wysylki.");

                DataSender dataSender = new DataSender();

                dataSender.save(configTable.configList.get(i).getRemoteServer(),
                        configTable.configList.get(i).getRemoteUser(),
                        configTable.configList.get(i).getRemotePassword(),
                        configTable.configList.get(i).getRemoteAddress(),
                        fileName,
                        configTable.configList.get(i).getTransmissionType(),
                        configTable.configList.get(i).getConnectionName());
            }
        }

        LOGGER.info("--== Koniec pracy ==--");

    }

}
