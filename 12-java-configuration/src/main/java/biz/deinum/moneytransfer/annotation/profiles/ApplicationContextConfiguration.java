package biz.deinum.moneytransfer.annotation.profiles;

import biz.deinum.moneytransfer.annotation.MoneyTransferServiceImpl;
import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedAccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedTransactionRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author Marten Deinum
 */
@Configuration
public class ApplicationContextConfiguration {

	@Bean
	public AccountRepository accountRepository() {
		return new MapBasedAccountRepository();
	}

	@Bean
	public MoneyTransferService moneyTransferService() {
		return new MoneyTransferServiceImpl();
	}

	@Configuration
	@Profile("test")
	public static class TestContextConfiguration {
		@Bean
		public TransactionRepository transactionRepository() {
			return new StubTransactionRepository();
		}
	}

	@Configuration
	@Profile("local")
	public static class LocalContextConfiguration {

		@Bean
		public TransactionRepository transactionRepository() {
			return new MapBasedTransactionRepository();
		}

	}

}
