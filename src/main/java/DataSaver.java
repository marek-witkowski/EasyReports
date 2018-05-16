import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataSaver {


    private static final Logger LOGGER = LoggerFactory.getLogger(DataSaver.class);

    String asFile(String[][] temporaryDataTable, String fileType, String fileName, String period) {
        DateTimeFormatter dtf = DateTimeFormatter.BASIC_ISO_DATE;

        String fileDate = "";


        switch (period) {

            case "DAILY":
                fileDate = dtf.format(LocalDate.now().minusDays(1));

                break;
            case "MONTHLY":
                fileDate = dtf.format(LocalDate.now().minusMonths(1));
                fileDate = fileDate.substring(0, 6);
                break;

            case "ANNUAL":
                fileDate = dtf.format(LocalDate.now().minusYears(1));
                fileDate = fileDate.substring(0, 4);
                break;

            default:
                break;

        }


        fileName = fileName + fileDate + "." + fileType.toLowerCase();

        LOGGER.debug("Utworzono nazwę pliku:" + fileName);

        switch (fileType) {

            case "CSV":

                try {
                    BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName));
                    CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withDelimiter(';').withQuote('"').withQuoteMode(QuoteMode.ALL));

                    for (String[] aTemporaryDataTable : temporaryDataTable) {
                        csvPrinter.printRecord((Object[]) aTemporaryDataTable);
                    }

                    csvPrinter.flush();
                    LOGGER.info("Zapisano plik: " + fileName);
                    return fileName;

                } catch (IOException e) {
                    LOGGER.error("Błąd w zapisie: " + fileName, e);
                }
                break;

            case "JSON":


                break;


            case "XML":


                break;

        }

        return "";

    }
}
