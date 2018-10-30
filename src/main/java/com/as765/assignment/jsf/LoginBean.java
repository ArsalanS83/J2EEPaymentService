package com.as765.assignment.jsf;

import com.as765.assignment.ejb.UserService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * Attempts To Login The User Or Administrator
 * @author 105977
 */
@Named
@RequestScoped
public class LoginBean {
    
    private String username;
    private String password;
    
    
    @EJB
    UserService userService;

    public LoginBean() {
    }

    /**
     * Bean Constructor
     * @param username username to login
     * @param password password to login
     */
    public LoginBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get The Username Used For Logging In
     * @return username used for logging in
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the Username Used For Logging In
     * @param username the desired username to be used for logging in
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get The Password Used For Logging In
     * @return password used for logging in
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the Password Used For Logging In
     * @param password the desired password to be used for logging in
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Logs In The User Or Administrator
     * Authenticates The Login Process
     * @return desired page relating to the outcome of the login attempt
     */
    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        
        // print attempted login username and login password to console
        // used for debugging purposes
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        
        
        // attempt to login the user or administrator
        try
        {
            request.login(this.username, this.password);
        }
        catch (ServletException e)
        {
            // If the password was incorrect or the username does not exist
            // notify user of eror by returning an error page
            return "error";

        }
        
        // print attempted URI to the console
        // used for debugging purposes
        System.out.println(request.getRequestURI());
        
        // check the role of the logged in user for security purposes
        // return the appropriate page based on the logged in users's role
        if (context.getExternalContext().isUserInRole("users"))
        {
            return "home";
        }
        else
        {
            return "adminHome";
        }
    }   
}