package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for Trade entity CRUD operations.
 */
@Service
public class TradeServiceImpl implements TradeService {

  @Autowired
  private TradeRepository tradeRepository;

  @Override
  public List<TradeDto> findAll() {
    return tradeRepository.findAll()
        .stream()
        .map(trade -> new TradeDto(
            trade.getTradeId(),
            trade.getAccount(),
            trade.getType(),
            trade.getBuyQuantity()
        ))
        .collect(Collectors.toList());
  }

  @Override
  public TradeDto findById(int id) throws ResourceNotFoundException {
    return tradeRepository.findById(id)
        .map(trade -> new TradeDto(
            trade.getTradeId(),
            trade.getAccount(),
            trade.getType(),
            trade.getBuyQuantity()
        ))
        .orElseThrow(() -> new ResourceNotFoundException("This trade is not found"));
  }

  @Override
  public void add(TradeDto tradeDto) {
    Trade tradeToAdd = new Trade(
        tradeDto.getAccount(),
        tradeDto.getType(),
        tradeDto.getBuyQuantity()
    );
    tradeRepository.save(tradeToAdd);
  }

  @Override
  public void update(TradeDto tradeDto) throws ResourceNotFoundException {
    Trade tradeToUpdate = tradeRepository.findById(tradeDto.getTradeId())
        .orElseThrow(() -> new ResourceNotFoundException("This trade is not found"));
    tradeToUpdate.setAccount(tradeDto.getAccount());
    tradeToUpdate.setType(tradeDto.getType());
    tradeToUpdate.setBuyQuantity(tradeDto.getBuyQuantity());
    tradeRepository.save(tradeToUpdate);
  }

  @Override
  public void delete(int id) {

  }

}
