public class Main {

    public static void main(String[] args) {


        ConfigTable configTable = new ConfigTable();

       configTable = ReportsTable.init("ConfigReports.json");

        System.out.println(configTable.configList.get(1).getConnectionName() );



    }

}
