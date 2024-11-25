package com.ssafy.urs.controller;

import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/weather")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class WeatherController {

    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
    private static final String SERVICE_KEY = "keXqkPkDeCmVX4aIWgC0N8M6kiBklRgfb6WvP%2BSrMZ7x2xC9yzlg4xazLuD8%2B4YIJFN9sHnUPISv%2B5VVyPvopw%3D%3D";

    @GetMapping("/forecast")
    public ResponseEntity<?> getWeatherForecast(
            @RequestParam String baseDate,
            @RequestParam String baseTime,
            @RequestParam int nx,
            @RequestParam int ny) {

        // URL 구성
        StringBuilder url = new StringBuilder(BASE_URL);
        url.append("?").append("serviceKey").append("=").append(SERVICE_KEY);
        url.append("&").append("base_date").append("=").append(baseDate);
        url.append("&").append("base_time").append("=").append(baseTime);
        url.append("&").append("nx").append("=").append(nx);
        url.append("&").append("ny").append("=").append(ny);
        url.append("&").append("numOfRows").append("=").append(1000);
        url.append("&").append("pageNo").append("=").append(1);
        url.append("&").append("dataType").append("=").append("JSON");

        // RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();
        try {
            URI uri = new URI(url.toString()); // URI 객체 생성
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                // 성공적으로 데이터를 가져온 경우
                return ResponseEntity.ok(response.getBody());
            } else {
                // API 호출 실패
                return ResponseEntity.status(response.getStatusCode())
                        .body("Failed to fetch data from external API.");
            }
        } catch (URISyntaxException e) {
            // URI 구성 오류
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid URI: " + e.getMessage());
        } catch (Exception e) {
            // 기타 예외 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
}
