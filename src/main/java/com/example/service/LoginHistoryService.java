package com.example.service;

import com.example.model.LoginHistory;
import com.example.repository.LoginHistoryRepository;
import com.example.utils.DeviceUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository repo;
    private final DeviceUtils deviceUtils;
    private final GeoLocationService geoService;

    private final UserAgentAnalyzer analyzer =
            UserAgentAnalyzer.newBuilder().withCache(1000).build();
    public boolean isNewDevice(String username, String deviceId) {
        return !repo.existsByUsernameAndDeviceId(username, deviceId);
    }

    @Async
    @Transactional
    public void save(String username, String ip, String userAgent) {

        var agent = analyzer.parse(userAgent);

        String deviceId = deviceUtils.generateDeviceId(userAgent);

        repo.updateCurrentSession(username);

        LoginHistory history = new LoginHistory();

        history.setUsername(username);
        history.setIpAddress(ip);
        history.setBrowser(agent.getValue("AgentName"));
        history.setOs(agent.getValue("OperatingSystemNameVersion"));
        history.setDeviceType(agent.getValue("DeviceClass"));
        history.setDeviceId(deviceId);
        history.setLoginTime(LocalDateTime.now());
        history.setCurrentSession(true);

        var location = geoService.getLocation(ip);

        history.setCity(location.get("city"));
        history.setCountry(location.get("country"));

        repo.save(history);
    }

}