package com.sportbot;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverInfo;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MathcesTaker extends Thread {
    public static StringBuilder matches;
    static Thread t=new Thread();
    String binpath=System.getenv("GOOGLE_CHROME_BIN");
    String chromedriverpath=System.getenv("CHROMEDRIVER_PATH");
    static Object lock=new Object();
    //Test
    @Override
    public void run() {
        synchronized (MathcesTaker.lock) {
            matches = new StringBuilder();
            System.out.println(binpath);
            System.out.println(chromedriverpath);
            System.setProperty("webdriver.chrome.driver", chromedriverpath);
            ChromeOptions options = new ChromeOptions();
            options.setHeadless(true);
            options.setBinary(binpath);
            WebDriver driver = new ChromeDriver(options);
            driver.get("https://www.livescore.com/soccer/2020-07-24/");
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now));
                List<WebElement> region = driver.findElements(By.xpath("(//div[@class='row row-tall '])"));
                System.out.println(region.size());
                for (int z = 0; z < region.size(); z++) {
                    matches.setLength(0);//
                    List<WebElement> left = driver.findElements(By.xpath("(//a[@data-stg-id='" + region.get(z).getAttribute("data-stg-id") + "']/div[@class='ply tright name'])"));
                    List<WebElement> right = driver.findElements(By.xpath("(//a[@data-stg-id='" + region.get(z).getAttribute("data-stg-id") + "']/div[@class='ply name'])"));
                    if (left.isEmpty()) {
                        left = driver.findElements(By.xpath("(//div[@data-stg-id='" + region.get(z).getAttribute("data-stg-id") + "']/div[@class='ply tright name'])"));
                        right = driver.findElements(By.xpath("(//div[@data-stg-id='" + region.get(z).getAttribute("data-stg-id") + "']/div[@class='ply name'])"));
                    }
                    matches.append(region.get(z).getText()).append("\n");
                    for (int i = 0; i < left.size(); i++) {
                        Thread.sleep(500);
                   //     System.out.println(left.get(i).getAttribute("innerText"));
                        matches.append(left.get(i).getText()).append(" vs ").append(right.get(i).getText()).append("\n");
                    }
                    System.out.println(matches.toString());
                    try {
                        System.out.println(Thread.currentThread());
                        lock.notify();
                        lock.wait();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println();
                }
                driver.close();
                System.out.println("here");
                driver.close();
                DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now2 = LocalDateTime.now();
                System.out.println(dtf2.format(now2));
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


