Project Plan for Don't Wreck My House

Daily Tasks:

Day 1 (1-1.5 hours):
- Create project plan ✅
- Brainstorm and list steps ✅
- Set up packages ✅
- pom.xml configuration ✅

Day 2 (1.5-2 hours):
- Begin working on model layer ✅
- Design Guest, Host, Reservation classes ✅
- Model all necessary fields ✅
- Getters/Setters, Constructors, etc. ✅
- Test model validation ✅

Day 3 (3-4 hours):
- Implement FileRepository classes (Guest, Host, Reservation) ✅
- Implement DataException ✅
- Handle reading/writing .csv files ✅
- Unit tests for each file repository ✅

Day 4 (3-4 hours):
- Apply business logic and validation for ReservationService ✅
- Inject repositories as dependencies ✅
- Test for overlap logic, correct pricing (weekday/weekend), validation ✅

Day 5 (3-4 hours):
- Implement ConsoleIO, View, Controller ✅
- Create Main Menu and any sub-menus ✅
- Test proper functionality and design ✅

Day 6 and 7 (4-8 hours):
- Continue testing to ensure full functionality ✅
- Add missing tests, if needed ✅
- Refactor any code, as needed ✅
- Submit work and celebrate 🎉🥳 ✅✅✅✅✅✅✅✅

Class Diagram:

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
