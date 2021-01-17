package com.sales.notification.app.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import com.sales.notification.app.domain.CompletedSaleRecords;
import com.sales.notification.app.domain.SaleMessage;
import com.sales.notification.app.service.SalesService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SalesNotificationApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private SalesService saleService;

	@Test
	public void testPauseSalesListner() {

		this.restTemplate.getForObject("http://localhost:" + port + "/rest/pauseSalesNotificationService",
				String.class);
		assertFalse(saleService.getListenerStatus());
		this.restTemplate.getForObject("http://localhost:" + port + "/rest/resumeSalesNotificationService",
				String.class);
		assertTrue(saleService.getListenerStatus());
	}

	@Test
	public void testSalesNotifyApp() throws IOException {

		Files.readAllLines(Paths.get("src/test/resources/salesMessages.txt")).forEach(eachline -> {
			String arr[] = eachline.split(",");
			SaleMessage saleMessage = new SaleMessage(arr[0], arr[1], Integer.parseInt(arr[2]),
					BigDecimal.valueOf(Double.valueOf(arr[3])), arr[4]);

			this.restTemplate.postForObject("http://localhost:" + port + "/rest/publish", saleMessage, String.class);

		});

		assertEquals(13, CompletedSaleRecords.getSalesProductCount());

	}

}
