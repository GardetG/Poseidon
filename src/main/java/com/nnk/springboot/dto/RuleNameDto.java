package com.nnk.springboot.dto;

import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * RuleName DTO between view and model for CRUD operations.
 */
public class RuleNameDto {

  public RuleNameDto() { }

  /**
   * Create an instance of the DTO with required fields.
   *
   * @param id          of the DTO
   * @param name        of the DTO
   * @param description of the DTO
   * @param json        of the DTO
   * @param template    of the DTO
   * @param sqlStr      of the DTO
   * @param sqlPart     of the DTO
   */
  public RuleNameDto(Integer id, String name, String description, String json,
                     String template, String sqlStr, String sqlPart) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.json = json;
    this.template = template;
    this.sqlStr = sqlStr;
    this.sqlPart = sqlPart;
  }

  private Integer id;
  @NotBlank(message = "Name is mandatory")
  @Length(max = 125, message = "Name cannot exceed 125 characters")
  private String name;
  @NotBlank(message = "Description is mandatory")
  @Length(max = 125, message = "Description cannot exceed 125 characters")
  private String description;
  @NotBlank(message = "Json is mandatory")
  @Length(max = 125, message = "Json cannot exceed 125 characters")
  private String json;
  @NotBlank(message = "Template is mandatory")
  @Length(max = 512, message = "Template cannot exceed 512 characters")
  private String template;
  @NotBlank(message = "SQL is mandatory")
  @Length(max = 125, message = "SQL cannot exceed 125 characters")
  private String sqlStr;
  @NotBlank(message = "SQLPart is mandatory")
  @Length(max = 125, message = "SQLPart cannot exceed 125 characters")
  private String sqlPart;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

  public String getTemplate() {
    return template;
  }

  public void setTemplate(String template) {
    this.template = template;
  }

  public String getSqlStr() {
    return sqlStr;
  }

  public void setSqlStr(String sqlStr) {
    this.sqlStr = sqlStr;
  }

  public String getSqlPart() {
    return sqlPart;
  }

  public void setSqlPart(String sqlPart) {
    this.sqlPart = sqlPart;
  }

}
