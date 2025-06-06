package biz.deinum.moneytransfer.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author Marten Deinum
 */
public class SimpleMoneyTransfer {

	private static final Logger logger = LoggerFactory.getLogger(SimpleMoneyTransfer.class);

	public static void main(String[] args) {
		var service = new SimpleMoneyTransferServiceImpl();

		var transaction = service.transfer("123456", "987654", new BigDecimal("250.00"));

		logger.info("Money Transfered: {}", transaction);

	}

}
