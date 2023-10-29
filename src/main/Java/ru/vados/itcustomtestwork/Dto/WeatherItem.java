package ru.vados.itcustomtestwork.Dto;

import lombok.Value;

import java.time.Instant;

@Value
public class WeatherItem {
    String city;
    String country;
    float temp;
    Instant date;
}
