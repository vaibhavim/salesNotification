package com.sales.notification.app;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.sales.notification.app.domain.ReportDenomination;

import lombok.Data;

/**
 * @author Vaibhavi Malusare The class contains properties required for
 *         application
 */
@ConfigurationProperties(prefix = "sales")
@Data

public class SalesConfigurationProperties {

	private int reportInterval;
	private int pauseInterval;
	private ReportDenomination reportDenomination;

}
