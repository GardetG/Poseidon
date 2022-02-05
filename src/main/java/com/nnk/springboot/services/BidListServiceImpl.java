package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.utils.BidListMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for BidList entity CRUD operations.
 */
@Service
public class BidListServiceImpl implements BidListService {

  private static final Logger LOGGER = LoggerFactory.getLogger(BidListServiceImpl.class);

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
    return BidListMapper.toDto(getOrThrowException(id));
  }

  @Override
  public void add(BidListDto bidListDto) {
    BidList bidListToAdd = new BidList();
    BidListMapper.toEntity(bidListDto, bidListToAdd);
    bidListRepository.save(bidListToAdd);
  }

  @Override
  public void update(BidListDto bidListDto) throws ResourceNotFoundException {
    BidList bidListToUpdate = getOrThrowException(bidListDto.getBidListId());
    BidListMapper.toEntity(bidListDto, bidListToUpdate);
    bidListRepository.save(bidListToUpdate);
  }

  @Override
  public void delete(int id) throws ResourceNotFoundException {
    BidList bidListToDelete = getOrThrowException(id);
    bidListRepository.delete(bidListToDelete);
  }

  private BidList getOrThrowException(int id) throws ResourceNotFoundException {
    return bidListRepository.findById(id)
        .orElseThrow(() -> {
          LOGGER.error("The BidList with id {} is not found", id);
          return new ResourceNotFoundException("This bidList is not found");
        });
  }

}
