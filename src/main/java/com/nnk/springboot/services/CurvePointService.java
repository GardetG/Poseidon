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

  List<CurvePointDto> findAll();

  CurvePointDto findById(int id) throws ResourceNotFoundException;

  void add(CurvePointDto curvePointDto);

  void update(CurvePointDto curvePointDto) throws ResourceNotFoundException;

  void delete(int id) throws ResourceNotFoundException;

}
