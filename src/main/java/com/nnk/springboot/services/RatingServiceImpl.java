package com.nnk.springboot.services;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for Rating entity CRUD operations.
 */
@Service
public class RatingServiceImpl implements RatingService {

  @Autowired
  private RatingRepository ratingRepository;

  @Override
  public List<RatingDto> findAll() {
    return ratingRepository.findAll()
        .stream()
        .map(rating -> new RatingDto(
                rating.getId(),
                rating.getMoodysRating(),
                rating.getSandpRating(),
                rating.getFitchRating(),
                rating.getOrderNumber()
            )
        ).collect(Collectors.toList());
  }

  @Override
  public RatingDto findById(int id) throws ResourceNotFoundException {
    return ratingRepository.findById(id)
        .map(rating -> new RatingDto(
                rating.getId(),
                rating.getMoodysRating(),
                rating.getSandpRating(),
                rating.getFitchRating(),
                rating.getOrderNumber()
            )
        ).orElseThrow(() -> new ResourceNotFoundException("This rating is not found"));
  }

  @Override
  public void add(RatingDto ratingDto) {

  }

  @Override
  public void update(RatingDto ratingDto) {

  }

  @Override
  public void delete(int id) {

  }
}
