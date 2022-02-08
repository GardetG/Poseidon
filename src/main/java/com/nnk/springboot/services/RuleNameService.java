package com.nnk.springboot.services;

import com.nnk.springboot.dto.RuleNameDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for RuleName entity CRUD operations.
 */
@Service
public interface RuleNameService {

  List<RuleNameDto> findAll();

  RuleNameDto findById(int id);

  void add(RuleNameDto ruleNameDto);

  void update(RuleNameDto ruleNameDto);

  void delete(int id);

}
