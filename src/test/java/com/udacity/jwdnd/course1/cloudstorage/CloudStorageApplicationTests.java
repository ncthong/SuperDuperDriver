package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.time.Duration;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void notAccessibleWithoutLogin() {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void flowLoginLogout() {
		doMockSignUp("flowLoginLogout","flowLoginLogout","flowLoginLogout","flowLoginLogout");
		doLogIn("flowLoginLogout", "flowLoginLogout");

		// Verify that the user is logged in.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		webDriverWait.until(ExpectedConditions.titleContains("Home"));
		Assertions.assertEquals("Home", driver.getTitle());

		// Verify that the user is logged out.
		WebElement logoutButton= driver.findElement(By.id("btn-logout"));
		logoutButton.click();
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	public void createNote() {
		doMockSignUp("note","note","note","note");
		doLogIn("note", "note");

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));

		// Go to Notes tab
		driver.findElement(By.id("nav-notes-tab")).click();

		// Press add new note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-btn")));
		WebElement addNewNoteButton= driver.findElement(By.id("new-note-btn"));
		addNewNoteButton.click();

		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.click();
		inputTitle.sendKeys("Note title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.sendKeys("Note description");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
		WebElement submitNote = driver.findElement(By.id("save-note-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();

		// Verify that the note was created
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement table = driver.findElement(By.id("userTable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement firstNoteTitle = rows.get(1).findElement(By.tagName("th"));
		WebElement firstNoteDetail = (rows.get(1).findElements(By.tagName("td"))).get(1);

		Assertions.assertAll("Should return correct note",
				() -> Assertions.assertEquals("Note title", firstNoteTitle.getText()),
				() -> Assertions.assertEquals("Note description", firstNoteDetail.getText()));
	}

	@Test
	public void editNote() {
		try {
			doLogIn("note", "note");
		} catch (Exception e) {
			doMockSignUp("note","note","note","note");
			doLogIn("note", "note");
		}

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Go to Notes tab
		driver.findElement(By.id("nav-notes-tab")).click();
		// Press add new note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-btn")));
		WebElement addNewNoteButton= driver.findElement(By.id("new-note-btn"));
		addNewNoteButton.click();

		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.click();
		inputTitle.sendKeys("Note title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.sendKeys("Note description");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
		WebElement submitNote = driver.findElement(By.id("save-note-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();

		// Press edit note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement table = driver.findElement(By.id("userTable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement editbtn = rows.get(1).findElement(By.className("note-entry-edit"));
		editbtn.click();

		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.click();
		inputTitle.clear();
		inputTitle.sendKeys("Note title edited");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.clear();
		inputDescription.sendKeys("Note description edited");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
		submitNote = driver.findElement(By.id("save-note-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();

		// Verify that the note was created
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		table = driver.findElement(By.id("userTable"));
		rows = table.findElements(By.tagName("tr"));
		WebElement firstNoteTitle = rows.get(1).findElement(By.tagName("th"));
		WebElement firstNoteDetail = (rows.get(1).findElements(By.tagName("td"))).get(1);

		Assertions.assertAll("Should return correct note",
				() -> Assertions.assertEquals("Note title edited", firstNoteTitle.getText()),
				() -> Assertions.assertEquals("Note description edited", firstNoteDetail.getText()));
	}

	@Test
	public void deleteNote() {
		try {
			doLogIn("note", "note");
		} catch (Exception e) {
			doMockSignUp("note","note","note","note");
			doLogIn("note", "note");
		}

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Go to Notes tab
		driver.findElement(By.id("nav-notes-tab")).click();
		// Press add new note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-note-btn")));
		WebElement addNewNoteButton= driver.findElement(By.id("new-note-btn"));
		addNewNoteButton.click();

		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement inputTitle = driver.findElement(By.id("note-title"));
		inputTitle.click();
		inputTitle.sendKeys("Note title");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement inputDescription = driver.findElement(By.id("note-description"));
		inputDescription.click();
		inputDescription.sendKeys("Note description");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-note-btn")));
		WebElement submitNote = driver.findElement(By.id("save-note-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();
		// Press edit note button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		WebElement table = driver.findElement(By.id("userTable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement deletebtn = rows.get(1).findElement(By.tagName("a"));
		deletebtn.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		driver.findElement(By.id("nav-notes-tab")).click();

		// Verify that the note was created
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		table = driver.findElement(By.id("userTable"));
		rows = table.findElements(By.tagName("tr"));

		Assertions.assertEquals(1, rows.size());
	}

	@Test
	public void addCredential() {
		try {
			doLogIn("credential", "credential");
		} catch (Exception e) {
			doMockSignUp("credential","credential","credential","credential");
			doLogIn("credential", "credential");
		}

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Go to credential tab
		driver.findElement(By.id("nav-credentials-tab")).click();

		// Press add new credential button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-credential-btn")));
		WebElement addNewCredentialButton= driver.findElement(By.id("new-credential-btn"));
		addNewCredentialButton.click();

		// Fill out the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputUrl = driver.findElement(By.id("credential-url"));
		inputUrl.click();
		inputUrl.sendKeys("google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.sendKeys("username");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.sendKeys("123");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-cred-btn")));
		WebElement submitNote = driver.findElement(By.id("save-cred-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();

		// Verify that the credential was created
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement table = driver.findElement(By.id("credentialTable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement firstCredentialUrl = rows.get(1).findElement(By.tagName("th"));
		WebElement firstCredentialPassword = (rows.get(1).findElements(By.tagName("td"))).get(1);

		Assertions.assertAll("Should return correct credential",
				() -> Assertions.assertEquals("google.com", firstCredentialUrl.getText()),
				() -> Assertions.assertEquals("username", firstCredentialPassword.getText()));
	}

	@Test
	public void editCredential() {
		try {
			doLogIn("credential", "credential");
		} catch (Exception e) {
			doMockSignUp("credential","credential","credential","credential");
			doLogIn("credential", "credential");
		}

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Go to credential tab
		driver.findElement(By.id("nav-credentials-tab")).click();
		// Press add new credential button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-credential-btn")));
		WebElement addNewCredentialButton= driver.findElement(By.id("new-credential-btn"));
		addNewCredentialButton.click();

		// Fill out the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputUrl = driver.findElement(By.id("credential-url"));
		inputUrl.click();
		inputUrl.sendKeys("google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.sendKeys("username");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.sendKeys("123");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-cred-btn")));
		WebElement submitNote = driver.findElement(By.id("save-cred-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();
		// Press edit credential button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement table = driver.findElement(By.id("credentialTable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
 		WebElement editbtn = rows.get(1).findElement(By.tagName("button"));
		editbtn.click();

		// Edit the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		inputUrl = driver.findElement(By.id("credential-url"));
		inputUrl.click();
		inputUrl.clear();
		inputUrl.sendKeys("google.com.vn");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.clear();
		inputUsername.sendKeys("usernameedited");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.clear();
		inputPassword.sendKeys("456");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-cred-btn")));
		submitNote = driver.findElement(By.id("save-cred-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();

		// Verify that the credential was created
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		table = driver.findElement(By.id("credentialTable"));
		rows = table.findElements(By.tagName("tr"));
		WebElement firstCredentialUrl = rows.get(1).findElement(By.tagName("th"));
		WebElement firstCredentialPassword = (rows.get(1).findElements(By.tagName("td"))).get(1);

		Assertions.assertAll("Should return correct credential",
				() -> Assertions.assertEquals("google.com.vn", firstCredentialUrl.getText()),
				() -> Assertions.assertEquals("usernameedited", firstCredentialPassword.getText()));
	}

	@Test
	public void deleteCredential() {
		try {
			doLogIn("credential", "credential");
		} catch (Exception e) {
			doMockSignUp("credential","credential","credential","credential");
			doLogIn("credential", "credential");
		}

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Go to credential tab
		driver.findElement(By.id("nav-credentials-tab")).click();
		// Press add new credential button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-credential-btn")));
		WebElement addNewCredentialButton= driver.findElement(By.id("new-credential-btn"));
		addNewCredentialButton.click();

		// Fill out the credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement inputUrl = driver.findElement(By.id("credential-url"));
		inputUrl.click();
		inputUrl.sendKeys("google.com");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement inputUsername = driver.findElement(By.id("credential-username"));
		inputUsername.click();
		inputUsername.sendKeys("username");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement inputPassword = driver.findElement(By.id("credential-password"));
		inputPassword.click();
		inputPassword.sendKeys("123");

		// Submit
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("save-cred-btn")));
		WebElement submitNote = driver.findElement(By.id("save-cred-btn"));
		submitNote.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();

		// Press delete credential button
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		WebElement table = driver.findElement(By.id("credentialTable"));
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		WebElement deletebtn = rows.get(1).findElement(By.className("cred-entry-delete"));
		deletebtn.click();

		// Back to home page
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-go-home")));
		driver.findElement(By.className("btn-go-home")).click();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
		driver.findElement(By.id("nav-credentials-tab")).click();

		// Verify that the note was deleted
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		table = driver.findElement(By.id("credentialTable"));
		rows = table.findElements(By.tagName("tr"));

		Assertions.assertEquals(1, rows.size());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
	}



	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

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
		doMockSignUp("Redirection","Test","RT","123");

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
		doMockSignUp("URL","Test","UT","123");
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
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}

