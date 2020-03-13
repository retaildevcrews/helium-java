package com.microsoft.cse.helium.app.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class Validator {

    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    /* Valid input: starts with 'nm' (case sensitive)
        followed by 5-9 digits
        parses to int > 0
    */
    private final String validActorRegex = "[nm]{2}[0-9]{5,9}";

    public Boolean validActorId(String actorId){
        Pattern p = Pattern.compile(validActorRegex);
        return p.matcher(actorId).matches();
    }

}
