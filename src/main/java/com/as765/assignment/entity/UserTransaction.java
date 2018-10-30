package com.as765.assignment.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 * Entity Representing a Payment Or Request Transaction
 * @author 105977
 */
@NamedQueries({
    @NamedQuery(name="getTransactions", query="SELECT t FROM UserTransaction t WHERE t.sender.user_id.username = :username OR t.reciever.user_id.username = :username"),
    @NamedQuery(name="getPaymentRequests", query="SELECT t FROM UserTransaction t WHERE t.reciever.user_id.username = :username AND t.transactionStatus = :status"),
    @NamedQuery(name="setPaymentRequestStatus", query="UPDATE UserTransaction t SET t.transactionStatus =:status WHERE t.transactionID = :id"),
    @NamedQuery(name="getAllTransactions", query="SELECT t FROM UserTransaction t")
})
@Entity
public class UserTransaction implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="TRANSACTION_ID")
    private Long transactionID;
    
    private String transactionType;
    
    @OneToOne
    private AppUser sender;
    // a transaction involves exactly 1 sender
    
    @OneToOne
    private AppUser reciever;
    // a transaction involves exactly 1 recipient
    
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;
    // represents the initial sent amount from the sender
    
    @Column(precision = 10, scale = 2)
    private BigDecimal convertedAmount;
    // represents the recieved amount after currency conversion 
    
    private String transactionStatus;
    // "Complete" status when transaction is a payment
    // "Requested" status when a transaction is a payment request
    // "Accepted" status when a payment request transction is accepted
    // "Rejected" status when a payment request transaction is rejected

    public UserTransaction() {   
    }

    /**
     * UserTransaction Constructor
     * @param transactionType type of transaction (payment request or payment)
     * @param sender sender of the transaction
     * @param reciever receiver of the transaction
     * @param amount amount of the transaction
     * @param transactionStatus status of the transaction
     */
    public UserTransaction(String transactionType, AppUser sender, AppUser reciever, BigDecimal amount, String transactionStatus) {
        this.transactionType = transactionType;
        this.sender = sender;
        this.reciever = reciever;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
    }

    /**
     * Get The Transaction Id
     * @return transaction id
     */
    public Long getTransactionID() {
        return transactionID;
    }

    /**
     * Set The Transaction iD
     * @param transactionID transaction id to set
     */
    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }

    /**
     * Get The Transaction Type
     * @return transaction type
     */
    public String getTransactionType() {
        return transactionType;
    }

    /**
     * Set The Transaction Type
     * @param transactionType transaction type to set
     */
    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Get The Sender Of The Transaction
     * @return sender of the transaction
     */
    public AppUser getSender() {
        return sender;
    }

    /**
     * Set The Sender Of The Transaction
     * @param sender sender of the transaction to set
     */
    public void setSender(AppUser sender) {
        this.sender = sender;
    }

    /**
     * Get The Receiver Of The Transaction
     * @return receiver of the transaction
     */
    public AppUser getReciever() {
        return reciever;
    }

    /**
     * Set The Receiver Of The Transaction
     * @param reciever
     */
    public void setReciever(AppUser reciever) {
        this.reciever = reciever;
    }

    /**
     * Get The Amount Sent By The Sender
     * @return amount sent by the sender
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set The Amount To Be Sent By The Sender
     * @param amount amount to be sent by the sender
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Get The Received Converted Amount
     * @return received converted amount
     */
    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    /**
     * Set The Received Converted Amount
     * @param convertedAmount received converted amount to set
     */
    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    /**
     * Get The Transaction Status
     * @return transaction status
     */
    public String getTransactionStatus() {
        return transactionStatus;
    }

    /**
     * Set The Transaction Status
     * @param transactionStatus transaction status to set
     */
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.transactionID);
        hash = 53 * hash + Objects.hashCode(this.transactionType);
        hash = 53 * hash + Objects.hashCode(this.sender);
        hash = 53 * hash + Objects.hashCode(this.reciever);
        hash = 53 * hash + Objects.hashCode(this.amount);
        hash = 53 * hash + Objects.hashCode(this.transactionStatus);
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
        final UserTransaction other = (UserTransaction) obj;
        if (!Objects.equals(this.transactionType, other.transactionType)) {
            return false;
        }
        if (!Objects.equals(this.transactionStatus, other.transactionStatus)) {
            return false;
        }
        if (!Objects.equals(this.transactionID, other.transactionID)) {
            return false;
        }
        if (!Objects.equals(this.sender, other.sender)) {
            return false;
        }
        if (!Objects.equals(this.reciever, other.reciever)) {
            return false;
        }
        if (!Objects.equals(this.amount, other.amount)) {
            return false;
        }
        return true;
    }
}