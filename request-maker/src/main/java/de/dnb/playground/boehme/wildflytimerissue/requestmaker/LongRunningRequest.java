package de.dnb.playground.boehme.wildflytimerissue.requestmaker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Provides a HTTP resource which takes 30 seconds to complete a request.
 */
@Path("/make-request")
@RequestScoped
public class LongRunningRequest {

    private static final Logger log = LoggerFactory.getLogger(LongRunningRequest.class);

    private static final long requestDuration = 30_000;

    @GET
    public String makeRequest() {

        log.info("Request started");
        spendingTime();
        log.info("Request finished");
        return "Okay, I'm finished";
    }

    private void spendingTime() {

        final long start = System.currentTimeMillis();
        long logNext = start;
        long now;
        do {
            now = System.currentTimeMillis();
            if (logNext < now) {
                log.info("Still processing request ...");
                log.info("Still processing request ...");
                logNext += 1000;
            }
        } while (start + requestDuration > now);
    }

}
