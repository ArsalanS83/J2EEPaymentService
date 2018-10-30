package com.as765.assignment.jsf;

import com.as765.assignment.ejb.UserService;
import java.util.Date;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Bean For Registering Users
 * @author 105977
 */
@Named
@RequestScoped
public class RegistrationBean {
    
    @EJB
    UserService userService;
    
    // details of user attemtping to register
    String firstName;
    String surname;
    Date dateOfBirth;
    String username;
    String userpassword;
    String confirmPassword;
    String emailAddress;
    String phoneNumber;
    String accountCurrency;

    public RegistrationBean() {
    }

    /**
     * Registration Bean Constructor
     * @param userService EJB service to perform registration of user
     * @param firstName chosen first name
     * @param surname chosen surname
     * @param dateOfBirth chosen date of birth
     * @param username chosen username
     * @param userpassword chosen password in plain text
     * @param emailAddress chosen email address
     * @param phoneNumber chosen phone number
     * @param accountCurrency selected account currency
     */
    public RegistrationBean(UserService userService, String firstName, String surname, Date dateOfBirth, String username, String userpassword, String emailAddress, String phoneNumber, String accountCurrency) {
        this.userService = userService;
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.userpassword = userpassword;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.accountCurrency = accountCurrency;
    }

    /**
     * Get The EJB Service Used To Perform Registration
     * @return EJB service used to perform registration
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Set The EJB Service Used To Perform Registration
     * @param userService EJB service used to perform registration
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get Chosen First Name
     * @return chosen first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set Chosen First Name
     * @param firstName chosen first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get Chosen Surname
     * @return chosen surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set Chosen Surname
     * @param surname chosen surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Get Chosen Username
     * @return chosen username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set Chosen Username
     * @param username chosen username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get Chosen Date of Birth
     * @return chosen date of birth
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set Chosen Date Of Birth
     * @param dateOfBirth chosen date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    /**
     * Get Chosen Password
     * @return chosen password
     */
    public String getUserpassword() {
        return userpassword;
    }

    /**
     * Set Chosen Password
     * @param userpassword chosen password
     */
    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    /**
     * Get Confirmed Password
     * @return the confirmed password
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Set Confirmed Password
     * @param confirmPassword the confirmed password
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    /**
     * Get Chosen Email Address
     * @return chosen email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set Chosen Email Address
     * @param emailAddress chosen email address
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Get Chosen Phone Number
     * @return chosen phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set Chosen Phone Number
     * @param phoneNumber chosen phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get Chosen Account Currency
     * @return chosen account currency
     */
    public String getAccountCurrency() {
        return accountCurrency;
    }

    /**
     * Set Chosen Account Currency
     * @param accountCurrency chosen account currency
     */
    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }
    
    /**
     * Register A User
     * @return page detailing outcome of registration attempt
     */
    public String register() {
        
        // Check if username is not already in the database    
        if (userService.checkUserExists(username))
        {
            // notify user of error if user already exists and return the same view
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("User Already Exists!"));
            return null;
        }
        else
        {
            // otherwise check if the password and password confirmation fields match
            if (!validatePassword(userpassword,confirmPassword))
            {
                // notify user of error if they don't match and return the same view
                FacesContext context = FacesContext.getCurrentInstance();
                context.addMessage(null, new FacesMessage("Passwords Do Not Match!"));
                return null;
            }
            else
            {
                // if the password and confirmation password match
                // check if the email address does not already exist in the database
                if (userService.isValidUserEmail(emailAddress)) {
                    
                    // notify user of error message if the email address exists and return the same view
                    FacesContext context = FacesContext.getCurrentInstance();
                    context.addMessage(null, new FacesMessage("Email Address Already Exists!")); 
                    return null;
                }
                else
                {
                    // otherwise register the user
                    // return the user's welcome page
                    userService.register(firstName,surname,dateOfBirth,username,userpassword,emailAddress,phoneNumber,accountCurrency);
                    return "welcome";    
                }
            }
        }    
    }
    
    /**
     * Determine If Password and Confirmation Password Fields Match
     * @param password entered password
     * @param confirmPassword entered password confirmation
     * @return true if they match, false if they do not match
     */
    public boolean validatePassword(String password, String confirmPassword) {
        
        return password.equals(confirmPassword);     
    }
}