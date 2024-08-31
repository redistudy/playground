package dev.jihogrammer.redistudy.localsearch;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
@RequiredArgsConstructor
public class DefaultController {

    private final String apiKey;

    private final RedisPoiRepository repository;

    @GetMapping
    public String home(final Model model) {
        model.addAttribute("apiKey", apiKey);

        return "home";
    }

    @GetMapping("/search")
    @ResponseBody
    public Collection<PointOfInterest> search(
            @RequestParam("lat") final Double lat,
            @RequestParam("lon") final Double lon,
            @RequestParam("radius") final Integer radius,
            @RequestParam(value = "radius-unit", defaultValue = "m") final String radiusUnit
    ) {
        final double radiusValue;
        if ("m".equals(radiusUnit)) {
            radiusValue = radius;
        } else if ("km".equals(radiusUnit)) {
            radiusValue = radius * 1000;
        } else {
            throw new RuntimeException(new IllegalStateException("UNKNOWN Radius Unit - " + radiusUnit));
        }

        return this.repository.findByLocationAndRadius(new Point(lon, lat), radiusValue);
    }

}
