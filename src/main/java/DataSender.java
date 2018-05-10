import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSender.class);

    void save(String remoteServer, String remoteUser, String remotePassword, String remoteAddress, String fileName, String transmissionType) {

        LOGGER.info("Przygotowanie do wysłania pliku.");

        switch (transmissionType) {

            case "FILE":

                saveFileToRemoteServer(fileName, remoteServer, remoteUser, remotePassword, remoteAddress);
                LOGGER.info("Plik " + fileName + " zapisano.");
                break;

            case "FTP":


                sendFileViaFtp(fileName, remoteServer, remoteUser, remotePassword, remoteAddress);
                LOGGER.info("Plik " + fileName + " wysłano przez FTP.");
                break;

            case "MAIL":


                LOGGER.info("Plik " + fileName + " wysłano mailem.");
                break;
        }
    }


    private void saveFileToRemoteServer(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress) {

        Path sourceFile = new File(fileName).toPath();
        Path destinationFile = new File(remoteServer + "\\" + remoteAddress + "\\" + fileName).toPath();

        LOGGER.debug("Scieżka zródłowa:" + fileName);
        LOGGER.debug("Scieżka docelowa:" + destinationFile);

        try {
            Files.move(sourceFile, destinationFile);
            LOGGER.debug("Plik zapisano: " + sourceFile.toString());
        } catch (IOException e) {
            LOGGER.error("Błąd przy przegrywaniu pliku " + sourceFile.toString(), e);
        }


    }


    private void sendFileViaFtp(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress) {

        FTPClient client = new FTPClient();

        try {

            FileInputStream is = new FileInputStream(new File(fileName));

            LOGGER.info("Lączenie z serwerem FTP: " + remoteServer);
            client.connect(remoteServer);
            client.login(remoteUser, remotePassword);
            client.storeFile(fileName, is);
            client.logout();

            LOGGER.info("Pomyślnie przesłano plik: " + fileName);
        } catch (Exception e) {
            LOGGER.error("Nie udało się przesłać pliku: " + fileName + " na serwer FTP: " + remoteServer, e);
        } finally {
            try {
                client.disconnect();
            } catch (Exception e) {
                LOGGER.error("Nie udało się zakończyć sesji z serwerem FTP: " + remoteServer, e);
            }
        }


    }

    private void sendFileViaMail(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress) {
    }

}

