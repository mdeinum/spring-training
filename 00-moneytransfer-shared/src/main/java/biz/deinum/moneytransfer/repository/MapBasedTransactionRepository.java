package biz.deinum.moneytransfer.repository;

import biz.deinum.moneytransfer.domain.Account;
import biz.deinum.moneytransfer.domain.MoneyTransferTransaction;
import biz.deinum.moneytransfer.domain.Transaction;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository("transactionRepository")
public class MapBasedTransactionRepository implements TransactionRepository {


  private final Logger logger = LoggerFactory.getLogger(MapBasedTransactionRepository.class);

  private final Map<Account, Set<Transaction>> storage = new ConcurrentHashMap<>();

  @Override
  public Transaction store(Transaction transaction) {
    store(transaction.getSource(), transaction);
    if (transaction instanceof MoneyTransferTransaction mtt) {
      store(mtt.getTarget(), transaction);
    }
    return transaction;
  }

  private void store(Account account, Transaction transaction) {
    var transactions = this.storage.computeIfAbsent(account, k -> new HashSet<>());
    transactions.add(transaction);
    this.logger.info("Stored transaction: {} for account {}.", transaction, account.getNumber());
  }

  @Override
  public Iterable<Transaction> findForAccount(Account account) {
    return this.storage.getOrDefault(account, Set.of());
  }}
