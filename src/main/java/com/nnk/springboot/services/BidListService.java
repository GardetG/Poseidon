package com.nnk.springboot.services;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for BidList entity CRUD operations.
 */
@Service
public interface BidListService {

  /**
   * Find all persisted BidList and return the corresponding list of DTO.
   *
   * @return List of BidListDto
   */
  List<BidListDto> findAll();

  /**
   * Find a persisted BidList by Id and return the corresponding DTO.
   *
   * @param id of the BidList
   * @return BidListDto
   * @throws ResourceNotFoundException when BidList not found
   */
  BidListDto findById(int id) throws ResourceNotFoundException;

  /**
   * Add a new BidList from values of the DTO and persist it.
   *
   * @param bidListDto to create
   */
  void add(BidListDto bidListDto);

  /**
   * Update a BidList with value from the DTO and persist it.
   *
   * @param bidListDto to update
   * @throws ResourceNotFoundException when BidList not found
   */
  void update(BidListDto bidListDto) throws ResourceNotFoundException;

  /**
   * Delete a persisted BidList by Id.
   *
   * @param id of the BidList
   * @throws ResourceNotFoundException when BidList not found
   */
  void delete(int id) throws ResourceNotFoundException;

}
