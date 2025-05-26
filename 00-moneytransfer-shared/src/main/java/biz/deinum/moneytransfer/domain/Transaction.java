package biz.deinum.moneytransfer.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.core.style.ToStringCreator;

/**
 * Base class for transaction subclasses.
 *
 * @author Marten Deinum
 */
public abstract class Transaction {

  private final Account source;
  private final BigDecimal amount;
  private final LocalDateTime timestamp = LocalDateTime.now();

  protected Transaction(final Account source, final BigDecimal amount) {
    super();
    this.source = source;
    this.amount = amount;
  }

  public Account getSource() {
    return this.source;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public LocalDateTime getTimestamp() {
    return this.timestamp;
  }

  @Override
  public String toString() {
    return new ToStringCreator(this)
        .append("source", this.source)
        .append("amount", this.amount)
        .append("timestamp", this.timestamp).toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, amount, timestamp);
  }
}
