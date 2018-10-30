package com.as765.assignment.entity;

import com.as765.assignment.entity.AppUser;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-18T01:51:07")
@StaticMetamodel(UserTransaction.class)
public class UserTransaction_ { 

    public static volatile SingularAttribute<UserTransaction, String> transactionType;
    public static volatile SingularAttribute<UserTransaction, BigDecimal> amount;
    public static volatile SingularAttribute<UserTransaction, BigDecimal> convertedAmount;
    public static volatile SingularAttribute<UserTransaction, AppUser> sender;
    public static volatile SingularAttribute<UserTransaction, AppUser> reciever;
    public static volatile SingularAttribute<UserTransaction, String> transactionStatus;
    public static volatile SingularAttribute<UserTransaction, Long> transactionID;

}