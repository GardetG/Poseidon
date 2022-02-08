package com.nnk.springboot.services;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for RuleName entity CRUD operations.
 */
@Service
public interface RuleNameService {

  /**
   * Find all persisted RuleName and return the corresponding list of DTO.
   *
   * @return List of RuleNameDto
   */
  List<RuleNameDto> findAll();

  /**
   * Find a persisted RuleName by Id and return the corresponding DTO.
   *
   * @param id of the RuleName
   * @return RuleNameDto
   * @throws ResourceNotFoundException when RuleName not found
   */
  RuleNameDto findById(int id) throws ResourceNotFoundException;

  /**
   * Add a new RuleName from values of the DTO and persist it.
   *
   * @param ruleNameDto to create
   */
  void add(RuleNameDto ruleNameDto);

  /**
   * Update a RuleName with value from the DTO and persist it.
   *
   * @param ruleNameDto to update
   * @throws ResourceNotFoundException when RuleName not found
   */
  void update(RuleNameDto ruleNameDto) throws ResourceNotFoundException;

  /**
   * Delete a persisted RuleName by Id.
   *
   * @param id of the RuleName
   * @throws ResourceNotFoundException when RuleName not found
   */
  void delete(int id) throws ResourceNotFoundException;

}
