package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.RuleNameService;
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
 * Controller class for displaying thymeleaf view of RuleName management.
 */
@Controller
public class RuleNameController {

  private static final Logger LOGGER = LoggerFactory.getLogger(RuleNameController.class);

  @Autowired
  private RuleNameService ruleNameService;

  /**
   * Show a view displaying all the RuleNames.
   *
   * @param model of the view
   * @return View
   */
  @RequestMapping("/ruleName/list")
  public String home(Model model) {
    LOGGER.info("Request list of all RuleNames");
    model.addAttribute("ruleNames", ruleNameService.findAll());
    return "ruleName/list";
  }

  /**
   * Show a view with the add RuleName form.
   *
   * @param ruleNameDto of the form
   * @return View
   */
  @GetMapping("/ruleName/add")
  public String addRuleForm(RuleNameDto ruleNameDto) {
    LOGGER.info("Request add RuleName form");
    return "ruleName/add";
  }

  /**
   * Validate the add RuleName form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist RuleName and return the RuleName list view.
   *
   * @param ruleNameDto of the form
   * @param result      hold validation errors
   * @return View
   */
  @PostMapping("/ruleName/validate")
  public String validate(@Valid RuleNameDto ruleNameDto, BindingResult result) {
    LOGGER.info("Request add RuleName form validation");
    if (!result.hasErrors()) {
      ruleNameService.add(ruleNameDto);
      LOGGER.info("RuleName successfully added");
      return "redirect:/ruleName/list";
    }
    LOGGER.info("Failed to add RuleName, form contains errors");
    return "ruleName/add";
  }

  /**
   * Show a view with update RuleName form prefilled with RuleName current values.
   *
   * @param id    of the RuleName to update
   * @param model of the view
   * @return View
   * @throws ResourceNotFoundException when the requested RuleName not found
   */
  @GetMapping("/ruleName/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    LOGGER.info("Request update RuleName id {} form", id);
    model.addAttribute("ruleNameDto", ruleNameService.findById(id));
    return "ruleName/update";
  }

  /**
   * Validate the update RuleName form.
   * If form contains errors, show the form view to display fields validation errors.
   * Else, call the service layer to persist RuleName and return to RuleName list view.
   *
   * @param id          of the RuleName to update
   * @param ruleNameDto of the form
   * @param result      hold validation errors
   * @return View
   * @throws ResourceNotFoundException when the requested RuleName not found
   */
  @PostMapping("/ruleName/update/{id}")
  public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleNameDto ruleNameDto,
                               BindingResult result) throws ResourceNotFoundException {
    LOGGER.info("Request update RuleName id {} form validation", id);
    if (!result.hasErrors()) {
      ruleNameDto.setId(id);
      ruleNameService.update(ruleNameDto);
      LOGGER.info("RuleName id {} successfully updated", id);
      return "redirect:/ruleName/list";
    }
    LOGGER.info("Failed to update RuleName id {}, form contains errors", id);
    return "ruleName/update";
  }

  /**
   * Request the deletion of a RuleName and return to RuleName list view.
   *
   * @param id of the RuleName to delete
   * @return View
   * @throws ResourceNotFoundException when the requested RuleName not found
   */
  @GetMapping("/ruleName/delete/{id}")
  public String deleteRuleName(@PathVariable("id") Integer id)
      throws ResourceNotFoundException {
    LOGGER.info("Request RuleName id {} deletion", id);
    ruleNameService.delete(id);
    LOGGER.info("RuleName id {} successfully deleted", id);
    return "redirect:/ruleName/list";
  }

  /**
   * Handle ResourceNotFoundException and redirect to RuleName list view with an error message.
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
    return "redirect:/ruleName/list";
  }

}
