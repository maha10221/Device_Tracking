package com.example.service;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeoLocationService {

    private DatabaseReader reader;

    @PostConstruct
    public void init() throws Exception {

        InputStream db = getClass()
                .getResourceAsStream("/GeoLite2-City.mmdb");

        if (db == null) {
            throw new RuntimeException("GeoLite2 DB not found in resources");
        }

        reader = new DatabaseReader.Builder(db).build();
    }

    public Map<String, String> getLocation(String ip) {

        Map<String, String> map = new HashMap<>();

        try {

            InetAddress ipAddress = InetAddress.getByName(ip);

            CityResponse response = reader.city(ipAddress);

            map.put("city", response.getCity().getName());
            map.put("country", response.getCountry().getName());

        } catch (Exception e) {

            map.put("city", "Unknown");
            map.put("country", "Unknown");
        }

        return map;
    }

}