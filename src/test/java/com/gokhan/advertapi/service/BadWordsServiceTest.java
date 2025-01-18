package com.gokhan.advertapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.gokhan.advertapi.exception.ReadException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BadWordsServiceTest {

  private BadWordsService badWordsService;

  @BeforeEach
  void setUp() {
    badWordsService = spy(new BadWordsService());
  }

  @Test
  void shouldLoadBadWords() {
    // When
    String badWordsContent = "badWord\nbadWord2";
    InputStream inputStream = new ByteArrayInputStream(badWordsContent.getBytes());
    doReturn(inputStream).when(badWordsService).getBadWordsFileAsStream();

    badWordsService.loadBadWords();

    // Then
    assertNotNull(badWordsService.isBadWordListed("badWord"));
    assertNotNull(badWordsService.isBadWordListed("badWord2"));
    assertTrue(badWordsService.isBadWordListed("goodWord").isEmpty());
  }

  @Test
  void shouldListBadWordForInvalidInput() {
    // When
    doReturn(null).when(badWordsService).getBadWordsFileAsStream();

    // Then
    assertNotNull(badWordsService.isBadWordListed(null));
    assertNotNull(badWordsService.isBadWordListed(""));
    assertNotNull(badWordsService.isBadWordListed("   "));
  }

  @Test
  void shouldListBadWordForWordNotInList() {
    // When
    String badWordsContent = "badWord\nbadWord2";
    InputStream inputStream = new ByteArrayInputStream(badWordsContent.getBytes());
    doReturn(inputStream).when(badWordsService).getBadWordsFileAsStream();
    badWordsService.loadBadWords();

    // Then
    assertNotNull(badWordsService.isBadWordListed("goodWord"));
  }

  @Test
  void shouldListBadWordToCheckCaseSensitive() {
    // When
    String badWordsContent = "badWord\nbadWord2";
    InputStream inputStream = new ByteArrayInputStream(badWordsContent.getBytes());
    doReturn(inputStream).when(badWordsService).getBadWordsFileAsStream();
    badWordsService.loadBadWords();

    // Then
    assertNotNull(badWordsService.isBadWordListed("BADWORD1"));
    assertNotNull(badWordsService.isBadWordListed("BaDwOrD2"));
  }

  @Test
  void testLoadBadWordsWhenFileNotFound() {
    // When
    doReturn(null).when(badWordsService).getBadWordsFileAsStream();

    // Then
    ReadException exception = assertThrows(ReadException.class, badWordsService::loadBadWords);
    assertEquals("Failed to load blacklist words", exception.getMessage());
  }

  @Test
  public void testLoadBadWordsWhenIOError() throws Exception {
    // When
    InputStream mockInputStream = mock(InputStream.class);
    doReturn(mockInputStream).when(badWordsService).getBadWordsFileAsStream();
    doThrow(new IOException("IO error")).when(mockInputStream).close();

    // Then
    assertThrows(ReadException.class, () -> badWordsService.loadBadWords(),
        "Failed to load blacklist words");
  }

  @Test
  public void testGetBadWordsFileAsStream() {
    InputStream inputStream = badWordsService.getBadWordsFileAsStream();

    if (inputStream != null) {
      assertNotNull(inputStream, "The InputStream should not be null when the resource exists");
    } else {
      System.out.println("Badwords.txt resource not found. Make sure it exists in the classpath.");
    }
  }

}