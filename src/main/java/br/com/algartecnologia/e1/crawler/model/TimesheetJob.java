package br.com.algartecnologia.e1.crawler.model;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * @author paulovinicius
 */
public class TimesheetJob {

    private final TimesheetJobConfiguration config = new TimesheetJobConfiguration();
    private String login;
    private String senha;
    private String customer;
    private String project;
    private Map<FilaDemanda, List<Integer>> jobs = new EnumMap<FilaDemanda, List<Integer>>(FilaDemanda.class);
    private int count;
    private WebDriver driver;

    public TimesheetJob() {
    }

    public TimesheetJob(File jsonFile, WebDriver driver) throws IOException, ParseException {
        this();
        this.driver = driver;
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(jsonFile));
        JSONObject jsonObject = (JSONObject) obj;
        parseFile(jsonObject);
    }

    public void execute() throws InterruptedException {
        count = 0;
        driver.get("http://timesheet.synos.com.br");
        autenticar(driver);

        driver.get("http://timesheet.synos.com.br/actitime/tasks/add_new_tasks.do");
        insereDadosProjeto();

        insereSlots(calculaSlots(config, jobs), driver);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }

        insereTarefas();

        if (JOptionPane.showConfirmDialog(null, "Deseja inserir tarefas?", "CornoJobKiller:", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            ((JavascriptExecutor) driver).executeScript("createTasks();");
        }
    }

    private void insereDadosProjeto() throws IllegalArgumentException {
        try {
            Select select1 = new Select(driver.findElement(By.name("customerId")));
            select1.selectByVisibleText(customer);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Não foi possível encontrar o customer %s", this.customer));
        }

        try {
            Select select2 = new Select(driver.findElement(By.name("projectId")));
            select2.selectByVisibleText(project);
        } catch (Exception ex) {
            throw new IllegalArgumentException(String.format("Não foi possível encontrar o projeto %s", this.project));
        }
    }

    private void autenticar(WebDriver driver) throws InterruptedException {
        WebElement loginBox = driver.findElement(By.name("username"));
        loginBox.sendKeys(login);
        WebElement passwd = driver.findElement(By.name("pwd"));
        passwd.sendKeys(senha);
        driver.findElement(By.id("loginButton")).click();

        for (int tentativas = 0; tentativas < 10; tentativas++) {
            if (!driver.getCurrentUrl().contains("submit_tt.do")) {
                Thread.sleep(300);
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public final void parseFile(JSONObject obj) {
        this.login = (String) obj.get("login");
        this.senha = (String) obj.get("senha");
        this.customer = (String) obj.get("customer");
        this.project = (String) obj.get("project");

        checkNull(this.login, this.senha, this.customer, this.project);

        parseFilaDemandas(obj);
    }

    private static List<Integer> transformJsonArrayIntoList(JSONArray array) {
        List<Integer> result = new ArrayList<Integer>();
        for (int i = 0; i < array.size(); i++) {
            result.add(((Long) array.get(i)).intValue());
        }
        return result;
    }

    private void insereTarefa(String string, WebDriver driver) {
        WebElement task = driver.findElement(By.name(String.format("task[%s].name", count)));
        task.sendKeys(string);
        count++;
    }

    private void insereSlots(int tamanho, WebDriver driver) {
        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver).executeScript(String.format("javascript:addRows(%s);", tamanho));
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Map<FilaDemanda, List<Integer>> getJobs() {
        return jobs;
    }

    public void setJobs(Map<FilaDemanda, List<Integer>> jobs) {
        this.jobs = jobs;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getProject() {
        return project;
    }

    private void checkNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                throw new IllegalStateException("Favor informar os campos login,senha,customer e project.");
            }
        }
    }

    private int calculaSlots(TimesheetJobConfiguration config, Map<FilaDemanda, List<Integer>> jobs) {
        int result = 0;
        for (FilaDemanda fila : FilaDemanda.values()) {
            result += config.tarefas.get(fila).size() * jobs.get(fila).size();
        }
        return result - 5;
    }

    private void parseFilaDemandas(JSONObject obj) {
        for (FilaDemanda demanda : FilaDemanda.values()) {
            if (obj.containsKey(demanda.getJsonKey())) {
                this.jobs.put(demanda, transformJsonArrayIntoList((JSONArray) obj.get(demanda.getJsonKey())));
            } else {
                this.jobs.put(demanda, Collections.EMPTY_LIST);
            }
        }
    }

    private void insereTarefas() {
        for (Map.Entry<FilaDemanda, List<Integer>> parsedJobs : jobs.entrySet()) {
            FilaDemanda filaDemanda = parsedJobs.getKey();
            List<Integer> list = parsedJobs.getValue();
            if (list != null) {
                for (Integer os : list) {
                    for (String tarefa : config.tarefas.get(filaDemanda)) {
                        insereTarefa(String.format(tarefa, os), driver);
                    }
                }
            }
        }
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

}
