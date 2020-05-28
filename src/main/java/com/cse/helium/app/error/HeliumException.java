package com.cse.helium.app.error;

/** Custom Exception to address linter feedback. */
public class HeliumException extends Exception {
  private static final long serialVersionUID = -1905031427519507137L;

  public HeliumException() {
  }

  public HeliumException(String message) {
    super(message);
  }
}