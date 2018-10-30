package com.as765.assignment.entity;

import com.as765.assignment.entity.SystemUser;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-18T01:51:07")
@StaticMetamodel(AppUser.class)
public class AppUser_ { 

    public static volatile SingularAttribute<AppUser, String> firstName;
    public static volatile SingularAttribute<AppUser, String> emailAddress;
    public static volatile SingularAttribute<AppUser, String> telephoneNumber;
    public static volatile SingularAttribute<AppUser, BigDecimal> balance;
    public static volatile SingularAttribute<AppUser, SystemUser> user_id;
    public static volatile SingularAttribute<AppUser, String> surname;
    public static volatile SingularAttribute<AppUser, String> accountCurrency;
    public static volatile SingularAttribute<AppUser, Date> dateOfBirth;
    public static volatile SingularAttribute<AppUser, Long> version;

}