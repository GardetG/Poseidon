package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.TradeService;
import com.nnk.springboot.utils.TradeMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for Trade entity CRUD operations.
 */
@Service
public class TradeServiceImpl implements TradeService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TradeServiceImpl.class);

  @Autowired
  private TradeRepository tradeRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TradeDto> findAll() {
    return tradeRepository.findAll()
        .stream()
        .map(TradeMapper::toDto)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TradeDto findById(int id) throws ResourceNotFoundException {
    return TradeMapper.toDto(getOrThrowException(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(TradeDto tradeDto) {
    Trade tradeToAdd = new Trade();
    TradeMapper.toEntity(tradeToAdd, tradeDto);
    tradeRepository.save(tradeToAdd);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(TradeDto tradeDto) throws ResourceNotFoundException {
    Trade tradeToUpdate = getOrThrowException(tradeDto.getTradeId());
    TradeMapper.toEntity(tradeToUpdate, tradeDto);
    tradeRepository.save(tradeToUpdate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(int id) throws ResourceNotFoundException {
    Trade tradeToDelete = getOrThrowException(id);
    tradeRepository.delete(tradeToDelete);
  }

  private Trade getOrThrowException(int id) throws ResourceNotFoundException {
    return tradeRepository.findById(id)
        .orElseThrow(() -> {
          LOGGER.error("The Trade with id {} is not found", id);
          return new ResourceNotFoundException("This trade is not found");
        });
  }

}
