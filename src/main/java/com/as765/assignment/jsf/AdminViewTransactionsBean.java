package com.as765.assignment.jsf;

import com.as765.assignment.ejb.AdminService;
import com.as765.assignment.entity.UserTransaction;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Get All User Transactions In The Database
 * @author 105977
 */
@Named
@RequestScoped
public class AdminViewTransactionsBean {
    
     @EJB
     AdminService adminService;
     
     public AdminViewTransactionsBean() {
     }
     
    /**
     * Obtain List of all User Transactions
     * @return list of user transactions
     */
    public List<UserTransaction> getTransactions() {
         
         return adminService.getAllTransactions();

     }
     
    /**
     * Formats the Amount by adding the relevant currency symbol
     * @param amount amount to format
     * @param currency currency to format to
     * @return formated currency or empty string if no transaction exists
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