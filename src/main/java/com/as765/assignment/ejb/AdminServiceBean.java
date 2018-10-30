package com.as765.assignment.ejb;

import com.as765.assignment.entity.AppUser;
import com.as765.assignment.entity.SystemUser;
import com.as765.assignment.entity.SystemUserGroup;
import com.as765.assignment.entity.UserTransaction;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Implements Administrative Functionality
 * @author 105977
 */
@DeclareRoles({"admins"})
@Stateless
public class AdminServiceBean implements AdminService {
    
    @PersistenceContext
    EntityManager em;

    @RolesAllowed("admins")
    @Override
    public void addAdmin(String username, String adminPassword) {
         
        try {

            SystemUser admin = new SystemUser();
            admin.setUsername(username);
            // set the administrator username to the chosen username
        
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = adminPassword;
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);
            // convert chosen plain text password into hashed password
            
            admin.setUserpassword(paswdToStoreInDB);
            // set the administrator's password to the hashed password
            
            SystemUserGroup sys_user_group = new SystemUserGroup(username, "admins");
            // store the administrator into the admins group
            
            em.persist(admin);
            em.persist(sys_user_group);
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

    @RolesAllowed("admins")
    @Override
    public List<AppUser> getAllUsers() {
        
        Query getAllUsers = em.createNamedQuery("getAllUsers");
        // run query to obtain all users from the database
        
        List users = getAllUsers.getResultList();
        // get the result of the query and return the list of users
        
        return users;
    }

    @RolesAllowed("admins")
    @Override
    public List<UserTransaction> getAllTransactions() {
        
        Query transactionsQuery = em.createNamedQuery("getAllTransactions");
        // run query to obtain all transactions from the database
        
        List transactions = transactionsQuery.getResultList();
        // get the result of the query and return the list of transactions
        
        return transactions;
    }

    @RolesAllowed("admins")
    @Override
    public boolean adminExists(String username) {

        Query query = em.createNamedQuery("getAdmin");
        // run query to get an administrator by their username
        
        query.setParameter("username",username);
        // set the administrator username
        
        // return true if an administrator with the specified username exists
        // return false otherwise
        
        return !query.getResultList().isEmpty();
    }
}