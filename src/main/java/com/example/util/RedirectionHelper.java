package com.example.util;

import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by Tomasz Wąsik, tomaszwasik@live.com
 */
public class RedirectionHelper {

    public static String showControllerPage(Class controllerClass){

        final UriComponentsBuilder uriComponents = MvcUriComponentsBuilder.fromController(controllerClass);
        final String path = uriComponents.build().getPath();

        return "redirect:" + path;
    }
}
