package com.nnk.springboot.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller class implementing ErrorController for displaying custom error pages.
 */
@Controller
public class CustomErrorController implements ErrorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomErrorController.class);

  /**
   * Map the request error status and return the corresponding view.
   *
   * @param request with error status code
   * @return ModelAndView
   */
  @RequestMapping("/error")
  public ModelAndView handleError(HttpServletRequest request) {
    ModelAndView mav = new ModelAndView();
    Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    if (status != null) {
      int statusCode = Integer.parseInt(status.toString());
      LOGGER.info("Error {}: An error occurred.", statusCode);
      if (statusCode == HttpStatus.NOT_FOUND.value()) {
        mav.addObject("errorMsg", "The requested data are not found.");
        mav.setViewName("error/404");
        return mav;
      } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
        mav.addObject("errorMsg", "You are not authorized for the requested data.");
        mav.setViewName("error/403");
        return mav;
      }
      mav.addObject("errorCode", statusCode);
    }
    mav.setViewName("error/error");
    return mav;
  }

}
