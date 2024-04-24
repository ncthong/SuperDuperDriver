package com.udacity.jwdnd.course1.cloudstorage;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {
	@LocalServerPort
	protected Integer port;

	protected WebDriver driver;

	@BeforeEach
	public void beforeEach() {
		driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (driver != null) {
			driver.quit();
		}
	}
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	public void testSignUp(String firstName, String lastName, String userName, String password){
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		driver.get("http://localhost:" + this.port + "/signup");
		wait.until(ExpectedConditions.titleContains("Sign Up"));

		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));

		inputFirstName.sendKeys(firstName);
		inputLastName.sendKeys(lastName);
		inputUsername.sendKeys(userName);
		inputPassword.sendKeys(password);
		buttonSignUp.click();
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	public void doLogIn(String userName, String password) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		driver.get("http://localhost:" + this.port + "/login");

		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		WebElement loginButton = driver.findElement(By.id("login-button"));

		loginUserName.sendKeys(userName);
		loginPassword.sendKeys(password);
		loginButton.click();
		wait.until(ExpectedConditions.titleContains("Home"));

	}



	//Utilities
	public void navigateToTab(WebDriverWait wait, String name) {
		wait.until(ExpectedConditions.titleContains("Home"));
		WebElement tab = driver.findElement(By.id("nav-" + name +"-tab"));
		tab.click();
	}
	public void openModal(WebDriverWait wait, String name) {
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-"+ name +"-btn")));
		WebElement modalBtn = driver.findElement(By.id("new-"+ name +"-btn"));
		modalBtn.click();
	}
	public void directToHome(WebDriverWait wait) {
		wait.until(ExpectedConditions.titleContains("Result"));
		driver.findElement(By.className("btn-go-home")).click();
		wait.until(ExpectedConditions.titleContains("Home"));
	}

}

