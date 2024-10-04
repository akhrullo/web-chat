package com.akhrullo.webchat.contact;

import com.akhrullo.webchat.contact.dto.ContactCreateDto;
import com.akhrullo.webchat.contact.dto.ContactDto;
import com.akhrullo.webchat.contact.dto.ContactUpdateDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Class {@code ContactController} handles HTTP requests related to contact management.
 * It provides endpoints for adding, searching, retrieving, and deleting contacts.
 * This controller uses the {@link ContactService} to perform operations on contacts.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.1
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
public class ContactController {
    private final ContactService contactService;

    /**
     * Adds a new contact.
     *
     * @param createDto the DTO containing details for the new contact
     * @return ResponseEntity with the created ContactDto and HTTP status 201 Created
     */
    @PostMapping
    public ResponseEntity<ContactDto> addContact(@Valid @RequestBody ContactCreateDto createDto) {
        ContactDto contactDto = contactService.addContact(createDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactDto);
    }

    /**
     * Searches for contacts based on the owner ID and search term.
     *
     * @param searchTerm the term to search contacts by name or associated user email
     * @param page the page number for pagination
     * @param size the number of contacts per page
     * @return ResponseEntity with a Page of ContactDto and HTTP status 200 OK
     */
    @GetMapping
    public ResponseEntity<Page<ContactDto>> searchContacts(
            @RequestParam("search_term") String searchTerm,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContactDto> contactPage = contactService.searchContact(searchTerm, pageable);
        return ResponseEntity.ok(contactPage);
    }

    /**
     * Retrieves the profile of a contact by ID.
     *
     * @param contactId the ID of the contact to retrieve
     * @return ResponseEntity with the ContactDto and HTTP status 200 OK
     */
    @GetMapping("/{contactId}")
    public ResponseEntity<ContactDto> getContactProfile(@PathVariable("contactId") Long contactId) {
        ContactDto contactDto = contactService.getContactProfile(contactId);
        return ResponseEntity.ok(contactDto);
    }

    /**
     * Updates a contact by ID.
     *
     * @param contactId the ID of the contact to update
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @PutMapping("/{contactId}")
    public ResponseEntity<ContactDto> updateContact(@PathVariable("contactId") Long contactId, @Valid @RequestBody ContactUpdateDto updateDto) {
        ContactDto contactDto = contactService.updateContact(contactId, updateDto);
        return ResponseEntity.ok(contactDto);
    }

    /**
     * Deletes a contact by ID.
     *
     * @param contactId the ID of the contact to delete
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable("contactId") Long contactId) {
        contactService.deleteContact(contactId);
        return ResponseEntity.noContent().build();
    }
}

