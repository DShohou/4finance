package shohov.infrastructure.limit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class IpLimitServiceImpl implements IpLimitService {

    private final int limitPerDay;

    private final Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public IpLimitServiceImpl(@Value("${ip.limit.service.loans.per.day.limit}") int limitPerDay) {
        this.limitPerDay = limitPerDay;
    }

    @Override
    public boolean isLimitReached(String ip) {
        AtomicInteger counter = counters.computeIfAbsent(ip, k -> new AtomicInteger());
        return counter.incrementAndGet() > limitPerDay;
    }

    @Scheduled(cron = "0 0 0 * * *")
    void reset() {
        counters.clear();
    }
}
