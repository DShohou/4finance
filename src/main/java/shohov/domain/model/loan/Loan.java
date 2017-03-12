package shohov.domain.model.loan;

import shohov.domain.model.loan.extension.LoanExtension;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Entity
public class Loan {

    @Id
    @GeneratedValue
    private long id;
    @NotNull
    @Column(nullable = false)
    private BigDecimal amount;
    @NotNull
    @Column(nullable = false)
    private int term;
    @NotNull
    @Column(nullable = false)
    private String personId;
    @Column(nullable = false)
    private Long createdAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = EAGER)
    @JoinColumn(name = "loan_id")
    private List<LoanExtension> extensions;

    private transient BigDecimal amountToReturn;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public List<LoanExtension> getExtensions() {
        return extensions;
    }

    public void setExtensions(List<LoanExtension> extensions) {
        this.extensions = extensions;
    }

    public BigDecimal getAmountToReturn() {
        return amountToReturn;
    }

    public void setAmountToReturn(BigDecimal amountToReturn) {
        this.amountToReturn = amountToReturn;
    }
}
