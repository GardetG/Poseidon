package com.nnk.springboot.services;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for Rating entity CRUD operations.
 */
@Service
public interface RatingService {

  /**
   * Find all persisted Rating and return the corresponding list of DTO.
   *
   * @return List of RatingDto
   */
  List<RatingDto> findAll();

  /**
   * Find a persisted Rating by Id and return the corresponding DTO.
   *
   * @param id of the Rating
   * @return RatingDto
   * @throws ResourceNotFoundException when Rating not found
   */
  RatingDto findById(int id) throws ResourceNotFoundException;

  /**
   * Add a new Rating from values of the DTO and persist it.
   *
   * @param ratingDto to create
   */
  void add(RatingDto ratingDto);

  /**
   * Update a Rating with value from the DTO and persist it.
   *
   * @param ratingDto to update
   * @throws ResourceNotFoundException when Rating not found
   */
  void update(RatingDto ratingDto) throws ResourceNotFoundException;

  /**
   * Delete a persisted Rating by Id.
   *
   * @param id of the Rating
   * @throws ResourceNotFoundException when Rating not found
   */
  void delete(int id) throws ResourceNotFoundException;

}
