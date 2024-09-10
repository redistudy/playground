package dev.jihogrammer.redistudy.filebeat.web;

import dev.jihogrammer.redistudy.filebeat.service.RandomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/random")
@RequiredArgsConstructor
@Slf4j
class RandomValueController {

    private final RandomService randomService;

    @GetMapping("/int")
    ResponseEntity<RandomValueModel> randomInteger(
            @RequestParam(name = "positive", required = false) final Boolean positive,
            @RequestParam(name = "bound", required = false) final Integer bound
    ) {
        try {
            final var value = this.randomService.randomInt(positive, bound);
            return ResponseEntity.ok(RandomValueModel.of(value));
        } catch (Exception e) {
            log.error("Failed to generate random integer value.", e);
            return ResponseEntity.badRequest().body(RandomValueModel.of(e.getMessage()));
        }
    }

    @GetMapping("/uuid")
    UUID randomUUID() {
        return UUID.randomUUID();
    }

}
