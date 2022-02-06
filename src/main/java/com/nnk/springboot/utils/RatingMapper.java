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

  /**
   * Map a RatingDto into entity.
   *
   * @param rating to map into
   * @param ratingDto to map from
   */
  public static void toEntity(Rating rating, RatingDto ratingDto) {
    rating.setMoodysRating(ratingDto.getMoodysRating());
    rating.setSandpRating(ratingDto.getSandpRating());
    rating.setFitchRating(ratingDto.getFitchRating());
    rating.setOrderNumber(ratingDto.getOrderNumber());
  }
  
}
