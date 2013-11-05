package br.com.algartecnologia.e1.crawler;

import br.com.algartecnologia.e1.crawler.model.FilaDemanda;
import br.com.algartecnologia.e1.crawler.model.TimesheetJob;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TimesheetJobTest {

    private static WebDriver driver;

    @BeforeClass
    public static void setUp() {
        TimesheetJobTest.driver = new FirefoxDriver();
    }

    @AfterClass
    public static void tearDown() {
        try {
            TimesheetJobTest.driver.quit();
        } catch (Exception ex) {
        }
    }

    @Test
    public void testTimesheetMock() throws InterruptedException {
        TimesheetJob timesheetJob = createMockJob();
        timesheetJob.setDriver(driver);
        timesheetJob.execute();
        driver.quit();
    }

    private static TimesheetJob createMockJob() {
        TimesheetJob timesheetJob = new TimesheetJob();
        timesheetJob.setLogin("gustavohlf");
        timesheetJob.setSenha("algar123");
        Map<FilaDemanda, List<Integer>> jobs = new EnumMap<FilaDemanda, List<Integer>>(FilaDemanda.class);
        jobs.put(FilaDemanda.REQUISITOS, Arrays.asList(0, 1, 2, 3));
        jobs.put(FilaDemanda.HOMOLOGACAO, Arrays.asList(4, 5, 6, 7));
        jobs.put(FilaDemanda.DESENVOLVIMENTO, Arrays.asList(8, 9));
        timesheetJob.setJobs(jobs);
        timesheetJob.setCustomer("Empresa1");
        timesheetJob.setProject("BHZ13F001-Vortex");
        return timesheetJob;
    }

}
