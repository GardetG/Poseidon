package com.nnk.springboot.services;

import com.nnk.springboot.dto.CurvePointDto;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for CurvePoint entity CRUD operations.
 */
@Service
public interface CurvePointService {

  List<CurvePointDto> findAll();

  CurvePointDto findById(int id);

  void add(CurvePointDto curvePointDto);

  void update(CurvePointDto curvePointDto);

  void delete(int id);

}
