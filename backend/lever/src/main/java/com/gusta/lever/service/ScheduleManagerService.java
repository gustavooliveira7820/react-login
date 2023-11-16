package com.gusta.lever.service;

import com.gusta.lever.dto.NewDateForPickupDTO;
import com.gusta.lever.dto.ProductDTO;
import com.gusta.lever.model.Product;
import com.gusta.lever.repository.ScheduleManagerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleManagerService {
    private final ScheduleManagerRepository repository;
    private final MailService mailService;

    public ProductDTO createScheduling(ProductDTO dto) {
        dto.setUuid(UUID.randomUUID());
        dto.setCollected(false);
        Product product = new Product(
                dto.getUuid(),
                dto.getCompany(),
                dto.getCompanyEmail(),
                dto.getCnpj(),
                dto.getAddress(),
                dto.getProdType(),
                dto.getWeight(),
                dto.getAmount(),
                dto.getScheduledDate(),
                dto.getWarnings(),
                dto.getCollected()
        );
        repository.save(product);
        mailService.pickupRequestMail(dto);
        return dto;
    }

    public Page<ProductDTO> findAllByScheduledDateAndCollected(Pageable pageable, LocalDate date, Boolean isCollected) {
        List<ProductDTO> productDTOList;
        if (isCollected == null) {
            productDTOList = repository.findByScheduledDate(pageable, date).stream()
                    .map(e -> new ProductDTO(e.getUuid(),
                            e.getCompany(),
                            e.getCompanyEmail(),
                            e.getCnpj(),
                            e.getAddress(),
                            e.getProdType(),
                            e.getWeight(),
                            e.getAmount(),
                            e.getScheduledDate(),
                            e.getWarnings(),
                            e.getCollected()))
                    .toList();
        } else {
            productDTOList = repository.findByScheduledDateAndCollected(pageable, date, isCollected).stream()
                    .map(e -> new ProductDTO(e.getUuid(),
                            e.getCompany(),
                            e.getCompanyEmail(),
                            e.getCnpj(),
                            e.getAddress(),
                            e.getProdType(),
                            e.getWeight(),
                            e.getAmount(),
                            e.getScheduledDate(),
                            e.getWarnings(),
                            e.getCollected()))
                    .toList();
        }

        return new PageImpl<>(productDTOList, pageable, productDTOList.size());
    }

    @Transactional
    public void updateCollected(UUID productUUID) {
        if (!repository.existsById(productUUID)) {
            throw new EntityNotFoundException("Unable to locate user with uuid: " + productUUID);
        }
        repository.updateCollectedStatus(productUUID);
    }

    @Transactional
    public void updateDate(UUID productUUID, NewDateForPickupDTO dto) {
        if (repository.existsById(productUUID)) {
            repository.updateScheduledDate(productUUID, dto.getDate());
            Optional<String> companyEmail = repository.findCompanyEmailByUuid(productUUID);
            mailService.warnClientAboutCollectDate(companyEmail.get(), dto.getDate().toString());
        }
    }
}
