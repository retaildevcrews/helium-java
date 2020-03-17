package com.microsoft.cse.helium.app.utils;

import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class Validator {

  /* Valid input: starts with 'nm' (case sensitive)
      followed by 5-9 digits
      parses to int > 0
  */
  private final String validActorRegex = "[nm]{2}[0-9]{5,9}";

  public Boolean isValidActorId(String actorId) {
    Pattern p = Pattern.compile(validActorRegex);
    return p.matcher(actorId).matches();
  }

}
