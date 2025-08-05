# Don’t Wreck My House 🏠
A Java-based mini booking system for managing guest reservations in a fictional "capsule hotel" setting.

## 📌 Project Overview
This console application simulates an Airbnb-style booking system with file-based data persistence and custom business logic. It allows hosts to manage availability and guests to book or cancel reservations, while enforcing validation rules and preventing booking conflicts.

## 🛠️ Tech Stack
- **Language:** Java
- **Framework:** Spring Boot (non-web)
- **Architecture:** Layered (domain → service → data)
- **Persistence:** File I/O (CSV files)
- **Testing:** JUnit, Test-Driven Development (TDD)
- **Tools:** IntelliJ IDEA, Git, GitHub

## ✅ Features
- Create, update, and delete guest, host, and reservation records
- Enforces rules for double bookings and reservation date limits
- Performs input validation with helpful feedback
- Includes seed data for hosts and guests
- Fully tested with JUnit test cases

## 👨‍💻 My Role
- Implemented business rules and layered architecture for clean code separation
- Wrote and maintained unit tests to ensure data integrity
- Applied TDD to develop core features with a focus on maintainability
- Handled CSV file persistence for host, guest, and reservation records

## 📂 Project Structure
src
├───main
│   ├───java
│   │   └───learn
│   │       └───mastery
│   │           │   App.java
│   │           │
│   │           ├───data
│   │           │       DataException.java
│   │           │       GuestFileRepository.java
│   │           │       GuestRepository.java
│   │           │       HostFileRepository.java
│   │           │       HostRepository.java
│   │           │       ReservationFileRepository.java
│   │           │       ReservationRepository.java
│   │           │
│   │           ├───domain
│   │           │       GuestService.java
│   │           │       HostService.java
│   │           │       ReservationService.java
│   │           │       Result.java                 // Helper class to run checks inside 
│   │           │                                   // ReservationService.java
│   │           │
│   │           ├───models
│   │           │       Guest.java
│   │           │       Host.java
│   │           │       Reservation.java
│   │           │
│   │           └───ui
│   │                   ConsoleIO.java
│   │                   Controller.java
│   │                   View.java
│   │                   MainMenuOption.java
│   │
│   └───resources
└───test
└───java
└───learn
└───mastery
├───data
│       GuestFileRepositoryTest.java
│       GuestRepositoryDouble.java
│       HostFileRepositoryTest.java
│       HostRepositoryDouble.java
│       ReservationFileRepositoryTest.java
│       ReservationRepositoryDouble.java
│
└───domain
        GuestServiceTest.java
        HostServiceTest.java
        ReservationServiceTest.java


## 🚀 Getting Started
1. Clone the repo  
   `git clone https://github.com/rhenry2032/Don-t-Wreck-My-House.git`
2. Open in your Java IDE
3. Run `App.java` to launch the program in the console

## 📄 License
This project is for educational use.


