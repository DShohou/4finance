package shohov.infrastructure.limit;

public interface IpLimitService {

    boolean isLimitReached(String ip);
}
