package ru.lightdigital.semenov.proc_sys.db.application;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("""
            SELECT a
            FROM Application a
            WHERE a.client.id = :clientId
            ORDER BY creationDate ASC
            LIMIT 5 OFFSET :offset
            """)
    List<Application> findOrderByCreationDateAsc(String clientId, Integer offset);

    @Query("""
            SELECT a
            FROM Application a
            WHERE a.client.id = :clientId
            ORDER BY creationDate DESC
            LIMIT 5 OFFSET :offset
            """)
    List<Application> findOrderByCreationDateDesc(String clientId, Integer offset);

    @Query("""
            SELECT a
            FROM Application a
            WHERE a.status = ApplicationStatus.SENT AND a.client.firstName ILIKE :firstName%
            ORDER BY creationDate ASC
            LIMIT 5 OFFSET :offset
            """)
    List<Application> findSentOrderByCreationDateAsc(int offset, String firstName);

    @Query("""
            SELECT a
            FROM Application a
            WHERE a.status = ApplicationStatus.SENT AND a.client.firstName ILIKE :firstName%
            ORDER BY creationDate DESC
            LIMIT 5 OFFSET :offset
            """)
    List<Application> findSentOrderByCreationDateDesc(int offset, String firstName);

    @Query("""
            SELECT a
            FROM Application a
            WHERE a.status != ApplicationStatus.SKETCH AND a.client.firstName ILIKE :firstName%
            ORDER BY creationDate ASC
            LIMIT 5 OFFSET :offset
            """)
    List<Application> findNotSketchOrderByCreationDateAsc(int offset, String firstName);

    @Query("""
            SELECT a
            FROM Application a
            WHERE a.status != ApplicationStatus.SKETCH AND a.client.firstName ILIKE :firstName%
            ORDER BY creationDate DESC
            LIMIT 5 OFFSET :offset
            """)
    List<Application> findNotSketchOrderByCreationDateDesc(int offset, String firstName);
}
