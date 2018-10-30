package com.as765.assignment.jsf;

import com.as765.assignment.ejb.CurrencyConverterBean;
import com.as765.assignment.ejb.UserService;
import com.as765.assignment.entity.AppUser;
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
 * Bean To Accept/Reject Payment Requests
 * @author 105977
 */
@Named
@RequestScoped
public class PendingRequestBean {
   
    @EJB
    UserService userService;
    
    @EJB
    CurrencyConverterBean currencyConverter;
    // 1) format, convert and display requested amount in the logged in user's currency
    // 2) convert requested amount into currency of payment recipient

    public PendingRequestBean() {
    }
    
    /**
     * Obtain List Of Logged In User's Payment Requests
     * @return list of payment requests for logged in user
     */
    public List<UserTransaction> getPaymentRequests() {
        
         FacesContext context = FacesContext.getCurrentInstance();
        
        /* Gets The Username Of The Logged In User */
        String name = context.getExternalContext().getUserPrincipal().getName();
        
        return userService.getPaymentRequests(name); 
    }
    
    /**
     * Reject Payment Request
     * @param paymentRequestID ID of the payment request transaction
     * @return page notifying user that payment request was rejected
     */
    public String rejectPaymentRequest(Long paymentRequestID) {
        
        userService.rejectPaymentRequest(paymentRequestID);
        
        return "paymentRequestRejected";   
    }
    
    /**
     * Accept Payment Request
     * @param sender logged in user
     * @param reciever recipient of payment
     * @param amount amount requested which is going to be sent
     * @param paymentRequestID the id of the payment request transaction
     * @return page notifying user of the outcome of accepting the payment request
     */
    public String acceptPaymentRequest(String sender, String reciever, BigDecimal amount, Long paymentRequestID) {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        // check that the logged in user's balance is >= amount being sent
        if (userService.isSufficientBalance(sender, amount) == 1 | userService.isSufficientBalance(sender, amount) == 0)
        {
            // set the payment request to accepted
            // process the payment and obtain an outcome message
            String message = userService.acceptPaymentRequest(paymentRequestID,sender,reciever,amount);
                                  
            // if the payment was sucessfull then notify user that payment was successfull
            if (message.equals("paymentSent")) {
                
                return "paymentAccepted"; 
            }
            else
            {
                // otherwise error is due to concurrent access
                // notify user of error and tell them to retry the payment
                // return the same view
                context.addMessage(null, new FacesMessage("Payment Conflict Error! Please Retry Payment!"));
                return null;
            }
        } 
        else
        {
            // if the logged in user's balance <= amount being sent
            // notify user they have insufficient funds
            // return the same view
            context.addMessage(null, new FacesMessage("You Have Insufficient Funds!"));
            return null; 
        }
    }
    
    /**
     * Format The Requested Amount With The Relevant Currency Symbol
     * Converts The Requested Amount Into An Amount Equivalent To Logged In User's Currency When Required
     * @param amount amount to be formatted and converted
     * @param currency currency symbol to format to
     * @return formatted and possibly converted requested amount
     */
    public String format(BigDecimal amount, String currency) {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        /* Gets The Username Of The Logged In User */
        String name = context.getExternalContext().getUserPrincipal().getName();
        
        // get the logged in user
        AppUser user = userService.getUser(name);
        
        if (!currency.equals(user.getAccountCurrency()))
        {
            // convert the amount into the logged in user's currency 
            amount = currencyConverter.convert(currency, user.getAccountCurrency(), amount);
        }
        
        // determine the currency of the logged in user
        // add the relevant currency symbol
        String currencySymbol = null;
        
        if (user.getAccountCurrency().equals("GB Pounds"))
        {
            currencySymbol = "£";
        }
        
        if (user.getAccountCurrency().equals("Euros"))
        {
            currencySymbol = "€";
        }
        
        if (user.getAccountCurrency().equals("US Dollars"))
        {
            currencySymbol = "$";
        }
        
        return currencySymbol + amount.setScale(2, RoundingMode.DOWN).toString(); 
    }  
}