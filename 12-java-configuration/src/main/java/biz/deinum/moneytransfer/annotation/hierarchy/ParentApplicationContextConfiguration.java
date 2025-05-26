package biz.deinum.moneytransfer.annotation.hierarchy;

import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedAccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedTransactionRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marten Deinum
 */
@Configuration
public class ParentApplicationContextConfiguration {

	@Bean
	public AccountRepository accountRepository() {
		return new MapBasedAccountRepository();
	}

	@Bean
	public TransactionRepository transactionRepository() {
		return new MapBasedTransactionRepository();
	}

}
