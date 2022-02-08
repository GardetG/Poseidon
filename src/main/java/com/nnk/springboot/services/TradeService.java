package com.nnk.springboot.services;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for Trade entity CRUD operations.
 */
@Service
public interface TradeService {

  /**
   * Find all persisted Trade and return the corresponding list of DTO.
   *
   * @return List of TradeDto
   */
  List<TradeDto> findAll();

  /**
   * Find a persisted Trade by Id and return the corresponding DTO.
   *
   * @param id of the Trade
   * @return TradeDto
   * @throws ResourceNotFoundException when Trade not found
   */
  TradeDto findById(int id) throws ResourceNotFoundException;

  /**
   * Add a new Trade from values of the DTO and persist it.
   *
   * @param tradeDto to create
   */
  void add(TradeDto tradeDto);

  /**
   * Update a Trade with value from the DTO and persist it.
   *
   * @param tradeDto to update
   * @throws ResourceNotFoundException when Trade not found
   */
  void update(TradeDto tradeDto) throws ResourceNotFoundException;

  /**
   * Delete a persisted Trade by Id.
   *
   * @param id of the Trade
   * @throws ResourceNotFoundException when Trade not found
   */
  void delete(int id) throws ResourceNotFoundException;

}
