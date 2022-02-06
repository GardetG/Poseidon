package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.utils.CurvePointMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class implementation for CurvePoint entity CRUD operations.
 */
@Service
public class CurvePointServiceImpl implements CurvePointService {

  private static final Logger LOGGER = LoggerFactory.getLogger(CurvePointServiceImpl.class);

  @Autowired
  private CurvePointRepository curvePointRepository;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CurvePointDto> findAll() {
    return curvePointRepository.findAll()
        .stream()
        .map(CurvePointMapper::toDto)
        .collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CurvePointDto findById(int id) throws ResourceNotFoundException {
    return CurvePointMapper.toDto(getOrThrowException(id));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(CurvePointDto curvePointDto) {
    CurvePoint curvePointToAdd = new CurvePoint();
    CurvePointMapper.toEntity(curvePointToAdd, curvePointDto);
    curvePointRepository.save(curvePointToAdd);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update(CurvePointDto curvePointDto) throws ResourceNotFoundException {
    CurvePoint curvePointToUpdate = getOrThrowException(curvePointDto.getId());
    CurvePointMapper.toEntity(curvePointToUpdate, curvePointDto);
    curvePointRepository.save(curvePointToUpdate);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(int id) throws ResourceNotFoundException {
    CurvePoint curvePointToDelete = getOrThrowException(id);
    curvePointRepository.delete(curvePointToDelete);
  }

  private CurvePoint getOrThrowException(int id) throws ResourceNotFoundException {
    return curvePointRepository.findById(id)
        .orElseThrow(() -> {
          LOGGER.error("The CurvePoint with id {} is not found", id);
          return new ResourceNotFoundException("This curvePoint is not found");
        });
  }
}
