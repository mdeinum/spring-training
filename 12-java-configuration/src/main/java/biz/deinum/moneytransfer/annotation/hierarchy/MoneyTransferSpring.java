package biz.deinum.moneytransfer.annotation.hierarchy;

import biz.deinum.moneytransfer.ApplicationContextLogger;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

		var parent = new AnnotationConfigApplicationContext(ParentApplicationContextConfiguration.class);
		var child = new AnnotationConfigApplicationContext();
		child.register(ChildApplicationContextConfiguration.class);
		child.setParent(parent);
		child.refresh();

		transfer(child);

		ApplicationContextLogger.log(parent);
		ApplicationContextLogger.log(child);
	}

	private static void transfer(ApplicationContext ctx) {
		var service = ctx.getBean(MoneyTransferService.class);
		var transaction = service.transfer("123456", "987654", new BigDecimal("250.00"));

		logger.info("Money Transfered: {}", transaction);
	}

}
