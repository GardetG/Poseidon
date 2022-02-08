package com.nnk.springboot.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RuleName entity with name, description, json, template and SQL.
 */
@Entity
@Table(name = "rulename")
public class RuleName {

  public RuleName() { }

  /**
   * Create an instance of RuleName with name, description, json, template and SQL.
   *
   * @param name        of RuleName
   * @param description of RuleName
   * @param json        of RuleName
   * @param template    of RuleName
   * @param sqlStr      of RuleName
   * @param sqlPart     of RuleName
   */
  public RuleName(String name, String description, String json, String template,
                  String sqlStr, String sqlPart) {
    this.name = name;
    this.description = description;
    this.json = json;
    this.template = template;
    this.sqlStr = sqlStr;
    this.sqlPart = sqlPart;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "json")
  private String json;
  @Column(name = "template")
  private String template;
  @Column(name = "sqlstr")
  private String sqlStr;
  @Column(name = "sqlpart")
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
