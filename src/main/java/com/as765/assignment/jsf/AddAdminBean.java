package com.as765.assignment.jsf;

import com.as765.assignment.ejb.AdminService;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Add The Desired Administrator into The Database
 * @author 105977
 */
@Named
@RequestScoped
public class AddAdminBean {
    
    @EJB
    AdminService adminService; 
    // used to call methods of EJB to perform add function
    // used to call validator methods to check if administrator already exists
    
    String username; // chosen username of administrator to add
    String adminpassword; // chosen passsword of administrator to add
    
    public AddAdminBean() {
    }

    /**
     * Bean Constructor
     * @param username desired username of administrator to add
     * @param adminpassword desired password of administrator to add
     */
    public AddAdminBean(String username, String adminpassword) {
        this.username = username;
        this.adminpassword = adminpassword;
    }

    /**
     * Obtain the Administrator's Username
     * @return Administrator's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the Administrator's Username
     * @param username The desired username to set to
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtain the Administrator's Password
     * @return Administrator's password
     */
    public String getAdminpassword() {
        return adminpassword;
    }

    /**
     * Set the Administrator's Password
     * @param adminpassword The desired password to set to
     */
    public void setAdminpassword(String adminpassword) {
        this.adminpassword = adminpassword;
    }
    
    /**
     * Adds the Administrator into the database
     * Calls backing EJB to add the administrator into the database
     * @return desired page to return to upon success or failure
     */
    public String add() {
        
        // check if the administrator exists in the database
        if (adminService.adminExists(username))
        {
            // return an error message to the same view if the admin exists
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("Admin Already Exists!"));
            return null; 
        }
        else
        {
            // otherwise proceed to add the administrator into the database
            // return to page notifying the administrator of a successfull add
            adminService.addAdmin(username,adminpassword);
            return "adminAdded";
        }
    }   
}