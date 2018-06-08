import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class DataSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSender.class);

    void save(String remoteServer, String remoteUser, String remotePassword, String remoteAddress, String fileName, String transmissionType, String connectionName) {

        LOGGER.info("Przygotowanie do wyslania pliku.");

        switch (transmissionType) {

            case "FILE":

                saveFileToRemoteServer(fileName, remoteServer, remoteUser, remotePassword, remoteAddress);
                break;

            case "FTP":

                sendFileViaFtp(fileName, remoteServer, remoteUser, remotePassword, remoteAddress);
                break;

            case "MAIL":

                sendFileViaMail(fileName, remoteServer, remoteUser, remotePassword, remoteAddress, connectionName);
                break;
        }

    }


    private void saveFileToRemoteServer(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress) {

        Path sourceFile = new File(fileName).toPath();
        Path destinationFile = new File(remoteServer + "\\" + remoteAddress + "\\" + fileName).toPath();

        LOGGER.debug("Sciezka zrodlowa:" + fileName);
        LOGGER.debug("Sciezka docelowa:" + destinationFile);

        try {
            Files.move(sourceFile, destinationFile);
            LOGGER.info("Plik zapisano: " + sourceFile.toString());
        } catch (IOException e) {
            LOGGER.error("Blad przy przegrywaniu pliku " + sourceFile.toString(), e);
        }


    }

    private void sendFileViaFtp(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress) {

        FTPClient client = new FTPClient();

        try {

            FileInputStream is = new FileInputStream(new File(fileName));

            LOGGER.debug("Laczenie z serwerem FTP: " + remoteServer);
            client.connect(remoteServer);
            client.login(remoteUser, remotePassword);
            client.storeFile(fileName, is);
            client.logout();

            LOGGER.info("Pomyslnie przeslano plik przez FTP: " + fileName);
        } catch (Exception e) {
            LOGGER.error("Nie udalo sie przeslac pliku: " + fileName + " na serwer FTP: " + remoteServer, e);
        } finally {
            try {
                client.disconnect();
            } catch (Exception e) {
                LOGGER.error("Nie udalo sie zakonczyc sesji z serwerem FTP: " + remoteServer, e);
            }
        }


    }


    private void sendFileViaMail(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress, String connectionName) {


        String sender = "raporty@intra.eu";


        try {


            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(fileName);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);


            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(remoteServer);
            email.addTo(remoteAddress);
            email.setFrom(sender);
            email.setSubject("Raport z dnia " + LocalDate.now());
            email.setMsg("Raport z dnia " + LocalDate.now() + " wysyłany automatycznie. Nie odpowiadaj na niego.");
            email.attach(attachment);

            email.send();


            LOGGER.info("Plik " + fileName + " wyslano mailem.");

        } catch (Exception e) {

            LOGGER.error("Blad przy wysylaniu raportu przez e-mail: " + e.getMessage());
            e.printStackTrace();

        }


    }

}

