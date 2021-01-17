package com.sales.notification.app.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.sales.notification.app.SalesConfigurationProperties;

/**
 * 
 * @author Vaibhavi Malusare
 * 
 *         This class add and adjust the sales records
 */

public class CompletedSaleRecords {

	private static ConcurrentHashMap<String, List<SalesRecord>> productSalesRecord = new ConcurrentHashMap<>();
	private static List<SaleMessage> asListAdjustedRecords = new ArrayList<SaleMessage>();
	private static List<SaleMessage> asListNotAdjustedRecords = new ArrayList<SaleMessage>();
	private static final BigDecimal CONVERT_TO_POUND_FACTOR = new BigDecimal(100);

	/**
	 * 
	 * This method adds new sale record to map productSalesRecord which is received
	 * from queue
	 * 
	 * @param saleMessage - SaleMessage
	 * 
	 */

	public static void addNewSaleRecord(SaleMessage saleMessage) {
		/**
		 * If message type is 2 i.e. contains the details of a sale and the number of
		 * occurrences of that sale
		 */
		if ("2".equals(saleMessage.getMessageType())) {
			saleMessage.setValue(saleMessage.getValue().multiply(new BigDecimal(saleMessage.getQuantity())));

		}
		SalesRecord rec = new SalesRecord(saleMessage.getProductType(), saleMessage.getQuantity(),
				saleMessage.getValue());

		if (productSalesRecord.containsKey(rec.getProductType())) {
			productSalesRecord.get(rec.getProductType()).add(rec);
		} else {
			List<SalesRecord> salesRecordList = new ArrayList<>();
			salesRecordList.add(rec);
			productSalesRecord.put(rec.getProductType(), salesRecordList);
		}
	}

	/**
	 * 
	 * This method performs the adjustment on the sale. This method is called when
	 * message type is 3
	 * 
	 * @param saleMessage - SaleMessage
	 * 
	 */

	public static void performSaleOperation(SaleMessage saleMessage) {

		BigDecimal value = saleMessage.getValue();
		String productType = saleMessage.getProductType();
		String salesOperation = saleMessage.getOperationType();

		/**
		 * If adjustment is received for existing sale
		 * 
		 */
		if (productSalesRecord.containsKey(productType)) {

			for (Map.Entry<String, List<SalesRecord>> productEntrySet : productSalesRecord.entrySet()) {

				if (productEntrySet.getKey().equalsIgnoreCase(productType)) {

					List<SalesRecord> asList = new ArrayList<SalesRecord>();
					for (SalesRecord salesRecord : productEntrySet.getValue()) {

						switch (salesOperation) {
						case "add":
							salesRecord.setValue(salesRecord.getValue()
									.add(value.multiply(new BigDecimal(salesRecord.getProductQuantity()))));

							asList.add(salesRecord);
							break;
						case "subtract":

							salesRecord.setValue(salesRecord.getValue()
									.subtract(value.multiply(new BigDecimal(salesRecord.getProductQuantity()))));

							asList.add(salesRecord);
							break;
						case "multiply":

							salesRecord.setValue(salesRecord.getValue()
									.multiply(value.multiply(new BigDecimal(salesRecord.getProductQuantity()))));
							asList.add(salesRecord);
							break;
						default:

							System.out.println("Invalid Sales Operation");
							asListNotAdjustedRecords.add(saleMessage);
							break;
						}
						productEntrySet.setValue(asList);

					}

				}

			}
			asListAdjustedRecords.add(saleMessage);

		}

		/**
		 * If adjustment is received for non existing sale record
		 */
		else {
			asListNotAdjustedRecords.add(saleMessage);

		}

	}

	/**
	 * This method generates report at interval when message count reaches
	 * {@link SalesConfigurationProperties#getReportInterval()}
	 */
	public static void printTotalSalesRecordTillNow(ReportDenomination reportDenomination) {
		System.out.format("%32s%32s%32s\n", "ProductType", "TotalQuantity", "TotalSale");
		for (Map.Entry<String, List<SalesRecord>> productEntrySet : productSalesRecord.entrySet()) {
			BigDecimal totalVal = new BigDecimal(0);
			Long quantity = 0L;
			for (SalesRecord salesRecord : productEntrySet.getValue()) {

				totalVal = totalVal.add(salesRecord.getValue());
				quantity = Long.sum(quantity, salesRecord.getProductQuantity());
			}

			if (reportDenomination == ReportDenomination.pound) {
				totalVal = totalVal.divide(CONVERT_TO_POUND_FACTOR);
			}

			System.out.format("%32s%32d%32f\n", productEntrySet.getKey(), quantity, totalVal);
		}
	}

	/**
	 * This method generates report at interval when message count reaches
	 * {@link SalesConfigurationProperties#getPauseInterval()()}
	 */

	public static void printSalesAdjustmentsTillNow(ReportDenomination reportDenomination) {
		System.out.format("%32s%32s%32s\n", "ProductType", "Adjustment", "Adjustment_amount");

		for (SaleMessage saleMessage : asListAdjustedRecords) {

			System.out.format("%32s%32s%32f\n", saleMessage.getProductType(), saleMessage.getOperationType(),
					reportDenomination == ReportDenomination.pound
							? saleMessage.getValue().divide(CONVERT_TO_POUND_FACTOR)
							: saleMessage.getValue());
		}

		if (asListNotAdjustedRecords.size() > 0) {
			System.out.println(
					"Following is the adjustment report which were not applied to the Sales due to Invalid Productype");
			System.out.format("%32s%32s%32s\n", "ProductType", "Adjustment", "Adjustment_amount");

			for (SaleMessage saleMessage : asListNotAdjustedRecords) {

				System.out.format("%32s%32s%32f\n", saleMessage.getProductType(), saleMessage.getOperationType(),
						reportDenomination == ReportDenomination.pound
								? saleMessage.getValue().divide(CONVERT_TO_POUND_FACTOR)
								: saleMessage.getValue());
			}

		}

	}

	public static int getSalesProductCount() {
		return productSalesRecord.size();
	}

}
