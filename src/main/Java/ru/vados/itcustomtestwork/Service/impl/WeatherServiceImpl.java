package ru.vados.itcustomtestwork.Service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vados.itcustomtestwork.Dto.WeatherItem;
import ru.vados.itcustomtestwork.Entity.WeatherEntity;
import ru.vados.itcustomtestwork.Repository.WeatherRepository;
import ru.vados.itcustomtestwork.Service.WeatherService;

import java.time.Instant;

@Service
@AllArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository weatherRepository;

    @Override
    public WeatherItem getWeatherByCity(String city){
        return convert(weatherRepository.findFirstByCityOrderByPublishedDateDesc(city).orElseThrow());
    }

    @Override
    public WeatherItem getWeatherByCityAndDate(String city, Instant date){
        return convert(weatherRepository.findByCityAndClosestDate(city, date));
    }

    WeatherItem convert(WeatherEntity entity){
       return new WeatherItem(entity.getCity(), entity.getCountry(), entity.getTemp(),entity.getPublishedDate());
    }
}
