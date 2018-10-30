package com.as765.assignment.jsf;

import com.as765.assignment.ejb.AdminService;
import com.as765.assignment.entity.AppUser;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 * Get All Payment Service Users From The Database
 * @author 105977
 */
@Named
@RequestScoped
public class AdminViewUsersBean {
    
    @EJB
    AdminService adminService;
    
    public AdminViewUsersBean() {
    }
    
    /**
     * Obtain List Of All Payment Service Users
     * @return list of all payment service users
     */
    public List<AppUser> getUsers() {
        
        return adminService.getAllUsers();
        
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
    
    /**
     * Formats Date of Birth To The Desired dd//MM/yyyy pattern
     * @param dob date of birth object to format to
     * @return string representing the formatted date of birth
     */
    public String formatDateOfBirth(Date dob) {
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        return simpleDateFormat.format(dob);
    }
}