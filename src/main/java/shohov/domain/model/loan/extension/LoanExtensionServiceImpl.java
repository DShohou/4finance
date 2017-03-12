package shohov.domain.model.loan.extension;

import org.springframework.stereotype.Service;

@Service
public class LoanExtensionServiceImpl implements LoanExtensionService {

    private final LoanExtensionRepository loanExtensionRepository;

    public LoanExtensionServiceImpl(LoanExtensionRepository loanExtensionRepository) {
        this.loanExtensionRepository = loanExtensionRepository;
    }

    @Override
    public void save(long loanId, int term) {
        LoanExtension loanExtension = new LoanExtension();
        loanExtension.setTerm(term);
        loanExtension.setLoanId(loanId);
        loanExtensionRepository.save(loanExtension);
    }
}
