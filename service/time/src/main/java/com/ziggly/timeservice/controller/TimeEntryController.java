package com.ziggly.timeservice.controller;

import com.ziggly.model.dto.TimeEntryDTO;
import com.ziggly.timeservice.api.TimeEntryService;
import com.ziggly.timeservice.service.TimeEntryServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeEntryController implements TimeEntryService {
    @Autowired
    private TimeEntryServiceImpl timeEntryService;

    @Override
    public ResponseEntity<String> createTimeEntry(TimeEntryDTO timeEntryDTO, HttpServletRequest request) {
        return timeEntryService.createTimeEntry(timeEntryDTO, request);
    }

    @Override
    public ResponseEntity<TimeEntryDTO> getTimeEntry(Integer timeEntryId, HttpServletRequest request) {
        return timeEntryService.getTimeEntry(timeEntryId, request);
    }

    @Override
    public ResponseEntity<String> updateTimeEntry(Integer timeEntryId, TimeEntryDTO timeEntryDTO, HttpServletRequest request) {
        return timeEntryService.updateTimeEntry(timeEntryId, timeEntryDTO, request);
    }

    @Override
    public ResponseEntity<String> deleteTimeEntry(Integer timeEntryId, HttpServletRequest request) {
        return timeEntryService.deleteTimeEntry(timeEntryId, request);
    }

    @Override
    public ResponseEntity<List<TimeEntryDTO>> getAllTimeEntries(HttpServletRequest request) {
        return timeEntryService.getAllTimeEntries(request);
    }
}
