package shohov.web.controller.loan;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import shohov.domain.model.loan.Loan;
import shohov.domain.model.loan.LoanService;
import shohov.domain.model.loan.risk.RiskAnalysisService;
import shohov.infrastructure.limit.IpLimitService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Clock;

import static shohov.util.HttpHelper.getClientIpAddress;

@RestController
@RequestMapping("/loan")
public class LoanRestController {

    private static final String DEFAULT_PAGE_NUM = "0";
    private static final String DEFAULT_PAGE_SIZE = "20";

    private final Clock clock;
    private final LoanService loanService;
    private final RiskAnalysisService riskAnalysisService;
    private final IpLimitService ipLimitService;

    public LoanRestController(Clock clock, LoanService loanService, RiskAnalysisService riskAnalysisService,
                              IpLimitService ipLimitService) {
        this.clock = clock;
        this.loanService = loanService;
        this.riskAnalysisService = riskAnalysisService;
        this.ipLimitService = ipLimitService;
    }

    @PutMapping(consumes = {"application/json"})
    public Loan applyForLoan(@RequestBody @Valid Loan loan, HttpServletRequest request) {
        loan.setCreatedAt(clock.millis());
        if (!riskAnalysisService.isLoanAllowed(loan)) {
            throw new FailedRiskAnalysisException("Risk analysis result does not allow a loan to be issued");
        }
        if (ipLimitService.isLimitReached(getClientIpAddress(request))) {
            throw new LimitExceededException("Loan limit is exceeded for your ip address");
        }
        Loan savedLoan = loanService.save(loan);
        savedLoan.setAmountToReturn(loanService.calculateAmountToReturn(savedLoan));
        return savedLoan;
    }

    @GetMapping(value = "/{personId}", produces = {"application/json"})
    public Page<Loan> getByUserPersonId(@PathVariable("personId") String personId,
                                        @RequestParam(value = "page", defaultValue = DEFAULT_PAGE_NUM) int page,
                                        @RequestParam(value = "size", defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Page<Loan> pageOfLoans = loanService.getByUserPersonId(personId, page, size);
        pageOfLoans.forEach(loan -> loan.setAmountToReturn(loanService.calculateAmountToReturn(loan)));
        return pageOfLoans;
    }
}
