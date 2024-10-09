package com.example.MuseumTicketing.tribal.tribMalayalam;

import com.example.MuseumTicketing.tribal.tribEnglish.TribalEnglish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TribalMalayalamRepo extends JpaRepository<TribalMalayalam,Integer> {
    Optional<TribalMalayalam> findByTribMalUid(String malId);

    Optional<TribalMalayalam> findByTitle(String title);
    Page<TribalMalayalam> findByTribMalUid(String tribMalUid, Pageable pageable);

    List<TribalMalayalam> findBytribMalUid(String malId);


}
