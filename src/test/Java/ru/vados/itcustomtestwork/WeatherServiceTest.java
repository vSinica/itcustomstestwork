package ru.vados.itcustomtestwork;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.vados.itcustomtestwork.Dto.WeatherItem;
import ru.vados.itcustomtestwork.Repository.WeatherRepository;
import ru.vados.itcustomtestwork.Service.WeatherService;

import java.time.*;

@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = {
                "/sql/ddl-before-test.sql",
                "/sql/prepare-test-set1.sql"
        })
})

public class WeatherServiceTest extends AbstractTest {
        private static final String NOVOSIBIRSK_CITY = "novosibirsk";
        private static final Float NOVOSIBIRSK_TEMP = 13.6f;
        private static final String NOVOSIBIRSK_DATETIME_1 = "2023-10-26T07:00:00";
        private static final String NOVOSIBIRSK_DATETIME_2 = "2023-10-26T10:00:00";
        private static final String NOVOSIBIRSK_CLOSEST_DATE = "2023-10-26T08:00:00";
        private static final String NOVOSIBIRSK_DATE_NEAR_CLOSEST = "2023-10-26T07:00:00";

        private final WeatherRepository weatherRepository;
        private final WeatherService weatherService;

        @Autowired
        public WeatherServiceTest(WeatherRepository weatherRepository, WeatherService weatherService) {
                this.weatherRepository = weatherRepository;
                this.weatherService = weatherService;
        }

        @Test
        public void test__get_weather_without_date() {
                WeatherItem item = weatherService.getWeatherByCity(NOVOSIBIRSK_CITY);

                Assertions.assertEquals(NOVOSIBIRSK_TEMP, item.getTemp());
                Assertions.assertEquals(NOVOSIBIRSK_CITY, item.getCity());

                Instant expectInstant = LocalDateTime.parse(NOVOSIBIRSK_DATETIME_2).toInstant(ZoneOffset.UTC);
                Assertions.assertEquals(expectInstant, item.getDate());

        }

        @Test
        public void test__get_weather_with_date() {
                Instant expectInstant = LocalDateTime.parse(NOVOSIBIRSK_DATETIME_1).toInstant(ZoneOffset.UTC);
                WeatherItem item = weatherService.getWeatherByCityAndDate(NOVOSIBIRSK_CITY, expectInstant);

                Assertions.assertEquals(NOVOSIBIRSK_TEMP, item.getTemp());
                Assertions.assertEquals(NOVOSIBIRSK_CITY, item.getCity());
                Assertions.assertEquals(expectInstant, item.getDate());
        }

        @Test
        public void test__get_weather_with_date_closest_near() {
                Instant expectInstant = LocalDateTime.parse(NOVOSIBIRSK_CLOSEST_DATE).toInstant(ZoneOffset.UTC);
                WeatherItem item = weatherService.getWeatherByCityAndDate(NOVOSIBIRSK_CITY, expectInstant);

                Assertions.assertEquals(NOVOSIBIRSK_TEMP, item.getTemp());
                Assertions.assertEquals(NOVOSIBIRSK_CITY, item.getCity());
                Assertions.assertTrue(
                        Math.abs(expectInstant.getEpochSecond() - item.getDate().getEpochSecond()) < (60 * 60 * 24));
        }

}
