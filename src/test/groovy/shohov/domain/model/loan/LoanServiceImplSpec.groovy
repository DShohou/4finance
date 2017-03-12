package shohov.domain.model.loan

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import shohov.domain.model.loan.extension.LoanExtension
import spock.lang.Specification

class LoanServiceImplSpec extends Specification {

    def loanRepository = Mock LoanRepository
    def interestRate = 1.5

    def service = new LoanServiceImpl(loanRepository, interestRate)

    def loanId = 634L
    def extension = new LoanExtension()
    def loan = new Loan(extensions: [extension])
    def page = 11
    def size = 22
    def personId = "persid"

    def "saves the loan, resetting extensions"() {
        given:
        def savedLoan = new Loan()

        when:
        def result = service.save(loan)

        then:
        1 * loanRepository.save({ it.is(loan) && it.extensions.size() == 0 } as Loan) >> savedLoan
        result == savedLoan
    }

    def "returns loan by id"() {
        when:
        def result = service.getById(loanId)

        then:
        1 * loanRepository.findOne(loanId) >> loan
        result == loan
    }

    def "returns page of loans for personal id"() {
        given:
        def pageResult = Mock Page

        when:
        def result = service.getByUserPersonId(personId, page, size)

        then:
        1 * loanRepository.findByPersonId(personId, {
            it.pageNumber == page && it.pageSize == size
        } as Pageable) >> pageResult
        result == pageResult
    }

    def "calculates amount to return based on interest per week"() {
        given:
        def extension1 = new LoanExtension(term: eterm1)
        def extension2 = new LoanExtension(term: eterm2)
        def loan = new Loan(term: term, amount: amount, extensions: [extension1, extension2])

        expect:
        service.calculateAmountToReturn(loan) == amountToReturn

        where:
        amount | term | eterm1 | eterm2 || amountToReturn
        1      | 1    | 1      | 1       | 1.5
        1      | 6    | 1      | 1       | 1.5
        1      | 2    | 6      | 6       | 2.25
    }
}
