package com.transactions.controller;

import com.transactions.model.Transaction;
import com.transactions.model.TransactionStatistics;
import com.transactions.service.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.*;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * TransactionApplication endpoints.
 */
@AllArgsConstructor
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
public class TransactionController {

    private static final Duration ONE_MINUTE = Duration.ofMinutes(1);

    private final Clock clock;
    StatisticsService service;

    private Boolean isValidISO8601Date(Instant timeStamp) {
        Boolean hasOffset = null;

//        System.out.println("Size:" + timeStamp.toString().length() );
        if(timeStamp.toString().length() < 20 || timeStamp.toString().length() > 24) {
            return false;
        }

        try {
            OffsetDateTime odt = OffsetDateTime.parse ( timeStamp.toString() );
            hasOffset = Boolean.TRUE;
            ZoneOffset offset = odt.getOffset ();
//            System.out.println ( "input: " + timeStamp.toString() + " | hasOffset: " + hasOffset + " | odt: " + odt + " | offset: " + offset );
        } catch ( java.time.format.DateTimeParseException e1 ) {
            // Perhaps input lacks offset-from-UTC. Try parsing as a local date-time.
            try {
                LocalDateTime ldt = LocalDateTime.parse ( timeStamp.toString() );
                hasOffset = Boolean.FALSE;
                System.out.println ( "input: " + timeStamp.toString() + " | hasOffset: " + hasOffset + " | ldt: " + ldt );
            } catch ( java.time.format.DateTimeParseException e2 ) {
                System.out.println ( "ERROR - Unexpected format in the input string" ); // FIXME: Handle format exception.
                return false;
            }
        }

        return true;
    }

    @PostMapping(path = "/transactions")
    public ResponseEntity<Void> postTransactions(HttpServletRequest request, @Valid @RequestBody Transaction transaction) {
        Instant now = clock.instant();

        if (transaction.getTimestamp().isBefore(now.minus(ONE_MINUTE))) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        }
        if (transaction.getTimestamp().isAfter(now) ||
                isValidISO8601Date(transaction.getTimestamp()) == false) {
            return ResponseEntity
                    .status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .build();
        }
        // save the incoming transaction
        service.add(transaction);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping(path = "/transactions")
    public ResponseEntity<Void> deleteTransactions() {
        service.clear();
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping(path = "/statistics")
    public TransactionStatistics getStatistics() {
        return service.getStatistics();
    }
}