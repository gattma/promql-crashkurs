package at.gepardec.promql.rest;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

@Path("/hist")
public class HistogramMetricResource {

    private final Timer timer;

    HistogramMetricResource(MeterRegistry registry) {
        this.timer = Timer.builder("promql.hist")
                .description("Latency of prime number calculation")
                .serviceLevelObjectives(
                        Duration.ofMillis(1),
                        Duration.ofMillis(10),
                        Duration.ofMillis(100),
                        Duration.ofMillis(300),
                        Duration.ofMillis(500),
                        Duration.ofMillis(1000)
                )
                .register(registry);

    }

    @GET
    @Path("prime/{number}")
    public String checkIfPrime(long number) {
        if (number < 1) {
            return "Only natural numbers can be prime numbers.";
        }
        if (number == 1 || number % 2 == 0) {
            return number + " is not prime.";
        }

        if ( testPrimeNumber(number) ) {
            return number + " is prime.";
        } else {
            return number + " is not prime.";
        }
    }

    protected boolean testPrimeNumber(long number) {
        return Boolean.TRUE.equals(timer.record(() -> {
            randomDelay();
            for (int i = 3; i < Math.floor(Math.sqrt(number)) + 1; i = i + 2) {
                if (number % i == 0) {
                    return false;
                }
            }
            return true;
        }));
    }

    private void randomDelay() {
        try {
            TimeUnit.MILLISECONDS.sleep(RandomGenerator.getDefault().nextLong(500));
        } catch (InterruptedException e) {
            // Ignore
        }
    }

}
