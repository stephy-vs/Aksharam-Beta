package com.LocalFileUpload.localUpload;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalUploadService {
    @Autowired
    private UploadRepo uploadRepo;
    @Value("${app.file.upload-dir}")
    private String uploadDir;

    //private final String uploadDir = "newVideo/";

    public ResponseEntity<?> uploadVideoFileData(MultipartFile file) {
        if (file.isEmpty()){
            return new ResponseEntity<>("Please select a file to upload", HttpStatus.NOT_FOUND);
        }
        try {
            File dir = new File(uploadDir);
            if (!dir.exists()){
                dir.mkdirs();
            }
            File videoFile = new File(dir,file.getOriginalFilename());
            file.transferTo(videoFile);

            FileData fileData = new FileData();
            fileData.setFile_name(file.getOriginalFilename());
            String name = file.getOriginalFilename();
            uploadRepo.save(fileData);
            return new ResponseEntity<>(name+" file uploaded successfully : "+videoFile.getAbsolutePath(),HttpStatus.OK);
        }catch (IOException e){
            return new ResponseEntity<>("Failed to upload file"+e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<StreamingResponseBody> getVideoFile(String fileName, String rangeHeader) {
        try {
            Path filePath = Paths.get(getUploadDir()).resolve(fileName).normalize();
            org.springframework.core.io.Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()){
                return ResponseEntity.notFound().build();
            }
            long fileLength = resource.contentLength();
             long start =0;
             long end = fileLength-1;
            if (rangeHeader !=null){
                String[] ranges = rangeHeader.replace("bytes=","").split("-");
                start = Long.parseLong(ranges[0]);
                if (ranges.length > 1 && !ranges[1].isEmpty()){
                    end = Long.parseLong(ranges[1]);
                }
            }
            final  long finalStart = start;
            final long finalEnd = end;
            long contentLength = finalEnd-finalStart+1;
            HttpHeaders headers = new HttpHeaders();//
            headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM_VALUE);
            headers.add(HttpHeaders.CONTENT_LENGTH,String.valueOf(contentLength));
            headers.add(HttpHeaders.CONTENT_RANGE,"bytes "+start + "_"+end + "/" + fileLength);
            headers.add(HttpHeaders.ACCEPT_RANGES, "bytes");
            StreamingResponseBody responseBody = outputStream -> {
                try (InputStream inputStream = resource.getInputStream()){
                    inputStream.skip(finalStart);
                    byte[] buffer = new byte[1024];
                    long byteRead;
                    long totalBytesRead =0;

                    while ((byteRead = inputStream.read(buffer)) != -1 && totalBytesRead < contentLength){
                        outputStream.write(buffer,0,(int)  Math.min(byteRead,contentLength-totalBytesRead));
                        totalBytesRead += byteRead;
                    }

                }catch (IOException e){
                    throw new RuntimeException("Error reading file",e);
                }
            };
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers).body(responseBody);
        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String getUploadDir() {
        return uploadDir;
    }
}
