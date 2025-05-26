package biz.deinum.moneytransfer.repository;

import biz.deinum.moneytransfer.domain.Account;

public interface AccountRepository {

  Account findByNumber(String number);

}
