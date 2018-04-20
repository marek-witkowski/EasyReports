
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

class loadConfig {

    private String serverAddress;
    private String userName;
    private String userPassword;

    private static final Logger LOGGER = LoggerFactory.getLogger(loadConfig.class);


    public loadConfig(String fileName) {

        try {
            FileReader fr = new FileReader(new File(fileName));
            BufferedReader bf = new BufferedReader(fr);

            serverAddress = bf.readLine();
            userName = bf.readLine();
            userPassword = bf.readLine();

            bf.close();
            fr.close();

        } catch (IOException e) {
            LOGGER.error("Load configuration error!", e);
        }
    }


    public String getServerAddress() {
        return serverAddress;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
