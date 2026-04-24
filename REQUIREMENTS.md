# Requirements

## Functional Requirements Implemented

- 5-step wizard: Profile -> Define -> Plan -> Collect -> Analyse
- Step 1 collects user information
- Step 2 enforces single selection for quality type, mode, and scenario
- Step 3 shows scenario dimensions/metrics in read-only tables
- Step 4 shows raw values and metric scores (1-5 scale)
- Step 5 shows weighted dimension scores, radar chart, and weakest-dimension analysis
- Hard-coded scenario repository with multiple modes and scenarios
- Command-line compile/run support via `javac` and `java`

## Technical Requirements

- Java SE 17 or higher (tested with Java 21)
- Java Swing components (`JFrame`, `JPanel`, `JTable`, `JRadioButton`, `JButton`, etc.)
- Collections Framework usage (`ArrayList`, `HashMap`, `List`, `Map`)
- OOP principles with encapsulation and inheritance-based Swing structure
- No external dependencies (standard Java SE only)
