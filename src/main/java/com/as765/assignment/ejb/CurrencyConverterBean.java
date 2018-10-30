package com.as765.assignment.ejb;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

/**
 * Converts an Amount From A Specified Currency To Another
 * @author 105977
 */
@DeclareRoles("users")
@Stateless
public class CurrencyConverterBean {
    
    // Correct as of 14/07/2017
    private final double GBPToEuroRate = 1.17927;
    private final double EuroToGBPRate = 0.847946;
    private final double GBPToUSDRate = 1.25288;
    private final double USDToGBPRate = 0.798138;
    private final double EuroToUSDRate = 1.06241;
    private final double USDToEuroRate = 0.941260;
    
    public CurrencyConverterBean() {
    }
    
    /**
     * Converts an Amount From The Senders Currency To The Receiver's Currency
     * @param senderCurrency the currency of the sender
     * @param recieverCurrency the currency of the receiver
     * @param amount the amount to convert
     * @return converted amount
     */
    @RolesAllowed("users")
    public BigDecimal convert(String senderCurrency, String recieverCurrency,BigDecimal amount) {
        
        if (senderCurrency.equals("GB Pounds") && recieverCurrency.equals("Euros")) {
            
            BigDecimal exchangeRate = new BigDecimal(GBPToEuroRate);
            BigDecimal convertedAmount;
            convertedAmount = amount.multiply(exchangeRate).setScale(2,RoundingMode.DOWN);
            return convertedAmount;
        }
        
        if (senderCurrency.equals("Euros") && recieverCurrency.equals("GB Pounds")) {
            
            BigDecimal exchangeRate = new BigDecimal(EuroToGBPRate);
            BigDecimal convertedAmount;
            convertedAmount = amount.multiply(exchangeRate).setScale(2,RoundingMode.DOWN);
            return convertedAmount;
        }
        
        if (senderCurrency.equals("GB Pounds") && recieverCurrency.equals("US Dollars")) {
            
            BigDecimal exchangeRate = new BigDecimal(GBPToUSDRate);
            BigDecimal convertedAmount;
            convertedAmount = amount.multiply(exchangeRate).setScale(2,RoundingMode.DOWN);
            return convertedAmount;
        }
        
        if (senderCurrency.equals("US Dollars") && recieverCurrency.equals("GB Pounds")) {
            
            BigDecimal exchangeRate = new BigDecimal(USDToGBPRate);
            BigDecimal convertedAmount;
            convertedAmount = amount.multiply(exchangeRate).setScale(2,RoundingMode.DOWN);
            return convertedAmount;
        }
        
        if (senderCurrency.equals("US Dollars") && recieverCurrency.equals("Euros")) {
            
            BigDecimal exchangeRate = new BigDecimal(USDToEuroRate);
            BigDecimal convertedAmount;
            convertedAmount = amount.multiply(exchangeRate).setScale(2,RoundingMode.DOWN);
            return convertedAmount;
        }
        
        if (senderCurrency.equals("Euros") && recieverCurrency.equals("US Dollars")) {
            
            BigDecimal exchangeRate = new BigDecimal(EuroToUSDRate);
            BigDecimal convertedAmount;
            convertedAmount = amount.multiply(exchangeRate).setScale(2,RoundingMode.DOWN);
            return convertedAmount;
        }

        return null;
    }
}