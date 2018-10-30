package com.as765.assignment.jsf;

import com.as765.assignment.ejb.UserService;
import com.as765.assignment.entity.AppUser;
import com.as765.assignment.entity.UserTransaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Bean Used On User's Home Page
 * 1) Displays Welcome Message To User
 * 2) Displays User's Balance
 * @author 105977
 */
@Named
@RequestScoped
public class WelcomeBean {
    
    @EJB
    UserService userService;
    
    
    public WelcomeBean() { 
    }
    
    /**
     * Gets The User's First Name and Surname From The Database
     * Displays "Welcome: FirstName Surname" Message
     * @return "welcome firstName surname" message
     */
    public String getAccountName() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        /* Gets The Username Of The Logged In User */
        String name = context.getExternalContext().getUserPrincipal().getName();
        
        // get the user based on the username
        AppUser u = userService.getUser(name);
        
        // obtain the user's first name and surname
        // concatenate with "Welcome:" string
        return "Welcome: " + u.getFirstName() + " " + u.getSurname();
    }
    
    /**
     * Gets The Account Username Of The Administrator
     * Used Because Administrator's Don't Have First Name and Surname In The Database
     * @return "welcome: adminUsername" message
     */
    public String getAccountUsername() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        /* Gets The Username Of The Logged In Administrator */
        String name = context.getExternalContext().getUserPrincipal().getName();
        
        return "Welcome: " + name; 
    }
    
    /**
     * Gets The User's Balance
     * @return string representing concatenated currency symbol and balance to 2 decimal places
     */
    public String displayBalance() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        /* Gets The Username Of The Logged In User */
        String name = context.getExternalContext().getUserPrincipal().getName();
        
        // get the user based on the username
        AppUser u = userService.getUser(name);
        
        
        // determine the approrpiate currency symbol to show for the user's balance
        String currencySymbol = null;
        
        if (u.getAccountCurrency().equals("GB Pounds"))
        {
            currencySymbol = "£";
        }
        
        if (u.getAccountCurrency().equals("Euros"))
        {
            currencySymbol = "€";
        }
        
        if (u.getAccountCurrency().equals("US Dollars"))
        {
            currencySymbol = "$";
        }
        
        // display the balance formatted to 2 decimal places with the currency symbol
        // e.g. if balance is £1000 then balance will be formatted to £1000.00
        return "Account Balance: " + currencySymbol + u.getBalance().setScale(2,RoundingMode.DOWN);
    }
    
    /**
     * Get Logged In User's Transactions
     * @return transactions of logged in user
     */
    public List<UserTransaction> getTransactions() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        /* Gets The Username Of The Logged In User */
        String name = context.getExternalContext().getUserPrincipal().getName();
        
        return userService.getUserTransactions(name);
    }
    
    /**
     * Formats The Amount of a User's Transaction
     * @param amount amount to format
     * @param currency currency to format to
     * @return formatted amount with currency symbol
     */
    public String format(BigDecimal amount, String currency) {
                
        String currencySymbol = null;
        
        if (currency.equals("GB Pounds"))
        {
            currencySymbol = "£";
        }
        
        if (currency.equals("Euros"))
        {
            currencySymbol = "€";
        }
        
        if (currency.equals("US Dollars"))
        {
            currencySymbol = "$";
        }
        
        if (amount == null)
        {
            return "";
        }
        else
        {
            return currencySymbol + amount.setScale(2, RoundingMode.DOWN).toString(); 
        }
    }   
}