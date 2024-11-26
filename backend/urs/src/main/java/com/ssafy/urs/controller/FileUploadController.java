package com.ssafy.urs.controller;

import com.ssafy.urs.dto.CompletedRouteDto;
import com.ssafy.urs.service.CompletedRouteService;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class FileUploadController {

    private final CompletedRouteService completedRouteService;

    private final String UPLOAD_DIR = "C:\\Users\\sonjh\\Desktop\\workspace\\final-project\\uploadImg"; // 적절한 로컬 저장소 경로 설정

    @PostMapping("/upload/{userId}/{routeId}")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @PathVariable String userId,
                                             @PathVariable int routeId) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("파일이 비어 있습니다.");
        }

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String uniqueFileName = userId + "_" + routeId + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            completedRouteService.createCompletedRoute(new CompletedRouteDto(userId, routeId, filePath.toString()));

            return ResponseEntity.status(HttpStatus.OK).body("파일이 성공적으로 업로드되었습니다: " + file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("userId") String userId,
                                                 @RequestParam("routeId") String routeId) {
        try {
            String filePattern = userId + "_" + routeId + "_";
            File directory = new File(UPLOAD_DIR);
            File[] matchingFiles = directory.listFiles((dir, name) -> name.startsWith(filePattern));
            if (matchingFiles == null || matchingFiles.length == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            Path filePath = matchingFiles[0].toPath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
