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

}
