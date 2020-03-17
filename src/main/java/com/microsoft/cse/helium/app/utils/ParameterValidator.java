package com.microsoft.cse.helium.app.utils;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class ParameterValidator {

    /* Valid input: starts with 'nm' (case sensitive)
        followed by 5-9 digits
        parses to int > 0
    */
    private final static String  validActorRegex = "[nm]{2}[0-9]{5,9}";

    public Boolean isValidActorId(String actorId){
        Pattern p = Pattern.compile(validActorRegex);
        return p.matcher(actorId).matches();
    }

    public Boolean isValidSearchQuery(String query){
        Boolean isValid = true;
        if ( query.length() < 2 || query.length() > 20){
            isValid = false;
        }
        return isValid;
    }

    public Boolean isValidPageNumber(String pageNumber){
        Boolean isValid = true;
        try {
            Integer pageNo = Integer.parseInt(pageNumber);
            if (pageNo < 1 || pageNo > 10000)
                isValid = false;
        }catch (Exception ex){
            isValid = false;
        }
        return isValid;
    }

    public Boolean isValidPageSize(String pageSize){
        Boolean isValid = true;
        try {
            Integer pageSz = Integer.parseInt(pageSize);
            if (pageSz < 1 || pageSz > 1000)
                isValid = false;
        }catch (Exception ex){
            isValid = false;
        }
        return isValid;
    }

}
