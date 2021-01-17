package com.sales.notification.app;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sales.notification.app.domain.SaleMessage;
import com.sales.notification.app.service.SalesService;

/**
 * @author Vaibhavi 
 * This class is controller class and methods are exposed to
 *         outside world
 *
 */
@RestController
@RequestMapping("/rest")

public class SalesNotificationController {

	@Autowired
	private Queue queue;

	@Autowired
	private JmsTemplate jmstemplate;

	@Autowired
	private SalesService salesService;

	/**
	 * This rest call receives different types of SaleMessages and send message to
	 * queue * {@link SalesConfigurations#queue() bean }
	 * 
	 * @param saleMessage
	 * @return String
	 */
	@PostMapping("/publish")
	public String publish(@RequestBody SaleMessage saleMessage) {
		jmstemplate.convertAndSend(queue, saleMessage);
		return "Published Succesfully";

	}

	/**
	 * This rest call is used to pause the Salesnotification service when message
	 * count is {@link SalesConfigurationProperties#getPauseInterval()}
	 * 
	 * @return String
	 */
	@GetMapping("/pauseSalesNotificationService")
	public String pauseSalesNotificationService() {
		return salesService.pauseSalesNotificationService();
	}

	/**
	 * This rest call is used to restart the Salesnotification service *
	 * 
	 * @return String
	 */

	@GetMapping("/resumeSalesNotificationService")
	public String resumeSalesNotificationService() {
		return salesService.resumeSalesNotificationService();
	}

}
