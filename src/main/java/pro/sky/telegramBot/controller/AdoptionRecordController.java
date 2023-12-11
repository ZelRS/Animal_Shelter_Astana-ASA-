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
    @Operation(summary = "Завести новую запись")
    public ResponseEntity<AdoptionRecord> addAdoptionRecord(
            @RequestParam Long userId,
            @RequestParam Integer trialPeriodDays,
            @RequestParam Long petId) {
        AdoptionRecord newAdoptionRecord = adoptionRecordService.createNewAdoptionRecord(
                userId,
                trialPeriodDays,
                petId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAdoptionRecord);
    }
}