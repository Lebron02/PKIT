# **System Zakupu Kursów Online**

---

## **Opis projektu**
Projekt realizuje proces zakupu kursów online za pomocą modelowania procesów biznesowych w **Camunda BPM**. Aplikacja umożliwia użytkownikom:
- Wybór kursu.
- Przetwarzanie płatności.
- Przypisanie dostępu do kursu.
- Generowanie potwierdzenia zakupu.

Procesy są w pełni zautomatyzowane i zintegrowane z bazą danych **PostgreSQL**, co pozwala na śledzenie i zarządzanie zakupami użytkowników.

---

## **Funkcjonalności**

1. **Wybór kursu** – Użytkownik wybiera kurs spośród dostępnych opcji.
2. **Logowanie/Rejestracja** – Obsługa użytkowników z możliwością zakładania konta.
3. **Walidacja danych użytkownika** – Sprawdzenie poprawności wprowadzonych informacji.
4. **Przydzielenie zniżek** – Obliczenie zniżki w oparciu o tabelę **DMN**.
5. **Obsługa płatności** – Przetwarzanie i potwierdzanie płatności.
6. **Przypisanie dostępu do kursu** – Automatyczne przypisanie zakupionego kursu do użytkownika w bazie danych.
7. **Platforma kursów** – Przedstawienie tematów zawartych w kursie umożliwiając naukę.
8. **Generowanie certyfikatu** – Użytkownik otrzymuje potwierdzenie wykonania kursu.

---

## **Technologie**

| Technologia         | Opis                                          |
|----------------------|----------------------------------------------|
| **Java**            | Backend aplikacji.                          |
| **Camunda BPM**      | Modelowanie i zarządzanie procesami.         |
| **PostgreSQL**       | Baza danych dla użytkowników i kursów.       |
| **Spring Boot**      | Tworzenie mikroserwisów obsługujących logikę.|
| **Camunda Modeler**  | Tworzenie modeli BPMN i DMN.                |

---

## **Struktura projektu**

### **Worker-y:**
- `CourseSelectionWorker` – Obsługa wyboru kursu.
- `ConfirmPayment` – Obsługa potwierdzania płatności.
- `AssignCourseAccessWorker` – Przypisywanie kursów do użytkowników.

### **Baza danych:**
- Tabele:
  - `user_courses` – Przechowuje dane o zakupionych kursach.
  - `courses` – Przechowuje listę dostępnych kursów.
  - `users` – Przechowuje dane użytkowników.

### **Modele BPMN i DMN:**
- **BPMN:** Model procesu zakupowego.
- **DMN:** Tabela zniżek w oparciu o status użytkownika (np. student).

---

## **Wymagania**

1. **JDK 11+**
2. **Camunda Platform 8** lub **Camunda Platform 7**.
3. **Maven** – Do budowania projektu.

---
