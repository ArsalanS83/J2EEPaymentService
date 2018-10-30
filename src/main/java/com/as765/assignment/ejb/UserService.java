package com.as765.assignment.ejb;

import com.as765.assignment.entity.AppUser;
import com.as765.assignment.entity.UserTransaction;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * Determines A Payment Service User's Functionality
 * @author 105977
 */
@Local
public interface UserService {
    
    /**
     * Register A Payment Service User
     * @param firstName first name to be registered
     * @param surname surname to be registered
     * @param dateOfBirth date of birth to be registered
     * @param username username to be registered
     * @param userpassword password to be registered
     * @param emailAddress email address to be registered
     * @param phoneNumber phone number to be registered
     * @param accountCurrency currency of account to be registered
     */
    public void register(String firstName, String surname, Date dateOfBirth, String username, String userpassword, String emailAddress,
                         String phoneNumber, String accountCurrency);
    
    /**
     * Check If A Payment Service User Exists
     * @param username username of user to check
     * @return true if a user exists with the specified username, false otherwise
     */
    public boolean checkUserExists(String username);
    
    /**
     * Check If A Payment Service User Exists
     * @param email email address of user to check
     * @return true if a user exists with the specified email address, false otherwise
     */
    public boolean isValidUserEmail(String email);
    
    /**
     * Check If A Payment Service User Has Sufficient Funds
     * @param username username of user to check
     * @param amount payment amount to be sent
     * @return -1 if balance is less than amount, 0 if they are equal, 1 if balance is greater than the amount
     */
    public int isSufficientBalance(String username, BigDecimal amount);
    
    /**
     * Check If A Payment Service User Has Sufficient funds
     * @param email email address of user to check
     * @param amount payment amount to be sent
     * @return -1 if balance is less than amount, 0 if they are equal, 1 if balance is greater than the amount
     */
    public int isSufficientBalanceByEmail(String email, BigDecimal amount);

    /**
     * Get A Payment Service User
     * @param username username of user
     * @return the relevant AppUser entity
     */
    public AppUser getUser(String username);

    /**
     * Process Sending Of A Payment
     * @param name username of sender
     * @param reciever email address of receiver
     * @param amount amount to send
     * @return payment successful outcome or payment conflict outcome
     */
    public String sendPayment(String name, String reciever, BigDecimal amount);

    /**
     * Get A User's Transactions
     * @param name username of user
     * @return list of user's transactions
     */
    public List<UserTransaction> getUserTransactions(String name);

    /**
     * Send A Payment Request
     * @param name username of sender
     * @param reciever email address of receiver
     * @param amount amount requested
     */
    public void sendPaymentRequest(String name, String reciever, BigDecimal amount);

    /**
     * Get All Payment Requests Of A User
     * @param name username of user
     * @return list of user's payment requests
     */
    public List<UserTransaction> getPaymentRequests(String name);

    /**
     * Reject A Payment Request
     * @param paymentRequestID id of payment request transaction
     */
    public void rejectPaymentRequest(Long paymentRequestID);

    /**
     * Accept A Payment Request
     * @param paymentRequestID id of payment request transaction
     * @param sender username of payment request recipient
     * @param reciever email address of sender of payment request
     * @param amount requested amount in the currency of the payment request sender
     * @return payment successful outcome or payment conflict outcome 
     */
    public String acceptPaymentRequest(Long paymentRequestID, String sender, String reciever, BigDecimal amount);
    
    /**
     * Get A User By Their Email Address
     * @param email email address of user
     * @return the relevant AppUser entity
     */
    public AppUser getUserByEmail(String email);
}