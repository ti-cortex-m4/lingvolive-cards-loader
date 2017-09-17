package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Service
public class ApplicationRunner implements CommandLineRunner {

    @Value("${google.account.login}")
    private String login;

    @Value("${google.account.password}")
    private String password;

    @Autowired
    private WebDriver webDriver;

    @Override
    public void run(String... args) throws Exception {
        Progress progress = new Progress("progress.properties");
        int i_min = progress.readInt("i", 0);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("Proficiency.only.txt").getFile());

        Scanner scanner = new Scanner(file);
        List<String> lines = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            lines.add(scanner.nextLine());
        }

        webDriver.get("https://www.lingvolive.com/en-us");
        webDriver.findElement(By.xpath("//span[text()='Log in via Google']")).click();
        TimeUnit.SECONDS.sleep(1);

        webDriver.findElement(By.id("identifierId")).sendKeys(login);
        webDriver.findElement(By.xpath("//span[text()='Далее']")).click();
        TimeUnit.SECONDS.sleep(1);

        webDriver.findElement(By.xpath("//input[@type='password']")).sendKeys(password);
        webDriver.findElement(By.xpath("//span[text()='Далее']")).click();
        TimeUnit.SECONDS.sleep(1);

        for (int i = i_min; i < lines.size(); i++) {
            String line = lines.get(i);
            System.out.println(line + " " + i + "/" + lines.size() + " " + 100 * i / lines.size() + "%");

            webDriver.get("https://www.lingvolive.com/en-us/translate/en-ru/" + line);
            webDriver.findElement(By.xpath("//a[@title='Add to my dictionary']")).click();
            TimeUnit.SECONDS.sleep(1);

            By xpath = By.xpath("//span[@class='_1JozP']/span[text()='Learn']");

            if (webDriver.findElements(xpath).size() > 0
                    && webDriver.findElement(xpath).isDisplayed()) {
                webDriver.findElement(xpath).click();
                TimeUnit.SECONDS.sleep(1);
            } else {
                System.out.println("not found");
            }

            progress.writeInt("i", i);
        }
    }

}
