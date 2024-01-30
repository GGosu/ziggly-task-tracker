package com.ziggly.timeservice.repository;

import com.ziggly.model.timeentry.TimeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeEntryRepository extends JpaRepository<TimeEntry, Integer> {

}
