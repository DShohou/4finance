package shohov.domain.model.loan;

import org.springframework.data.domain.Page;

import java.math.BigDecimal;

public interface LoanService {

    Loan save(Loan loan);

    Loan getById(long loanId);

    Page<Loan> getByUserPersonId(String personId, int page, int size);

    BigDecimal calculateAmountToReturn(Loan loan);
}
