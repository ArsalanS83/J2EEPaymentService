package com.as765.assignment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 * Entity Representing a Payment Service User
 * @author 105977
 */
@NamedQueries({
    
    @NamedQuery(name="getUser", query="SELECT u FROM AppUser u WHERE u.user_id.username = :username"),
    @NamedQuery(name="getUserEmail", query="SELECT u FROM AppUser u WHERE u.emailAddress = :email"),
    @NamedQuery(name="updateBalance", query="UPDATE AppUser u SET u.balance =:newBalance WHERE u.emailAddress = :email"),
    @NamedQuery(name="getAllUsers", query="SELECT u FROM AppUser u"),
    @NamedQuery(name="getBalance", query = "SELECT u.balance FROM AppUser u WHERE u.user_id.username = :username"),
    @NamedQuery(name="getBalanceByEmail", query = "SELECT u.balance FROM AppUser u WHERE u.emailAddress = :email")
})
@Entity
public class AppUser implements Serializable {
    
    @Id
    @OneToOne
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JoinColumn(name="USER_ID")
    private SystemUser user_id;
    // 1 to 1 relationship with the table used for authorisation
    
    private String accountCurrency;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal balance;
    // ensure the balance is stored with a scale of 2 decimal places
    // max number of integers can be 10 digits long
    
    private String firstName;
    
    private String surname;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateOfBirth;
    
    private String emailAddress;
    
    private String telephoneNumber;
    
    @Version
    private Long version;
    // used for concurrent access control
    
    public AppUser() {  
    }

    /**
     * AppUser Constructor
     * @param user_id the id of the user
     * @param accountCurrency the currency of the user
     * @param balance the balance of the user
     * @param firstName the first name of the user
     * @param surname the surname of the user
     * @param dateOfBirth the date of birth of the user
     * @param emailAddress the email address of the user
     * @param telephoneNumber the phone number of the user
     */
    public AppUser(SystemUser user_id, String accountCurrency, BigDecimal balance, String firstName, String surname, Date dateOfBirth, String emailAddress, String telephoneNumber) {
        this.user_id = user_id;
        this.accountCurrency = accountCurrency;
        this.balance = balance;
        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.emailAddress = emailAddress;
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Get The User Object
     * @return SystemUser object representing the user
     */
    public SystemUser getUser() {
        return user_id;
    }

    /**
     * Set The User Object
     * @param user SystemUser object to set the user to
     */
    public void setUser(SystemUser user) {
        this.user_id = user;
    }

    /**
     * Get The User's Currency
     * @return the user's currency
     */
    public String getAccountCurrency() {
        return accountCurrency;
    }

    /**
     * Set The User's Currency
     * @param accountCurrency the currency to set
     */
    public void setAccountCurrency(String accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    /**
     * Get The User's Balance
     * @return balance of the user
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Set The User's Balance
     * @param balance the balance to set
     */
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    /**
     * Get The First Name Of The User
     * @return first name of the user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set The First Name Of The User
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get The Surname Of The User
     * @return surname of the user
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set The Surname Of The User
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Get The User's Date Of Birth
     * @return date of birth of the user
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set The User's Date Of Birth
     * @param dateOfBirth the date of birth to set
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    /**
     * Get The User's Email Address
     * @return email address of the user
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set The User's Email Address
     * @param emailAddress the email address to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Get The Telephone Number
     * @return telephone number of the user
     */
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    /**
     * Set The Telephone Number Of The User
     * @param telephoneNumber the telephone number to set
     */
    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    /**
     * Get The Version Number Of The AppUser Entity
     * @return version number of the AppUser entity
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Set The Version Number Of The AppUser Entity
     * @param version the version number to set
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.user_id);
        hash = 11 * hash + Objects.hashCode(this.accountCurrency);
        hash = 11 * hash + Objects.hashCode(this.balance);
        hash = 11 * hash + Objects.hashCode(this.firstName);
        hash = 11 * hash + Objects.hashCode(this.surname);
        hash = 11 * hash + Objects.hashCode(this.dateOfBirth);
        hash = 11 * hash + Objects.hashCode(this.emailAddress);
        hash = 11 * hash + Objects.hashCode(this.telephoneNumber);
        hash = 11 * hash + Objects.hashCode(this.version);
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
        final AppUser other = (AppUser) obj;
        if (!Objects.equals(this.accountCurrency, other.accountCurrency)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        if (!Objects.equals(this.emailAddress, other.emailAddress)) {
            return false;
        }
        if (!Objects.equals(this.telephoneNumber, other.telephoneNumber)) {
            return false;
        }
        if (!Objects.equals(this.user_id, other.user_id)) {
            return false;
        }
        if (!Objects.equals(this.balance, other.balance)) {
            return false;
        }
        if (!Objects.equals(this.dateOfBirth, other.dateOfBirth)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }
}