package com.as765.assignment.jsf;

import com.as765.assignment.ejb.UserService;
import com.as765.assignment.entity.UserTransaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Get A Particular User's Sent and Received Transactions
 * Used By The Administrator
 * @author 105977
 */
@Named
@RequestScoped
public class AdminUserTransactionsBean {
    
    String username; // username of user to search for
    List<UserTransaction> transactions; // transactions of selected user
    
    @EJB
    UserService userService;

    public AdminUserTransactionsBean() {
    }

    /**
     * User Transaction Bean
     * @param username username of selected user
     * @param transactions transactions of selected user
     * @param userService EJB service to use to obtain list of selected user's transactions
     */
    public AdminUserTransactionsBean(String username, List<UserTransaction> transactions, UserService userService) {
        this.username = username;
        this.transactions = transactions;
        this.userService = userService;
    }

    /**
     * Get Username Of Selected User
     * @return username of selected user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set Username Of Selected User
     * @param username username of selected user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get Transactions Of Selected User
     * @return transactions of selected user
     */
    public List<UserTransaction> getTransactions() {
        return transactions;
    }

    /**
     * Set Transactions Of Selected User
     * @param transactions list of selected user's transactions
     */
    public void setTransactions(List<UserTransaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Get EJB Service Used To Get Selected User's Transactions
     * @return EJB service used to obtain transactions of selected user
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Set EJB Service Used To Get Selected User's Transactions
     * @param userService EJB service to obtain transactions of selected user
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    /**
     * Search Database For Selected User's Transactions
     */
    public void search() {
        
        // check if the selected username exists in the database
        if (userService.checkUserExists(username))
        {
            // get the selected user's transactions
            List<UserTransaction> userTransactions = userService.getUserTransactions(username);
            transactions = userTransactions;
        }
        else
        {
            // otherwise if the username does not exist
            // notify the administrator of the error and return the same view
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage("User Does Not Exist!"));
        }
    }
    
    /**
     * Formats An Amount In The Selected User's Transaction
     * @param amount amount to format
     * @param currency currency symbol to format to
     * @return formatted amount
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