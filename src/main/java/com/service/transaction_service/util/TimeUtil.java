package com.service.transaction_service.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class TimeUtil {

    private final Clock clock;

    public ZonedDateTime getCurrentZonedDateTime() {
        return ZonedDateTime.now(clock);
    }
}
