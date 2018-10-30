package com.as765.assignment.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Entity Representing All Users And Their Respective Groups
 * @author 105977
 */
@Entity
public class SystemUserGroup implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String groupname;
    
    public SystemUserGroup() {
    }

    /**
     * SystemUserGroup Constructor
     * @param username username of the user
     * @param groupname group name of the user
     */
    public SystemUserGroup(String username, String groupname) {
        this.username = username;
        this.groupname = groupname;
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
     * Get The Group Name Of The User
     * @return group name of the user
     */
    public String getGroupname() {
        return groupname;
    }

    /**
     * Set The Group Name Of The User
     * @param groupname the group name of the user to set
     */
    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        hash = 59 * hash + Objects.hashCode(this.username);
        hash = 59 * hash + Objects.hashCode(this.groupname);
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
        final SystemUserGroup other = (SystemUserGroup) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.groupname, other.groupname)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
}