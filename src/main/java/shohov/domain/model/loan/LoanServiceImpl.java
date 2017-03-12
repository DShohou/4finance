package shohov.domain.model.loan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import shohov.domain.model.loan.extension.LoanExtension;

import java.math.BigDecimal;

import static java.util.Collections.emptyList;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BigDecimal interestRate;

    public LoanServiceImpl(LoanRepository loanRepository,
                           @Value("${loan.interest.rate}") double interestRate) {
        this.loanRepository = loanRepository;
        this.interestRate = BigDecimal.valueOf(interestRate);
    }

    @Override
    public Loan save(Loan loan) {
        loan.setExtensions(emptyList());
        return loanRepository.save(loan);
    }

    @Override
    public Loan getById(long loanId) {
        return loanRepository.findOne(loanId);
    }

    @Override
    public Page<Loan> getByUserPersonId(String personId, int page, int size) {
        return loanRepository.findByPersonId(personId, new PageRequest(page, size));
    }

    @Override
    public BigDecimal calculateAmountToReturn(Loan loan) {
        int term = getTotalTerm(loan);
        BigDecimal amount = loan.getAmount();
        int weeks = term / 7;
        if (weeks == 0) {
            return amount.multiply(interestRate);
        } else {
            for (int i = 0; i < weeks; i++) {
                amount = amount.multiply(interestRate);
            }
            return amount;
        }
    }

    private int getTotalTerm(Loan loan) {
        int term = loan.getTerm();
        return term + (loan.getExtensions() == null ? 0 :
                loan.getExtensions().stream().mapToInt(LoanExtension::getTerm).sum());
    }
}
