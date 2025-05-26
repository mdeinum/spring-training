package biz.deinum.moneytransfer.jndi;

import biz.deinum.moneytransfer.ApplicationContextLogger;
import biz.deinum.moneytransfer.repository.MapBasedAccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedTransactionRepository;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import java.math.BigDecimal;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author Marten Deinum
 */
public class JndiMoneyTransferSpring {

	private static final Logger logger = LoggerFactory.getLogger(JndiMoneyTransferSpring.class);

	/**
	 * @param args
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException {
		setupJndi();
		var ctx1 = new AnnotationConfigApplicationContext(ApplicationContextConfiguration.class);
		var service = ctx1.getBean(MoneyTransferService.class);
		var transaction = service.transfer("123456", "987654", new BigDecimal("250.00"));

		logger.info("Money Transfered: {}", transaction);
		ApplicationContextLogger.log(ctx1);

	}

	private static void setupJndi() throws NamingException {
		logger.info("Setting up a Simple JNDI server.");
		var accountRepository = new MapBasedAccountRepository();
		accountRepository.initialize();
		var ctx = new InitialContext();
		ctx.bind("accountRepository", accountRepository);
		ctx.bind("transactionRepository", new MapBasedTransactionRepository());
	}
}
