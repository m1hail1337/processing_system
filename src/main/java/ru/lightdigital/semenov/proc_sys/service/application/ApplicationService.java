package ru.lightdigital.semenov.proc_sys.service.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lightdigital.semenov.proc_sys.db.application.Application;
import ru.lightdigital.semenov.proc_sys.db.application.ApplicationDbService;
import ru.lightdigital.semenov.proc_sys.db.application.ApplicationStatus;
import ru.lightdigital.semenov.proc_sys.db.client.Client;
import ru.lightdigital.semenov.proc_sys.dto.application.ApplicationDto;
import ru.lightdigital.semenov.proc_sys.dto.application.creation.CreationResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private static final String EMPTY_NAME = "";

    private final ApplicationDbService applicationDbService;

    public ApplicationDto getApplication(long applicationId, Client client) {
        Application requestedApplication = applicationDbService.getApplication(applicationId);

        if (isApplicationBelongsToClient(client.getId(), requestedApplication)) {
            return convertToDto(requestedApplication);
        }

        throw new RuntimeException("Клиент пытается посмотреть чужую заявку.");
    }

    public CreationResponse createApplication(String message, Client client) {
        Application applicationToCreate = Application.builder()
                .client(client)
                .message(message)
                .creationDate(LocalDateTime.now())
                .build();
        Application createdApplication = applicationDbService.saveApplication(applicationToCreate);
        return CreationResponse.builder().applicationId(createdApplication.getId()).build();
    }

    @Transactional
    public void editApplicationMessage(Client client, String message, long applicationId) {
        Application application = applicationDbService.getApplication(applicationId);

        if (!isApplicationBelongsToClient(client.getId(), application)) {
            throw new RuntimeException("Клиент пытается изменить чужую заявку.");
        }

        if (application.getStatus() != ApplicationStatus.SKETCH) {
            throw new RuntimeException("Клиент пытается изменить отправленную заявку");
        }

        application.setMessage(message);
        applicationDbService.saveApplication(application);
    }

    public List<ApplicationDto> getApplications(Client client, Integer offset, String sortBy) {
        String clientId = client.getId();
        List<Application> page = applicationDbService.getFiveApplicationsForClient(clientId, offset, sortBy);
        return page.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * @param firstName может быть не целым именем пользователя, а частью. Например, "Iva" для имени "Ivan".
     */
    public List<ApplicationDto> getSentApplications(Integer offset, String sortBy, String firstName) {
        List<Application> page = applicationDbService.getFiveSentApplications(
                offset,
                sortBy,
                firstName != null ? firstName : EMPTY_NAME
        );
        return page.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * @param firstName может быть не целым именем пользователя, а частью. Например, "Iva" для имени "Ivan".
     */
    public List<ApplicationDto> getAllApplications(Integer offset, String sortBy, String firstName) {
        List<Application> page = applicationDbService.getFiveApplications(
                offset,
                sortBy,
                firstName != null ? firstName : EMPTY_NAME
        );
        return page.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public void send(Client client, long applicationId) {
        Application application = applicationDbService.getApplication(applicationId);

        if (!isApplicationBelongsToClient(client.getId(), application)) {
            throw new RuntimeException("Клиент пытается изменить чужую заявку.");
        }

        if (application.getStatus() != ApplicationStatus.SKETCH) {
            throw new RuntimeException("Клиент пытается изменить отправленную заявку");
        }

        application.setStatus(ApplicationStatus.SENT);
        applicationDbService.saveApplication(application);
    }

    public ApplicationDto getSentApplication(long applicationId) {
        Application application = applicationDbService.getApplication(applicationId);
        if (application.getStatus() == ApplicationStatus.SENT) {
            return convertToDto(application);
        }

        throw new RuntimeException("Заявка не находится в статусе SENT");
    }

    @Transactional
    public void changeStatus(long applicationId, ApplicationStatus status) {
        Application application = applicationDbService.getApplication(applicationId);

        if (application.getStatus() != ApplicationStatus.SENT) {
            throw new RuntimeException("Заявка не находится в статусе SENT");
        }
        if (status == ApplicationStatus.ACCEPTED || status == ApplicationStatus.DECLINED) {
            application.setStatus(status);
            applicationDbService.saveApplication(application);
            return;
        }

        throw new RuntimeException("Оператор не может установить статус заявки: " + status.name());
    }

    private ApplicationDto convertToDto(Application application) {
        return ApplicationDto.builder()
                .id(application.getId())
                .creationDate(application.getCreationDate())
                .status(application.getStatus())
                .message(application.getMessage())
                .firstName(application.getClient().getFirstName())
                .lastName(application.getClient().getLastName())
                .phoneNumber(application.getClient().getPhoneNumber())
                .build();
    }

    private boolean isApplicationBelongsToClient(String clientId, Application application) {
        return clientId.equals(application.getClient().getId());
    }
}
