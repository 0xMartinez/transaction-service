# System do obsługi transakcji płatniczych w Spring Boot

Aplikacja wykorzystuje dockera do konteneryzacji aplikacji, Apache kafkę do publikowania i zarządzania tematami.
Rest api zostało wygenerowane przez plik openapi.yaml w katalogu resources/static
Swagger z wystawionymi endpointami REST: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
W celu zapewnienia większej stabilności, użyte zostały 2 instancje Kafki.
W systemie użyto bazę danych H2, operacje dodawania, usuwania, aktualizowanie są logowane.
Konsola do bazy danych znajduje się pod endpointem [http://localhost:8080/h2-console/login.jsp](http://localhost:8080/h2-console/login.jsp)
Plik .wsdl znajduje się w katalogu resources.

System udostępnia REST api do edytowania transakcji:

* wylistowanie wszystkich transakcji w bazie
* uzyskanie informacji o transakcji z konkretnym id
* utworzenie transakcji
* aktualizacja istniejącej transakcji
* usunięcie istniejącej transakcji

Dodatkowo system udostępnia SOAP api do zarządzania płatnościami:
* utworzenie płatności (nowa transakcja)
* edycja kwoty istniejącej płatności

System co 30 sekund przeprowadza skanowanie Schedulerem:
* dla transakcji o statusie COMPLETED zmienionym przez REST w ostatnich 30
  sekundach, publikuje komunikat na topic Kafka "transakcje zrealizowane" z wiadomością zawierającą id, amount, currency i status transakcji
* dla transakcji o statusie PENDING, których czas utworzenia przekroczył 30 sekund, aktualizuje status na EXPIRED i publikuje komunikat na topic Kafka "transakcje przeterminowane" z tymi
  samymi polami

Instrukcja uruchomienia
-------------------------------
```
mvn clean package
```
 ```
docker-compose up -d
```