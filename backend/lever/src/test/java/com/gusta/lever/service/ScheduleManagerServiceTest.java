package com.gusta.lever.service;

import com.gusta.lever.dto.NewDateForPickupDTO;
import com.gusta.lever.dto.ProductDTO;
import com.gusta.lever.mocks.dto.ProductDTOMock;
import com.gusta.lever.mocks.model.ProductMock;
import com.gusta.lever.model.Product;
import com.gusta.lever.repository.ScheduleManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ScheduleManagerServiceTest {
    private final ScheduleManagerRepository repository = mock(ScheduleManagerRepository.class);
    private final MailService mailService = mock(MailService.class);
    private final ScheduleManagerService service = new ScheduleManagerService(repository, mailService);

    private final Product entity = ProductMock.generateMock(1);
    private final ProductDTO dto = ProductDTOMock.generateMock(1);
    private final List<Product> entityList = ProductMock.generateMockList(20);


    @BeforeEach
    public void setup() {
        reset(repository, mailService);
    }

    @Test
    void createScheduleTest() {
        ProductDTO result = service.createScheduling(dto);

        assertNotNull(result);
        assertNotNull(result.getUuid());
        assertFalse(result.getCollected());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(booleans = {true, false})
    void findAllByScheduleDateAndCollected(Boolean aBoolean) {
        if (aBoolean == null) {
            when(repository.findByScheduledDate(Pageable.unpaged(), LocalDate.MIN)).thenReturn(new PageImpl<>(entityList));

            Page<ProductDTO> result = service.findAllByScheduledDateAndCollected(Pageable.unpaged(), LocalDate.MIN, null);

            assertNotNull(result);
            result.forEach(p -> {
                assertNotNull(p.getUuid());
                assertEquals(LocalDate.MIN, p.getScheduledDate());
            });
        } else {
            List<Product> mappedEntityList = entityList.stream().filter(p -> p.getCollected() == aBoolean).toList();

            when(repository.findByScheduledDateAndCollected(Pageable.unpaged(), LocalDate.MIN, aBoolean)).thenReturn(new PageImpl<>(mappedEntityList));

            Page<ProductDTO> result = service.findAllByScheduledDateAndCollected(Pageable.unpaged(), LocalDate.MIN, aBoolean);
            result.getContent().forEach(p -> {
                assertNotNull(p.getUuid());
                assertEquals(aBoolean, p.getCollected());
            });
        }
    }

    @Test
    void updateCollected() {
        UUID uuid = UUID.randomUUID();
        when(repository.existsById(any(UUID.class))).thenReturn(true);

        service.updateCollected(uuid);

        verify(repository).existsById(uuid);
        verify(repository).updateCollectedStatus(uuid);
    }

    @Test
    void updateDate() {
        UUID uuid = UUID.randomUUID();
        NewDateForPickupDTO date = new NewDateForPickupDTO(LocalDate.MIN);
        when(repository.existsById(any(UUID.class))).thenReturn(true);
        when(repository.findCompanyEmailByUuid(uuid)).thenReturn(Optional.of("email"));

        service.updateDate(uuid, date);

        verify(repository).existsById(uuid);
        verify(repository).updateScheduledDate(uuid, dto.getScheduledDate());
        verify(mailService).warnClientAboutCollectDate("email", LocalDate.MIN.toString());
    }

}
