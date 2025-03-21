
:: Spring Boot ::                (v3.4.3)
project: MAven
Language: Java 17


1. User Endpoints
    * Skapa en ny användare: POST http://localhost:8080/users/ Body (JSON):   

    Exempel på anvädning:
       {
       "username": "Pontus",
       "email": "pontus@example.com",
       "password": "password2"
       }
   * Hämta alla användare: GET http://localhost:8080/users/
   * Radera användrare : DELETE http://localhost:8080/users/{id}
   * Uppdatera användare: PUT http://localhost:8080/users/{id}
     {
     "username": "Pontus",
     "email": "pontus@example.com",
     "password": "password2"
     }
2. Channel Endpoints
   * Skapa en ny kanal: POST http://localhost:8080/channels/ Body (JSON):
    
     Exempel på anvädning:
         {
         "name": "Music lover",
         "username": "Pontus"
         }
  *  Hämta alla kanaler: GET http://localhost:8080/channels/
  *  Radera en specifik kanal: DELETE http://localhost:8080/channels/{id}
  *  Skapar ett nytt meddelande i en kanal: PUT http://localhost:8080/channels/{id}
     {
     "content": "Ny text för meddelandet",
     "username": "Uno"
     }
  * Hämtar alla meddelande i en kanal GET http://localhost:8080/channels/{id}

3. Message Endpoints
  * Hämta alla meddelanden i alla kanaler samt vem som skapade:
      GET http://localhost:8080/messages/
  * Uppdatera befintlig meddelande: PUT http://localhost:8080/messages/{messageId}
    {
    "content": "Ny text för meddelandet"
    }
  * Radera meddelande: DELETE http://localhost:8080/messages/{messageId}
