package biz.deinum.moneytransfer;

import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedAccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedTransactionRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.MoneyTransferService;
import org.springframework.context.annotation.Bean;

public class MoneyTransferConfig {

  @Bean
  public AccountRepository accountRepository() {
    return new MapBasedAccountRepository();
  }

  @Bean
  public TransactionRepository transactionRepository() {
    return new MapBasedTransactionRepository();
  }

  @Bean
  public MoneyTransferService moneyTransferService(AccountRepository accountRepository,
                                                   TransactionRepository transactionRepository) {
    return new MoneyTransferServiceImpl(accountRepository, transactionRepository);
  }
}
