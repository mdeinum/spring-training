package biz.deinum.moneytransfer.scanning;

import biz.deinum.moneytransfer.repository.AccountRepository;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import biz.deinum.moneytransfer.service.AbstractMoneyTransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Marten Deinum
 */
@Service("moneyTransferService")
public class MoneyTransferServiceImpl extends AbstractMoneyTransferService {

	@Autowired
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

	@Autowired
	public void setTransactionRepository(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
}
