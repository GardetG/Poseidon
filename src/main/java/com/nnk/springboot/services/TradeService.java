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

  List<TradeDto> findAll();

  TradeDto findById(int id) throws ResourceNotFoundException;

  void add(TradeDto tradeDto);

  void update(TradeDto tradeDto);

  void delete(int id);

}
