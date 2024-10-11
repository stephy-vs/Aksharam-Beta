package com.LocalFileUpload.localUpload;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@CrossOrigin
@RequestMapping(path = "file/Video")
public class LocalUploadController {
    @Autowired
    private LocalUploadService localUploadService;

    @PostMapping(path = "/upload")
    public ResponseEntity<?> uploadVideoFile(@RequestParam("file")MultipartFile file){
        try {
            return localUploadService.uploadVideoFileData(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("Something went wrong", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping(path = "/download/{fileName:.+}")
    public ResponseEntity<StreamingResponseBody> downloadFile(@PathVariable String fileName,
                                                              @RequestHeader(value = "Range",required = false) String rangeHeader){
        try {
            return localUploadService.getVideoFile(fileName,rangeHeader);
        }catch (Exception e){
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
