package com.nnk.springboot.utils;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;

/**
 * Mapper utility class to map CurvePoint DTO and entity.
 */
public class CurvePointMapper {

  private CurvePointMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Map a CurvePoint into DTO.
   *
   * @param curvePoint to map
   * @return corresponding CurvePointDto
   */
  public static CurvePointDto toDto(CurvePoint curvePoint) {
    return new CurvePointDto(
        curvePoint.getId(),
        curvePoint.getCurveId(),
        curvePoint.getTerm(),
        curvePoint.getValue()
    );
  }

}
