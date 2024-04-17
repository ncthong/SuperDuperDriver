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

import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CredentialTest extends CloudStorageApplicationTests{

    @Test
    public void addCredentialTest(){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        doMockSignUp("Thong","Ng","credentialTest","123");
        doLogIn("credentialTest", "123");
        navigateToTab(wait, "credentials");
        openModal(wait,"credential");
        fillCredentialAndSave(wait,"http://localhost:8080/home", "thongnc", "123");
        directToHome(wait);
        navigateToTab(wait, "credentials");
        // Verify credential added
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        String url = driver.findElement(By.className("cred-entry-url")).getText();
        Assertions.assertEquals(url, "http://localhost:8080/home");
        String username = driver.findElement(By.className("cred-entry-username")).getText();
        Assertions.assertEquals(username, "thongnc");
        String pass = driver.findElement(By.className("cred-entry-password")).getText();
        Assertions.assertNotEquals(pass, "123");
    }
    @Test
    public void updateCredentialTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        addCredentialTest();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        driver.findElement(By.className("cred-entry-edit")).click();
        // update credential
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
        WebElement url = driver.findElement(By.id("credential-url"));
        WebElement username = driver.findElement(By.id("credential-username"));
        WebElement pass = driver.findElement(By.id("credential-password"));
        String currentPass = pass.getText();
        WebElement save = driver.findElement(By.id("save-cred-btn"));
        url.click();
        url.clear();
        url.sendKeys("http://localhost:8080/login");
        username.click();
        username.clear();
        username.sendKeys("UsernameUpdated");
        pass.click();
        pass.sendKeys("@password");
        save.click();
        directToHome(wait);
        navigateToTab(wait, "credentials");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        // Verify credential updated
        String urlUpdated = driver.findElement(By.className("cred-entry-url")).getText();
        Assertions.assertEquals(urlUpdated, "http://localhost:8080/login");
        String usernameUpdated = driver.findElement(By.className("cred-entry-username")).getText();
        Assertions.assertEquals(usernameUpdated, "UsernameUpdated");
        String newpass = driver.findElement(By.className("cred-entry-password")).getText();
        Assertions.assertNotEquals(currentPass, newpass);
    }
    @Test
    public void deleteCredential() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        addCredentialTest();
        // Press delete note button
        WebElement deleteBtn = driver.findElement(By.className("cred-entry-delete"));
        deleteBtn.click();
        directToHome(wait);
        navigateToTab(wait, "credentials");
        // Verify that the note was deleted
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
        WebElement table = driver.findElement(By.id("credentialTable"));
        List<WebElement> rowsnum = table.findElements(By.tagName("tr"));
        rowsnum = table.findElements(By.tagName("tr"));
        Assertions.assertEquals(1, rowsnum.size());
    }

    private void fillCredentialAndSave(WebDriverWait wait, String url, String username,String password){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModalLabel")));
        WebElement urlCrd = driver.findElement(By.id("credential-url"));
        WebElement usernameCrd = driver.findElement(By.id("credential-username"));
        WebElement passwordCrd = driver.findElement(By.id("credential-password"));
        WebElement save = driver.findElement(By.id("save-cred-btn"));
        urlCrd.click();
        urlCrd.sendKeys(url);
        usernameCrd.click();
        usernameCrd.sendKeys(username);
        passwordCrd.click();
        passwordCrd.sendKeys(password);
        save.click();
    }
}
