package shohov.domain.model.loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface LoanRepository extends PagingAndSortingRepository<Loan, Long> {

    Page<Loan> findByPersonId(String personId, Pageable pageable);
}
