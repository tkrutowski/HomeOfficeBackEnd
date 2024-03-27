package net.focik.homeoffice.library.api;

import lombok.AllArgsConstructor;
import net.focik.homeoffice.library.api.dto.SeriesDto;
import net.focik.homeoffice.library.domain.model.Series;
import net.focik.homeoffice.library.domain.port.primary.FindSeriesUseCase;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/library/series")
public class SeriesController {

    private final FindSeriesUseCase findSeriesUseCase;
    private final ModelMapper mapper;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('LIBRARY_READ_ALL','LIBRARY_READ')")
    ResponseEntity<List<SeriesDto>> getAllSeries() {
        List<Series> allSeries = findSeriesUseCase.getAllSeries();
        return new ResponseEntity<>(allSeries.stream()
                .map(series -> mapper.map(series, SeriesDto.class))
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}