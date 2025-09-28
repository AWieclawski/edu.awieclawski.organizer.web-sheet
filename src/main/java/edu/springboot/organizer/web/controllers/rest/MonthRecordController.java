package edu.springboot.organizer.web.controllers.rest;

import edu.springboot.organizer.web.dtos.MonthRecordDto;
import edu.springboot.organizer.web.services.MonthRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/${endpoint.month-records}")
@RequiredArgsConstructor
public class MonthRecordController {

    private final MonthRecordService monthRecordService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MonthRecordDto> getByMonthRecordById(@PathVariable String id) {
        try {
            MonthRecordDto visitors = monthRecordService.getMonthRecordById(id);
            return new ResponseEntity<>(visitors, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonthRecordDto>> getAllMonthRecords() {
        try {
            List<MonthRecordDto> monthRecords = monthRecordService.getAllMonthRecords();
            return new ResponseEntity<>(monthRecords, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/set/{setId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MonthRecordDto>> getMonthRecordsByUser(@PathVariable("setId") String setId) {
        try {
            List<MonthRecordDto> monthRecords = monthRecordService.getMonthRecordBySet(setId);
            return new ResponseEntity<>(monthRecords, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping(path = "/month/{month}/year/{year}/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<MonthRecordDto>> getMonthRecordsByMonthYearUser(
//            @PathVariable("month") int month,
//            @PathVariable("year") int year,
//            @PathVariable("userId") String userId) {
//        try {
//            List<MonthRecordDto> dateCells = monthRecordService.getMonthRecordByMonthYearUser(month, year,userId);
//            return new ResponseEntity<>(dateCells, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
