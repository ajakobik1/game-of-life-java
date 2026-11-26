module org.example {
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires java.sql;
    exports org.example;
    exports org.example.exceptions;  // Eksportuj odpowiednie pakiety
}