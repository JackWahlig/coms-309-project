package com.example.crossfire.system;

/**
 * @author Zane Seuser
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.servlet.error.ErrorController;

@RestController
public class SystemController implements ErrorController{

    private static final String PATH = "/error";

    @RequestMapping(value = PATH)
    public String handleError() {
        return "An error occurred, and you were routed through the handleError() method.";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
