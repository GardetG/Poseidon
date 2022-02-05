package com.nnk.springboot.services;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for BidList entity CRUD operations.
 */
@Service
public interface BidListService {

  List<BidListDto> findAll();

  BidListDto findById(int id) throws ResourceNotFoundException;

  void add(BidListDto bidListDto);

  void update(BidListDto bidListDto) throws ResourceNotFoundException;

  void delete(int id);

}
