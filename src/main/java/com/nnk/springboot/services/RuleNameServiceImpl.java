package com.nnk.springboot.services;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for RuleName entity CRUD operations.
 */
@Service
public class RuleNameServiceImpl implements RuleNameService {

  @Autowired
  private RuleNameRepository ruleNameRepository;

  @Override
  public List<RuleNameDto> findAll() {
    return ruleNameRepository.findAll()
        .stream()
        .map(ruleName -> new RuleNameDto(
            ruleName.getId(),
            ruleName.getName(),
            ruleName.getDescription(),
            ruleName.getJson(),
            ruleName.getTemplate(),
            ruleName.getSqlStr(),
            ruleName.getSqlPart()
            )
        ).collect(Collectors.toList());
  }

  @Override
  public RuleNameDto findById(int id) {
    return null;
  }

  @Override
  public void add(RuleNameDto ruleNameDto) {

  }

  @Override
  public void update(RuleNameDto ruleNameDto) {

  }

  @Override
  public void delete(int id) {

  }

}
