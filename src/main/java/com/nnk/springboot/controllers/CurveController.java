package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.CurvePointService;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller class for displaying thymeleaf view of CurvePoint management.
 */
@Controller
public class CurveController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CurveController.class);

  @Autowired
  private CurvePointService curvePointService;

  /**
   * Show a view displaying all the CurvePoints.
   *
   * @param model of the view
   * @return View
   */
  @RequestMapping("/curvePoint/list")
  public String home(Model model) {
    LOGGER.info("Request list of all CurvePoints");
    model.addAttribute("curvePoints", curvePointService.findAll());
    return "curvePoint/list";
  }

  /**
   * Show a view with the add CurvePoint form.
   *
   * @param curvePointDto of the form
   * @return View
   */
  @GetMapping("/curvePoint/add")
  public String addCurveForm(CurvePointDto curvePointDto) {
    LOGGER.info("Request form to add CurvePoint");
    return "curvePoint/add";
  }

  /**
   * Validate the add CurvePoint form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist CurvePoint and return the CurvePoint list view.
   *
   * @param curvePointDto of the form
   * @param result        hold validation errors
   * @return View
   */
  @PostMapping("/curvePoint/validate")
  public String validate(@Valid CurvePointDto curvePointDto, BindingResult result) {
    LOGGER.info("Request validation on CurvePoint adding");
    if (!result.hasErrors()) {
      curvePointService.add(curvePointDto);
      LOGGER.info("CurvePoint successfully added");
      return "redirect:/curvePoint/list";
    }
    LOGGER.info("Failed to add CurvePoint, form contains errors");
    return "curvePoint/add";
  }

  /**
   * Show a view with update CurvePoint form prefilled with CurvePoint current values.
   *
   * @param id    of the CurvePoint to update
   * @param model of the view
   * @return View
   * @throws ResourceNotFoundException when the requested CurvePoint not found
   */
  @GetMapping("/curvePoint/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    LOGGER.info("Request form to update CurvePoint id {}", id);
    model.addAttribute("curvePointDto", curvePointService.findById(id));
    return "curvePoint/update";
  }

  /**
   * Validate the update CurvePoint form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist CurvePoint and return to CurvePoint list view.
   *
   * @param id            of the CurvePoint to update
   * @param curvePointDto of the form
   * @param result        hold validation errors
   * @return View
   * @throws ResourceNotFoundException when the requested CurvePoint not found
   */
  @PostMapping("/curvePoint/update/{id}")
  public String updateCurve(@PathVariable("id") Integer id, @Valid CurvePointDto curvePointDto,
                            BindingResult result) throws ResourceNotFoundException {
    LOGGER.info("Request validation on CurvePoint id {} update", id);
    if (!result.hasErrors()) {
      curvePointDto.setId(id);
      curvePointService.update(curvePointDto);
      LOGGER.info("CurvePoint id {} successfully updated", id);
      return "redirect:/curvePoint/list";
    }
    LOGGER.info("Failed to update CurvePoint id {}, form contains errors", id);
    return "curvePoint/update";
  }

  /**
   * Request the deletion of a CurvePoint and return to CurvePoint list view.
   *
   * @param id of the CurvePoint to delete
   * @return View
   * @throws ResourceNotFoundException when the requested CurvePoint not found
   */
  @GetMapping("/curvePoint/delete/{id}")
  public String deleteCurve(@PathVariable("id") Integer id)
      throws ResourceNotFoundException {
    LOGGER.info("Request CurvePoint id {} deletion", id);
    curvePointService.delete(id);
    LOGGER.info("CurvePoint id {} successfully deleted", id);
    return "redirect:/curvePoint/list";
  }

  /**
   * Handle ResourceNotFoundException and redirect to CurvePoint list view with an error message.
   *
   * @param e                  exception handled
   * @param redirectAttributes to add message to the view
   * @return View
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    LOGGER.info(e.getMessage());
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/curvePoint/list";
  }

}
