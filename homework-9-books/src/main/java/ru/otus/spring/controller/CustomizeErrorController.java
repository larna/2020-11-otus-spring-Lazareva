package ru.otus.spring.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomizeErrorController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/error";
    }
    @GetMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public String handleError(HttpServletRequest request, Model model) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String message = "";
        if (status != null) {
            HttpStatus httpStatus = HttpStatus.resolve((int) status);
            switch (httpStatus) {
                case BAD_REQUEST:
                    message = "Bad request...";
                    break;
                case NOT_FOUND:
                    message = "Resource not found";
                    break;
                case INTERNAL_SERVER_ERROR:
                    message = "Internal error";
                    break;
                default:
                    message = "Unknown error";
                    break;
            }
        }
        model.addAttribute("message", message);
        return "error";
    }

}

