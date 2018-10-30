package com.as765.assignment.jsf;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Logs Out User Or Administrator
 * @author 105977
 */
@Named
@RequestScoped
public class LogoutBean {
    
    /**
     * Attempts To Logout The User Or Administrator
     * @return page representing the outcome of the logout attempt
     */
    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // attempt to logout the user or administrator
        try
        {
            // proceed with the logout attempt
            request.logout();
            
            // return to home page
            return "logout";
        }
        catch (ServletException e)
        {
            // notify user that logout has failed
            context.addMessage(null, new FacesMessage("Logout failed."));
            
            // stay on the same view
            return null;
        }
    }
}