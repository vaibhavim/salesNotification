package com.sales.notification.app.service;

import javax.jms.JMSException;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import com.sales.notification.app.SalesConfigurationProperties;
import com.sales.notification.app.SalesConfigurations;
import com.sales.notification.app.domain.CompletedSaleRecords;
import com.sales.notification.app.domain.SaleMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Vaibhavi This class is listener class to read messages from queue
 *         {@link SalesConfigurations#queue()}
 *
 */
@Service
@Slf4j
@RequiredArgsConstructor

public class SalesMessageListner {

	private final SalesConfigurationProperties salesConfigurationProperties;
	private final SalesService salesService;
	private static long messageCount = 0;

	/**
	 * This method receives messages from queue {@link SalesConfigurations#queue()}
	 * 
	 * @param saleMessage
	 * @throws JMSException
	 */
	@JmsListener(destination = "salesMessage.queue", containerFactory = "jmsFactory", id = "salesJMSListener")
	public void receieveSalesMessage(SaleMessage saleMessage) throws JMSException {

		messageCount++;
		log.info("Received Message {}", messageCount);

		if ("3".equals(saleMessage.getMessageType())) {
			salesService.processAdjustmentMessage(saleMessage);

		}

		else {
			if ("1".equals(saleMessage.getMessageType())) {
				saleMessage.setQuantity(1);
			}
			salesService.processMessage(saleMessage);
		}

		if (messageCount % salesConfigurationProperties.getReportInterval() == 0) {
			System.out.println("Next " + salesConfigurationProperties.getReportInterval()
					+ " message received, sales report till now..");
			CompletedSaleRecords.printTotalSalesRecordTillNow(salesConfigurationProperties.getReportDenomination());
		}

		if (messageCount == salesConfigurationProperties.getPauseInterval()) {
			System.out.println(salesConfigurationProperties.getPauseInterval()
					+ " messages received, adjustment report till now..");
			CompletedSaleRecords.printSalesAdjustmentsTillNow(salesConfigurationProperties.getReportDenomination());
			System.out.println(
					"Application is pausing and will no longer accept messages. Please use dedicated REST API to restart ");
			salesService.pauseSalesNotificationService();

		}

	}
}
