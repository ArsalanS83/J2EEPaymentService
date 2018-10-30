package com.as765.assignment.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;

/**
 * Entity Representing All Usernames And All Passwords
 * The Entity Is Also Used To Represent Administrators
 * @author 105977
 */
@NamedQuery(name="getAdmin", query="SELECT u FROM SystemUser u WHERE u.username = :username")
@Entity
public class SystemUser implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;
            
    @NotNull
    @Column(unique=true)
    private String username;
    // ensures usernames must be unique
    
    @NotNull
    private String userpassword;
    
    
    public SystemUser() {  
    }

    /**
     * SystemUser Constructor
     * @param username username of the user
     * @param userpassword password of the user
     */
    public SystemUser(String username, String userpassword) {
        this.username = username;
        this.userpassword = userpassword;
    }

    /**
     * Get The Id Of The User
     * @return id of the user
     */
    public Long getId() {
        return id;
    }

    /**
     * Set The Id Of The User
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get The Username Of The User
     * @return username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set The Username Of The User
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get The Password Of The User
     * @return password of the user
     */
    public String getUserpassword() {
        return userpassword;
    }

    /**
     * Set The Password Of The User
     * @param userpassword password to set
     */
    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.username);
        hash = 79 * hash + Objects.hashCode(this.userpassword);
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
        final SystemUser other = (SystemUser) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.userpassword, other.userpassword)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}