package shohov.web.controller.loan.extend;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shohov.domain.model.loan.Loan;
import shohov.domain.model.loan.LoanService;
import shohov.domain.model.loan.extension.LoanExtensionService;

@RestController
@RequestMapping("/loan/extend")
public class LoanExtendRestController {

    private final LoanService loanService;
    private final LoanExtensionService loanExtensionService;

    public LoanExtendRestController(LoanService loanService, LoanExtensionService loanExtensionService) {
        this.loanService = loanService;
        this.loanExtensionService = loanExtensionService;
    }

    @PutMapping(produces = {"application/json"})
    public Loan extendLoan(@RequestParam("loanId") long loanId,
                           @RequestParam("term") int term) {
        loanExtensionService.save(loanId, term);
        Loan loan = loanService.getById(loanId);
        loan.setAmountToReturn(loanService.calculateAmountToReturn(loan));
        return loan;
    }
}
