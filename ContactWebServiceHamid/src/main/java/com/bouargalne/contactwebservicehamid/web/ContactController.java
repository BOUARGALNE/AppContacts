package com.bouargalne.contactwebservicehamid.web;

import com.bouargalne.contactwebservicehamid.entities.Contact;
import com.bouargalne.contactwebservicehamid.repositories.ContactRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ContactController {
    private final ContactRepository contactRepository;

    @GetMapping("/contacts")
    public List<Contact> all() {
        return contactRepository.findAll();
    }

    @PostMapping("/contact")
    Contact newContact(@RequestBody Contact newContact) {
        return contactRepository.save(newContact);
    }

    @GetMapping("/contacts/{id}")
    Contact one(@PathVariable Long id) {
        return contactRepository.findById(id).isPresent() ? contactRepository.findById(id).get() : new Contact();
    }

    @DeleteMapping("/deleteContacts/{id}")
    void deleteContact(@PathVariable Long id) {

        System.out.println("delete" + id);
        contactRepository.deleteById(id);
    }

    @PutMapping("/contacts/{id}")
    Contact updateContact(@RequestBody Contact contact, @PathVariable Long id) {
        return contactRepository.findById(id)
                .map(contact1 -> {
                    contact1.setFirst_name(contact.getFirst_name());
                    contact1.setPhone(contact.getPhone());
                    contact1.setLast_name(contact.getLast_name());
                    contact1.setEmail(contact.getEmail());
                    contact1.setPhoto(contact.getPhoto());
                    return contactRepository.save(contact1);
                })
                .orElseGet(() -> {
                    contact.setId(id);
                    return contactRepository.save(contact);
                });
    }

}