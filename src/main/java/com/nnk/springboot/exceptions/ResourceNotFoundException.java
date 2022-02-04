package com.nnk.springboot.exceptions;

/**
 * Exception thrown when the required resource is not found.
 */
public class ResourceNotFoundException extends Exception {

  public ResourceNotFoundException(String s) {
    super(s);
  }

}
