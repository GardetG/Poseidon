package com.nnk.springboot.services;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.repositories.CurvePointRepository;
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
        .map(curvePoint -> new CurvePointDto(
                curvePoint.getId(),
                curvePoint.getCurveId(),
                curvePoint.getTerm(),
                curvePoint.getValue()
            )
        ).collect(Collectors.toList());
  }

  @Override
  public CurvePointDto findById(int id) {
    return null;
  }

  @Override
  public void add(CurvePointDto curvePointDto) {

  }

  @Override
  public void update(CurvePointDto curvePointDto) {

  }

  @Override
  public void delete(int id) {

  }

}
