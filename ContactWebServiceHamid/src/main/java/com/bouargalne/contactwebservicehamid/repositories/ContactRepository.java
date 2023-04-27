package com.bouargalne.contactwebservicehamid.repositories;

import com.bouargalne.contactwebservicehamid.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact,Long> {
}
