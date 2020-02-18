package com.microsoft.azure.helium.app.controllers;

// import com.microsoft.applicationinsights.core.dependencies.apachecommons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
 
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/*  
@RestController
public class RobotsController {
    @RequestMapping(value={"/robots*.txt"}, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Mono<Resource> robots() {
        return fileRepository.findByName(fileName)
                 .map(name -> new FileSystemResource(name));
    }

    public void oldrobot(HttpServletResponse response) {

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

}*/
