package com.as765.assignment.jsf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 * Converts Date String Into Object
 * Converts Date Object Into String
 * Please Note: This Code Is Taken From The Lab 4 JSFVisitorBook Application
 * @author University of Sussex
 */
@FacesConverter(value = "multiDateConverter")
public class MultiDateConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
        List<String> patterns = getPatterns(component);
        Date date = null;
        for (String pattern : patterns) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            try {
                date = sdf.parse(value);
                break;
            } catch (ParseException ignore) {
                System.out.println("could not match with pattern");
            }
        }
        if (date == null) {
            throw new ConverterException(new FacesMessage("Invalid date format, must match either of " + patterns)); // this one will be rendered by h:message tags referring to the date field
        }
        return date;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
        // use the first pattern to present the date to the user
        return new SimpleDateFormat(getPatterns(component).get(0)).format((Date) value);
    }

    private static List<String> getPatterns(UIComponent component) {
        List<String> patterns = new ArrayList<>();

        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            
            String pattern = (String) component.getAttributes().get("pattern" + i); // remember the name attribute of the f:attribute tag

            if (pattern != null) {
                patterns.add(pattern);
            } else {
                break;
            }
        }
        if (patterns.isEmpty()) {
            throw new IllegalArgumentException("Please provide <f:attribute name=\"patternX\"> where X is the order number");
        }

        return patterns;
    }
}