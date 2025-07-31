package de.THM.devops.controller;


import de.THM.devops.dto.EntriesDto;
import de.THM.devops.service.EntriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/entries")
@CrossOrigin(origins = {"http://localhost:3000", "https://devops.westeurope.cloudapp.azure.com", "http://135.236.202.15"})
@RestController
public class EntriesController {

    private final EntriesService entriesService;

    public EntriesController(EntriesService entriesService) {
        this.entriesService = entriesService;
    }

    /**
     * Handles the creation of a new entry.
     *
     * @param entriesDto The data transfer object containing details of the entry to be created.
     * @return ResponseEntity containing the saved entry details and HTTP status CREATED.
     */
    @PostMapping
    public ResponseEntity<EntriesDto> createEntry(@RequestBody EntriesDto entriesDto) {
        EntriesDto savedEntry = entriesService.createEntry(entriesDto);
        return new ResponseEntity<>(savedEntry, HttpStatus.CREATED);
    }

    /**
     * Handles the retrieval of an existing entry by ID.
     *
     * @param entryId The ID of the entry to be retrieved.
     * @return ResponseEntity containing the retrieved entry details and HTTP status OK.
     */
    @GetMapping("{id}")
    public ResponseEntity<EntriesDto> getEntryById(@PathVariable("id") Long entryId) {
        EntriesDto entriesDto = entriesService.getEntryById(entryId);
        return ResponseEntity.ok(entriesDto);
    }

    /**
     * Returns a list of all entries.
     *
     * @return ResponseEntity containing a list of all entries and HTTP status OK.
     */
    @GetMapping
    public ResponseEntity<Iterable<EntriesDto>> getAllEntries() {
        Iterable<EntriesDto> entriesDtos = entriesService.getAllEntries();
        return ResponseEntity.ok(entriesDtos);
    }

    /**
     * Handles the update of an existing entry.
     *
     * @param entryId The ID of the entry to be updated.
     * @param updatedEntry The data transfer object containing the updated details of the entry.
     * @return ResponseEntity containing the updated entry details and HTTP status OK.
     */
    @PutMapping("{id}")
    public ResponseEntity<EntriesDto> updateEntry(@PathVariable("id") Long entryId, @RequestBody EntriesDto updatedEntry) {
        EntriesDto entriesDto = entriesService.updateEntry(entryId, updatedEntry);
        return ResponseEntity.ok(entriesDto);
    }

    /**
     * Handles the deletion of an existing entry by ID.
     *
     * @param entryId The ID of the entry to be deleted.
     * @return ResponseEntity with HTTP status NO_CONTENT upon successful deletion.
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable("id") Long entryId) {
        entriesService.deleteEntry(entryId);
        return ResponseEntity.noContent().build();
    }
}
