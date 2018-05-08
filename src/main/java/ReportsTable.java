import org.json.JSONArray;
import org.json.JSONObject;

public class ReportsTable {

public static ConfigTable init(String fileName){

    ConfigFromFile cff = new ConfigFromFile();
    ConfigTable configTable = new ConfigTable();

    JSONObject jObject = new JSONObject(cff.getConfig("ConfigReports.json"));
    JSONArray jArray = jObject.getJSONArray("Reports");

    for (int i = 0; i < jArray.length(); i++) {

        Config temp = new Config();

        temp.setConnectionName(jArray.getJSONObject(i).getString("connectionName"));
        temp.setDbServer(jArray.getJSONObject(i).getString("dbServer"));
        temp.setDbUser(jArray.getJSONObject(i).getString("dbUser"));
        temp.setDbPassword(jArray.getJSONObject(i).getString("dbPassword"));
        temp.setDbView(jArray.getJSONObject(i).getString("dbView"));

//       temp.setTransmissionType(jArray.getJSONObject(i).getEnum(TransmissionTypes,"TransmissionType"));

        temp.setFileName(jArray.getJSONObject(i).getString("fileName"));
        temp.setFileHeadName(jArray.getJSONObject(i).getString("fileHeadName"));
        temp.setRemoteServer(jArray.getJSONObject(i).getString("remoteServer"));
        temp.setRemoteUser(jArray.getJSONObject(i).getString("remoteUser"));
        temp.setRemotePassword(jArray.getJSONObject(i).getString("remotePassword"));
        temp.setRemoteAddress(jArray.getJSONObject(i).getString("remoteAddress"));


        configTable.configList.add(temp);

    }

    return configTable;
}


}
