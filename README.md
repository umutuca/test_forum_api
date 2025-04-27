Teststrategi Vi använder följande typer av tester:

Enhetstester: Testar UserService isolerat genom att mocka UserRepository med Mockito.

Komponenttester: Testar UserController genom att mocka UserService, och verifiera svar och HTTP-statuskoder.

Integrationstester: Testar hela flödet från Controller → Service → Repository → MySQL i en separat testdatabas (forum_test).

Alla tester körs automatiskt med JUnit 5 och Spring Boot Test-miljö.

Testade Metoder och Motivering

Typ av test Klass Motivering Enhetstest UserServiceTest Testar affärslogik, t.ex. skapa, hämta och validera användare utan beroende av databas. Komponenttest UserControllerTest Testar att REST API-endpoints svarar korrekt och returnerar rätt data och statuskoder. Integrationstest UserIntegrationTest Testar hela applikationen mot en riktig MySQL testdatabas för att säkerställa att hela kedjan fungerar korrekt. Vi fokuserade på:

Lyckad och misslyckad skapande av användare (createUser).

Hämta befintlig och icke-befintlig användare (getUserById).

Testa edge cases som redan existerande användare (dubbelregistrering).

Varje testmetod har en kommentar högst upp som beskriver:

Vad metoden testar.

Vilken typ av test det är (Enhetstest, Komponenttest, Integrationstest).
