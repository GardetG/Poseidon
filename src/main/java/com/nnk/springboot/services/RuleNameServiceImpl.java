package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.utils.RuleNameMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for RuleName entity CRUD operations.
 */
@Service
public class RuleNameServiceImpl implements RuleNameService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(RuleNameServiceImpl.class);

  @Autowired
  private RuleNameRepository ruleNameRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RuleNameDto> findAll() {
    return ruleNameRepository.findAll()
        .stream()
        .map(RuleNameMapper::toDto)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RuleNameDto findById(int id) throws ResourceNotFoundException {
    return RuleNameMapper.toDto(getOrThrowException(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(RuleNameDto ruleNameDto) {
    RuleName ruleNameToAdd = new RuleName();
    RuleNameMapper.toEntity(ruleNameToAdd, ruleNameDto);
    ruleNameRepository.save(ruleNameToAdd);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(RuleNameDto ruleNameDto) throws ResourceNotFoundException {
    RuleName ruleNameToUpdate = getOrThrowException(ruleNameDto.getId());
    RuleNameMapper.toEntity(ruleNameToUpdate, ruleNameDto);
    ruleNameRepository.save(ruleNameToUpdate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(int id) throws ResourceNotFoundException {
    RuleName ruleNameToDelete = getOrThrowException(id);
    ruleNameRepository.delete(ruleNameToDelete);
  }

  private RuleName getOrThrowException(int id) throws ResourceNotFoundException {
    return ruleNameRepository.findById(id)
        .orElseThrow(() -> {
          LOGGER.error("The RuleName with id {} is not found", id);
          return new ResourceNotFoundException("This ruleName is not found");
        });
  }
  
}
