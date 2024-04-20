package work.webprak;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class WebAppTest {
    @Test
    void MainPage() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/");
        assertEquals("Главная страница", driver.getTitle());
        driver.quit();
    }

    @Test
    void AccessPagesTest() {
        ChromeDriver driver = new ChromeDriver();
        WebElement button;
        driver.get("http://localhost:8080/");

        button = driver.findElement(By.id("rootLink"));
        button.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Главная страница", driver.getTitle());

        button = driver.findElement(By.id("workersListLink"));
        button.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Сотрудники", driver.getTitle());

        button = driver.findElement(By.id("postsListLink"));
        button.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Должности", driver.getTitle());

        button = driver.findElement(By.id("subdivisionsListLink"));
        button.click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Подразделения", driver.getTitle());

        driver.get("http://localhost:8080/worker?workerId=1");
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Информация о сотруднике", driver.getTitle());

        driver.get("http://localhost:8080/post?postId=1");
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Должность", driver.getTitle());

        driver.get("http://localhost:8080/subdivision?subdivisionId=1");
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Информация о подразделении", driver.getTitle());

        driver.get("http://localhost:8080/subdivision?subdivisionId=0");
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Ошибка запроса", driver.getTitle());

        driver.quit();
    }

    @Test
    void UseCasePost() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/posts");
        assertEquals("Должности", driver.getTitle());

        // Add post
        driver.findElement(By.id("postName")).sendKeys("Test Name");
        driver.findElement(By.id("postDescription")).sendKeys("Test Test");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check post has been added
        assertEquals("Должности", driver.getTitle());
        WebElement postsTable = driver.findElement(By.id("table"));
        List<WebElement> cells = postsTable.findElements(By.tagName("td"));
        assertEquals("Test Name", cells.get(cells.size() - 1).getText());

        // Go to post page
        cells.get(cells.size() - 1).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Должность", driver.getTitle());

        // Edit post
        driver.findElement(By.id("editButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Редактировать должность", driver.getTitle());

        driver.findElement(By.id("postName")).clear();
        driver.findElement(By.id("postName")).sendKeys("Test Name New");
        driver.findElement(By.id("postDescription")).clear();
        driver.findElement(By.id("postDescription")).sendKeys("Test Test Test");
        driver.findElement(By.id("submitButton")).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check post has been edited
        assertEquals("Должность", driver.getTitle());
        postsTable = driver.findElement(By.id("postInfo"));
        cells = postsTable.findElements(By.tagName("p"));
        assertEquals("Название: Test Name New", cells.get(0).getText());
        assertEquals("Обязанности: Test Test Test", cells.get(1).getText());

        // Delete post
        driver.findElement(By.id("deleteButton")).click();
        alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check post has been deleted
        assertEquals("Должности", driver.getTitle());
        postsTable = driver.findElement(By.id("table"));
        cells = postsTable.findElements(By.tagName("td"));
        assertNotEquals("Test Name New", cells.get(cells.size() - 1).getText());

        driver.quit();
    }

    @Test
    void UseCaseWorker() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/workers");
        assertEquals("Сотрудники", driver.getTitle());

        // Add worker
        driver.findElement(By.id("workerName")).sendKeys("Test Name");
        driver.findElement(By.id("workerBirthDate")).sendKeys("2000-01-02");
        driver.findElement(By.id("workerAddress")).sendKeys("Test");
        driver.findElement(By.id("workerGraduation")).sendKeys("Test");
        driver.findElement(By.id("workerExperience")).sendKeys("123");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check worker has been added
        assertEquals("Сотрудники", driver.getTitle());
        WebElement workerTable = driver.findElement(By.id("table"));
        List<WebElement> cells = workerTable.findElements(By.tagName("td"));
        assertEquals("Test Name", cells.get(cells.size() - 5).getText());

        // Go to worker page
        cells.get(cells.size() - 5).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Информация о сотруднике", driver.getTitle());

        // Go to edit worker page
        driver.findElement(By.id("editButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Редактировать сотрудника", driver.getTitle());

        // Add worker history
        driver.findElement(By.id("popupButton")).click();
        driver.findElement(By.id("postName")).sendKeys("family");
        driver.findElement(By.id("subdivisionName")).sendKeys("passion");
        driver.findElement(By.id("workStart")).sendKeys("2024-04-18");
        driver.findElement(By.id("addButton")).click();

        // Check worker history has been added
        assertEquals("Редактировать сотрудника", driver.getTitle());
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        assertEquals("family", cells.get(0).getText());

        // Delete worker history
        cells.get(cells.size() - 6).findElement(By.tagName("button")).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();

        // Check worker history has been deleted
        assertEquals("Редактировать сотрудника", driver.getTitle());
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        assertEquals(5, cells.size());

        // Edit worker
        driver.findElement(By.id("workerName")).sendKeys(" New");
        driver.findElement(By.id("workerBirthDate")).clear();
        driver.findElement(By.id("workerBirthDate")).sendKeys("2001-01-02");
        driver.findElement(By.id("workerAddress")).sendKeys(" New");
        driver.findElement(By.id("workerGraduation")).sendKeys(" New");
        driver.findElement(By.id("workerExperience")).clear();
        driver.findElement(By.id("workerExperience")).sendKeys("123");
        driver.findElement(By.id("submitButton")).click();
        alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check worker has been edited
        assertEquals("Информация о сотруднике", driver.getTitle());
        workerTable = driver.findElement(By.id("workerInfo"));
        cells = workerTable.findElements(By.tagName("p"));
        assertEquals("ФИО: Test Name New", cells.get(0).getText());
        assertEquals("Дата рождения: 2001-01-02", cells.get(1).getText());
        assertEquals("Опыт работы: 123", cells.get(4).getText());

        // Delete worker
        driver.findElement(By.id("deleteButton")).click();
        alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check worker has been deleted
        assertEquals("Сотрудники", driver.getTitle());
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        assertNotEquals("Test Name New", cells.get(cells.size() - 5).getText());

        driver.quit();
    }

    @Test
    void UseCaseWorkerError() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/workers");
        assertEquals("Сотрудники", driver.getTitle());

        // Add worker
        driver.findElement(By.id("workerName")).sendKeys("Test Name Error");
        driver.findElement(By.id("workerBirthDate")).sendKeys("2000-01-02");
        driver.findElement(By.id("workerAddress")).sendKeys("Test");
        driver.findElement(By.id("workerGraduation")).sendKeys("Test");
        driver.findElement(By.id("workerExperience")).sendKeys("123");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check worker has been added
        assertEquals("Сотрудники", driver.getTitle());
        WebElement workerTable = driver.findElement(By.id("table"));
        List<WebElement> cells = workerTable.findElements(By.tagName("td"));
        assertEquals("Test Name Error", cells.get(cells.size() - 5).getText());

        // Go to worker page
        cells.get(cells.size() - 5).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Информация о сотруднике", driver.getTitle());

        // Go to edit worker page
        driver.findElement(By.id("editButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Редактировать сотрудника", driver.getTitle());

        // Add worker history with error
        driver.findElement(By.id("popupButton")).click();
        driver.findElement(By.id("postName")).sendKeys("xxx");
        driver.findElement(By.id("subdivisionName")).sendKeys("xxx");
        driver.findElement(By.id("workStart")).sendKeys("2024-04-18");
        driver.findElement(By.id("addButton")).click();

        // Check that we have an error
        assertEquals("Ошибка", driver.getTitle());

        // Delete worker
        driver.get("http://localhost:8080/workers");
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        cells.get(cells.size() - 5).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("deleteButton")).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check worker has been deleted
        assertEquals("Сотрудники", driver.getTitle());
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        assertNotEquals("Test Name New", cells.get(cells.size() - 5).getText());

        driver.quit();
    }

    @Test
    void UseCaseSubdivision() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/subdivisions");
        assertEquals("Подразделения", driver.getTitle());

        // Add subdivision
        driver.findElement(By.id("subdivisionName")).sendKeys("Test Name");
        driver.findElement(By.id("directorName")).sendKeys("Cortez Waller");
        driver.findElement(By.id("headSubdName")).sendKeys("sir");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check subdivision has been added
        assertEquals("Подразделения", driver.getTitle());
        WebElement workerTable = driver.findElement(By.id("table"));
        List<WebElement> cells = workerTable.findElements(By.tagName("td"));
        assertEquals("Test Name", cells.get(cells.size() - 3).getText());

        // Go to subdivision page
        cells.get(cells.size() - 3).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Информация о подразделении", driver.getTitle());

        // Go to subdivision edit page
        driver.findElement(By.id("editButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Редактировать подразделение", driver.getTitle());

        // Add inner subdivision
        driver.findElement(By.id("popupButton1")).click();
        driver.findElement(By.id("subdivisionName1")).sendKeys("library");
        driver.findElement(By.id("addButton1")).click();

        // Check inner subdivision has been added
        assertEquals("Редактировать подразделение", driver.getTitle());
        workerTable = driver.findElement(By.id("table1"));
        cells = workerTable.findElements(By.tagName("td"));
        assertEquals("library", cells.get(0).getText());

        // Delete inner subdivision
        workerTable = driver.findElement(By.id("table1"));
        cells = workerTable.findElements(By.tagName("td"));
        cells.get(cells.size() - 4).findElement(By.tagName("button")).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();

        // Check inner subdivision has been deleted
        assertEquals("Редактировать подразделение", driver.getTitle());
        workerTable = driver.findElement(By.id("table1"));
        cells = workerTable.findElements(By.tagName("td"));
        assertEquals( 3, cells.size());

        // Add subdivision worker
        driver.findElement(By.id("popupButton2")).click();
        driver.findElement(By.id("workerName")).sendKeys("Kassidy Shepard");
        driver.findElement(By.id("postName")).sendKeys("family");
        driver.findElement(By.id("addButton2")).click();

        // Check subdivision worker has been added
        assertEquals("Редактировать подразделение", driver.getTitle());
        workerTable = driver.findElement(By.id("table2"));
        cells = workerTable.findElements(By.tagName("td"));
        assertEquals("Kassidy Shepard", cells.get(3).getText());

        // Delete subdivision worker
        workerTable = driver.findElement(By.id("table2"));
        cells = workerTable.findElements(By.tagName("td"));
        cells.get(cells.size() - 4).findElement(By.tagName("button")).click();
        alert = driver.switchTo().alert();
        alert.accept();

        // Check subdivision worker has been deleted
        assertEquals("Редактировать подразделение", driver.getTitle());
        workerTable = driver.findElement(By.id("table2"));
        cells = workerTable.findElements(By.tagName("td"));
        assertEquals(6, cells.size());

        // Edit subdivision
        driver.findElement(By.id("subdivisionName")).sendKeys(" New");
        driver.findElement(By.id("directorName")).clear();
        driver.findElement(By.id("directorName")).sendKeys("Melvin Galloway");
        driver.findElement(By.id("headSubdName")).clear();
        driver.findElement(By.id("headSubdName")).sendKeys("population");
        driver.findElement(By.id("submitButton")).click();
        alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check subdivision has been edited
        assertEquals("Информация о подразделении", driver.getTitle());
        workerTable = driver.findElement(By.id("subdivisionInfo"));
        cells = workerTable.findElements(By.tagName("p"));
        assertEquals("Название: Test Name New", cells.get(0).getText());
        assertEquals("Руководитель: Melvin Galloway", cells.get(1).getText());
        assertEquals("Головное подразделение: population", cells.get(2).getText());

        // Delete subdivision
        driver.findElement(By.id("deleteButton")).click();
        alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check subdivision has been deleted
        assertEquals("Подразделения", driver.getTitle());
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        assertNotEquals("Test Name New", cells.get(cells.size() - 3).getText());

        driver.quit();
    }

    @Test
    void UseCaseSubdivisionError() {
        ChromeDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/subdivisions");
        assertEquals("Подразделения", driver.getTitle());

        // Add subdivision
        driver.findElement(By.id("subdivisionName")).sendKeys("Test Name Error");
        driver.findElement(By.id("directorName")).sendKeys("Cortez Waller");
        driver.findElement(By.id("headSubdName")).sendKeys("sir");
        driver.findElement(By.id("submitButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check subdivision has been added
        assertEquals("Подразделения", driver.getTitle());
        WebElement workerTable = driver.findElement(By.id("table"));
        List<WebElement> cells = workerTable.findElements(By.tagName("td"));
        assertEquals("Test Name Error", cells.get(cells.size() - 3).getText());

        // Go to subdivision page
        cells.get(cells.size() - 3).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Информация о подразделении", driver.getTitle());

        // Go to subdivision edit page
        driver.findElement(By.id("editButton")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        assertEquals("Редактировать подразделение", driver.getTitle());

        // Add inner subdivision with error
        driver.findElement(By.id("popupButton1")).click();
        driver.findElement(By.id("subdivisionName1")).sendKeys("xxx");
        driver.findElement(By.id("addButton1")).click();

        // Check that we have an error and go back
        assertEquals("Ошибка", driver.getTitle());
        driver.navigate().back();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Add subdivision worker
        driver.findElement(By.id("popupButton2")).click();
        driver.findElement(By.id("workerName")).sendKeys("xxx");
        driver.findElement(By.id("postName")).sendKeys("xxx");
        driver.findElement(By.id("addButton2")).click();

        // Check that we have an error and go back
        assertEquals("Ошибка", driver.getTitle());
        driver.navigate().back();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Delete subdivision
        driver.get("http://localhost:8080/subdivisions");
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        cells.get(cells.size() - 3).findElement(By.tagName("a")).click();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("deleteButton")).click();
        Alert alert = driver.switchTo().alert();
        alert.accept();
        driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);

        // Check subdivision has been deleted
        assertEquals("Подразделения", driver.getTitle());
        workerTable = driver.findElement(By.id("table"));
        cells = workerTable.findElements(By.tagName("td"));
        assertNotEquals("Test Name Error", cells.get(cells.size() - 3).getText());

        driver.quit();
    }
}
