package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class AutomaticRegistration {
    private static final Logger logger = LogManager.getLogger(AutomaticRegistration.class);

    private static boolean flag = true;
    private static boolean buttonFlag = true;

    public static void main(String[] args) {

        // Создание объекта опций для настройки браузера
        ChromeOptions options = new ChromeOptions();

        // Создание экземпляра веб-драйвера
        WebDriver driver = new ChromeDriver(options);
        // Открытие страницы для регистрации на игру
        driver.get("https://vtb.mzgb.net/account");
        logger.info("Opened page of authorisation");

        //ввод мыла
        WebElement emailInput = driver.findElement(By.name("email"));
        emailInput.sendKeys(Configuration.getEmail());
        logger.info("Email entered.");

        //ввод пароля
        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys(Configuration.getPassword());
        logger.info("Password entered.");

        //нажать Войти
        WebElement enterButton = driver.findElement(By.className("btn-filled"));
        enterButton.click();
        logger.info("'Login' button pressed.");

        // Находим элемент по CSS-селектору(мозгобойня)
        WebElement element = driver.findElement(By.cssSelector(
                "body > nav > div > div.flex.flex-row.items-center > div:nth-child(1) > a"));
        element.click();
        logger.info("'Мозгобойня' element pressed");

        // Периодическая проверка каждые 1 секунд
        while (flag) {
            // Проверка задачи по расписанию каждые 1 секунд
            try {
                Thread.sleep(1000); // Подождать 1 секунд
                scheduleTask(driver); // Выполнить задачу
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void scheduleTask(WebDriver driver) throws InterruptedException {
        // Создание объекта для текущей даты и времени
        LocalDateTime now = LocalDateTime.now();

        // Проверка, является ли сегодня понедельником и время 12.00
        if (now.getDayOfWeek() == DayOfWeek.MONDAY && now.getHour() == 12 && now.getMinute() == 0) {
            driver.navigate().refresh();
            // Поиск элемента, содержащего текст "туц,туц"
            //WebElement elementWithText = driver.findElement(By.xpath("//*[contains(text(), 'Туц Туц')]"));

            // Если элемент с текстом найден, нажать на кнопку "Запись в резерв"
            //            if (elementWithText != null) {
            //                logger.info("Найден элемент с текстом 'Туц Туц'.");
            //нажать кнопку зарегистрироваться


            /*Проверка в цикле содержит ли кнопка атрибут
            "disabled" и попытка нажать на кнопку*/
            while (buttonFlag) {
                logger.info("Cycle of checking the 'Зарегистрироваться' button");
                List<WebElement> registerButtons = driver.findElements(By.xpath(
                        "//button[contains(text(), 'Зарегистрироваться')]"));
                logger.info("Count of buttons 'Зарегистрироваться' = {}", registerButtons.size());
                List<WebElement> activeButtons = registerButtons.stream()
                        .filter(button -> button.getAttribute("disabled") == null)
                        .toList();
                logger.info("Count of active buttons 'Зарегистрироваться' = {}", activeButtons.size());
                if (activeButtons.isEmpty()) {
                    driver.navigate().refresh();
                    logger.info("List of active buttons is empty");
                } else
                    registerButtonClick(activeButtons.getFirst(), driver);
                logger.info("Button 'Зарегистрироваться' was clicked");
                /*   registerButtonClick(registerButton, driver);*/
            }

            //нажать далее
            WebElement moveButton = driver.findElement(By.xpath("//button[contains(text(), 'Далее')]"));
            moveButton.click();
            logger.info("Button 'Далее' was clicked");
            // Найти элемент с атрибутом src="/img/icons/plus.svg" и нажать на него 4 раза
            WebElement plusIcon = driver.findElement(By.cssSelector("img[src='/img/icons/plus.svg']"));
            for (int i = 0; i < 4; i++) {
                plusIcon.click();
                logger.info("Button '+' was clicked 4 times");
            }

            WebElement moveButton1 = driver.findElement(By.xpath("//button[contains(text(), 'Далее')]"));
            moveButton1.click();
            logger.info("Button 'Далее' was clicked");

            WebElement registration = driver.findElement(By.xpath("//button[contains(text(), 'Регистрация на игру')]"));
            registration.click();
            logger.info("Button 'Регистрация на игру' was clicked");

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            logger.info("Waiting 10 sec");
            logger.info("Закрытие браузера.");
            // Закрыть браузер
            driver.quit();
            flag = false;
        } else {
            logger.info("Еще не время для регистрации.");
        }
    }

    /*Метод проверки доступности кнопки и попытка нажать на нее */
    private static void registerButtonClick(WebElement registerButton, WebDriver driver) {
      /*  if (registerButton != null && registerButton.getAttribute("disabled") != null) {
            logger.info("Кнопка ЗАРЕГИСТРИРОВАТЬСЯ НЕДОСТУПНА");
            driver.navigate().refresh();
        } else*/
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].scrollIntoView(true);", registerButton);
            assert registerButton != null;
            registerButton.click();
            buttonFlag = false;
            logger.info("Кнопка 'Зарегистрироваться' нажата");
        } catch (StaleElementReferenceException e) {
            throw new RuntimeException(e);
        }
    }
}


