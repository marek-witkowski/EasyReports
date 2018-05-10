import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataSaver {

    String[][] temporaryDataTable;
    String fileType;
    String fileName;


    private static final Logger LOGGER = LoggerFactory.getLogger(DataSaver.class);

        String  asFile(String[][] temporaryDataTable, String fileType, String fileName){
        DateTimeFormatter dtf = DateTimeFormatter.BASIC_ISO_DATE;
        fileName = fileName + dtf.format(LocalDate.now()) + "." + fileType.toLowerCase();
        File temporaryFile = new File(fileName);

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
