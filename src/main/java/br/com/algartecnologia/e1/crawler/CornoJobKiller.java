package br.com.algartecnologia.e1.crawler;

import br.com.algartecnologia.e1.crawler.model.TimesheetJob;
import java.io.File;
import java.io.IOException;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CornoJobKiller {

    public static WebDriver createDriver() {
        WebDriver driver;
        driver = new FirefoxDriver();
        return driver;
    }

    public static void main(String[] args) throws IOException, ParseException {
        String file;
        if (args.length < 1) {
            file = System.console().readLine("Digite o caminho completo do arquivo: ");
        } else {
            file = args[0];
        }
        try {
            WebDriver driver = createDriver();
            TimesheetJob timesheetJob = new TimesheetJob(new File(file), driver);
            timesheetJob.execute();
            driver.quit();
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(null, ex.getMessage());
            ex.printStackTrace();
        }
    }

}
