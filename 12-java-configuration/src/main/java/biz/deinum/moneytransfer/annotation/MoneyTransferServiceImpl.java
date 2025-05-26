package biz.deinum.moneytransfer.annotation;

import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.AbstractMoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marten Deinum
 */
public class MoneyTransferServiceImpl extends AbstractMoneyTransferService {

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Override
	protected AccountRepository getAccountRepository() {
		return this.accountRepository;
	}

	@Override
	protected TransactionRepository getTransactionRepository() {
		return this.transactionRepository;
	}
}
