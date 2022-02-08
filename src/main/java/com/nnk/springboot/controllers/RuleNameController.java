package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleNameDto;
import com.nnk.springboot.exceptions.ResourceNotFoundException;
import com.nnk.springboot.services.RuleNameService;
import javax.validation.Valid;
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
public class RuleNameController {

  @Autowired
  private RuleNameService ruleNameService;

  @RequestMapping("/ruleName/list")
  public String home(Model model) {
    model.addAttribute("ruleNames", ruleNameService.findAll());
    return "ruleName/list";
  }

  @GetMapping("/ruleName/add")
  public String addRuleForm(RuleNameDto ruleNameDto) {
    return "ruleName/add";
  }

  @PostMapping("/ruleName/validate")
  public String validate(@Valid RuleNameDto ruleNameDto, BindingResult result, Model model) {
    if (!result.hasErrors()) {
      ruleNameService.add(ruleNameDto);
      return "redirect:/ruleName/list";
    }
    return "ruleName/add";
  }

  @GetMapping("/ruleName/update/{id}")
  public String showUpdateForm(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    model.addAttribute("ruleNameDto", ruleNameService.findById(id));
    return "ruleName/update";
  }

  @PostMapping("/ruleName/update/{id}")
  public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleNameDto ruleNameDto,
                               BindingResult result, Model model) throws ResourceNotFoundException {
    if (!result.hasErrors()) {
      ruleNameDto.setId(id);
      ruleNameService.update(ruleNameDto);
      return "redirect:/ruleName/list";
    }
    return "ruleName/update";
  }

  @GetMapping("/ruleName/delete/{id}")
  public String deleteRuleName(@PathVariable("id") Integer id, Model model)
      throws ResourceNotFoundException {
    ruleNameService.delete(id);
    return "redirect:/ruleName/list";
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public String handleResourceNotFoundException(ResourceNotFoundException e,
                                                RedirectAttributes redirectAttributes) {
    redirectAttributes.addFlashAttribute("error", e.getMessage());
    return "redirect:/ruleName/list";
  }

}
