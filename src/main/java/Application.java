import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.Timer;
import com.codahale.metrics.jmx.JmxReporter;
import com.spotify.apollo.Environment;
import com.spotify.apollo.RequestContext;
import com.spotify.apollo.Response;
import com.spotify.apollo.httpservice.HttpService;
import com.spotify.apollo.httpservice.LoadingException;
import com.spotify.apollo.route.Route;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;
import static com.spotify.apollo.Status.BAD_REQUEST;
import static com.spotify.apollo.Status.OK;

public class Application {

    private static final GreetingService service = new GreetingService();
    private static final MetricRegistry metrics = new MetricRegistry();
    private static final Timer timer = metrics.timer(name(Application.class, "greet"));

    public static void main(String... args) throws LoadingException {
        final Slf4jReporter reporter1 = Slf4jReporter.forRegistry(metrics)
                .outputTo(LoggerFactory.getLogger("com.pdincau.metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter1.start(1, TimeUnit.SECONDS);
        final JmxReporter reporter = JmxReporter.forRegistry(metrics).build();
        reporter.start();
        HttpService.boot(Application::init, "java-metrics", args);
    }

    static void init(Environment environment) {
        environment.routingEngine()
                .registerAutoRoute(Route.sync("GET", "/hello", Application::greet))
                .registerAutoRoute(Route.sync("GET", "/ping", rc -> "pong"));
    }

    private static Response greet(RequestContext context) {
        final Timer.Context c = timer.time();
        try {
            String name = nameToGreetFrom(context);
            String message = service.greet(name);
            return Response.of(OK, message);
        } finally {
            c.stop();
        }
    }

    private static String nameToGreetFrom(RequestContext context) {
        return context.request().parameter("name").orElse("");
    }
}
