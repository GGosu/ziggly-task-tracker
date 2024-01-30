package com.ziggly.timeservice.api;

import com.ziggly.model.dto.TimeEntryDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface TimeEntryService {

    @PostMapping("/api/time-entries")
    ResponseEntity<String> createTimeEntry(@RequestBody TimeEntryDTO timeEntryDTO, HttpServletRequest request);

    @GetMapping("/api/time-entries/{timeEntryId}")
    ResponseEntity<TimeEntryDTO> getTimeEntry(@PathVariable Integer timeEntryId, HttpServletRequest request);

    @PutMapping("/api/time-entries/{timeEntryId}")
    ResponseEntity<String> updateTimeEntry(@PathVariable Integer timeEntryId, @RequestBody TimeEntryDTO timeEntryDTO, HttpServletRequest request);

    @DeleteMapping("/api/time-entries/{timeEntryId}")
    ResponseEntity<String> deleteTimeEntry(@PathVariable Integer timeEntryId, HttpServletRequest request);

    @GetMapping("/api/time-entries")
    ResponseEntity<List<TimeEntryDTO>> getAllTimeEntries(HttpServletRequest request);

}
