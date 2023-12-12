package pro.sky.telegramBot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegramBot.model.adoption.AdoptionRecord;
import pro.sky.telegramBot.service.AdoptionRecordService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/adoption/record")
@Tag(name = "API для работы с записями об усыновлении")
public class AdoptionRecordController {
    private final AdoptionRecordService adoptionRecordService;
    @PostMapping
    @Operation(summary = "Завести новую запись об усыновлении")
    public ResponseEntity<AdoptionRecord> addAdoptionRecord(
            @RequestParam Long userId,
            @RequestParam Long petId) {
        AdoptionRecord newAdoptionRecord = adoptionRecordService.createNewAdoptionRecord(
                userId,
                petId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdoptionRecord);
    }
    @PutMapping("/{id}")
    @Operation(summary = "Продлить запись об усыновлении")
    public ResponseEntity<AdoptionRecord> extendAdoptionRecord(
            @RequestParam Long adoptionRecordId) {
        AdoptionRecord newAdoptionRecord = adoptionRecordService.extendAdoptionRecord(adoptionRecordId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdoptionRecord);
    }
    @PutMapping("/terminate/{id}")
    @Operation(summary = "Закрыть запись об усыновлении")
    public ResponseEntity<AdoptionRecord>  terminateAdoptionRecord(
            @RequestParam Long adoptionRecordId) {
        AdoptionRecord newAdoptionRecord = adoptionRecordService.terminateAdoptionRecord(adoptionRecordId);
        return ResponseEntity.status(HttpStatus.OK).body(newAdoptionRecord);
    }
}