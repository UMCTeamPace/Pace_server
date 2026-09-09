package com.example.pace.domain.weather.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.pace.domain.weather.enums.WeatherConditionCode;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WeatherConverterTest {

    @ParameterizedTest
    @MethodSource("standardConditionMappings")
    @DisplayName("OpenWeather 표준 상태를 앱 상태 코드로 변환한다")
    void toConditionCode_standardCondition_returnsMappedCode(
            String condition,
            WeatherConditionCode expected
    ) {
        // given

        // when
        WeatherConditionCode result = WeatherConverter.toConditionCode(condition);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("atmosphereConditions")
    @DisplayName("OpenWeather 대기 상태를 ATMOSPHERE로 변환한다")
    void toConditionCode_atmosphereCondition_returnsAtmosphere(
            String condition
    ) {
        // given

        // when
        WeatherConditionCode result = WeatherConverter.toConditionCode(condition);

        // then
        assertThat(result).isEqualTo(WeatherConditionCode.ATMOSPHERE);
    }

    @ParameterizedTest
    @MethodSource("unknownConditions")
    @DisplayName("알 수 없는 날씨 상태를 UNKNOWN으로 변환한다")
    void toConditionCode_unknownCondition_returnsUnknown(String condition) {
        // given

        // when
        WeatherConditionCode result = WeatherConverter.toConditionCode(condition);

        // then
        assertThat(result).isEqualTo(WeatherConditionCode.UNKNOWN);
    }

    private static Stream<Arguments> standardConditionMappings() {
        return Stream.of(
                Arguments.of("Clear", WeatherConditionCode.CLEAR),
                Arguments.of("Clouds", WeatherConditionCode.CLOUDS),
                Arguments.of("Rain", WeatherConditionCode.RAIN),
                Arguments.of("Drizzle", WeatherConditionCode.DRIZZLE),
                Arguments.of("Thunderstorm", WeatherConditionCode.THUNDERSTORM),
                Arguments.of("Snow", WeatherConditionCode.SNOW)
        );
    }

    private static Stream<String> atmosphereConditions() {
        return Stream.of("Mist", "Smoke", "Haze", "Dust", "Fog", "Sand", "Ash", "Squall", "Tornado");
    }

    private static Stream<String> unknownConditions() {
        return Stream.of(null, "VolcanicAsh");
    }
}
