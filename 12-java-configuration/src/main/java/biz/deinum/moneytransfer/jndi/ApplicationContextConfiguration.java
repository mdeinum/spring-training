package biz.deinum.moneytransfer.jndi;

import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import biz.deinum.moneytransfer.setter.MoneyTransferServiceImpl;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiCallback;
import org.springframework.jndi.JndiLocatorDelegate;

/**
 * @author Marten Deinum
 */
@Configuration
public class ApplicationContextConfiguration {

	@Bean
	public JndiLocatorDelegate jndiLocator() {
		var jndiProperties = new Properties();
		jndiProperties.put("java.naming.factory.initial", "org.osjava.sj.SimpleContextFactory");
		var jndi = new JndiLocatorDelegate();
		jndi.setJndiEnvironment(jndiProperties);
		return jndi;
	}

	@Bean
	public AccountRepository accountRepository(JndiLocatorDelegate jndi) throws NamingException {
		return jndi.lookup("accountRepository", AccountRepository.class);
	}

	@Bean(initMethod = "")
	public TransactionRepository transactionRepository(JndiLocatorDelegate jndi) throws NamingException {
		jndi.getJndiTemplate().execute(new JndiCallback<Object>() {
			@Override
			public Object doInContext(Context ctx) throws NamingException {
				var objects = ctx.list("java:comp/env");
				while (objects.hasMore()) {
					System.out.println("Object: " + objects.next().getName());
				}
				return null;
			}
		});
		return jndi.lookup("transactionRepository", TransactionRepository.class);
	}

	@Bean
	public MoneyTransferService moneyTransferService(AccountRepository accountRepository,
																									 TransactionRepository transactionRepository) {
		var transferService = new MoneyTransferServiceImpl();
		transferService.setAccountRepository(accountRepository);
		transferService.setTransactionRepository(transactionRepository);
		return transferService;
	}

}
