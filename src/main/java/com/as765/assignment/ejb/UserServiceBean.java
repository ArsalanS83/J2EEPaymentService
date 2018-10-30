package com.as765.assignment.ejb;

import com.as765.assignment.entity.AppUser;
import com.as765.assignment.entity.SystemUser;
import com.as765.assignment.entity.SystemUserGroup;
import com.as765.assignment.entity.UserTransaction;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Implements Payment Service User Functionality
 * @author 105977
 */
@DeclareRoles({"users","admins"})
@Stateless
public class UserServiceBean implements UserService {
    
    @PersistenceContext
    EntityManager em;
    
    @EJB
    CurrencyConverterBean currencyConverter;
    // used to convert currencies when sending payments and receiving payment requests
    
    public UserServiceBean() {
    }
    
    @Override
    public void register(String firstName, String surname, Date dateOfBirth, String username, String userpassword, String emailAddress, String phoneNumber, String accountCurrency)
    {
        try {
            
            SystemUser sys_user;
            SystemUserGroup sys_user_group;
            AppUser app_user;

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = userpassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);
            // convert chosen plain text password into hashed password
            
            BigDecimal balance = new BigDecimal(1000.00);
            // initialise balance of 1000.000

            sys_user = new SystemUser(username,paswdToStoreInDB);
            sys_user_group = new SystemUserGroup(username, "users");
            // create a SystemUser with the specified username and the hashed password
            // ensure the created user's username is stored in the "users" group
            
            app_user = new AppUser(sys_user,accountCurrency,balance,firstName,surname,dateOfBirth,emailAddress,phoneNumber);
            // set registration details for the AppUser
            
            em.persist(sys_user);
            em.persist(sys_user_group);
            em.persist(app_user);
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public boolean checkUserExists(String username) {
        
        Query query = em.createNamedQuery("getUser");
        // run query to get an AppUser by their username
        
        query.setParameter("username",username);
        // set the username of the user as the parameter
        
        return !query.getResultList().isEmpty();
    }
    
    @Override
    public boolean isValidUserEmail(String email) {
        
        Query query = em.createNamedQuery("getUserEmail");
        // run query to get an AppUser by their email address
        
        query.setParameter("email",email);
        // set the email address of the user as the parameter
        
        return !query.getResultList().isEmpty();   
    }
    
    @RolesAllowed("users")
    @Override
    public int isSufficientBalance(String username, BigDecimal amount) {
        
        Query query = em.createNamedQuery("getBalance");
        // run query to get an AppUser's balance by their username
        
        query.setParameter("username",username);
        // set the username of the user as the parameter
        
        BigDecimal balance = (BigDecimal) query.getSingleResult();
        // obtain the balance in order to compare it with the amount
        
        return balance.compareTo(amount);   
    }
    
    @RolesAllowed("users")
    @Override
    public int isSufficientBalanceByEmail(String email, BigDecimal amount) {
        
        Query query = em.createNamedQuery("getBalanceByEmail");
        // run query to get an AppUser's balance by their email address
        
        query.setParameter("email",email);
        // set the email address of the user as the parameter
        
        BigDecimal balance = (BigDecimal) query.getSingleResult();
        // obtain the balance in order to compare it with the amount
        
        return balance.compareTo(amount);   
    }
    
    @RolesAllowed("users")
    @Override
    public AppUser getUser(String username) {

        Query query = em.createNamedQuery("getUser");
        // run query to get an AppUser by their username
        
        query.setParameter("username",username);
        // set the username of the user as the parameter
        
        AppUser u = (AppUser) query.getSingleResult();
        // obtain the AppUser object
                
        return u; 
    }
    
    @RolesAllowed("users")
    @Override
    public String sendPayment(String name, String reciever, BigDecimal amount) {
        
        try {
            
        // obtain AppUser object of the sender
        Query senderQuery = em.createNamedQuery("getUser");     
        senderQuery.setParameter("username",name);
        AppUser sender = (AppUser) senderQuery.getSingleResult();
        
        // obtain AppUser object of the reciever
        Query recieverQuery = em.createNamedQuery("getUserEmail");   
        recieverQuery.setParameter("email",reciever);
        AppUser recipient = (AppUser) recieverQuery.getSingleResult();
        
        // create a new user transaction representing the payment
        // set the type of transaction to payment
        // set the sender and recipient
        // set the amount to the amount sent by the sender
        // set the status of the transaction to complete
        UserTransaction payment = new UserTransaction("Payment",sender,recipient,amount,"Complete");
              
        // if the sender's currency does not equal the recipient's currency
        if (!sender.getAccountCurrency().equals(recipient.getAccountCurrency()))
        {
            // convert the payment amount from the sender's currency to the recipient's currency
            BigDecimal convertedAmount = currencyConverter.convert(sender.getAccountCurrency(), recipient.getAccountCurrency(), amount);
            
            // set the converted amount field in the payment object to the converted amount
            payment.setConvertedAmount(convertedAmount);
            
            Query updateRecieverBalance = em.createNamedQuery("updateBalance");
        
            // obtain the balance of the recipient
            // add the converted amount to the recipients balance
            // update the recipients balance to this new balance using an update query 
            BigDecimal balance = recipient.getBalance();
            BigDecimal newBalance = balance.add(convertedAmount);
            updateRecieverBalance.setParameter("newBalance",newBalance);  
            updateRecieverBalance.setParameter("email", recipient.getEmailAddress());
            updateRecieverBalance.executeUpdate();
            
            Query updateSenderBalance = em.createNamedQuery("updateBalance");
        
            // obtain the balance of the sender
            // subtract the payment amount sent by the sender from the senders balance
            // update the senders balance to this new balance using an update query
            BigDecimal senderBalance = sender.getBalance();
            BigDecimal newSenderBalance = senderBalance.subtract(amount);
            updateSenderBalance.setParameter("newBalance",newSenderBalance);
            updateSenderBalance.setParameter("email",sender.getEmailAddress());
            updateSenderBalance.executeUpdate();
        }
        else
        {
            // if the currencies are equal do not perform a conversion
            
            // set the converted amount to be exactly the same as the amount sent by the sender
            // this means no conversion occured
            payment.setConvertedAmount(amount);
            
            Query updateRecieverBalance = em.createNamedQuery("updateBalance");
        
            // obtain the balance of the recipient
            // add the amount sent by the sender to the recipients balance
            // update the recipients balance to this new balance using an update query 
            BigDecimal balance = recipient.getBalance();
            BigDecimal newBalance = balance.add(amount);
            updateRecieverBalance.setParameter("newBalance",newBalance);  
            updateRecieverBalance.setParameter("email", recipient.getEmailAddress());
            updateRecieverBalance.executeUpdate();
            
            Query updateSenderBalance = em.createNamedQuery("updateBalance");
        
            // obtain the balance of the sender
            // subtract the payment amount sent by the sender from the senders balance
            // update the senders balance to this new balance using an update query
            BigDecimal senderBalance = sender.getBalance();
            BigDecimal newSenderBalance = senderBalance.subtract(amount);
            updateSenderBalance.setParameter("newBalance",newSenderBalance);
            updateSenderBalance.setParameter("email",sender.getEmailAddress());
            updateSenderBalance.executeUpdate(); 
        }
        
        em.persist(payment);
        em.flush();
        
        return "paymentSent";
        
        }
        
        // in case of concurrent access to balances
        // Optimistic Locking is in place to catch an exception should a conflict occur
        catch(EJBTransactionRolledbackException e) {
            
            return "conflict";  
        }
    }
     
    @RolesAllowed({"users","admins"})
    @Override
    public List<UserTransaction> getUserTransactions(String name) {
        
        Query transactionsQuery = em.createNamedQuery("getTransactions");
        // run query to get all user's transactions using their username
              
        transactionsQuery.setParameter("username",name);
        // set the username of the user as the parameter
        
        List transactions = transactionsQuery.getResultList();
        // obtain the users transactions
        
        return transactions;
    }

    @RolesAllowed("users")
    @Override
    public void sendPaymentRequest(String name, String reciever, BigDecimal amount) {
        
        Query senderQuery = em.createNamedQuery("getUser");
        // run query to get the sender's AppUser object
              
        senderQuery.setParameter("username",name);
        // set the parameter as the username of the logged in user
        
        AppUser sender = (AppUser) senderQuery.getSingleResult();
        // obtain the AppUser object of the sender
        
        Query recieverQuery = em.createNamedQuery("getUserEmail");
        // run query to get the receivers AppUser object
              
        recieverQuery.setParameter("email",reciever);
        // set the parameter as the email address of the receiver 
        
        AppUser recipient = (AppUser) recieverQuery.getSingleResult();
        // obtain the AppUser object of the receiver
        
        // create a payment request
        // set the transaction type to payment request
        // set the sender and reciever of the payment request
        // set the requested amount
        // set the transaction status to requested
        UserTransaction paymentRequest = new UserTransaction("Payment Request",sender,recipient,amount,"Requested");
        
        em.persist(paymentRequest); 
    }
    
    @RolesAllowed("users")
    @Override
    public List<UserTransaction> getPaymentRequests(String name) {
        
        Query paymentRequestsQuery = em.createNamedQuery("getPaymentRequests");
        // run query to obtain payment requests of a user
              
        paymentRequestsQuery.setParameter("status","Requested");
        // set the parameter of the transaction status to requested
        // this is to ensure all payment requests that have not been accepted or rejected are obtained
        
        paymentRequestsQuery.setParameter("username",name);
        // set the username of the user as the parameter
        
        List paymentRequests = paymentRequestsQuery.getResultList();
        // obtain the payment requests for the specified user
        
        return paymentRequests;
    }

    @RolesAllowed("users")
    @Override
    public void rejectPaymentRequest(Long paymentRequestID) {
        
        Query rejectPaymentRequest = em.createNamedQuery("setPaymentRequestStatus");
        // run query to change the status of a payment request transaction
        
        rejectPaymentRequest.setParameter("status","Rejected");
        // set the parameter of the new status of the transaction as "rejected"
        
        rejectPaymentRequest.setParameter("id",paymentRequestID);
        // set the id of the payment request to reject to
        
        // set the specified payment request's status to rejected
        rejectPaymentRequest.executeUpdate();
    }

    @RolesAllowed("users")
    @Override
    public String acceptPaymentRequest(Long paymentRequestID, String sender, String reciever, BigDecimal amount) {
        
        try {
            
            Query acceptPaymentRequest = em.createNamedQuery("setPaymentRequestStatus");
            // run query to change the status of a payment request transaction
        
            acceptPaymentRequest.setParameter("status","Accepted");
            // set the parameter of the new status of the transaction as "accepted"
        
            acceptPaymentRequest.setParameter("id",paymentRequestID);
            // set the id of the payment request to reject to
        
            // set the specified payment request's status to accepted
            acceptPaymentRequest.executeUpdate();
        
            // obtain AppUser object of the sender
            Query senderQuery = em.createNamedQuery("getUser");     
            senderQuery.setParameter("username",sender);
            AppUser paymentSender = (AppUser) senderQuery.getSingleResult();
        
            // obtain AppUser object of the reciever
            Query recieverQuery = em.createNamedQuery("getUserEmail");   
            recieverQuery.setParameter("email",reciever);
            AppUser recipient = (AppUser) recieverQuery.getSingleResult();
            
            // define a payment user transaction
            UserTransaction payment;
            
            // if ogged in user's currency doesn't equal currency of user who sent payment request 
            if (!paymentSender.getAccountCurrency().equals(recipient.getAccountCurrency()))
            {
                   
            // convert amount from payment requester's currency to the logged in user's currency
            BigDecimal convertedAmount = currencyConverter.convert(recipient.getAccountCurrency(),paymentSender.getAccountCurrency(), amount);
            
            // create a new user transaction representing the payment
            // set the type of transaction to payment
            // set the sender and recipient
            // set the amount to the amount to be taken out of the sender's account in the sender's currency
            // set the status of the transaction to complete
            payment = new UserTransaction("Payment",paymentSender,recipient,convertedAmount,"Complete");
        
            // convert the converted amount back into the payment requester's currency
            BigDecimal convertedRequestedAmount = currencyConverter.convert(paymentSender.getAccountCurrency(), recipient.getAccountCurrency(), convertedAmount);
            
            // set the converted amount field in the payment object
            // this amount is convertedd from the logged in user's currency...
            // to the currency of the user who sent the payment request
            payment.setConvertedAmount(convertedRequestedAmount);
            
            Query updateRecieverBalance = em.createNamedQuery("updateBalance");
        
            // obtain the balance of the recipient
            // add the converted amount to the recipients balance
            // update the recipients balance to this new balance using an update query 
            BigDecimal balance = recipient.getBalance();
            BigDecimal newBalance = balance.add(convertedRequestedAmount);
            updateRecieverBalance.setParameter("newBalance",newBalance);  
            updateRecieverBalance.setParameter("email", recipient.getEmailAddress());
            updateRecieverBalance.executeUpdate();
            
            Query updateSenderBalance = em.createNamedQuery("updateBalance");
        
            // obtain the balance of the sender
            // subtract the payment amount sent by the sender from the senders balance
            // note this payment amount has been converted into the logged in user's currency
            // update the senders balance to this new balance using an update query
            BigDecimal senderBalance = paymentSender.getBalance();
            BigDecimal newSenderBalance = senderBalance.subtract(convertedAmount);
            updateSenderBalance.setParameter("newBalance",newSenderBalance);
            updateSenderBalance.setParameter("email",paymentSender.getEmailAddress());
            updateSenderBalance.executeUpdate();
            
            }
            else
            {
                // otherwise user is accepting a payment request from an account matching their currency
                // create a new user transaction representing the payment
                // set the type of transaction to payment
                // set the sender and recipient
                // set the amount to the amount sent by the sender
                // set the status of the transaction to complete
                payment = new UserTransaction("Payment",paymentSender,recipient,amount,"Complete");
                
                // the converted amount is the same as the amount sent by the sender
                // this is because both the sender and reciever's currencies are the same
                payment.setConvertedAmount(amount);
                
                Query updateRecieverBalance = em.createNamedQuery("updateBalance");
                
                // obtain the balance of the recipient
                // add the converted amount to the recipients balance
                // update the recipients balance to this new balance using an update query 
                BigDecimal balance = recipient.getBalance();
                BigDecimal newBalance = balance.add(amount);
                updateRecieverBalance.setParameter("newBalance",newBalance);  
                updateRecieverBalance.setParameter("email", recipient.getEmailAddress());
                updateRecieverBalance.executeUpdate();
            
                Query updateSenderBalance = em.createNamedQuery("updateBalance");
        
                // obtain the balance of the sender
                // subtract the payment amount sent by the sender from the senders balance
                // update the senders balance to this new balance using an update query
                BigDecimal senderBalance = paymentSender.getBalance();
                BigDecimal newSenderBalance = senderBalance.subtract(amount);
                updateSenderBalance.setParameter("newBalance",newSenderBalance);
                updateSenderBalance.setParameter("email",paymentSender.getEmailAddress());
                updateSenderBalance.executeUpdate(); 
            }
            
            em.persist(payment);
            em.flush();
            
            return "paymentSent";  

        }
        
        // in case of concurrent access to balances
        // Optimistic Locking is in place to catch an exception should a conflict occur
        catch (EJBTransactionRolledbackException e) {
            
            return "conflict";

        }  
    }

    @RolesAllowed("users")
    @Override
    public AppUser getUserByEmail(String email) {
        
        Query recieverQuery = em.createNamedQuery("getUserEmail");
        // run query to get an AppUser by their email address
              
        recieverQuery.setParameter("email",email);
        // set the email address of the user as the parameter
        
        AppUser recipient = (AppUser) recieverQuery.getSingleResult();
        // obtain the AppUser object representing the reciever
        
        return recipient;
    }   
}