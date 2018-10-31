# J2EEPaymentService
Web based, multi-user payment service using Java Enterprise Edition (J2EE) technologies completed as part of my Web Applications and Services final year module at university.

Download the Project ZIP file in the repo which contains everything to run the application.

## Overview

The system is a much simplified version of PayPal. 

Through a JSF-based web interface, users can send money to other registered users using their registered email address as their unique identifier, request money from other registered users and manage their own account (look at their recent transactions). Super-users (i.e. admins) can access all user accounts and transactions. 

Each user has a single online account whose currency is selected upon registration. A user can select to have their account in GB Pounds, US dollars or Euros. All registered users start with 1000 of their chosen currency.

A user can instruct the system to make a direct payment to another user. If this request is accepted (i.e. the recipient of the payment exists and there are enough funds), money is transferred (within a single J2EE transaction) to the recipient immediately. A user can check for notifications regarding payments in their account.

A user can instruct the system to request payment from some other user. A user can then check about such notifications for requests for payment. They can reject the request, or, in response to it, make a payment to the requesting user.

Users can access all their transactions, that is, sent and received payments and requests for payments as well as their current account balance.

## Web Layer

The web layer consists of a set .xhtml (facelets) pages through which users and administrators interact with the web application. 

Users can:

* View all their transactions
* Make direct payments to other registered users
* Request payments from registered users

Administrators can:

* access user accounts
* access all payment transactions
* register new administrators

## Service Layer

The service layer consists of a set of Enterprise Java Beans (EJBs) which implement the business logic for the system. EJBs also support J2EE transactions so that data integrity is preserved. Container-managed transactions are utilisied so the code doesn't need to cope with opening, committing or roll-backing transactions. EJBs are annotated with the appropriate transaction attributes.

The service layer is responsible for accessing the data (persistence) layer. Persistence (JPA) entity managers are injected in  my EJBs. Access to persistent data only takes place through these entity managers.

## Data Layer

The data layer consists of a relational database and JPA entities. To simplify deployment and configuration I used JavaDB as my Relational Database Management System (RDBMS). JavaDB is an in-memory RDBMS which is installed with GlassFish.

My data model is written as a set of persistence entities (JPA). Upon deployment, JPA creates the actual relational database tables. Access to the database always takes place through manipulating JPA entities rather than accessing the database directly using JDBC.

## Security

A user must be logged-in in order to interact with the system. Users cannot see other users' information nor access pages and functionality for administrators. Administrators access their own set of pages, through which they have access to all users information. Users and administrators can logout from the web application.

I have implemented and supported:

* Communication on top of HTTPS for every interaction with users and admins
* Form-based authentication in a jdbcRealm where users can subscribe
* Logout functionality
* Declarative security to restrict access to web pages to non-authorised users
* Declarative security to restrict access to EJB methods


## Documentation

I have written a report critically assessing the strengths and limitations of this implementation which also includes the following:

* How my design fits with the 3-tier architectural model and the model-view-controller software pattern
* The strengths and weaknesses of my chosen methods for securing the application, comparing my approach to other options
* How my design could be extended so that my server is not a single point of failure
* How my system would deal with concurrent users accessing functionality and data


## Future Improvements

### Currency Conversion REST Service

I would like to implement a REST Service that is accessed by the service layer. I want the service to be deployed on the same server but accessed from the service layer in the standard way (i.e. through HTTP).

I want the currency conversion RESTful web service to respond only to GET requests. 

I want the resource to take the following path: 

baseURL/conversion/{currency1}/{currency2}/{amount_of_currency1}

The RESTful web service will return an HTTP response with the conversion rate (currency1 to currency2) or the appropriate HTTP status code if one or both of the provided currencies are not supported.

e.g. GET baseURL/conversion/{currency1}/{currency2} HTTP/1.1 should return a status ok response with a very simple response (e.g. in JSON that says that 1.00 GBP = 1.21217 EUR).


### Implementation of DAO and DTO Access Patterns

In real world applications, the service layer never accesses the persistence layer directly. Back-end storage resources may change (e.g. new relational or non-relational databases may be added) over time and the service layer code must be independent of such changes. The most common software pattern for implementing such independence is the DAO pattern. DTO objects are also used instead of moving entity objects across layers.

### Apache Thrift Timestamp service

I would like all transactions to be timestamped by accessing a 'remote' Thrift timestamp service (which is deployed on the same server as my system). The service should return the current date and time to my system when requested by the Enterprise Java Bean. I am thinking of implementing the Thrift server as a deployable EJB which uses a separate thread to accept time-stamping requests at port 10000.

### Deployment on AWS

Finally I would also like to deploy my application on AWS.

