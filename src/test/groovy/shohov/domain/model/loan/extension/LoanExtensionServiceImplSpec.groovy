package shohov.domain.model.loan.extension;

import spock.lang.Specification;

class LoanExtensionServiceImplSpec extends Specification {
    def loanExtensionRepository = Mock LoanExtensionRepository

    def service = new LoanExtensionServiceImpl(loanExtensionRepository);

    def loanId = 3523L
    def term = 343

    def "extends term of loan"() {
        when:
        service.save(loanId, term)
        then:
        1 * loanExtensionRepository.save({ it.term == term && it.loanId == loanId } as LoanExtension)
    }
}