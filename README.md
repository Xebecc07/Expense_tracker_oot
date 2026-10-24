# Expense Tracker (Java 17 + Spring Boot 3)

A phone-style expense tracking web app built to demonstrate OOP. 8 insight features are unlocked by
subscription plans (Free / Student / Basic).

## Run it
Requirements: JDK 17+ and Maven 3.8+.

```
mvn test               # 25 JUnit 5 tests (core domain layer)
mvn spring-boot:run    # starts on port 8080
```
Open http://localhost:8080, log in with any name + a 4-6 digit PIN (a new name creates an account),
then tap **Load demo data** on Home or Profile to fill every feature. Data is saved in `./data` (H2 file DB).
Delete the `data` folder to reset.

## Open on your phone
1. Connect the phone and laptop to the same Wi-Fi.
2. Find the laptop IP: Windows `ipconfig` (IPv4 Address), macOS/Linux `ifconfig` / `ip a`.
3. On the phone browser open `http://<laptop-ip>:8080` (e.g. http://192.168.1.5:8080).
4. If it does not load, allow Java / port 8080 through the laptop firewall.
5. Install: browser menu -> **Add to Home screen**. (Full PWA install and the service worker need HTTPS or
   localhost, so over plain http on the phone you get a home-screen shortcut; on the laptop at
   http://localhost:8080 it installs as a real app.)

## Project layout
| Package | What it holds |
|---|---|
| `core` | Pure Java OOP domain (no Spring): enums, `Expense`/`FestivalExpense`, abstract `SubscriptionPlan` + 3 plans, `User`, `Insight` interface + 8 implementations, custom exception |
| `entity` | JPA entities (database rows) |
| `repository` | Spring Data repositories |
| `service` | Load entities -> build core `User` -> call core classes |
| `web` | Controllers, login interceptor, `@ControllerAdvice` |
| `resources/templates`, `static` | Thymeleaf pages, CSS, JS, manifest, service worker |

## OOP concepts (look for the comments in `core`)
- **Encapsulation:** private fields + getters in `Expense`, `User`, `Goal`; `User.addExpense` guards the plan limit.
- **Inheritance:** `FestivalExpense extends Expense`; `FreePlan`/`StudentPlan`/`BasicPlan extend SubscriptionPlan`.
- **Abstraction:** abstract class `SubscriptionPlan`.
- **Interface:** `Insight` (with a default `run()` method that does plan gating) implemented by 8 classes.
- **Polymorphism:** `getType()` overridden; `InsightController` treats all features as `Insight`; plans used through `SubscriptionPlan`.
- **Custom exception:** `ExpenseLimitReachedException` (checked) -> `GlobalAdvice` shows an upgrade prompt.
