# Design

## Architecture (MVC-style Separation)

- `model/` -> domain entities, score logic, scenario repository, app state
- `gui/` -> Swing panels, wizard navigation UI, indicator, and chart rendering

## Design Patterns Used

- CardLayout Wizard: panel transitions handled in `MainFrame`
- Singleton: `AppState` and `ScenarioRepository`
- Repository Pattern: `ScenarioRepository` centralizes all hard-coded datasets

## Package Structure

```text
src/
  Main.java
  model/
    AppState.java
    Metric.java
    QualityDimension.java
    Scenario.java
    ScenarioRepository.java
    UserProfile.java
  gui/
    MainFrame.java
    RadarChartPanel.java
    Step1ProfilePanel.java
    Step2DefinePanel.java
    Step3PlanPanel.java
    Step4CollectPanel.java
    Step5AnalysePanel.java
    StepIndicatorPanel.java
    UIConstants.java
```

## Modes and Scenarios

- Custom: Custom Starter Scenario (bonus baseline dataset)
- Education: Scenario C (Team Alpha), Scenario D (Team Beta)
- Health: Scenario A (Hospital System), Scenario B (Pharmacy System)

## OOP Notes

- Encapsulation: private fields + getters in model classes
- Polymorphism: overridden `paintComponent` methods in custom UI components
- Cohesion: each class has a focused responsibility

## Score Calculation Formula

```text
Higher is better -> score = 1 + (value - min) / (max - min) * 4
Lower is better  -> score = 5 - (value - min) / (max - min) * 4
```

Rules:

- Clamp score to [1.0, 5.0]
- Round to nearest 0.5

## Class Diagram

```mermaid
classDiagram
    class Main {
        +main(String[] args)
    }

    class MainFrame {
        -CardLayout cardLayout
        -JPanel cardPanel
        -StepIndicatorPanel stepIndicator
        +goToStep(int step)
        +resetSession()
    }

    class AppState {
        -UserProfile profile
        -String qualityType
        -String mode
        -Scenario selectedScenario
        +getInstance() AppState
        +clear()
    }

    class UserProfile {
        -String username
        -String school
        -String sessionName
    }

    class ScenarioRepository {
        -Map~String,List~Scenario~~ scenarioMap
        +getInstance() ScenarioRepository
        +getScenariosByMode(String mode) List~Scenario~
    }

    class Scenario {
        -String name
        -String mode
        -String qualityType
        -List~QualityDimension~ dimensions
        +addDimension(QualityDimension)
    }

    class QualityDimension {
        -String name
        -int coefficient
        -List~Metric~ metrics
        +addMetric(Metric)
        +calculateScore() double
    }

    class Metric {
        -String name
        -int coefficient
        -String direction
        -double rangeMin
        -double rangeMax
        -String unit
        -double value
        +calculateScore() double
    }

    class Step1ProfilePanel
    class Step2DefinePanel
    class Step3PlanPanel
    class Step4CollectPanel
    class Step5AnalysePanel
    class StepIndicatorPanel
    class RadarChartPanel

    Main --> MainFrame : launches
    MainFrame --> StepIndicatorPanel : owns
    MainFrame --> Step1ProfilePanel : owns
    MainFrame --> Step2DefinePanel : owns
    MainFrame --> Step3PlanPanel : owns
    MainFrame --> Step4CollectPanel : owns
    MainFrame --> Step5AnalysePanel : owns

    Step1ProfilePanel --> AppState : sets profile
    Step2DefinePanel --> AppState : sets selections
    Step2DefinePanel --> ScenarioRepository : loads scenarios
    Step3PlanPanel --> AppState : reads selected scenario
    Step4CollectPanel --> AppState : reads selected scenario
    Step5AnalysePanel --> AppState : reads selected scenario
    Step5AnalysePanel --> RadarChartPanel : renders chart

    AppState --> UserProfile : holds
    AppState --> Scenario : holds selected
    ScenarioRepository --> Scenario : provides
    Scenario --> QualityDimension : contains
    QualityDimension --> Metric : contains
```
