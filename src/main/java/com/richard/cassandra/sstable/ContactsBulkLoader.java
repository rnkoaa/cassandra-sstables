package com.richard.cassandra.sstable;

import com.datastax.driver.core.UDTValue;
import com.datastax.driver.core.UserType;
import com.datastax.driver.core.utils.UUIDs;
import com.google.common.collect.Sets;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

/**
 * Created on 11/28/2016.
 * https://git1-us-west.apache.org/repos/asf?p=cassandra.git;a=blob;f=test/unit/org/apache/cassandra/io/sstable/CQLSSTableWriterTest.java;h=437e7a305de4207733f12f0b420c840269b26c59;hb=cc90d042
 */
public class ContactsBulkLoader {
    private final Set<Person> contacts = Sets.newHashSet();

    ContactsBulkLoader() {
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Richard")
                                     .lastName("Amoako Agyei")
                                     .middleName("Odame")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Jacqueline")
                                     .lastName("Amoako Agyei")
                                     .middleName("Adwubi")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Olive")
                                     .lastName("Amoako Agyei")
                                     .middleName("Grace")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("John")
                                     .lastName("Agyei")
                                     .middleName("Amoako")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Grace")
                                     .lastName("Agyei")
                                     .middleName("Amoako")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Anat")
                                     .lastName("Amoako")
                                     .middleName("Agyei")
                                     .build())
                           .build());

        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Patricia")
                                     .lastName("Sarpong")
                                     .middleName("Serwaah")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Augustus")
                                     .lastName("Sarpong")
                                     .middleName("Owusu")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Skylar")
                                     .lastName("Sarpong")
                                     .middleName("Simpson")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Thadaeus")
                                     .lastName("Sarpong")
                                     .middleName("Owusu")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Elijah")
                                     .lastName("Sarpong")
                                     .middleName("Owusu")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Mavis")
                                     .lastName("Owusu-Ansah")
                                     .middleName("Amoako Agyei")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Eric")
                                     .lastName("Owusu-Ansah")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Erica")
                                     .lastName("Owusu-Ansah")
                                     .middleName("Andrea")
                                     .build())
                           .build());
        contacts.add(Person.builder()
                           .id(UUIDs.random())
                           .createdAt(LocalDateTime.now())
                           .updatedAt(LocalDateTime.now())
                           .name(Name.builder()
                                     .firstName("Javon")
                                     .lastName("Owusu-Ansah")
                                     .middleName("Manuel")
                                     .build())
                           .build());
    }

    private static String CONTACT_NAME_TYPE = "CREATE TYPE IF NOT EXISTS user_contacts.contact_name (lastName TEXT, firstName TEXT, middleName TEXT)";

    private static final String CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS user_contacts.person (" +
            "id UUID, " +
            "name contact_name, " +
            "created_at TIMESTAMP, " +
            "updated_at TIMESTAMP, " +
            "PRIMARY KEY(id, created_at))" +
            "WITH CLUSTERING ORDER BY (created_at ASC)";

    private static final String CONTACT_TABLE_INSERT = "INSERT INTO user_contacts.person(id, name, created_at, updated_at)" +
            "values (?, ?, ?, ?)";

    public void load() {
        CQLSSTableWriter writer = CQLSSTableWriter.builder()
                                                  .inDirectory("data/sstables/user_contacts/person")
                                                  .withType(CONTACT_NAME_TYPE)
                                                  .forTable(CONTACT_TABLE)
                                                  .using(CONTACT_TABLE_INSERT).build();

        UserType contactNameUDT = writer.getUDType("contact_name");

        contacts.forEach(contact -> {
            final UDTValue udtValue = contactNameUDT.newValue().setString("lastName", contact.getName().getLastName())
                                                    .setString("firstName", contact.getName().getFirstName())

                                                    .setString("middleName", contact.getName().getMiddleName());
            try {
                writer.addRow(contact.getId(), udtValue, toDate(contact.getCreatedAt()), toDate(contact.getUpdatedAt()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            writer.close();
        } catch (IOException ignore) {
        }
    }

    public Date toDate(LocalDateTime localDateTime) {
        Date in = new Date();
        LocalDateTime ldt = LocalDateTime.ofInstant(in.toInstant(), ZoneId.systemDefault());
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
