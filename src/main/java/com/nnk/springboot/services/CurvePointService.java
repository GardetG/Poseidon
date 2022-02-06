package com.nnk.springboot.services;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for CurvePoint entity CRUD operations.
 */
@Service
public interface CurvePointService {

  /**
   * Find all persisted CurvePoint and return the corresponding list of DTO.
   *
   * @return List of CurvePointDto
   */
  List<CurvePointDto> findAll();

  /**
   * Find a persisted CurvePoint by Id and return the corresponding DTO.
   *
   * @param id of the CurvePoint
   * @return CurvePointDto
   * @throws ResourceNotFoundException when CurvePoint not found
   */
  CurvePointDto findById(int id) throws ResourceNotFoundException;

  /**
   * Add a new CurvePoint from values of the DTO and persist it.
   *
   * @param curvePointDto to create
   */
  void add(CurvePointDto curvePointDto);

  /**
   * Update a CurvePoint with value from the DTO and persist it.
   *
   * @param curvePointDto to update
   * @throws ResourceNotFoundException when CurvePoint not found
   */
  void update(CurvePointDto curvePointDto) throws ResourceNotFoundException;

  /**
   * Delete a persisted CurvePoint by Id.
   *
   * @param id of the CurvePoint
   * @throws ResourceNotFoundException when CurvePoint not found
   */
  void delete(int id) throws ResourceNotFoundException;

}
