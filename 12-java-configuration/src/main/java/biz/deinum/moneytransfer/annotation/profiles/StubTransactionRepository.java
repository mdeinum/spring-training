package biz.deinum.moneytransfer.annotation.profiles;

import biz.deinum.moneytransfer.domain.Account;
import biz.deinum.moneytransfer.domain.Transaction;
import biz.deinum.moneytransfer.repository.TransactionRepository;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marten Deinum
 */
public class StubTransactionRepository implements TransactionRepository {

	private final Logger logger = LoggerFactory.getLogger(StubTransactionRepository.class);

	@Override
	public Transaction store(Transaction transaction) {
		this.logger.info("Stored: {}", transaction);
		return transaction;
	}

	@Override
	public Iterable<Transaction> findForAccount(Account account) {
		return new HashSet<>();
	}

}
