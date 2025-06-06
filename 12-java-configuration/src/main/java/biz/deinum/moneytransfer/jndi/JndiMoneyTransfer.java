package biz.deinum.moneytransfer.jndi;

import biz.deinum.moneytransfer.repository.MapBasedAccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedTransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.math.BigDecimal;

/**
 * @author Marten Deinum
 */
public class JndiMoneyTransfer {

	private static final Logger logger = LoggerFactory.getLogger(JndiMoneyTransfer.class);

	/**
	 * @param args
	 * @throws NamingException
	 */
	public static void main(String[] args) throws NamingException {
		setupJndi();
		var service = new JndiMoneyTransferServiceImpl();
		var transaction = service.transfer("123456", "987654", new BigDecimal("250.00"));

		logger.info("Money Transfered: {}", transaction);

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
