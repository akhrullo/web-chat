package com.akhrullo.webchat.contact;

import com.akhrullo.webchat.config.SessionContext;
import com.akhrullo.webchat.contact.dto.ContactCreateDto;
import com.akhrullo.webchat.contact.dto.ContactDto;
import com.akhrullo.webchat.user.User;
import com.akhrullo.webchat.exception.WebChatApiException;
import com.akhrullo.webchat.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


/**
 * Class {@code ContactServiceImpl} is implementation of interface {@link ContactService}
 * and intended to work with {@link Contact} objects.
 *
 * @author Akhrullo Ibrokhimov
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {
    private final ContactMapper mapper;
    private final UserService userService;
    private final ContactRepository repository;

    public ContactDto addContact(ContactCreateDto createDto) {
        User owner = SessionContext.getCurrentUser();
        User user = userService.findUserById(createDto.getUserId());

        // Check if the contact already exists
        if (repository.existsByOwnerAndUser(owner, user)) {
            throw WebChatApiException.contactAlreadyExists();
        }

        Contact contact = mapper.toEntity(createDto, owner, user);

        repository.save(contact);
        return mapper.toDto(contact);
    }

    @Override
    public Page<ContactDto> searchContact(String searchTerm, Pageable pageable) {
        User owner = SessionContext.getCurrentUser();

        Page<Contact> contactPage = repository.findByOwnerAndNameContainingOrUserEmailContaining(
                owner, searchTerm, searchTerm, pageable
        );

        return contactPage.map(mapper::toDto);
    }

    @Override
    public ContactDto getContactProfile(Long contactId) {
        Contact contact = repository.findById(contactId)
                .orElseThrow(WebChatApiException::contactNotFoundException);
        return mapper.toDto(contact);
    }

    @Override
    public void deleteContact(Long contactId) {
        User owner = SessionContext.getCurrentUser();

        Contact contact = repository.findByIdAndOwner(contactId, owner)
                .orElseThrow(WebChatApiException::contactNotFoundException);

        repository.delete(contact);
    }
}
