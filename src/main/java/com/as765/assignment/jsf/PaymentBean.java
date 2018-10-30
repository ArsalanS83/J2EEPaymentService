package com.as765.assignment.jsf;

import com.as765.assignment.ejb.UserService;
import com.as765.assignment.entity.AppUser;
import java.math.BigDecimal;
import java.util.Objects;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 * Process The Sending Of Payments
 * @author 105977
 */
@Named
@RequestScoped
public class PaymentBean {
    
    private String reciever; // the recipient's email address
    private BigDecimal amount; // the amount to send
    
    @EJB
    UserService userService;
    
    
    public PaymentBean() { 
    }

    /**
     * Payment Bean Constructor
     * @param reciever recipient's email address
     * @param amount amount to send
     * @param userService backing EJB service to use for payment processing
     */
    public PaymentBean(String reciever, BigDecimal amount, UserService userService) {
        this.reciever = reciever;
        this.amount = amount;
        this.userService = userService;
    }

    /**
     * Get The Recipient's Email Address
     * @return
     */
    public String getReciever() {
        return reciever;
    }

    /**
     * Set The Recipient's Email Address
     * @param reciever the email address to set the recipient to
     */
    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    /**
     * Get The Amount To Send
     * @return amount to send
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set The Amount To Send
     * @param amount amount to send
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Get The EJB Service Used To Process Payments
     * @return
     */
    public UserService getUserService() {
        return userService;
    }

    /**
     * Set The EJB Service Used To Process Payments
     * @param userService EJB To Set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.reciever);
        hash = 53 * hash + Objects.hashCode(this.amount);
        hash = 53 * hash + Objects.hashCode(this.userService);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PaymentBean other = (PaymentBean) obj;
        if (!Objects.equals(this.reciever, other.reciever)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        if (!Objects.equals(this.userService, other.userService)) {
            return false;
        }
        return true;
    }
    
    /**
     * Process The Payment
     * @return page representing outcome of payment processing
     */
    public String sendPayment() {
        
        FacesContext context = FacesContext.getCurrentInstance();
        
        // check if the user has entered an existing email address
        if (userService.isValidUserEmail(reciever)) {
            
            /* Gets The Username Of The Logged In User */
            String sender = context.getExternalContext().getUserPrincipal().getName();
            
            // is the sender's balance >= amount being sent
            if (userService.isSufficientBalance(sender, amount) == 1 | userService.isSufficientBalance(sender, amount) == 0)
            {
                // obtain the sender
                AppUser user = userService.getUser(sender);
                
                // check if the sender's email address does not equal the recipient's email address
                // Cannot send payment to yourself
                if (user.getEmailAddress().equals(reciever)) {
                    context.addMessage(null, new FacesMessage("Error Cannot Send Payment To Yourself!"));
                    return null;
                }
                else
                {
                    // otherwise process the payment
                    // obtain a message from the EJB Service
                    // message details the outcome of the payment processing
                    String message = userService.sendPayment(sender,reciever,amount);
                    
                    // if the payment was successfull navigate the user to the payment successfull page
                    if (message.equals("paymentSent")) {
                        return "paymentSent"; 
                    }
                    else
                    {
                        // otherwise notify the user that there was an error
                        // this error is used to prevent concurrent accessing of data
                        // return the same view
                        context.addMessage(null, new FacesMessage("Payment Conflict Error! Please Retry Payment!"));
                        return null;
                    }
                }
            }
            else
            {
               // if the sender's balance is <= amount being sent
               // then they cannot send the payment due to insufficient funds
               // return the same view
               context.addMessage(null, new FacesMessage("You Have Insufficient Funds!"));
               return null;
            } 
        }
        else
        {
            // if the recipient's email address does not exist in the database
            // notify the user and return the same view
            context.addMessage(null, new FacesMessage("Payment Recipient Does Not Exist!"));
            return null;  
        }
    }
}