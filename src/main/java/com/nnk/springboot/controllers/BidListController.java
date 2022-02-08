package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.BidListService;
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
 * Controller class for displaying thymeleaf view of BidList management.
 */
@Controller
public class BidListController {

  private static final Logger LOGGER = LoggerFactory.getLogger(BidListController.class);

  @Autowired
  BidListService bidListService;

  /**
   * Show a view displaying all the BidLists.
   *
   * @param model of the view
   * @return View
   */
  @RequestMapping("/bidList/list")
  public String home(Model model) {
    LOGGER.info("Request list of all BidLists");
    model.addAttribute("bidLists", bidListService.findAll());
    return "bidList/list";
  }

  /**
   * Show a view with add BidList form.
   *
   * @param bidListDto of the form
   * @return View
   */
  @GetMapping("/bidList/add")
  public String addBidForm(BidListDto bidListDto) {
    LOGGER.info("Request add BidList form");
    return "bidList/add";
  }

  /**
   * Validate the add BidList form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist BidList and return to BidList list view.
   *
   * @param bidListDto of the form
   * @param result     hold validation errors
   * @return View
   */
  @PostMapping("/bidList/validate")
  public String validate(@Valid BidListDto bidListDto, BindingResult result) {
    LOGGER.info("Request add BidList form validation");
    if (!result.hasErrors()) {
      bidListService.add(bidListDto);
      LOGGER.info("BidList successfully added");
      return "redirect:/bidList/list";
    }
    LOGGER.info("Failed to add BidList, form contains errors");
    return "bidList/add";
  }

  /**
   * Show a view with update BidList form prefilled with BidList current values.
   *
   * @param id    of the BidList to update
   * @param model of the view
   * @return View
   * @throws ResourceNotFoundException when the requested BidList not found
   */
  @GetMapping("/bidList/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    LOGGER.info("Request update BidList id {} form", id);
    model.addAttribute("bidListDto", bidListService.findById(id));
    return "bidList/update";
  }

  /**
   * Validate the update BidList form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist BidList and return to BidList list view.
   *
   * @param id         of the BidList to update
   * @param bidListDto of the form
   * @param result     hold validation errors
   * @return View
   * @throws ResourceNotFoundException when the requested BidList not found
   */
  @PostMapping("/bidList/update/{id}")
  public String updateBid(@PathVariable("id") Integer id, @Valid BidListDto bidListDto,
                          BindingResult result)
      throws ResourceNotFoundException {
    LOGGER.info("Request update BidList id {} form validation", id);
    if (!result.hasErrors()) {
      bidListDto.setBidListId(id);
      bidListService.update(bidListDto);
      LOGGER.info("BidList id {} successfully updated", id);
      return "redirect:/bidList/list";
    }
    LOGGER.info("Failed to update BidList id {}, form contains errors", id);
    return "bidList/update";
  }

  /**
   * Request the deletion of a BidList and return to BidList list view.
   *
   * @param id of the BidList to delete
   * @return View
   * @throws ResourceNotFoundException when the requested Bidlist not found
   */
  @GetMapping("/bidList/delete/{id}")
  public String deleteBid(@PathVariable("id") Integer id)
      throws ResourceNotFoundException {
    LOGGER.info("Request BidList id {} deletion", id);
    bidListService.delete(id);
    LOGGER.info("BidList id {} successfully deleted", id);
    return "redirect:/bidList/list";
  }

  /**
   * Handle ResourceNotFoundException and redirect to BidList list view with an error message.
   *
   * @param e                  exception handled
   * @param redirectAttributes to add error message to the view
   * @return View
   */
  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    LOGGER.info(e.getMessage());
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/bidList/list";
  }

}
