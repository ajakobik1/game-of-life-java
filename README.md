# Conway's Game of Life (Java / JavaFX)

A multi-module desktop implementation of Conway's Game of Life built in Java, focused on clean object-oriented architecture, design patterns, relational database persistence, automated testing, and internationalization.

## Tech Stack

- **Language & Build:** Java (JPMS Modules), Apache Maven (multi-module project)
- **GUI:** JavaFX (FXML)
- **Persistence:** PostgreSQL 17, JDBC, pgAdmin 4, Docker Compose, File Serialization
- **Testing & Code Quality:** JUnit 5, Checkstyle, Log4j 2

## Architecture & Design Patterns

The project is structured into two decoupled Maven/JPMS modules:

- **`Model`** — Core simulation domain (`GameOfLifeBoard`, `GameOfLifeCell`, `GameOfLifeRow`, `GameOfLifeColumn`, `GameOfLifeNeighbors`), persistence layer, custom exception hierarchy, and automated test suites.
- **`View`** — JavaFX desktop client (`MenuController`, `BoardViewController`), FXML layouts, localized exception wrappers, and multi-language resource bundles.

### Key Engineering Highlights

- **Strategy Pattern:** `GameOfLifeSimulator` interface with `PlainGameOfLifeSimulator` decouples simulation rules from the board representation.
- **DAO & Factory Patterns:** `Dao` interface and `GameOfLifeBoardDaoFactory` abstract data persistence across two interchangeable backends:
  - `FileGameOfLifeBoardDao` for local file storage.
  - `JdbcGameOfLifeBoardDao` for relational database storage in PostgreSQL via JDBC.
- **Adapter Pattern:** `GameOfLifeCellAdapter` bridges domain cell objects with the JavaFX UI layer.
- **Internationalization (i18n):** Full UI and error-message localization supporting 4 languages: English (`en`), Polish (`pl`), Spanish (`es`), and Turkish (`tr`).
- **Error Handling & Logging:** Dedicated custom exception hierarchy (`GameOfLifeException`, `GameOfLifeDaoException`, `GameOfLifeDaoIoException`, `GameOfLifeDaoCloseException`) combined with Log4j 2 logging.
- **Testing & Static Analysis:** Unit and JDBC integration tests (`GameOfLifeBoardTest`, `DaoTest`, `JdbcDaoTest`) using JUnit 5 and Checkstyle rules enforcement (`checkstyle2023.xml`).

## Project Structure

```text
.
├── compose.yaml            # Docker Compose setup (PostgreSQL 17 + pgAdmin 4)
├── pom.xml                 # Parent Maven configuration (Model & View modules)
├── Model/                  # Simulation logic, DAO layer (File & JDBC), Log4j2, tests
└── View/                   # JavaFX application, FXML views, i18n bundles (EN/PL/ES/TR)
```

## Getting Started

### Prerequisites

- Java Development Kit (JDK)
- Apache Maven
- Docker & Docker Compose (for PostgreSQL persistence and JDBC tests)

### Running the Application

1. **Start the PostgreSQL database (and optional pgAdmin on port 80):**
   ```bash
   docker compose up -d
   ```

2. **Build all modules, install dependencies, and run tests:**
   ```bash
   mvn clean install
   ```

3. **Run the JavaFX desktop client:**
   ```bash
   cd View
   ./mvnw javafx:run
   ```
   *(On Windows, use `mvnw.cmd javafx:run`.)*
