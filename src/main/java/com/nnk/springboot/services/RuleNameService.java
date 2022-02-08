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

  List<RuleNameDto> findAll();

  RuleNameDto findById(int id) throws ResourceNotFoundException;

  void add(RuleNameDto ruleNameDto);

  void update(RuleNameDto ruleNameDto) throws ResourceNotFoundException;

  void delete(int id);

}
