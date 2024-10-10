package com.LocalFileUpload.localUpload;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadRepo extends JpaRepository<FileData,Integer> {
}
