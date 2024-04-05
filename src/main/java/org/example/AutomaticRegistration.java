package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;

public class AutomaticRegistration {
    private static final Logger logger = LogManager.getLogger(AutomaticRegistration.class);

    private static boolean flag = true;

    public static void main(String[] args) {

        // Создание объекта опций для настройки браузера
        ChromeOptions options = new ChromeOptions();

        // Создание экземпляра веб-драйвера
        WebDriver driver = new ChromeDriver(options);

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

    private static void scheduleTask(WebDriver driver) {
        // Создание объекта для текущей даты и времени
        LocalDateTime now = LocalDateTime.now();

        // Проверка, является ли сегодня понедельником и время 18:30
        if (now.getDayOfWeek() == DayOfWeek.MONDAY && now.getHour() == 12 && now.getMinute() == 0) {
            // Открытие страницы для регистрации на игру
            driver.get("https://vtb.mzgb.net/");
            logger.info("Открыта страница регистрации на игру.");

            // Поиск элемента, содержащего текст "туц,туц"
            WebElement elementWithText = driver.findElement(By.xpath("//*[contains(text(), 'Туц Туц')]"));

            // Если элемент с текстом найден, нажать на кнопку "Запись в резерв"
            if (elementWithText != null) {
                logger.info("Найден элемент с текстом 'Классика'.");
//аккаунт
                WebElement accountIcon = driver.findElement(By.xpath("//div[contains(@class, 'rounded-full') and contains(@class, 'w-8') and contains(@class, 'h-8') and contains(@class, 'ml-3') and contains(@class, 'flex') and contains(@class, 'flex-row') and contains(@class, 'items-center') and contains(@class, 'justify-center') and contains(@class, 'overflow-hidden') and contains(@class, 'border-white')]//img[@alt='']"));
                accountIcon.click();
                logger.info("Нажат элемент входа в личный кабинет.");
//ввод мыла
                WebElement emailInput = driver.findElement(By.id("email"));
                emailInput.sendKeys(Configuration.getEmail());
                logger.info("Введена электронная почта.");
//ввод пароля
                WebElement passwordInput = driver.findElement(By.id("password"));
                passwordInput.sendKeys(Configuration.getPassword());
                logger.info("Введен пароль.");
//нажать Войти
                WebElement enterButton = driver.findElement(By.xpath("//button[contains(text(), 'Войти')]"));
                enterButton.click();
                logger.info("Нажата кнопка 'Войти'.");

                try {
                    Thread.sleep(1500); // Подождать 1 секунду
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                logger.info("Ждем 1.5 секунды");
                // Находим элемент по CSS-селектору(мозгобойня)
                WebElement element = driver.findElement(By.cssSelector(
                        "body > nav > div > div.flex.flex-row.items-center > div:nth-child(1) > a"));
                element.click();
                logger.info("Нажат элемент 'Мозгобойня'");

//нажать кнопку запись в резерв
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
                logger.info("Ждем 2 секунды");
                WebElement reserveButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By
                        .xpath("//button[contains(text(), 'Зарегистрироваться')]")));
                logger.info("Найдена кнопка 'Зарегистрироваться'");
                if (reserveButton.isEnabled()) {
                    logger.info("Кнопка Зарегистрироваться кликабельна");
                    JavascriptExecutor executor = (JavascriptExecutor) driver;
                    executor.executeScript("arguments[0].scrollIntoView(true);", reserveButton);
                    reserveButton.click();
                    logger.info("Кнопка Зарегистрироваться нажата");
                } else {
                    logger.warn("Кнопка Зарегистрироваться не кликабельна");
                }

//                try {
//                    Thread.sleep(2000); // Подождать 2 секунды
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                //нажать далее
                WebElement moveButton = driver.findElement(By.xpath("//button[contains(text(), 'Далее')]"));
                moveButton.click();

//                try {
//                    Thread.sleep(2000); // Подождать 2 секунды
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//

                // Найти элемент с атрибутом src="/img/icons/plus.svg" и нажать на него 4 раза
                WebElement plusIcon = driver.findElement(By.cssSelector("img[src='/img/icons/plus.svg']"));
                for (int i = 0; i < 4; i++) {
                    plusIcon.click();
                }

//                try {
//                    Thread.sleep(2000); // Подождать 2 секунды
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }


                WebElement moveButton1 = driver.findElement(By.xpath("//button[contains(text(), 'Далее')]"));
                moveButton1.click();

//                try {
//                    Thread.sleep(2000); // Подождать 2 секунды
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }

                WebElement registration = driver.findElement(By.xpath("//button[contains(text(), 'Регистрация на игру')]"));
                registration.click();
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            logger.info("Закрытие браузера.");
            // Закрыть браузер
            driver.quit();
            flag = false;
        } else {
            logger.info("Еще не время для регистрации.");
        }
    }
}
