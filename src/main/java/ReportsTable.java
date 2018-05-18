import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReportsTable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportsTable.class);



static ConfigTable init(String fileName){

    ConfigFromFile cff = new ConfigFromFile();
    ConfigTable configTable = new ConfigTable();

    JSONObject jObject = new JSONObject(cff.getConfig(fileName));
    JSONArray jArray = jObject.getJSONArray("Reports");

    LOGGER.info("Wczytuje konfiguracje " + jArray.length() + " raportow.");

    for (int i = 0; i < jArray.length(); i++) {

        LOGGER.debug("Wczytuje konfiguracje " + (i + 1) + " raportu.");

        Config temp = new Config();


        temp.setConnectionName(jArray.getJSONObject(i).getString("connectionName"));
        temp.setDbServer(jArray.getJSONObject(i).getString("dbServer"));
        temp.setDbUser(jArray.getJSONObject(i).getString("dbUser"));
        temp.setDbPassword(jArray.getJSONObject(i).getString("dbPassword"));
        temp.setDbView(jArray.getJSONObject(i).getString("dbView"));

       temp.setTransmissionType(jArray.getJSONObject(i).getString("transmissionType"));
       temp.setFileType(jArray.getJSONObject(i).getString("fileType"));

        temp.setFileName(jArray.getJSONObject(i).getString("fileName"));
        temp.setFileHeadName(jArray.getJSONObject(i).getString("fileHeadName"));
        temp.setRemoteServer(jArray.getJSONObject(i).getString("remoteServer"));
        temp.setRemoteUser(jArray.getJSONObject(i).getString("remoteUser"));
        temp.setRemotePassword(jArray.getJSONObject(i).getString("remotePassword"));
        temp.setRemoteAddress(jArray.getJSONObject(i).getString("remoteAddress"));
        temp.setPeriodOfReport(jArray.getJSONObject(i).getString("periodOfReport"));


        configTable.configList.add(temp);

    }

    return configTable;
}


}
