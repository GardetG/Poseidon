package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.CurvePointService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller class for displaying thymeleaf view of CurvePoint management.
 */
@Controller
public class CurveController {

  @Autowired
  private CurvePointService curvePointService;

  @RequestMapping("/curvePoint/list")
  public String home(Model model) {
    model.addAttribute("curvePoints", curvePointService.findAll());
    return "curvePoint/list";
  }

  @GetMapping("/curvePoint/add")
  public String addCurveForm(CurvePointDto curvePointDto) {
    return "curvePoint/add";
  }

  @PostMapping("/curvePoint/validate")
  public String validate(@Valid CurvePointDto curvePointDto, BindingResult result, Model model) {
    if (!result.hasErrors()) {
      curvePointService.add(curvePointDto);
      return "redirect:/curvePoint/list";
    }
    return "curvePoint/add";
  }

  @GetMapping("/curvePoint/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    model.addAttribute("curvePointDto", curvePointService.findById(id));
    return "curvePoint/update";
  }

  @PostMapping("/curvePoint/update/{id}")
  public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePointDto curvePointDto,
                          BindingResult result, Model model) throws ResourceNotFoundException {
    if (!result.hasErrors()) {
      curvePointDto.setId(id);
      curvePointService.update(curvePointDto);
      return "redirect:/curvePoint/list";
    }
    return "curvePoint/update";
  }

  @GetMapping("/curvePoint/delete/{id}")
  public String deleteBid(@PathVariable("id") Integer id, Model model) {
    // TODO: Find Curve by Id and delete the Curve, return to Curve list
    return "redirect:/curvePoint/list";
  }

}
