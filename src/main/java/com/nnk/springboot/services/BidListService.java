package com.nnk.springboot.services;

import com.nnk.springboot.dto.BidListDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for BidList entity CRUD operations.
 */
@Service
public interface BidListService {

  List<BidListDto> findAll();

  BidListDto findById(int id);

  void add(BidListDto bidListDto);

  void update(BidListDto bidListDto);

  void delete(int id);

}
