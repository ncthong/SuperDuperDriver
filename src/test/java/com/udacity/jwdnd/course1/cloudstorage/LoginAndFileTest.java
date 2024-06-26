package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class LoginAndFileTest extends  CloudStorageApplicationTests{
    @Test
    public void getLoginPage() {
        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
    }
    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     *
     * If this test is failing, please ensure that you are handling redirecting users
     * back to the login page after a succesful sign up.
     * Read more about the requirement in the rubric:
     * https://review.udacity.com/#!/rubrics/2724/view
     */
    @Test
    public void testRedirection() {
        // Create a test account
        testSignUp("Redirection","Test","RT","123");

        // Check if we have been redirected to the log in page.
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
    }

    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     *
     * If this test is failing, please ensure that you are handling bad URLs
     * gracefully, for example with a custom error page.
     *
     * Read more about custom error pages at:
     * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
     */
    @Test
    public void testBadUrl() {
        // Create a test account
        testSignUp("URL","Test","UT","123");
        doLogIn("UT", "123");

        // Try to access a random made-up URL.
        driver.get("http://localhost:" + this.port + "/some-random-page");
        Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
    }


    /**
     * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the
     * rest of your code.
     * This test is provided by Udacity to perform some basic sanity testing of
     * your code to ensure that it meets certain rubric criteria.
     *
     * If this test is failing, please ensure that you are handling uploading large files (>1MB),
     * gracefully in your code.
     *
     * Read more about file size limits here:
     * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
     */
    @Test
    public void testLargeUpload() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        testSignUp("Thong","Ng","testUpload","1");
        doLogIn("testUpload", "1");
        String fileName = "upload5m.zip";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());
        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
        } catch (org.openqa.selenium.TimeoutException e) {
            System.out.println("Large File upload failed");
        }
        Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

    }

    @Test
    public void testLoginLogout() {
        //Login
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        testSignUp("Thong","Ng","testLogin","1");
        doLogIn("testLogin", "1");
        Assertions.assertEquals("Home", driver.getTitle());

        // Logout
        WebElement logoutBtn= driver.findElement(By.id("btn-logout"));
        logoutBtn.click();
        wait.until(ExpectedConditions.titleContains("Login"));
        driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertEquals("Login", driver.getTitle());
    }
    @Test
    public void testNoFileUpload(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        testSignUp("Thong","Ng","testUpload","1");
        doLogIn("testUpload", "1");
        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        wait.until(ExpectedConditions.titleContains("Result"));
        String textRs = driver.findElement(By.className("display-5")).getText();
        Assertions.assertEquals(textRs,"Error");
        directToHome(wait);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileTable")));
        WebElement table = driver.findElement(By.id("fileTable"));
        List<WebElement> rowsnum = table.findElements(By.tagName("tr"));
        rowsnum = table.findElements(By.tagName("tr"));
        Assertions.assertEquals(1, rowsnum.size());
    }
    @Test
    public void testDuplicateFileUpload(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        testSignUp("Thong","Ng","testUpload","1");
        doLogIn("testUpload", "1");
        String fileName = "upload5m.zip";

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
        fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

        WebElement uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();
        directToHome(wait);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
        WebElement newFile = driver.findElement(By.id("fileUpload"));
        newFile.sendKeys(new File(fileName).getAbsolutePath());
        uploadButton = driver.findElement(By.id("uploadButton"));
        uploadButton.click();

        wait.until(ExpectedConditions.titleContains("Result"));
        String textRs = driver.findElement(By.className("display-5")).getText();
        Assertions.assertEquals(textRs,"Error");
        directToHome(wait);

    }

}
