package biz.deinum.moneytransfer.repository;

import biz.deinum.moneytransfer.domain.Account;
import biz.deinum.moneytransfer.domain.Transaction;

/**
 * Repository to store and retrieve {@code Transaction}s.
 *
 * @author Marten Deinum
 */
public interface TransactionRepository {

  Transaction store(Transaction transaction);

  Iterable<Transaction> findForAccount(Account account);

}