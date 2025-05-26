package biz.deinum.moneytransfer.setter;

import biz.deinum.moneytransfer.ApplicationContextLogger;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Marten Deinum
 */
public class MoneyTransferSpring {

	private static final Logger logger = LoggerFactory.getLogger(MoneyTransferSpring.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		var ctx1 = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);

		transfer(ctx1);

		ApplicationContextLogger.log(ctx1);

		var ctx2 = SpringApplication.run(ApplicationContextConfiguration.class);
		transfer(ctx2);

		ApplicationContextLogger.log(ctx2);

	}

	private static void transfer(ApplicationContext ctx) {
		var service = ctx.getBean("moneyTransferService", MoneyTransferService.class);
		var transaction = service.transfer("123456", "987654", new BigDecimal("250.00"));

		logger.info("Money Transfered: {}", transaction);
	}

}
