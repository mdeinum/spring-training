package biz.deinum.moneytransfer.setter;


import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.AbstractMoneyTransferService;

/**
 * @author Marten Deinum
 */
public class MoneyTransferServiceImpl extends AbstractMoneyTransferService {

	private AccountRepository accountRepository;
	private TransactionRepository transactionRepository;

	@Override
	protected AccountRepository getAccountRepository() {
		return this.accountRepository;
	}

	@Override
	protected TransactionRepository getTransactionRepository() {
		return this.transactionRepository;
	}

	public void setAccountRepository(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	public void setTransactionRepository(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}

}
