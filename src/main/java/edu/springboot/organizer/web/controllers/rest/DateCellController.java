package edu.springboot.organizer.web.controllers.rest;

import edu.springboot.organizer.web.dtos.DateCellDto;
import edu.springboot.organizer.web.services.DateCellService;
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
@RequestMapping("/${endpoint.date-cells}")
@RequiredArgsConstructor
public class DateCellController {

    private final DateCellService dateCellService;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DateCellDto>> getAllDateCells() {
        try {
            List<DateCellDto> dateCells = dateCellService.getAllDateCells();
            return new ResponseEntity<>(dateCells, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/date/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DateCellDto>> getDateCellsByDate(@PathVariable String date) {
        try {
            List<DateCellDto> dateCells = dateCellService.getDateCellsByDate(date);
            return new ResponseEntity<>(dateCells, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/month-record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DateCellDto>> getDateCellsByMonthRecord(@PathVariable String id) {
        try {
            List<DateCellDto> visitors = dateCellService.getDateCellsDtosByMonthRecord(id);
            return new ResponseEntity<>(visitors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/date/{date}/month-record/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DateCellDto>> getDateCellsByDate(@PathVariable("date") String date,
                                                                @PathVariable("id") String id) {
        try {
            List<DateCellDto> dateCells = dateCellService.findDateCellsByDateAndMonthRecord(date, id);
            return new ResponseEntity<>(dateCells, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
