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
public class SaleMessage {

	/**
	 * Message Type 1. Single_Sale : contains the details of 1 sale E.g apple at10p
	 * 2. Multiple_Sale : contains the details of a sale and the number of
	 * occurrences of that sale. E.g 20 sales of apples at 10p each 3. *
	 * Adjustment_Sale : contains the details of a sale and an adjustment operation
	 * to be applied to all stored sales of this product type.
	 * 
	 * Expected Values - 1,2,3
	 * 
	 */
	
	private String messageType;

	private String productType;

	private long quantity = 1;

	private BigDecimal value;

	/**
	 * Required only if messageType = 3
	 * 
	 * Expected Values - "add" , "subtract" , "multiply"
	 */
	private String operationType;

	public SaleMessage(String messageType, String productType, long quantity, BigDecimal value, String operationType) {
		super();
		this.messageType = messageType;
		this.productType = productType;
		this.quantity = quantity;
		this.value = value;
		this.operationType = operationType;
	}

	public SaleMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

}
