package ru.otus.spring.controllers.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomizeErrorController implements ErrorController {
    private final static Logger logger = LoggerFactory.getLogger(CustomizeErrorController.class);

    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    public ResponseEntity<ErrorMessage> error(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            HttpStatus httpStatus = HttpStatus.resolve((int) status);
            logger.error("error {}", httpStatus);
            assert httpStatus != null;
            ErrorMessage errorMessage = new ErrorMessage(httpStatus.value(), httpStatus.name());
            return new ResponseEntity<>(errorMessage, httpStatus);
        }
        logger.error("unknown error");
        ErrorMessage errorMessage = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknown error");
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

