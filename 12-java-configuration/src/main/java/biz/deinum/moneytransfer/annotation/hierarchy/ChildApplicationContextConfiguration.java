package biz.deinum.moneytransfer.annotation.hierarchy;

import biz.deinum.moneytransfer.annotation.MoneyTransferServiceImpl;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marten Deinum
 */
@Configuration
public class ChildApplicationContextConfiguration {

	@Bean
	public MoneyTransferService moneyTransferService() {
		return new MoneyTransferServiceImpl();
	}

}
