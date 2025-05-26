package biz.deinum.moneytransfer.constructor;


import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.AbstractMoneyTransferService;

/**
 * @author Marten Deinum
 */
public class MoneyTransferServiceImpl extends AbstractMoneyTransferService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;

	public MoneyTransferServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
		super();
		this.accountRepository = accountRepository;
		this.transactionRepository = transactionRepository;
	}

	@Override
	protected AccountRepository getAccountRepository() {
		return this.accountRepository;
	}

	@Override
	protected TransactionRepository getTransactionRepository() {
		return this.transactionRepository;
	}
}
