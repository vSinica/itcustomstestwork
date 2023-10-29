package ru.vados.itcustomtestwork.Dto;

import lombok.Data;

@Data
public class WeatherApiDto {
    Location location;
    CurrentWeather current;

    @Data
    public static class Location{
        Float lat;
        Float lon;
        String country;
    }

    @Data
    public static class CurrentWeather{
        Float temp_c;
    }
}
