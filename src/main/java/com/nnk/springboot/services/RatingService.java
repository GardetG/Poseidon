package com.nnk.springboot.services;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.dto.RatingDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for Rating entity CRUD operations.
 */
@Service
public interface RatingService {

  List<RatingDto> findAll();

  RatingDto findById(int id);

  void add(RatingDto ratingDto);

  void update(RatingDto ratingDto);

  void delete(int id);

}
