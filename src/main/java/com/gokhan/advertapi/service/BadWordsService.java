package com.gokhan.advertapi.service;

import com.gokhan.advertapi.exception.ReadException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class BadWordsService {

  private Pattern badWordlistPattern;

  @PostConstruct
  public void loadBadWords() {
    try (InputStream inputStream = getBadWordsFileAsStream()) {
      if (Objects.isNull(inputStream)) {
        throw new ReadException("Failed to load blacklist words");
      }
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
        StringJoiner joiner = new StringJoiner("|");
        String line;
        while ((line = reader.readLine()) != null) {
          joiner.add(Pattern.quote(line.trim().toLowerCase()));
        }
        badWordlistPattern = Pattern.compile(joiner.toString());
      }
    } catch (IOException e) {
      throw new ReadException("Failed to load blacklist words", e);
    }
  }

  protected InputStream getBadWordsFileAsStream() {
    return getClass().getClassLoader().getResourceAsStream("Badwords.txt");
  }

  public List<String> isBadWordListed(String input) {
    List<String> badWordsList = new ArrayList<>();
    if (input == null || input.isBlank()) {
      return badWordsList;
    }

    Matcher matcher = badWordlistPattern.matcher(input.toLowerCase());
    while (matcher.find()) {
      badWordsList.add(matcher.group());
    }
    return badWordsList;
  }

}
