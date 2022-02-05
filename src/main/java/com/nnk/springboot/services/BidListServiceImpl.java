package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.utils.BidListMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for BidList entity CRUD operations.
 */
@Service
public class BidListServiceImpl implements BidListService {

  @Autowired
  private BidListRepository bidListRepository;

  @Override
  public List<BidListDto> findAll() {
    return bidListRepository.findAll()
        .stream()
        .map(BidListMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public BidListDto findById(int id) throws ResourceNotFoundException {
    return bidListRepository.findById(id)
        .map(BidListMapper::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("This bidList is not found"));
  }

  @Override
  public void add(BidListDto bidListDto) {
    BidList bidListToAdd = new BidList(
        bidListDto.getAccount(),
        bidListDto.getType(),
        bidListDto.getBidQuantity()
    );
    bidListRepository.save(bidListToAdd);
  }

  @Override
  public void update(BidListDto bidListDto) {

  }

  @Override
  public void delete(int id) {

  }

}
