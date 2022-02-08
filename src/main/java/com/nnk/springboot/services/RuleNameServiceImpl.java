package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.utils.RuleNameMapper;
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
        .map(RuleNameMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public RuleNameDto findById(int id) throws ResourceNotFoundException {
    return ruleNameRepository.findById(id)
        .map(RuleNameMapper::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("This ruleName is not found"));
  }

  @Override
  public void add(RuleNameDto ruleNameDto) {
    RuleName ruleNameToAdd = new RuleName();
    RuleNameMapper.toEntity(ruleNameToAdd, ruleNameDto);
    ruleNameRepository.save(ruleNameToAdd);
  }

  @Override
  public void update(RuleNameDto ruleNameDto) throws ResourceNotFoundException {
    RuleName ruleNameToUpdate = ruleNameRepository.findById(ruleNameDto.getId())
        .orElseThrow(() -> new ResourceNotFoundException("This ruleName is not found"));
    RuleNameMapper.toEntity(ruleNameToUpdate, ruleNameDto);
    ruleNameRepository.save(ruleNameToUpdate);
  }

  @Override
  public void delete(int id) throws ResourceNotFoundException {
    RuleName ruleNameToDelete = ruleNameRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("This ruleName is not found"));
    ruleNameRepository.delete(ruleNameToDelete);
  }

}
