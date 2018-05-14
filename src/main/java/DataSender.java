import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.io.Util;
import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.apache.commons.net.smtp.SimpleSMTPHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSender.class);

    void save(String remoteServer, String remoteUser, String remotePassword, String remoteAddress, String fileName, String transmissionType, String connectionName) {

        LOGGER.info("Przygotowanie do wysłania pliku.");

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

        LOGGER.debug("Scieżka zródłowa:" + fileName);
        LOGGER.debug("Scieżka docelowa:" + destinationFile);

        try {
            Files.move(sourceFile, destinationFile);
            LOGGER.info("Plik zapisano: " + sourceFile.toString());
        } catch (IOException e) {
            LOGGER.error("Błąd przy przegrywaniu pliku " + sourceFile.toString(), e);
        }


    }

    private void sendFileViaFtp(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress) {

        FTPClient client = new FTPClient();

        try {

            FileInputStream is = new FileInputStream(new File(fileName));

            LOGGER.debug("Lączenie z serwerem FTP: " + remoteServer);
            client.connect(remoteServer);
            client.login(remoteUser, remotePassword);
            client.storeFile(fileName, is);
            client.logout();

            LOGGER.info("Pomyślnie przesłano plik przez FTP: " + fileName);
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


    private void sendFileViaMail(String fileName, String remoteServer, String remoteUser, String remotePassword, String remoteAddress, String connectionName) {

        FileReader fileReader;
        Writer writer;
        SimpleSMTPHeader header;
        SMTPClient client;
        String sender = "raporty@intra.eu";


        try {

            header = new SimpleSMTPHeader(sender, remoteAddress, connectionName);


            fileReader = new FileReader(fileName);


            client = new SMTPClient();

            client.addProtocolCommandListener(new PrintCommandListener(
                    new PrintWriter(System.out), true));

            client.connect(remoteServer);

            if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.disconnect();
                LOGGER.error("Serwer SMTP odrzucił połączenie");
                System.exit(1);
            }

            client.login();

            client.setSender(sender);
            client.addRecipient(remoteAddress);


            writer = client.sendMessageData();

            if (writer != null) {
                writer.write(header.toString());
                Util.copyReader(fileReader, writer);
                writer.close();
                client.completePendingCommand();
            }
            fileReader.close();
            client.logout();
            client.disconnect();

            LOGGER.info("Plik " + fileName + " wysłano mailem.");

        } catch (IOException e) {

            LOGGER.error("File not found: " + e.getMessage());
            e.printStackTrace();

        }


    }

}

