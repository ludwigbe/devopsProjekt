# devopsProjekt


# To-Do:

# | Anwendung und Github aufsetzen
## 1. App erstellen: 
Softwareentwicklung (Maven, Spring Boot, REST): 
Entwicklung einer kleinen, aber funktionalen Spring-Boot-Anwendung mit RESTful API
• Saubere Projektstruktur und sinnvolle Architektur

## 2. Github & Planung:
Versionierung 
Issues/Boards
Branching Strategie

# | DevOps
## 3. Build & Deployment (Jenkins oder alternative CI/CD-Tools wie GitHub Actions)
• Aufbau einer CI/CD-Pipeline (Build, Test, Deployment)
• Konfiguration automatisierter Prozesse bei Codeänderung
## 4. Infrastruktur & Umgebung (Docker, optional Traefik oder andere Reverse
Proxies)
• Containerisierung der Anwendung
• Netzwerkstruktur und Routing
• Aufbau einer realitätsnahen Infrastruktur
## 5. Qualitätssicherung (optional) (SonarQube oder vergleichbar)
• Integration eines Tools zur statischen Codeanalyse
• Dokumentation der Codequalität anhand messbarer Metriken



Addtional Notes:

I. Projektstart & Versionsverwaltung
1. Repository anlegen (GitHub/GitLab)
2. Projektstruktur initialisieren (z. B. `src/`, `README.md`, `.gitignore`)
3. Branching-Strategie festlegen (z. B. Git Flow, Trunk-based)
4. Issues & Boards zur Aufgabenverfolgung anlegen (z. B. GitHub Issues, Kanban-Board)


II. Softwareentwicklung – Grundgerüst
5. Maven-Projekt mit Spring Boot initialisieren
6. REST-API-Grundstruktur entwickeln (z. B. einfache CRUD-Endpunkte)
7. Sinnvolle Projektstruktur (Controller, Service, Repository, etc.)
8. Lokale Tests der Anwendung (manuell oder mit Unit-Tests)


III. Versionsverwaltung – Workflows
9. Feature-Branches anlegen für neue Aufgaben
10. Pull Requests erstellen, Review durchführen & mergen
11. Automatische Verknüpfung von Commits mit Tickets (z. B. `Fixes #1`)


IV. Build & CI/CD einrichten
12. CI/CD-Tool auswählen (z. B. GitHub Actions, Jenkins)
13. Pipeline aufbauen:
- Code checken & bauen (`mvn clean install`)
- Tests ausführen
- Artefakte erzeugen
14. Automatisiertes Deployment in Testumgebung konfigurieren


V. Infrastruktur & Containerisierung
15. Anwendung mit Docker containerisieren
16. Docker Compose zur lokalen Entwicklung einrichten
17. Reverse Proxy optional einbinden (z. B. Traefik, Nginx)
18. Netzwerkstruktur/Routing konfigurieren
19. Lokale Infrastruktur testen


VI. Qualitätssicherung (optional)
20. Tool zur statischen Codeanalyse integrieren (z. B. SonarQube)
21. CI-Pipeline um statische Analyse erweitern
22. Metriken dokumentieren (z. B. Code Coverage, Bugs, Code Smells)


VII. Finalisierung & Produktiv-Deployment
23. Finales Testing der Gesamtanwendung
24. Produktionstaugliche Umgebung vorbereiten
25. Anwendung produktiv deployen (z. B. Cloud/VPS)



