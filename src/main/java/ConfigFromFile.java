import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

class ConfigFromFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFromFile.class);


    String getConfig(String fileName) {

        File file = new File(fileName);

        try {
            String lines = FileUtils.readFileToString(file, "UTF-8");

            LOGGER.info("Plik z raportami wczytano: " + fileName);

            return lines;

        } catch (IOException e) {

            LOGGER.error("Blad odczytu danych z raportami: " + fileName, e);

            System.exit(1);

        }

        return null;
    }


}
