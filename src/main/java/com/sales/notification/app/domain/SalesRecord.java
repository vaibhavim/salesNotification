package com.sales.notification.app.domain;


import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Vaibhavi Malusare
 *
 */
@Getter
@Setter
public class SalesRecord {
    private String productType;
    private long productQuantity;
    private BigDecimal value;

    public SalesRecord(String productType, long productQuantity, BigDecimal value) {
        this.productType = productType;
        this.productQuantity = productQuantity;
        this.value = value;
    }
}
