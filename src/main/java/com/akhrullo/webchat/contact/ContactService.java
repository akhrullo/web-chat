package com.akhrullo.webchat.contact;

import com.akhrullo.webchat.contact.dto.ContactCreateDto;
import com.akhrullo.webchat.contact.dto.ContactDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing contacts.
 * Provides methods to get, add, update, and remove contacts.
 */
public interface ContactService {

    ContactDto addContact(ContactCreateDto createDto);

    ContactDto getContactProfile(Long contactId);

    void deleteContact(Long contactId);

    Page<ContactDto> searchContact(String searchTerm, Pageable pageable);
}
