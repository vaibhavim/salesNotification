package com.sales.notification.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jms.config.JmsListenerEndpointRegistry;
import org.springframework.stereotype.Service;

import com.sales.notification.app.domain.CompletedSaleRecords;
import com.sales.notification.app.domain.SaleMessage;

/**
 * @author Vaibhavi
 *
 */
@Service
public class SalesService {

	@Autowired
	ApplicationContext context;

	@Autowired
	JmsListenerEndpointRegistry customRegistry;

	public void processMessage(SaleMessage saleMessage) {
		CompletedSaleRecords.addNewSaleRecord(saleMessage);
	}

	public void processAdjustmentMessage(SaleMessage saleMessage) {
		CompletedSaleRecords.performSaleOperation(saleMessage);
	}

	public String pauseSalesNotificationService() {
		customRegistry.getListenerContainer("salesJMSListener").stop();
		return "Pausing Sales Notification Service";
	}

	public boolean getListenerStatus()
	{
		return customRegistry.getListenerContainer("salesJMSListener").isRunning();
	}
	
	public String resumeSalesNotificationService() {
		customRegistry.getListenerContainer("salesJMSListener").start();
		;
		return "Resuming Sales Notification Service";
	}

}
