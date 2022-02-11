package com.nnk.springboot.exceptions;

/**
 * Exception thrown when the resource to create already exists.
 */
public class ResourceAlreadyExistsException extends Exception {

  public ResourceAlreadyExistsException(String s) {
    super(s);
  }

}
