package nextstep.subway.unit;

import nextstep.subway.applicaion.LineService;
import nextstep.subway.applicaion.StationService;
import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Station;
import nextstep.subway.utils.LineTestFixtures;
import nextstep.subway.utils.StationTestFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LineServiceMockTest {
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    private LineService lineService;

    @BeforeEach
    void setup() {
        lineService = new LineService(lineRepository, stationService);
    }

    @DisplayName("구간 등록하기")
    @Test
    void addSection() {

        // given
        when(stationService.findById(anyLong())).thenReturn(StationTestFixtures.지하철역_생성("강남역"));
        when(stationService.findById(anyLong())).thenReturn(StationTestFixtures.지하철역_생성("시청역"));
        when(stationService.findById(anyLong())).thenReturn(StationTestFixtures.지하철역_생성("구로디지털단지역"));

        Line 신분당선 = LineTestFixtures.노선_생성("신분당선", "red");
        ReflectionTestUtils.setField(신분당선, "id", 1L);
        when(lineRepository.findById(anyLong())).thenReturn(Optional.ofNullable(신분당선));

        SectionRequest sectionRequest = LineTestFixtures.구간요청_생성(1L, 2L, 10);

        // when
        lineService.addSection(신분당선.getId(), sectionRequest);

        // then
        Line 반환된_신분당선 = lineService.findLineById(신분당선.getId());
        assertThat(반환된_신분당선.getSections().size()).isEqualTo(1);
    }

    @DisplayName("구간 제거하기")
    @Test
    void removeSection() {

        // given
        final Station 강남역 = StationTestFixtures.지하철역_생성("강남역");
        ReflectionTestUtils.setField(강남역, "id", 1L);
        when(stationService.findById(anyLong())).thenReturn(강남역);

        final Station 시청역 = StationTestFixtures.지하철역_생성("시청역");
        ReflectionTestUtils.setField(시청역, "id", 2L);
        when(stationService.findById(anyLong())).thenReturn(시청역);

        final Station 구로디지털단지역 = StationTestFixtures.지하철역_생성("구로디지털단지역");
        ReflectionTestUtils.setField(구로디지털단지역, "id", 3L);
        when(stationService.findById(anyLong())).thenReturn(구로디지털단지역);

        final Line 신분당선 = LineTestFixtures.노선_생성("신분당선", "red");
        ReflectionTestUtils.setField(신분당선, "id", 1L);
        when(lineRepository.findById(anyLong())).thenReturn(Optional.ofNullable(신분당선));

        final SectionRequest 첫번째_구간_요청 = LineTestFixtures.구간요청_생성(강남역.getId(), 시청역.getId(), 10);
        final SectionRequest 두번째_구간_요청 = LineTestFixtures.구간요청_생성(시청역.getId(), 구로디지털단지역.getId(), 5);

        lineService.addSection(신분당선.getId(), 첫번째_구간_요청);
        lineService.addSection(신분당선.getId(), 두번째_구간_요청);

        when(lineRepository.findById(anyLong())).thenReturn(Optional.ofNullable(신분당선));
        when(stationService.findById(anyLong())).thenReturn(구로디지털단지역);

        //when
        lineService.removeSection(신분당선.getId(), 구로디지털단지역.getId());

        //then
        assertThat(신분당선.getSections().size()).isEqualTo(1);
    }
}
