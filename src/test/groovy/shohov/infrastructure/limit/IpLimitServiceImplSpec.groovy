package shohov.infrastructure.limit

import spock.lang.Specification

class IpLimitServiceImplSpec extends Specification {

    def limitPerDay = 2

    def service = new IpLimitServiceImpl(limitPerDay)

    def "returns true if limit is reached for country"() {
        when:
        def result1 = service.isLimitReached("1.1.1.1")
        def result2 = service.isLimitReached("1.1.1.1")
        def result3 = service.isLimitReached("1.1.1.1")
        def result4 = service.isLimitReached("2.2.2.2")

        then:
        !result1
        !result2
        result3
        !result4
    }

    def "resets the counters"() {
        when:
        def result1 = service.isLimitReached("1.1.1.1")
        def result2 = service.isLimitReached("1.1.1.1")
        def result3 = service.isLimitReached("1.1.1.1")
        service.reset()
        def result4 = service.isLimitReached("1.1.1.1")

        then:
        !result1
        !result2
        result3
        !result4
    }
}
