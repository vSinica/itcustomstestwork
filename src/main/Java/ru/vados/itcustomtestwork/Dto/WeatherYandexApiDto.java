package ru.vados.itcustomtestwork.Dto;

import lombok.Data;

@Data
public class WeatherYandexApiDto {

    Fact fact;

    @Data
    public static class Fact{
        public Float temp;
    }
}
