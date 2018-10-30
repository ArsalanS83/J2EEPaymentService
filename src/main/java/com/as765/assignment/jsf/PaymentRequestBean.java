package com.as765.assignment.jsf;

import com.as765.assignment.ejb.UserService;
import com.as765.assignment.entity.AppUser;
import java.math.BigDecimal;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Process The Sending Of Payment Requests
 * @author 105977
 */
@Named
@RequestScoped
public class PaymentRequestBean {
    
    private String reciever; //recipient's email address
    private BigDecimal amount; // amount requested
    
    @EJB
    UserService userService;
    

    public PaymentRequestBean() {
    }

    /**
     * Payment Request Bean Constructor
     * @param reciever recipient's email address
     * @param amount amount requested
     * @param userService EJB Service to use for payment request processing
     */
    public PaymentRequestBean(String reciever, BigDecimal amount, UserService userService) {
        this.reciever = reciever;
        this.amount = amount;
        this.userService = userService;
    }
    
    /**
     * Send Payment Request
     * @return page detailing outcome of payment request
     */
    public String sendRequest () {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        /* Gets The Username Of The Logged In User */
        String sender = context.getExternalContext().getUserPrincipal().getName();
        
        // check if the recipient's email address exists in the database
        if (userService.isValidUserEmail(reciever)) {
            
            // obtain the sender
            AppUser user = userService.getUser(sender);
            
            // check the sender is not sending a request to themselves
            if (user.getEmailAddress().equals(reciever)) {
                
               // notify user of error and return the same view
               context.addMessage(null, new FacesMessage("Cannot Send Payment Request To Yourself!"));
               return null;  
            }
            else
            {
                // otherwise process the payment request
                // return page notifying user that payment request succeeded
                userService.sendPaymentRequest(sender,reciever,amount);
                return "paymentRequestSent";  
            }
        }
        else
        {
            // if the recipient's email address does not exist
            // notify user of error message
            // return the same view
            context.addMessage(null, new FacesMessage("Recipient Does Not Exist!"));
            return null;
        }
    }

    /**
     * Get The Recipient's Email Address
     * @return email address of recipient
     */
    public String getReciever() {
        return reciever;
    }

    /**
     * Set The Recipient's Email Address
     * @param reciever email address to set recipient to
     */
    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    /**
     * Get The Requested Amount
     * @return requested amount from sender
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set The Requested Amount
     * @param amount requested amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Get EJB Service Used To Process Payment Requests
     * @return EJB service used to process payment requests
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Set EJB Service Used To Process Payment Requests
     * @param userService EJB Service used to process payment requests
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }  
}