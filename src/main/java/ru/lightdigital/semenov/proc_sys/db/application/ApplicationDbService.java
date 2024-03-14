package ru.lightdigital.semenov.proc_sys.db.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ApplicationDbService {

    public static final int ELEMENTS_PER_PAGE = 5;

    private final ApplicationRepository repository;

    public Application getApplication(long applicationId) {
        return repository.findById(applicationId).orElseThrow(NoSuchElementException::new);
    }

    public Application saveApplication(Application application) {
        return repository.save(application);
    }

    public List<Application> getFiveApplicationsForClient(String clientId, Integer offset, String sortBy) {
        int offsetForQuery = countOffsetForQuery(offset);
        if (sortBy.equals("asc")) {
            return repository.findOrderByCreationDateAsc(clientId, offsetForQuery);
        } else if (sortBy.equals("desc")) {
            return repository.findOrderByCreationDateDesc(clientId, offsetForQuery);
        } else {
            throw new RuntimeException("Неизвестный вид сортировки");
        }
    }

    public List<Application> getFiveSentApplications(Integer offset, String sortBy, String firstName) {
        int offsetForQuery = countOffsetForQuery(offset);
        if (sortBy.equals("asc")) {
            return repository.findSentOrderByCreationDateAsc(offsetForQuery, firstName);
        } else if (sortBy.equals("desc")) {
            return repository.findSentOrderByCreationDateDesc(offsetForQuery, firstName);
        } else {
            throw new RuntimeException("Неизвестный вид сортировки");
        }
    }

    private int countOffsetForQuery(int offset) {
        return (offset - 1)  * ELEMENTS_PER_PAGE;
    }

    public List<Application> getFiveApplications(Integer offset, String sortBy, String firstName) {
        int offsetForQuery = countOffsetForQuery(offset);
        if (sortBy.equals("asc")) {
            return repository.findNotSketchOrderByCreationDateAsc(offsetForQuery, firstName);
        } else if (sortBy.equals("desc")) {
            return repository.findNotSketchOrderByCreationDateDesc(offsetForQuery, firstName);
        } else {
            throw new RuntimeException("Неизвестный вид сортировки");
        }
    }
}
