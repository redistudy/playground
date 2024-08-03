package dev.jihogrammer.redistudy.localsearch.adaptor.web;

import dev.jihogrammer.redistudy.poi.application.port.in.PointOfInterestQuery;
import dev.jihogrammer.redistudy.poi.application.port.out.PointOfInterestStatistics;
import dev.jihogrammer.redistudy.poi.domain.PointOfInterest;
import dev.jihogrammer.redistudy.poi.domain.vo.Location;
import dev.jihogrammer.redistudy.poi.domain.vo.PointOfInterestId;
import dev.jihogrammer.redistudy.poi.domain.vo.Radius;
import lombok.RequiredArgsConstructor;
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

    private final PointOfInterestQuery pointOfInterestQuery;

    private final PointOfInterestStatistics pointOfInterestStatistics;

    @GetMapping
    public String home(final Model model) {
        model.addAttribute("apiKey", apiKey);
        model.addAttribute("baseLocation", Location.DEFAULT);

        return "home";
    }

    @GetMapping("/search")
    @ResponseBody
    public Collection<PointOfInterest> search(
            @RequestParam("lat") final Double lat,
            @RequestParam("lon") final Double lon,
            @RequestParam("radius") final Double radiusValue,
            @RequestParam(value = "radius-unit", defaultValue = "m") final String radiusUnit
    ) {
        var location = Location.of(lat, lon);
        var radius = Radius.of(radiusValue, Radius.Unit.of(radiusUnit));

        return this.pointOfInterestQuery.searchByLocationAndRadius(location, radius);
    }

    @GetMapping("/score")
    @ResponseBody
    public double score(@RequestParam("id") final Integer idValue) {
        return this.pointOfInterestStatistics.findScoreByPoiId(new PointOfInterestId(idValue));
    }

}
