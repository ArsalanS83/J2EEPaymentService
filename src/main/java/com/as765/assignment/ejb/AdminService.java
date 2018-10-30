package com.as765.assignment.ejb;

import com.as765.assignment.entity.AppUser;
import com.as765.assignment.entity.UserTransaction;
import java.util.List;
import javax.ejb.Local;

/**
 * Determines Administrator Functionality
 * @author 105977
 */
@Local
public interface AdminService {
    
    /**
     * Add an Administrator into The Database
     * @param username username of administrator to add
     * @param adminPassword password of administrator to add
     */
    public void addAdmin(String username, String adminPassword);

    /**
     * Get All Payment Service Users
     * @return
     */
    public List<AppUser> getAllUsers();

    /**
     * Get All Payment Service Transactions
     * @return payment service transactions
     */
    public List<UserTransaction> getAllTransactions();
    
    /**
     * Check Whether An Administrator Exists In The Database
     * @param username username of administrator to check
     * @return true if administrator exists, false otherwise
     */
    public boolean adminExists(String username);
}