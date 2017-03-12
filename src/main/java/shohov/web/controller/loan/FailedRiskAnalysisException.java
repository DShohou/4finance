package shohov.web.controller.loan;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class FailedRiskAnalysisException extends RuntimeException {
    public FailedRiskAnalysisException(String message) {
        super(message);
    }
}
