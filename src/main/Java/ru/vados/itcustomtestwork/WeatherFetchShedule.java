package ru.vados.itcustomtestwork;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.vados.itcustomtestwork.Dto.ApiNinjasDto;
import ru.vados.itcustomtestwork.Dto.WeatherApiDto;
import ru.vados.itcustomtestwork.Dto.WeatherYandexApiDto;
import ru.vados.itcustomtestwork.Entity.WeatherEntity;
import ru.vados.itcustomtestwork.Repository.WeatherRepository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


@Component
public class WeatherFetchShedule {
    @Value("${weather-api.yandex.base-url}")
    private String yandexBaseUrl;
    @Value("${weather-api.yandex.token-header-name}")
    private String yandexTokenHeaderName;
    @Value("${weather-api.yandex.token}")
    private String yandexToken;

    @Value("${weather-api.ninja-api.base-url}")
    private String ninjaBaseUrl;
    @Value("${weather-api.ninja-api.token-header-name}")
    private String ninjaTokenHeaderName;
    @Value("${weather-api.ninja-api.token}")
    private String ninjaToken;

    @Value("${weather-api.weather-api.base-url}")
    private String weatherApiBaseUrl;
    @Value("${weather-api.weather-api.token-param-name}")
    private String weatherApiTokenParamName;
    @Value("${weather-api.weather-api.token}")
    private String weatherApiToken;

    @Value("${city-date-list-to-find}")
    private String cityDateListFileName;
    private List<String> cityNameList = new ArrayList<>();



    private final ObjectMapper mapper;
    private final CloseableHttpClient httpClient;
    private final FileListReader fileListReader;
    private final WeatherRepository weatherRepository;

    public WeatherFetchShedule(ObjectMapper mapper, CloseableHttpClient httpClient, FileListReader fileListReader, WeatherRepository weatherRepository) {
        this.mapper = mapper;
        this.httpClient = httpClient;
        this.fileListReader = fileListReader;
        this.weatherRepository = weatherRepository;
    }


    @Scheduled(fixedRateString = "${update-rate-time-weathers}")
    public void fetchWeatherCitiesList() throws IOException, URISyntaxException, ParseException {
        readCityList();
        fetch();
    }

    @Transactional
    public void readCityList() {
        cityNameList = fileListReader.read(cityDateListFileName);
    }

    private void fetch() throws URISyntaxException, IOException, ParseException {

        for(String city: cityNameList) {

            HttpGet httpGet = new HttpGet(ninjaBaseUrl);
            URI uri = new URIBuilder(httpGet.getUri())
                    .addParameter("city", city)
                    .build();
            httpGet.setUri(uri);
            httpGet.addHeader(ninjaTokenHeaderName, ninjaToken);
            String responseString = EntityUtils.toString(httpClient.execute(httpGet).getEntity(), "UTF-8");
            ApiNinjasDto apiNinjasDto = mapper.readValue(responseString, ApiNinjasDto.class);


            httpGet = new HttpGet(weatherApiBaseUrl);
            uri = new URIBuilder(httpGet.getUri())
                    .addParameter(weatherApiTokenParamName, weatherApiToken)
                    .addParameter("q", city)
                    .build();
            httpGet.setUri(uri);
            responseString = EntityUtils.toString(httpClient.execute(httpGet).getEntity(), "UTF-8");
            WeatherApiDto weatherApiDto = mapper.readValue(responseString, WeatherApiDto.class);


            httpGet = new HttpGet(yandexBaseUrl);
            uri = new URIBuilder(httpGet.getUri())
                    .addParameter("lat", weatherApiDto.getLocation().getLat().toString())
                    .addParameter("lon", weatherApiDto.getLocation().getLon().toString())
                    .addParameter("limit", "1")
                    .build();
            httpGet.setUri(uri);
            httpGet.addHeader(yandexTokenHeaderName, yandexToken);
            responseString = EntityUtils.toString(httpClient.execute(httpGet).getEntity(), "UTF-8");
            WeatherYandexApiDto weatherYandexApiDto = mapper.readValue(responseString, WeatherYandexApiDto.class);

            WeatherEntity entity = new WeatherEntity();
            entity.setCity(city);

            Float averageTemp = (apiNinjasDto.getTemp() + weatherYandexApiDto.getFact().getTemp() + weatherApiDto.getCurrent().getTemp_c()) / 3;
            entity.setTemp(averageTemp);

            entity.setCountry(weatherApiDto.getLocation().getCountry());

            weatherRepository.save(entity);
        }
    }
}
