package shohov.web.controller.loan.extend

import shohov.domain.model.loan.Loan
import shohov.domain.model.loan.LoanService
import shohov.domain.model.loan.extension.LoanExtensionService
import spock.lang.Specification

class LoanExtendRestControllerSpec extends Specification {

    def loanService = Mock LoanService
    def loanExtensionService = Mock LoanExtensionService

    def service = new LoanExtendRestController(loanService, loanExtensionService)

    def loanId = 234512L
    def term = 3453
    def loan = new Loan()
    def amountToReturn = BigDecimal.valueOf(233)

    def "extends the loan"() {
        given:
        loanService.getById(loanId) >> loan
        loanService.calculateAmountToReturn(loan) >> amountToReturn

        when:
        def result = service.extendLoan(loanId, term)

        then:
        1 * loanExtensionService.save(loanId, term)
        result == loan
        loan.amountToReturn == amountToReturn
    }
}
