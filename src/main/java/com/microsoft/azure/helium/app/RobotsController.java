package com.microsoft.azure.helium.app;

import com.microsoft.applicationinsights.core.dependencies.apachecommons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

@Controller
public class RobotsController {
    @RequestMapping(value={"/robots*.txt"}, method = RequestMethod.GET)
    public void robot(HttpServletResponse response) {

        InputStream resourceAsStream = null;
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            resourceAsStream = classLoader.getResourceAsStream("robots.txt");
            response.addHeader("Content-disposition", "filename=robots.txt");
            response.setContentType("text/plain");
            IOUtils.copy(resourceAsStream, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            System.out.println("Problem with displaying robot.txt " + e.toString());
        }
    }

}
