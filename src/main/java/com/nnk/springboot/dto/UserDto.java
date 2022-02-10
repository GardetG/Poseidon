package com.nnk.springboot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * User DTO between view and model for CRUD operations.
 */
public class UserDto {

  public UserDto() { }

  /**
   * Create an instance of the DTO with required fields.
   *
   * @param id       of th Dto
   * @param username of th Dto
   * @param fullName of th Dto
   * @param role     of th Dto
   */
  public UserDto(Integer id, String username, String fullName, String role) {
    this.id = id;
    this.username = username;
    this.fullName = fullName;
    this.role = role;
  }

  private Integer id;
  @NotBlank(message = "Username is mandatory")
  @Length(max = 125, message = "Username cannot exceed 125 characters")
  private String username;
  @NotBlank(message = "Password is mandatory")
  @Length(max = 125, message = "Password cannot exceed 125 characters")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  @NotBlank(message = "FullName is mandatory")
  @Length(max = 125, message = "FullName cannot exceed 125 characters")
  private String fullName;
  @NotBlank(message = "Role is mandatory")
  @Length(max = 125, message = "Role cannot exceed 125 characters")
  private String role;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

}
