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
class NoteTests extends CloudStorageApplicationTests{
    @Test
    public void addNewNoteTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        testSignUp("Thong","Ng","noteTest","123");
        doLogIn("noteTest", "123");
        navigateToTab(wait, "notes");
        openModal(wait,"note");
        fillNoteAndSave(wait,"Note title", "New Note Content");
        directToHome(wait);
        navigateToTab(wait, "notes");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteList")));
        // Verify Note added
        String noteTitle = driver.findElement(By.className("note-entry-title")).getText();
        Assertions.assertEquals(noteTitle, "Note title");
        String noteContent = driver.findElement(By.className("note-entry-desc")).getText();
        Assertions.assertEquals(noteContent, "New Note Content");
    }

    @Test
    public void updateNoteTest() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        addNewNoteTest();
        driver.findElement(By.className("note-entry-edit")).click();

        // update note
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModalLabel")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        WebElement submitNote = driver.findElement(By.id("save-note-btn"));
        noteTitle.sendKeys(" updated");
        noteDescription.sendKeys(" updated");
        submitNote.click();
        directToHome(wait);
        navigateToTab(wait, "notes");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteList")));
        // Verify Note updated
        String noteTitleUpdated = driver.findElement(By.className("note-entry-title")).getText();
        Assertions.assertEquals(noteTitleUpdated, "Note title updated");
        String noteDescriptionUpdated = driver.findElement(By.className("note-entry-desc")).getText();
        Assertions.assertEquals(noteDescriptionUpdated, "New Note Content updated");
    }

    @Test
    public void deleteNote() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        addNewNoteTest();
        // Press delete note button
        WebElement deleteBtn = driver.findElement(By.className("note-entry-delete"));
        deleteBtn.click();
        directToHome(wait);
        navigateToTab(wait, "notes");
        // Verify that the note was deleted
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteList")));
        WebElement table = driver.findElement(By.id("noteList"));
        List<WebElement> rowsnum = table.findElements(By.tagName("tr"));
        rowsnum = table.findElements(By.tagName("tr"));
        Assertions.assertEquals(1, rowsnum.size());
    }



    private void fillNoteAndSave(WebDriverWait wait, String title, String content) {
        WebElement noteTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        WebElement noteSubmit = driver.findElement(By.id("save-note-btn"));

        noteTitle.sendKeys(title);
        noteDescription.sendKeys(content);
        noteSubmit.click();
    }
}
