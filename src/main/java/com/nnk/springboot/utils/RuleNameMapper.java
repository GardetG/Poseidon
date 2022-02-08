package com.nnk.springboot.utils;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;

/**
 * Mapper utility class to map RuleName DTO and entity.
 */
public class RuleNameMapper {

  private RuleNameMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Map a RuleName into DTO.
   *
   * @param ruleName to map
   * @return corresponding RuleNameDto
   */
  public static RuleNameDto toDto(RuleName ruleName) {
    return new RuleNameDto(
        ruleName.getId(),
        ruleName.getName(),
        ruleName.getDescription(),
        ruleName.getJson(),
        ruleName.getTemplate(),
        ruleName.getSqlStr(),
        ruleName.getSqlPart()
    );
  }

  /**
   * Map a RuleNameDto into entity.
   *
   * @param ruleName to map into
   * @param ruleNameDto to map from
   */
  public static void toEntity(RuleName ruleName, RuleNameDto ruleNameDto) {
    ruleName.setName(ruleNameDto.getName());
    ruleName.setDescription(ruleNameDto.getDescription());
    ruleName.setJson(ruleNameDto.getJson());
    ruleName.setTemplate(ruleNameDto.getTemplate());
    ruleName.setSqlStr(ruleNameDto.getSqlStr());
    ruleName.setSqlPart(ruleNameDto.getSqlPart());
  }
  
}
