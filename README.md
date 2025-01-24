System Zakupu Kursów Online

Opis projektu
Projekt realizuje proces zakupu kursów online za pomocą modelowania procesów biznesowych w Camunda BPM. Aplikacja umożliwia użytkownikom wybór kursu, przetwarzanie płatności, przypisanie dostępu do kursu oraz generowanie potwierdzenia zakupu.

Procesy są w pełni zautomatyzowane i zintegrowane z bazą danych PostgreSQL, co pozwala na śledzenie i zarządzanie zakupami użytkowników.

Funkcjonalności
Wybór kursu – Użytkownik wybiera kurs spośród dostępnych opcji.
Logowanie/Rejestracja – Obsługa użytkowników z możliwością zakładania konta.
Walidacja danych użytkownika – Sprawdzenie poprawności wprowadzonych informacji.
Przydzielenie zniżek – Obliczenie zniżki w oparciu o tabelę DMN.
Obsługa płatności – Przetwarzanie i potwierdzanie płatności.
Przypisanie dostępu do kursu – Automatyczne przypisanie zakupionego kursu do użytkownika w bazie danych.
Generowanie potwierdzenia – Użytkownik otrzymuje potwierdzenie zakupu.

Technologie
Java – Backend aplikacji.
Camunda BPM – Modelowanie i zarządzanie procesami biznesowymi.
PostgreSQL – Baza danych dla przechowywania informacji o użytkownikach i kursach.
Spring Boot – Tworzenie mikroserwisów obsługujących logikę procesów.
Camunda Modeler – Tworzenie modeli BPMN i DMN.

Struktura projektu
Worker-y:
CourseSelectionWorker – Obsługa wyboru kursu.
ConfirmPayment – Obsługa potwierdzania płatności.
AssignCourseAccessWorker – Przypisywanie kursów do użytkowników.

Baza danych:
Tabele: user_courses, courses, users.
Informacje o zakupionych kursach zapisywane w tabeli user_courses.

Modele BPMN i DMN:
BPMN: Model procesu zakupowego.
DMN: Tabela zniżek w oparciu o status użytkownika (np. student, żołnierz).

Wymagania
JDK 11+
PostgreSQL (z utworzonym schematem elearning i tabelami user_courses, courses, users)
Camunda Platform 8 lub Camunda Platform 7
Maven – Do budowania projektu
Instalacja i uruchomienie
Skonfiguruj bazę danych:

Upewnij się, że baza PostgreSQL jest uruchomiona.
Utwórz tabele:
courses – Przechowuje listę dostępnych kursów.
user_courses – Przechowuje dane o zakupionych kursach.
users – Przechowuje dane użytkowników.

Uruchom aplikację:
mvn spring-boot:run

Zaloguj się do Camunda Operate:
Śledź procesy w czasie rzeczywistym.
