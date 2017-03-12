package shohov.web.controller.loan

import org.springframework.data.domain.Page
import shohov.domain.model.loan.Loan
import shohov.domain.model.loan.LoanService
import shohov.domain.model.loan.risk.RiskAnalysisService
import shohov.infrastructure.limit.IpLimitService
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class LoanRestControllerSpec extends Specification {

    def now = Instant.now()
    def clock = Clock.fixed(now, ZoneOffset.UTC)
    def loanService = Mock LoanService
    def riskAnalysisService = Mock RiskAnalysisService
    def ipLimitService = Mock IpLimitService

    def controller = new LoanRestController(clock, loanService, riskAnalysisService, ipLimitService)

    def httpRequest = Mock HttpServletRequest
    def ip = "1.2.3.4"
    def personId = "persid"
    def loan = new Loan(personId: personId)
    def amountToReturn = BigDecimal.valueOf(35)
    def page = 11
    def size = 22

    def "saves and returns the loan"() {
        given:
        riskAnalysisService.isLoanAllowed(loan) >> true
        httpRequest.getRemoteAddr() >> ip
        ipLimitService.isLimitReached(ip) >> false
        def savedLoan = new Loan()

        when:
        def result = controller.applyForLoan(loan, httpRequest)

        then:
        1 * loanService.save({ it.is(loan) && it.createdAt == now.toEpochMilli() } as Loan) >> savedLoan
        1 * loanService.calculateAmountToReturn(savedLoan) >> amountToReturn
        result == savedLoan
        result.amountToReturn == amountToReturn
    }

    def "fails request if risk analysis doesn't allow the loan"() {
        given:
        riskAnalysisService.isLoanAllowed(loan) >> false

        when:
        controller.applyForLoan(loan, httpRequest)

        then:
        0 * loanService.save(_ as Loan)
        thrown FailedRiskAnalysisException
    }

    def "fails request if limit by ip is reached"() {
        given:
        riskAnalysisService.isLoanAllowed(loan) >> true
        httpRequest.getRemoteAddr() >> ip
        ipLimitService.isLimitReached(ip) >> true

        when:
        controller.applyForLoan(loan, httpRequest)

        then:
        0 * loanService.save(_ as Loan)
        thrown LimitExceededException
    }

    def "returns page of loans by person id"() {
        given:
        def pageResult = Mock Page
        pageResult.forEach(_) >> { it[0].accept(loan) }

        when:
        def result = controller.getByUserPersonId(personId, page, size)

        then:
        1 * loanService.getByUserPersonId(personId, page, size) >> pageResult
        1 * loanService.calculateAmountToReturn(loan) >> amountToReturn
        result == pageResult
        loan.amountToReturn == amountToReturn
    }
}
