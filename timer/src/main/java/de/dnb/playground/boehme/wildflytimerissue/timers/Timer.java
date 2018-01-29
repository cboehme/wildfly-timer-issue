/*
 * Copyright 2018 Christoph Böhme
 *
 * Licensed under the Apache License, Version 2.0 the "License";
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dnb.playground.boehme.wildflytimerissue.timers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Stateless;

/**
 * Defines a timer that runs every five seconds for one second.
 */
@Stateless
public class Timer {

    private static final Logger log = LoggerFactory.getLogger(javax.ejb.Timer.class);

    private static final long callbackDuration = 1000;

    private final String callbackId = getClass().getSimpleName();

    private int callCounter;

    @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
    private void callback(javax.ejb.Timer timer) {

        log.info("{} activated for {}. time", callbackId, ++callCounter);
        spendingTime();
        log.info("{} completed", callbackId);
    }

    private void spendingTime() {

        final long start = System.currentTimeMillis();
        long logNext = start;
        long now;
        do {
            now = System.currentTimeMillis();
            if (logNext < now) {
                log.info("{} is still doing work ...", callbackId);
                logNext += 1000;
            }
        } while (start + callbackDuration > now);
    }

}
