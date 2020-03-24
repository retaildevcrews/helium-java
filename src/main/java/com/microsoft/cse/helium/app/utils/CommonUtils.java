package com.microsoft.cse.helium.app.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/** CommonUtils. */
@Component
public class CommonUtils {

  /** GetPartitionKey. */

  public String getPartitionKey(String id) {
    // validate id
    if (!StringUtils.isEmpty(id) && id.length() > 5 && StringUtils.startsWithIgnoreCase(id, "tt")
        || StringUtils.startsWithIgnoreCase(id, "nm")) {
      int idInt = Integer.parseInt(id.substring(2));
      return String.valueOf(idInt % 10);
    }
    throw new IllegalArgumentException("Invalid Partition Key");
  }
}
