package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Домашка на файлы")
public class HW7FilesTest {

    @Disabled
    @Test
    @DisplayName("Загрузка файла по абсолютному пути (не рекомендуется).")
    void filenameShouldDisplayedAfterUploadActionAbsolutePathTest() {
        open("https://the-internet.herokuapp.com/upload");
        File exampleFile = new File("/home/cbet/IdeaProjects/qa_guru_1/settings.gradle");
        $("input[type='file']").uploadFile(exampleFile);
        $("#file-submit").click();
        $("#uploaded-files").shouldHave(text("settings.gradle"));
    }

    @Disabled
    @Test
    @DisplayName("Загрузка файла по относительному пути.")
    void filenameShouldDisplayedAfterUploadActionRelativePathTest() {
        open("https://the-internet.herokuapp.com/upload");
        File exampleFile = new File("src/test/resources/csv.csv");
        $("input[type='file']").uploadFile(exampleFile);
        $("#file-submit").click();
        $("#uploaded-files").shouldHave(text("csv.csv"));
    }

    @Disabled
    @Test
    @DisplayName("Скачивание текстового файла и проверка его содержимого")
    void downloadSimpleTextFileTest() throws IOException {
        open("https://filesamples.com/formats/txt");
        File download = $$("[Download=\"\"]").get(2).scrollTo().download();
        String fileContent = IOUtils.toString(new FileReader(download));
        System.out.println(fileContent);
        assertTrue(fileContent.contains("Quamquam id quidem licebit iis existimare"));
    }

    @Disabled
    @Test
    @DisplayName("Скачивание PDF файла")
    void pdfFileDownloadTest() throws IOException {
        open("https://all-the-books.ru/books/pushkin-aleksandr-poltava/");
        File pdf = $(byText("Скачать Полтава в PDF")).scrollTo().download();
        PDF parsedPdf = new PDF(pdf);
        Assertions.assertEquals("Александр Сергеевич Пушкин", parsedPdf.author);
    }

    @Test
    @DisplayName("Скачивание XLS файла")
    void xlsFileDownloadTestCheckCell() throws IOException {
        open("https://www.mybusinesscatalog.com/rus/shablony/spisok-bespatnix-shablonov.html");
        File file = $("a[title='Кликните чтобы загрузить образец прайс-листа в формате Excel']")
                .download();

        XLS parsedXls = new XLS(file);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(14)
                .getCell(2)
                .getStringCellValue()
                .contains("2016 New Fashion Women PU Leather Handbag High Quality");

        assertTrue(checkPassed);
    }

    @Test
    @DisplayName("Скачивание XLS файла")
    void xlsFileDownloadTestCheckSheetName() throws IOException {
        open("https://www.mybusinesscatalog.com/rus/shablony/spisok-bespatnix-shablonov.html");
        File file = $("a[title='Кликните чтобы загрузить образец прайс-листа в формате Excel']")
                .download();

        XLS parsedXls = new XLS(file);
        String checkPassed = parsedXls.excel.getSheetAt(0).getSheetName();

        Assertions.assertEquals(checkPassed, "Page 0");
    }

    @Test
    @DisplayName("Парсинг CSV файлов")
    void parseCsvFileTest() throws IOException, CsvException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream iStream = classLoader.getResourceAsStream("csv.csv");
             Reader reader = new InputStreamReader(iStream)) {
            CSVReader csvReader = new CSVReader(reader);

            List<String[]> strings = csvReader.readAll();
            Assertions.assertEquals(strings.get(0)[0], "Преподаватель");
        }
    }

    @Test
    @DisplayName("Парсинг ZIP файлов, проверка количества файлов")
    void parseZipFileTest() throws IOException {
        ClassLoader classLoader = this.getClass().getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream("zip_2MB.zip");
             ZipInputStream zis = new ZipInputStream(is)) {
            int number_of_files = 0;
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String current_entry_name = entry.getName();
                if (current_entry_name.charAt(current_entry_name.length() - 1) != '/'){
                    number_of_files ++;
                }

            }
            Assertions.assertEquals(3, number_of_files);
        }
    }

}
