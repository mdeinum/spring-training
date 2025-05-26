package biz.deinum.moneytransfer.simple;


import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedAccountRepository;
import biz.deinum.moneytransfer.repository.MapBasedTransactionRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.AbstractMoneyTransferService;

/**
 * {@code MoneyTransferService} implementation which instantiates the needed beans itself.
 *
 * @author Marten Deinum
 */
public class SimpleMoneyTransferServiceImpl extends AbstractMoneyTransferService {

	private final AccountRepository accountRepository = new MapBasedAccountRepository();
	private final TransactionRepository transactionRepository = new MapBasedTransactionRepository();

	public SimpleMoneyTransferServiceImpl() {
		super();
		((MapBasedAccountRepository) this.accountRepository).initialize();

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
