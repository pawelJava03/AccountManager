# Account Manager

Account Manager to aplikacja webowa w języku Java, stworzona przy użyciu technologii Spring Boot, HttpSessions, Thymeleaf, HTML/CSS oraz z zastosowaniem podejścia OOP. Aplikacja ta pełni rolę symulatora konta bankowego, umożliwiając wpłacanie, wypłacanie środków, inwestowanie oraz wypłacanie inwestycji na główne konto.

## Instrukcje instalacji

1. Pobierz projekt jako plik ZIP.
2. Utwórz bazę danych PostgreSQL, korzystając z poniższego skryptu SQL:

   ```sql
   create table users
   (
       id              serial primary key,
       name            varchar(50),
       surname         varchar(50),
       email           varchar(100),
       date_of_birth   date,
       account_balance numeric(10, 2) default 0,
       total_earned    numeric(10, 2) default 0,
       total_spent     numeric(10, 2) default 0,
       password        varchar(255) not null,
       invested_money  numeric(10, 2) default 0
   );

   alter table users owner to postgres;

Dodaj plik application.properties do projektu, a w nim uzupełnij dane dostępowe do bazy danych oraz konfigurację mailową:
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/nazwa_bazy
spring.datasource.username=twoja_nazwa_uzytkownika
spring.datasource.password=twoje_haslo
spring.datasource.driver-class-name=org.postgresql.Driver

# Mail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=twoj_adres_email
spring.mail.password=twoje_haslo_do_emaila
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

Uruchom aplikację, przechodząc do katalogu projektu i wykonując polecenie ./mvnw spring-boot:run.


1 Po uruchomieniu aplikacji, otwórz przeglądarkę i przejdź pod adres localhost:8080/login.
2 Możesz utworzyć nowe konto lub zalogować się do istniejącego.
3 Po zalogowaniu, aplikacja przeniesie Cię automatycznie na stronę główną.
4 Na podany adres e-mail otrzymasz powiadomienie potwierdzające utworzenie konta.

Autorzy

Autor: pawelJava03


Kontakt

Dla pytań lub informacji zwrotnych skontaktuj się z nami pod adresem pa.st.a@wp.pl.
