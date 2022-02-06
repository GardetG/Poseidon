package com.nnk.springboot.utils;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;

/**
 * Mapper utility class to map Rating DTO and entity.
 */
public class RatingMapper {

  private RatingMapper() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Map a Rating into DTO.
   *
   * @param rating to map
   * @return corresponding RatingDto
   */
  public static RatingDto toDto(Rating rating) {
    return new RatingDto(
        rating.getId(),
        rating.getMoodysRating(),
        rating.getSandpRating(),
        rating.getFitchRating(),
        rating.getOrderNumber()
    );
  }

}
