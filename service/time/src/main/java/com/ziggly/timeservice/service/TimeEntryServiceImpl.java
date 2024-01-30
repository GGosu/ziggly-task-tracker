package com.ziggly.timeservice.service;

import com.ziggly.model.dto.TimeEntryDTO;
import com.ziggly.model.timeentry.TimeEntry;
import com.ziggly.model.utils.Utils;
import com.ziggly.timeservice.api.TimeEntryService;
import com.ziggly.timeservice.repository.TimeEntryRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TimeEntryServiceImpl implements TimeEntryService {

    @Autowired
    private TimeEntryRepository timeEntryRepository;
    @Override
    public ResponseEntity<String> createTimeEntry(TimeEntryDTO timeEntryDTO, HttpServletRequest request) {
        if (timeEntryDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Time entry data is missing!");
        }

        if (timeEntryDTO.getTaskId() == null || Utils.getUserId(request) == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Task ID and User ID are required!");
        }

        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setTaskId(timeEntryDTO.getTaskId());

        timeEntry.setUserId(Utils.getUserId(request));

        timeEntry.setStartTime(timeEntryDTO.getStartTime());
        timeEntry.setEndTime(timeEntryDTO.getEndTime());

        timeEntry.setNotes(timeEntryDTO.getNotes());
        timeEntry.setCreatedAt(System.currentTimeMillis());

        timeEntryRepository.save(timeEntry);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Time Entry created successfully!");

    }

    @Override
    public ResponseEntity<TimeEntryDTO> getTimeEntry(Integer timeEntryId, HttpServletRequest request) {
        Optional<TimeEntry> timeEntryOptional = timeEntryRepository.findById(timeEntryId);
        if (timeEntryOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        TimeEntry timeEntry = timeEntryOptional.get();
        TimeEntryDTO timeEntryDTO = new TimeEntryDTO();

        BeanUtils.copyProperties(timeEntry, timeEntryDTO);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(timeEntryDTO);
    }

    @Override
    public ResponseEntity<String> updateTimeEntry(Integer timeEntryId, TimeEntryDTO timeEntryDTO, HttpServletRequest request) {

        if (timeEntryDTO == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Time entry data is missing!");
        }

        Optional<TimeEntry> timeEntryOptional = timeEntryRepository.findById(timeEntryId);
        if (timeEntryOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Time entry not found!");
        }

        TimeEntry timeEntry = timeEntryOptional.get();

        timeEntry.setTaskId(timeEntryDTO.getTaskId());
        // timeEntry.setUserId(timeEntryDTO.getUserId());
        timeEntry.setStartTime(timeEntryDTO.getStartTime());
        timeEntry.setEndTime(timeEntryDTO.getEndTime());

        timeEntry.setNotes(timeEntryDTO.getNotes());

        timeEntryRepository.save(timeEntry);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Time entry updated successfully!");

    }

    @Override
    public ResponseEntity<String> deleteTimeEntry(Integer timeEntryId, HttpServletRequest request) {
        if (!timeEntryRepository.existsById(timeEntryId)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Time entry not found!");
        }

        timeEntryRepository.deleteById(timeEntryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Time entry deleted successfully!");
    }

    @Override
    public ResponseEntity<List<TimeEntryDTO>> getAllTimeEntries(HttpServletRequest request) {

        List<TimeEntry> timeEntries = timeEntryRepository.findAll();
        List<TimeEntryDTO> timeEntryDTOs = timeEntries.stream()
                .map(timeEntry -> {
                    TimeEntryDTO dto = new TimeEntryDTO();
                    BeanUtils.copyProperties(timeEntry, dto);
                    return dto;
                }).collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(timeEntryDTOs);
    }
}
