package ru.vados.itcustomtestwork.Service;

import ru.vados.itcustomtestwork.Dto.WeatherItem;

import java.time.Instant;


public interface WeatherService {

    WeatherItem getWeatherByCity(String city);
    WeatherItem getWeatherByCityAndDate(String city, Instant date);

}
