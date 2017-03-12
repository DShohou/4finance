package shohov.domain.model.loan.risk;

import shohov.domain.model.loan.Loan;

public interface RiskAnalysisService {

    boolean isLoanAllowed(Loan loan);
}
