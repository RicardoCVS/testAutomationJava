package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikipediaSearch {

    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\vicar\\.cache\\selenium\\chromedriver\\win32\\112.0.5615.49\\chromedriver.exe");

        WebDriver driver = new ChromeDriver();
        driver.get("https://www.google.com");

        // Aceptar cookies
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10);
            WebElement cookiesAcceptButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='L2AGLb']/div")));
            cookiesAcceptButton.click();
        } catch (Exception e) {
            System.out.println("No se encontró el botón para aceptar las cookies. Omitiendo...");
        }

        WebElement searchBar = driver.findElement(By.xpath("//*[@id='APjFqb']"));
        searchBar.sendKeys("automatización");
        searchBar.sendKeys(Keys.RETURN);

        // Esperar a que se carguen los resultados
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        WebElement wikiLink = driver.findElement(By.xpath("//a[contains(@href, 'https://es.wikipedia.org')]"));
        wikiLink.click();

        // Esperar a que se cargue la página de Wikipedia
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Buscar el párrafo usando el XPath proporcionado
        WebElement paragraph = driver.findElement(By.xpath("//*[@id='mw-content-text']/div[1]/p[28]"));

        // Hacer scroll hasta el elemento paragraph
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", paragraph);

        // Esperar un tiempo adicional para asegurar que la página esté completamente renderizada
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String paragraphText = paragraph.getText();

        // Extraer los años (números de tres y cuatro dígitos) del párrafo
        Pattern fourDigitsPattern = Pattern.compile("\\b\\d{4}\\b");
        Matcher fourDigitsMatcher = fourDigitsPattern.matcher(paragraphText);
        StringBuilder fourDigitsYears = new StringBuilder();
        while (fourDigitsMatcher.find()) {
            fourDigitsYears.append(fourDigitsMatcher.group()).append(", ");
        }
        System.out.println("Años encontrados en el párrafo con 4 dígitos: " + fourDigitsYears);

        Pattern threeDigitsPattern = Pattern.compile("\\b\\d{3}\\b");
        Matcher threeDigitsMatcher = threeDigitsPattern.matcher(paragraphText);
        StringBuilder threeDigitsYears = new StringBuilder();
        while (threeDigitsMatcher.find()) {
            threeDigitsYears.append(threeDigitsMatcher.group()).append(", ");
        }
        System.out.println("Años encontrados en el párrafo con 3 dígitos: " + threeDigitsYears);

        // Guardar screenshot completo
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        Path desktopPath = Paths.get(System.getProperty("user.home"), "OneDrive/Desktop");
        try {
            Files.copy(screenshot.toPath(), desktopPath.resolve("wikipedia_page.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Finalizar el navegador
        driver.quit();
    }
}


