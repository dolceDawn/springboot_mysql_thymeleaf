package com.epam.weather.service.impl;

import com.epam.weather.exception.InvalidLocationException;
import com.epam.weather.service.WeatherService;
import com.epam.weather.vo.DataDetail;
import com.epam.weather.vo.Site;
import com.epam.weather.vo.WeatherResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import util.JacksonUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherServiceImpl implements WeatherService {

    //·Get the province code of China
    private static final String GET_PROVICE_URI = "http://www.weather.com.cn/data/city3jdata/china.html";

    //·Get the city code of one certain province, ‘10119’ is ‘province code’
    private static final String GET_CITY_URI = "http://www.weather.com.cn/data/city3jdata/provshi/";

    //·Get the county code of one certain city, ‘1011904’ is ‘province code + city code’
    private static final String GET_COUNTY_URI = "http://www.weather.com.cn/data/city3jdata/station/";

    //·Get the weather of one certain county, ‘101190401’ is ‘province code + city code + county code.’
    private static final String WEATHER_URI = "http://www.weather.com.cn/data/sk/";

    @Resource
    private RestTemplate restTemplate;

    public List<Site> getSites(String url) {
        String strBody;
        if (url == null) {
            //provice url
            strBody = this.getStrBody(GET_PROVICE_URI);
        } else {
            //city、county url
            strBody = this.getStrBody(url);
        }
        strBody = strBody.replace("{", "").replace("}", "");
        List<Site> sites = converToSite(strBody);
        return sites;
    }

    private List<Site> converToSite(String datas) {

        List<Site> sites = new ArrayList<>();

        String[] siteArr = datas.split(",");

        for (int i = 0; i <= siteArr.length - 1; i++) {

            String[] cols = siteArr[i].split(":");

            String code = StringUtils.trimAllWhitespace(cols[0].replace("\"", ""));
            String name = StringUtils.trimAllWhitespace(cols[1].replace("\"", ""));

            Site site = new Site(code, name);

            sites.add(site);

        }

        return sites;
    }

    public List<Site> getProvices() {
        return getSites(null);
    }

    public List<Site> getCities(String proviceCode) {
        String uri = GET_CITY_URI + proviceCode + ".html";
        return getSites(uri);
    }

    public List<Site> getConties(String provinceCityCode) {
        String uri = GET_COUNTY_URI + provinceCityCode + ".html";
        return getSites(uri);
    }


    private WeatherResponse getWeatherResponse(String code) {
        String url = WEATHER_URI + code + ".html";
        String strBody = this.getStrBody(url);
        WeatherResponse resp = null;
        try {
            resp = JacksonUtil.readValue(strBody, WeatherResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }


    @Override
    public Optional<Float> getTemperature(String province, String city, String county) throws Exception {

        checkLocationInvalid(province, city, county);

        String mixCode = province + city + county;

        WeatherResponse response = getWeatherResponse(mixCode);

        if (response != null && response.getWeatherinfo() != null) {
            return Optional.of(response.getWeatherinfo().getTemp());
        }

        return Optional.empty();
    }


    private String getStrBody(String url) {
        ResponseEntity<String> respString = restTemplate.getForEntity(url, String.class);
        String strBody = null;
        if (respString.getStatusCodeValue() == 200) {
            strBody = respString.getBody();
        }
        return strBody;
    }

    private void checkLocationInvalid(String province, String city, String county) throws InvalidLocationException {

        //check provice valid
        List<Site> provices = getProvices();

        Optional<Site> firstProvice = provices.stream().filter(a->a.getCode().equals(province)).findFirst();
        if (!firstProvice.isPresent()) {
            throw new InvalidLocationException("invalid provice code : " + province + " please check!" );
        }

        //check city valid
        List<Site> cities = getCities(province);
        Optional<Site> firstCity = cities.stream().filter(a->a.getCode().equals(city)).findFirst();
        if (!firstCity.isPresent()) {
            throw new InvalidLocationException("invalid city code : " + city + " please check!" );
        }

        //check county valid
        List<Site> counties = getConties(province + city);
        Optional<Site> firstCounty = counties.stream().filter(a->a.getCode().equals(county)).findFirst();
        if (!firstCounty.isPresent()) {
            throw new InvalidLocationException("invalid county code : " + county + " please check!" );
        }

    }

}
