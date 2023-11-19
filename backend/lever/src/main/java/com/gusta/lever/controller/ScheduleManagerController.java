package com.gusta.lever.controller;

import com.gusta.lever.dto.NewDateForPickupDTO;
import com.gusta.lever.dto.ProductDTO;
import com.gusta.lever.service.ScheduleManagerService;
import com.gusta.lever.utils.ParameterValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/lever")
@RequiredArgsConstructor
public class ScheduleManagerController {
    private final ScheduleManagerService service;

    @PostMapping(value = "/create")
    public ResponseEntity<ProductDTO> createSchedule(@RequestBody ProductDTO dto) {
        ParameterValidation.checkIfIsNullOrBlankThrowingEx(dto.allNullOrEmpty());
        return new ResponseEntity<>(service.createScheduling(dto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/find-all")
    public ResponseEntity<Page<ProductDTO>> findAllByScheduledDate(
            Pageable pageable,
            @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam(name = "collected", required = false) Boolean isCollected) {
        return new ResponseEntity<>(service.findAllByScheduledDateAndCollected(pageable, date, isCollected), HttpStatus.OK);
    }

    @PatchMapping(value = "update-collected/{uuid}")
    public ResponseEntity updateCollected(@PathVariable(value = "uuid") UUID productUUID) {
        if (productUUID == null) {
            throw new IllegalArgumentException("Please put a valid value");
        }
        service.updateCollected(productUUID);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "update-date/{uuid}")
    public ResponseEntity updateDate(@RequestBody NewDateForPickupDTO dto, @PathVariable(value = "uuid") UUID productUUID) {
        if (productUUID == null || dto.getDate() == null || dto.getDate().toString().isBlank()) {
            throw new IllegalArgumentException("Please put a valid value");
        }
        service.updateDate(productUUID, dto);
        return ResponseEntity.noContent().build();
    }
}
