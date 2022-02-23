package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.RatingService;
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
 * Controller class for displaying thymeleaf view of Rating management.
 */
@Controller
public class RatingController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

  @Autowired
  private RatingService ratingService;

  /**
   * Show a view displaying all the Ratings.
   *
   * @param model of the view
   * @return View
   */
  @RequestMapping("/rating/list")
  public String home(Model model) {
    LOGGER.info("Request list of all Ratings");
    model.addAttribute("ratings", ratingService.findAll());
    return "rating/list";
  }

  /**
   * Show a view with the add Rating form.
   *
   * @param ratingDto of the form
   * @return View
   */
  @GetMapping("/rating/add")
  public String addRatingForm(RatingDto ratingDto) {
    LOGGER.info("Request form to add Rating");
    return "rating/add";
  }

  /**
   * Validate the add Rating form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist Rating and return the Rating list view.
   *
   * @param ratingDto of the form
   * @param result    hold validation errors
   * @return View
   */
  @PostMapping("/rating/validate")
  public String validate(@Valid RatingDto ratingDto, BindingResult result) {
    LOGGER.info("Request validation on Rating adding");
    if (!result.hasErrors()) {
      ratingService.add(ratingDto);
      LOGGER.info("Rating successfully added");
      return "redirect:/rating/list";
    }
    LOGGER.info("Failed to add Rating, form contains errors");
    return "rating/add";
  }

  /**
   * Show a view with update Rating form prefilled with Rating current values.
   *
   * @param id    of the Rating to update
   * @param model of the view
   * @return View
   * @throws ResourceNotFoundException when the requested Rating not found
   */
  @GetMapping("/rating/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    LOGGER.info("Request form to update Rating id {}", id);
    model.addAttribute("ratingDto", ratingService.findById(id));
    return "rating/update";
  }

  /**
   * Validate the update Rating form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist Rating and return to Rating list view.
   *
   * @param id        of the Rating to update
   * @param ratingDto of the form
   * @param result    hold validation errors
   * @return View
   * @throws ResourceNotFoundException when the requested Rating not found
   */
  @PostMapping("/rating/update/{id}")
  public String updateRating(@PathVariable("id") Integer id, @Valid RatingDto ratingDto,
                             BindingResult result) throws ResourceNotFoundException {
    LOGGER.info("Request validation on Rating id {} update", id);
    if (!result.hasErrors()) {
      ratingDto.setId(id);
      ratingService.update(ratingDto);
      LOGGER.info("Rating id {} successfully updated", id);
      return "redirect:/rating/list";
    }
    LOGGER.info("Failed to update Rating id {}, form contains errors", id);
    return "rating/update";
  }

  /**
   * Request the deletion of a Rating and return to Rating list view.
   *
   * @param id of the Rating to delete
   * @return View
   * @throws ResourceNotFoundException when the requested Rating not found
   */
  @GetMapping("/rating/delete/{id}")
  public String deleteRating(@PathVariable("id") Integer id)
      throws ResourceNotFoundException {
    LOGGER.info("Request Rating id {} deletion", id);
    ratingService.delete(id);
    LOGGER.info("Rating id {} successfully deleted", id);
    return "redirect:/rating/list";
  }

  /**
   * Handle ResourceNotFoundException and redirect to Rating list view with an error message.
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
    return "redirect:/rating/list";
  }

}
