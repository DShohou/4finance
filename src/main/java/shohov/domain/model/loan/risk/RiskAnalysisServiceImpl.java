package shohov.domain.model.loan.risk;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import shohov.domain.model.loan.Loan;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneOffset;

import static java.time.format.DateTimeFormatter.ISO_TIME;

@Service
public class RiskAnalysisServiceImpl implements RiskAnalysisService {

    private final BigDecimal maxAmount;
    private final LocalTime riskyStartTime;
    private final LocalTime riskyEndTime;
    private final boolean riskyTimeReversed;

    public RiskAnalysisServiceImpl(@Value("${loan.max.amount}") BigDecimal maxAmount,
                                   @Value("${loan.risky.start.time}") String riskyStartTime,
                                   @Value("${loan.risky.end.time}") String riskyEndTime) {
        this.maxAmount = maxAmount;
        this.riskyStartTime = LocalTime.from(ISO_TIME.parse(riskyStartTime));
        this.riskyEndTime = LocalTime.from(ISO_TIME.parse(riskyEndTime));
        riskyTimeReversed = this.riskyEndTime.compareTo(this.riskyStartTime) < 0;
    }

    @Override
    public boolean isLoanAllowed(Loan loan) {
        return loan.getAmount().compareTo(maxAmount) <= 0 &&
                (!isRiskyTime(loan.getCreatedAt()) || loan.getAmount().compareTo(maxAmount) < 0);
    }

    private boolean isRiskyTime(long loanCreationTime) {
        LocalTime currentTime = Instant.ofEpochMilli(loanCreationTime).atZone(ZoneOffset.UTC).toLocalTime();
        if (riskyTimeReversed) {
            return currentTime.compareTo(riskyStartTime) >= 0 || currentTime.compareTo(riskyEndTime) < 0;
        } else {
            return currentTime.compareTo(riskyStartTime) >= 0 && currentTime.compareTo(riskyEndTime) < 0;
        }
    }
}
