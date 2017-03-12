package shohov.domain.model.loan.risk

import shohov.domain.model.loan.Loan
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

import static java.time.format.DateTimeFormatter.ISO_TIME

class RiskAnalysisServiceImplSpec extends Specification {

    def "returns false if loan amount is bigger than miximum alowed"() {
        given:
        def service = new RiskAnalysisServiceImpl(BigDecimal.valueOf(10), "00:00", "07:00")

        when:
        def allowed = service.isLoanAllowed(new Loan(amount: 11))

        then:
        !allowed
    }

    def "returns true or false depending on the amount and time"() {
        given:
        def service = new RiskAnalysisServiceImpl(BigDecimal.valueOf(10), startTime, endTime)

        expect:
        service.isLoanAllowed(new Loan(amount: amount, createdAt: LocalDate.now()
                .atTime(LocalTime.from(ISO_TIME.parse(time))).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli())) ==
                allowed

        where:
        amount | startTime | endTime | time    || allowed
        9      | "00:00"   | "07:00" | "01:00" || true
        10     | "00:00"   | "07:00" | "01:00" || false
        10     | "00:00"   | "07:00" | "00:00" || false
        10     | "00:00"   | "07:00" | "08:00" || true
        10     | "00:00"   | "07:00" | "07:00" || true
        10     | "22:00"   | "03:00" | "01:00" || false
        10     | "22:00"   | "03:00" | "22:00" || false
        10     | "22:00"   | "03:00" | "04:00" || true
        10     | "22:00"   | "03:00" | "21:00" || true
        10     | "22:00"   | "03:00" | "03:00" || true
    }
}
