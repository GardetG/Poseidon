package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.utils.CurvePointMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for BidList entity CRUD operations.
 */
@Service
public class CurvePointServiceImpl implements CurvePointService {

  @Autowired
  private CurvePointRepository curvePointRepository;

  @Override
  public List<CurvePointDto> findAll() {
    return curvePointRepository.findAll()
        .stream()
        .map(CurvePointMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public CurvePointDto findById(int id) throws ResourceNotFoundException {
    return curvePointRepository.findById(id)
        .map(CurvePointMapper::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("This curvePoint is not found"));
  }

  @Override
  public void add(CurvePointDto curvePointDto) {
    CurvePoint curvePointToAdd = new CurvePoint(
        curvePointDto.getCurveId(),
        curvePointDto.getTerm(),
        curvePointDto.getValue()
    );
    curvePointRepository.save(curvePointToAdd);
  }

  @Override
  public void update(CurvePointDto curvePointDto) {

  }

  @Override
  public void delete(int id) {

  }

}
