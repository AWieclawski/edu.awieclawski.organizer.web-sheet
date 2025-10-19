package edu.springboot.organizer.web.controllers.rest;

import edu.springboot.organizer.web.dtos.VisitorDto;
import edu.springboot.organizer.web.services.VisitorService;
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
@RequestMapping("/${endpoint.visitors}")
@RequiredArgsConstructor
public class VisitorController {

    private final VisitorService visitorService;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VisitorDto>> getAllVisitors() {
        try {
            List<VisitorDto> visitors = visitorService.getAllVisitors();
            return new ResponseEntity<>(visitors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/between/{start}/{end}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VisitorDto>> getVisitorsBetween(@PathVariable String start, @PathVariable String end) {
        try {
            List<VisitorDto> visitors = visitorService.getVisitorsBetweenDates(start, end);
            return new ResponseEntity<>(visitors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
