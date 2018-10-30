package com.as765.assignment.ejb;

import com.as765.assignment.entity.SystemUser;
import com.as765.assignment.entity.SystemUserGroup;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Loads Initial Administrator Into The Database
 * @author 105977
 */
@Singleton
@Startup
public class AdminLoadBean {
    
    @PersistenceContext
    EntityManager em;
    
    public AdminLoadBean() {   
    }
    
    /**
     * Load The Administrator Into The Database Upon Construction
     */
    @PostConstruct
    public void loadAdmin() {
        
        try {
            SystemUser admin = new SystemUser();
            admin.setUsername("admin1");
            // set the username of the administrator to admin1
        
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String passwd = "admin1";
            md.update(passwd.getBytes("UTF-8"));
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String paswdToStoreInDB = bigInt.toString(16);
            // convert plain text password into hashed password
            
            admin.setUserpassword(paswdToStoreInDB);
            // set the administrator's password to the hashed password
            
            SystemUserGroup sys_user_group = new SystemUserGroup("admin1", "admins");
            // store the administrator into the admins group
            
            em.persist(admin);
            em.persist(sys_user_group);
            
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}