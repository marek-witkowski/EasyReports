import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.QuoteMode;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
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

            case "WEEKLY":
                fileDate = dtf.format(LocalDate.now().minusDays(7)) + "-" + dtf.format(LocalDate.now().minusDays(1));
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

        LOGGER.debug("Utworzono nazwe pliku:" + fileName);

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
                    LOGGER.error("Blad w zapisie: " + fileName, e);
                }
                break;


            case "XLS":

                int i = 1;

                Workbook wb = new HSSFWorkbook();
                Sheet sheet = wb.createSheet();

                for (String[] aTemporaryDataTable : temporaryDataTable) {

                    Row row = sheet.createRow(i);

                    for (int j = 0; j < aTemporaryDataTable.length; j++) {

                        Cell cell = row.createCell(j);
                        cell.setCellValue(aTemporaryDataTable[j]);

                    }

                    i++;
                }

                try (FileOutputStream out = new FileOutputStream(fileName)) {
                    wb.write(out);
                    LOGGER.info("Zapisano plik: " + fileName);
                    return fileName;

                } catch (IOException e) {
                    LOGGER.error("Blad w zapisie: " + fileName, e);
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
