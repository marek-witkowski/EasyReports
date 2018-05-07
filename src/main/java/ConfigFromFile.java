
import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

class ConfigFromFile {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFromFile.class);


    String getConfig(String fileName) {

        File file = new File(fileName);

        try {
            String lines = FileUtils.readFileToString(file, "UTF-8");

            LOGGER.debug("Plik z danymi wczytano.");

            return lines;

        } catch (IOException e) {

            LOGGER.error("Błąd odczytu danych.", e);

            return "";

        }


    }


}
