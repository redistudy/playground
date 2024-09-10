package dev.jihogrammer.redistudy.filebeat.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;

@Service
public class RandomService {

    private static final Random RANDOM = new Random(Instant.now().toEpochMilli());

    public int randomInt(final Boolean positive, final Integer bound) {
        if (positive != null && bound != null) {
            if (positive) {
                return RANDOM.nextInt(0, bound);
            } else {
                return RANDOM.nextInt(-bound, 0);
            }
        }

        if (positive != null) {
            if (positive) {
                return RANDOM.nextInt(0, Integer.MAX_VALUE);
            } else {
                return RANDOM.nextInt(Integer.MIN_VALUE, 0);
            }
        }

        if (bound != null) {
            return RANDOM.nextInt(-bound, bound);
        }

        return RANDOM.nextInt();
    }

}
