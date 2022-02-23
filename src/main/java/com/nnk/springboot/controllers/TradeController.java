package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.TradeService;
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
 * Controller class for displaying thymeleaf view of Trade management.
 */
@Controller
public class TradeController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

  @Autowired
  private TradeService tradeService;

  /**
   * Show a view displaying all the Trades.
   *
   * @param model of the view
   * @return View
   */
  @RequestMapping("/trade/list")
  public String home(Model model) {
    LOGGER.info("Request list of all Trades");
    model.addAttribute("trades", tradeService.findAll());
    return "trade/list";
  }

  /**
   * Show a view with the add Trade form.
   *
   * @param tradeDto of the form
   * @return View
   */
  @GetMapping("/trade/add")
  public String addUser(TradeDto tradeDto) {
    LOGGER.info("Request form to add Trade");
    return "trade/add";
  }

  /**
   * Validate the add Trade form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist Trade and return the Trade list view.
   *
   * @param tradeDto of the form
   * @param result   hold validation errors
   * @return View
   */
  @PostMapping("/trade/validate")
  public String validate(@Valid TradeDto tradeDto, BindingResult result) {
    LOGGER.info("Request validation on Trade adding");
    if (!result.hasErrors()) {
      tradeService.add(tradeDto);
      LOGGER.info("Trade successfully added");
      return "redirect:/trade/list";
    }
    LOGGER.info("Failed to add Trade, form contains errors");
    return "trade/add";
  }

  /**
   * Show a view with update Trade form prefilled with Trade current values.
   *
   * @param id    of the Trade to update
   * @param model of the view
   * @return View
   * @throws ResourceNotFoundException when the requested Trade not found
   */
  @GetMapping("/trade/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    LOGGER.info("Request form to update Trade id {}", id);
    model.addAttribute("tradeDto", tradeService.findById(id));
    return "trade/update";
  }

  /**
   * Validate the update Trade form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist Trade and return to Trade list view.
   *
   * @param id       of the Trade to update
   * @param tradeDto of the form
   * @param result   hold validation errors
   * @return View
   * @throws ResourceNotFoundException when the requested Trade not found
   */
  @PostMapping("/trade/update/{id}")
  public String updateTrade(@PathVariable("id") Integer id, @Valid TradeDto tradeDto,
                            BindingResult result) throws ResourceNotFoundException {
    LOGGER.info("Request validation on Trade id {} update", id);
    if (!result.hasErrors()) {
      tradeDto.setTradeId(id);
      tradeService.update(tradeDto);
      LOGGER.info("Trade id {} successfully updated", id);
      return "redirect:/trade/list";
    }
    LOGGER.info("Failed to update Trade id {}, form contains errors", id);
    return "trade/update";
  }

  /**
   * Request the deletion of a Trade and return to Trade list view.
   *
   * @param id of the Trade to delete
   * @return View
   * @throws ResourceNotFoundException when the requested Trade not found
   */
  @GetMapping("/trade/delete/{id}")
  public String deleteTrade(@PathVariable("id") Integer id)
      throws ResourceNotFoundException {
    LOGGER.info("Request Trade id {} deletion", id);
    tradeService.delete(id);
    LOGGER.info("Trade id {} successfully deleted", id);
    return "redirect:/trade/list";
  }

  /**
   * Handle ResourceNotFoundException and redirect to Trade list view with an error message.
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
    return "redirect:/trade/list";
  }

}
