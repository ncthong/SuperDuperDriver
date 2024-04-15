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

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class CloudStorageApplicationTests {

	@LocalServerPort
	private Integer port;
	private static WebDriver driver;

	@BeforeEach
	public void beforeEach() {
		driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach(){
		if (driver != null) {
			// Attempt Graceful termination
			driver.close();
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
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

		/* Check that the sign-up was successful.
		// You may have to modify the element "success-msg" and the sign-up
		// success message below depending on the rest of your code.
		*/
		// Wait For Redirection!
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
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
	 * back to the login page after a successful sign-up.
	 * Read more about the requirement in the rubric:
	 * https://review.udacity.com/#!/rubrics/2724/view
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");

		// Check if we have been redirected to the login page.
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
		} catch (TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}


	// 1. Rubric - Test Signup and Login Flow
	@Test
	public void testSignupLogin() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// verifies that the home page is not accessible without logging in
		driver.get("http://localhost:" + this.port + "/home");
        Assertions.assertNotSame("Home", driver.getTitle());

		// signs up a new user, logs that user in, verifies that they can access the home page,
		doMockSignUp("Udacity", "Java", "student", "roll_number");
		doLogIn("student", "roll_number");
		Assertions.assertEquals("Home", driver.getTitle());

		// then logs out and verifies that the home page is no longer accessible
		WebElement buttonLogout = driver.findElement(By.id("btn-logout"));
		buttonLogout.click();
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotSame("Home", driver.getTitle());
	}

	private void openTab(WebDriverWait driverWait, String title) {
		driverWait.until(ExpectedConditions.titleContains("Home"));
		WebElement notesTab = driver.findElement(By.id("nav-" + title +"-tab"));
		notesTab.click();
	}

	private void openModal(WebDriverWait driverWait, String title) {
		driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("new-"+ title +"-btn")));
		WebElement notesModalBtn = driver.findElement(By.id("new-"+ title +"-btn"));
		notesModalBtn.click();
	}

	private void goHomeFromResult(WebDriverWait driverWait) {
		driverWait.until(ExpectedConditions.titleContains("Result"));
		driver.findElement(By.className("btn-go-home")).click();
		driverWait.until(ExpectedConditions.titleContains("Home"));
	}

	// 2. Rubric - Test adding, editing, and deleting notes
	@Test
	public void createNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// User Login
		doMockSignUp("Udacity", "Java", "student", "roll_number");
		doLogIn("student", "roll_number");

		// Open Note Section
		openTab(webDriverWait, "notes");

		// Open Note Modal
		openModal(webDriverWait, "note");

		// Fill out the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModalLabel")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		WebElement noteSubmit = driver.findElement(By.id("save-note-btn"));

		noteTitle.click();
		noteTitle.sendKeys("Note 1");
		noteDescription.click();
		noteDescription.sendKeys("Note 1 Description");
		noteSubmit.click();

		// Navigate to Home/Notes from Result Page
		goHomeFromResult(webDriverWait);
		openTab(webDriverWait, "notes");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

		// Verify Note Title
		String noteTitleText = driver.findElement(By.className("note-entry-title")).getText();
		Assertions.assertEquals(noteTitleText, "Note 1");
	}

	@Test
	public void editNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Test Data Prepare
		createNote();

		// Open Note Section
		openTab(webDriverWait, "notes");

		// Verify Existing Note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		String noteTitleText = driver.findElement(By.className("note-entry-title")).getText();
		String noteDescText = driver.findElement(By.className("note-entry-desc")).getText();

		Assertions.assertEquals(noteTitleText, "Note 1");
		Assertions.assertEquals(noteDescText, "Note 1 Description");

		// Edit Flow
		driver.findElement(By.className("note-entry-edit")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModalLabel")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		WebElement noteSubmit = driver.findElement(By.id("save-note-btn"));

		noteTitle.click();
		noteTitle.sendKeys(" - Edit");
		noteDescription.click();
		noteDescription.sendKeys(" - Edit");
		noteSubmit.click();

		// Navigate to Home/Notes from Result Page
		goHomeFromResult(webDriverWait);
		openTab(webDriverWait, "notes");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

		// Verify Note Edit
		String editNoteTitleText = driver.findElement(By.className("note-entry-title")).getText();
		String editNoteDescText = driver.findElement(By.className("note-entry-desc")).getText();
		Assertions.assertEquals(editNoteTitleText, "Note 1 - Edit");
		Assertions.assertEquals(editNoteDescText, "Note 1 Description - Edit");
	}

	@Test
	public void deleteNote() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Test Data
		editNote();

		// Open Note Section
		openTab(webDriverWait, "notes");

		// Verify Existing Note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		String noteTitleText = driver.findElement(By.className("note-entry-title")).getText();
		String noteDescText = driver.findElement(By.className("note-entry-desc")).getText();

		Assertions.assertEquals(noteTitleText, "Note 1 - Edit");
		Assertions.assertEquals(noteDescText, "Note 1 Description - Edit");

		// Delete Flow
		driver.findElement(By.className("note-entry-delete")).click();

		// Navigate to Home/Notes from Result Page
		goHomeFromResult(webDriverWait);
		openTab(webDriverWait, "notes");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));

		// Verify Note Doesn't Exists
		Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("note-entry-title")));
	}


	// 3. Test adding, editing and deleting credentials
	@Test
	public void createCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// User Login
		doMockSignUp("Udacity", "Java", "student", "roll_number");
		doLogIn("student", "roll_number");

		// Open Credentials Section
		openTab(webDriverWait, "credentials");

		// Open Credential Modal
		openModal(webDriverWait, "credential");

		// Fill out the Credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		WebElement credUrl = driver.findElement(By.id("credential-url"));
		WebElement credUsername = driver.findElement(By.id("credential-username"));
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		WebElement credSubmit = driver.findElement(By.id("save-cred-btn"));

		credUrl.click();
		credUrl.sendKeys("https://udacity.com");

		credUsername.click();
		credUsername.sendKeys("student");

		credPassword.click();
		credPassword.sendKeys("roll_number");
		credSubmit.click();

		// Navigate to Home/Credentials from Result Page
		goHomeFromResult(webDriverWait);
		openTab(webDriverWait, "credentials");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		// Verify Credential
		Assertions.assertEquals(driver.findElement(By.className("cred-entry-url")).getText(), "https://udacity.com");
		Assertions.assertEquals(driver.findElement(By.className("cred-entry-username")).getText(), "student");
		Assertions.assertNotEquals(driver.findElement(By.className("cred-entry-password")).getText(), "roll_number");
	}


	@Test
	public void editCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Prepare Data
		createCredential();

		// Open Credentials Section
		openTab(webDriverWait, "credentials");

		// Verify Existing Credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		String credEntryUrl = driver.findElement(By.className("cred-entry-url")).getText();
		String credEntryUsername = driver.findElement(By.className("cred-entry-username")).getText();
		String oldCredEntryPassword = driver.findElement(By.className("cred-entry-password")).getText();

		Assertions.assertEquals(credEntryUrl, "https://udacity.com");
		Assertions.assertEquals(credEntryUsername, "student");

		// Edit Flow
		driver.findElement(By.className("cred-entry-edit")).click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
		WebElement credUrl = driver.findElement(By.id("credential-url"));
		WebElement credUsername = driver.findElement(By.id("credential-username"));
		WebElement credPassword = driver.findElement(By.id("credential-password"));
		WebElement credSubmit = driver.findElement(By.id("save-cred-btn"));

		credUrl.click();
		credUrl.sendKeys("/user");
		credUsername.click();
		credUsername.sendKeys("_user");

		credPassword.click();
		credPassword.sendKeys("_update");
		credSubmit.click();

		// Navigate to Home/Credentials from Result Page
		goHomeFromResult(webDriverWait);
		openTab(webDriverWait, "credentials");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		// Verify Credentials Edit
		String editCredTitleText = driver.findElement(By.className("cred-entry-url")).getText();
		String editCredDescText = driver.findElement(By.className("cred-entry-username")).getText();
		String editCredPassText = driver.findElement(By.className("cred-entry-password")).getText();

		Assertions.assertEquals(editCredTitleText, "https://udacity.com/user");
		Assertions.assertEquals(editCredDescText, "student_user");
		Assertions.assertNotEquals(editCredPassText, oldCredEntryPassword);
	}

	@Test
	public void deleteCredential() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

		// Prepare Data
		editCredential();

		// Open Credential Section
		openTab(webDriverWait, "credentials");

		// Verify Existing Credential
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));
		String credEntryUrl = driver.findElement(By.className("cred-entry-url")).getText();
		String credEntryUsername = driver.findElement(By.className("cred-entry-username")).getText();

		Assertions.assertEquals(credEntryUrl, "https://udacity.com/user");
		Assertions.assertEquals(credEntryUsername, "student_user");

		// Delete Flow
		driver.findElement(By.className("cred-entry-delete")).click();

		// Navigate to Home/Credentials from Result Page
		goHomeFromResult(webDriverWait);
		openTab(webDriverWait, "credentials");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		// Verify Credential Doesn't Exists
		Assertions.assertThrows(NoSuchElementException.class, () -> driver.findElement(By.className("cred-entry-url")));
	}
}

