# QuizPlatform - Backend Microservices 🚀

[🇹🇷 Türkçe Sürüme Git (Go to Turkish Version)](#türkçe-sürüm)

## 📖 About The Project
QuizPlatform Backend is a robust, highly scalable, and production-ready microservices architecture built with **Java** and **Spring Boot 3**. It handles user authentication, quiz management, and dynamic evaluation routing, designed to support high concurrency and strict role-based security.

## 🏗️ Architecture
The system is divided into decoupled microservices communicating through a secure gateway:
* **Gateway Server (`gatewayserver`):** The main entry point. Handles routing and initial request filtering using Spring Cloud Gateway and WebFlux.
* **Service Registry (`eurekaserver`):** Spring Cloud Netflix Eureka server for internal service discovery.
* **Auth Service (`authservice`):** Manages user registration, login, and generates stateless JSON Web Tokens (JWT).
* **Quiz Service (`quizservice`):** The core domain service. Manages categories, topics, quizzes, questions, and attempts. Includes its own JWT validation for zero-trust security.

## 💻 Tech Stack
* **Core:** Java 17+, Spring Boot 3
* **Microservices:** Spring Cloud Gateway, Spring Cloud Netflix Eureka
* **Security:** Spring Security, JJWT (Stateless JSON Web Tokens), Role-Based Access Control (RBAC)
* **Database:** PostgreSQL (Spring Data JPA, Hibernate)
* **Caching / Rate Limiting:** Redis (Configured via Gateway)
* **Build Tool:** Maven, Lombok

## ✨ Key Features
* **Stateless Authentication:** JWT-based authentication eliminates session state on servers.
* **Defence-in-Depth Security:** Endpoint protection at both the API Gateway level and the internal microservice level (`@PreAuthorize`).
* **Clean Architecture:** Strict separation of concerns using Data Transfer Objects (DTOs) to protect internal entities.
* **Standardized Error Handling:** Global exception handlers returning consistent JSON responses (`ApiResponse<T>`).

## 🚀 How to Run
1. Ensure **PostgreSQL** and **Redis** are running locally.
2. Start the services in the following strict order:
   * `eurekaserver` (Port: 8761)
   * `authservice` (Port: 8081)
   * `quizservice` (Port: 8082)
   * `gatewayserver` (Port: 8080)
3. The API is now accessible at `http://localhost:8080`.

---

<a name="türkçe-sürüm"></a>
# QuizPlatform - Backend Mikroservisleri 🚀

## 📖 Proje Hakkında
QuizPlatform Backend, **Java** ve **Spring Boot 3** kullanılarak inşa edilmiş, yüksek oranda ölçeklenebilir ve canlı ortama (production) hazır bir mikroservis mimarisidir. Kullanıcı kimlik doğrulamasını, quiz yönetimini ve yüksek eşzamanlı trafiği kaldıracak şekilde tasarlanmıştır.

## 🏗️ Mimari
Sistem, güvenli bir ağ geçidi üzerinden haberleşen bağımsız mikroservislere bölünmüştür:
* **Gateway Server (`gatewayserver`):** Sistemin ana giriş kapısıdır. Spring Cloud Gateway ve WebFlux kullanarak yönlendirme (routing) yapar.
* **Service Registry (`eurekaserver`):** Spring Cloud Netflix Eureka sunucusudur. Servislerin birbirini bulmasını sağlayan telefon rehberi görevi görür.
* **Auth Service (`authservice`):** Kullanıcı kayıt/giriş işlemlerini ve durumsuz (stateless) JWT üretimini üstlenir.
* **Quiz Service (`quizservice`):** Temel iş mantığının bulunduğu servistir. Kategoriler, konular, quizler ve soruları yönetir. Zero-trust güvenliği için kendi JWT doğrulama mekanizmasına sahiptir.

## 💻 Kullanılan Teknolojiler
* **Temel:** Java 17+, Spring Boot 3
* **Mikroservisler:** Spring Cloud Gateway, Spring Cloud Netflix Eureka
* **Güvenlik:** Spring Security, JJWT (Stateless JSON Web Tokens), Rol Tabanlı Erişim (RBAC)
* **Veritabanı:** PostgreSQL (Spring Data JPA, Hibernate)
* **Önbellek (Cache):** Redis (Gateway üzerinden yapılandırıldı)
* **Araçlar:** Maven, Lombok

## ✨ Öne Çıkan Özellikler
* **Stateless Kimlik Doğrulama:** JWT tabanlı yapı sayesinde sunucularda session (oturum) tutulmaz, bellek tasarrufu sağlanır.
* **Derinlemesine Savunma:** Güvenlik sadece Gateway kapısında değil, aynı zamanda mikroservis içinde (`@PreAuthorize`) metot seviyesinde korunur.
* **Temiz Mimari (Clean Code):** Veritabanı tabloları dışarıya gizlenmiş, sadece DTO (Data Transfer Object) nesneleri ile güvenli veri aktarımı sağlanmıştır.
* **Standart Hata Yönetimi:** Frontend'in kolayca yakalayabilmesi için tüm API cevapları standart bir `ApiResponse<T>` zarfı ile döndürülür.

## 🚀 Nasıl Çalıştırılır?
1. Bilgisayarınızda **PostgreSQL** ve **Redis**'in çalıştığından emin olun.
2. Servisleri aşağıdaki sıraya göre ayağa kaldırın:
   * `eurekaserver` (Port: 8761)
   * `authservice` (Port: 8081)
   * `quizservice` (Port: 8082)
   * `gatewayserver` (Port: 8080)
3. Tüm API istekleri `http://localhost:8080` adresi üzerinden yapılabilir.
