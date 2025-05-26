package biz.deinum.moneytransfer.annotation;


import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedAccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedTransactionRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
	public TransactionRepository transactionRepository() {
		return new MapBasedTransactionRepository();
	}

	@Bean
	public MoneyTransferService moneyTransferService() {
		return new MoneyTransferServiceImpl();
	}
}
