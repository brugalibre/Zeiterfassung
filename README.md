Initiales Set-Up 
- Eine Kopie vom zeiterfassung.properties & turbo-bucher.properties in einem Ordner deines Vertrauens anlegen.
- Im turbo-bucher.properties braucht es nur zwei Einträge: 'loginPagem' und 'baseURL'
- Die übrige Konfiguration wird im zeiterfassung.properties eingetragen
- Optional kann mit dem key 'boardName' ein Scrum-Board angegeben werden. Anhand diesem Boardnamen werden alle Tickets des aktuellen Sprints ermittelt
- Falls Tickets von weiteren Sprints angezeigt werden sollen (z.B. ein ready-4-sprint oder ein analyse-sprint) kann mit dem key 'sprintNames' eine Liste von Semikolon separierten Sprint-Namen angegeben werden (z.B. sprintNames=sprintName1;sprintName2)
- Zusätzlich kann in einem optionalen 'default-tickets.txt' weitere Ticket-Nr angegeben werden, welche dann beim Erfassen der Zeit zur verfügung stehen. Die Ticket-Nr in diesem
  default-tickets.txt sind nicht Semikolon separiert sondern können einfach untereinander eingetragen werden 
- Anhand aller konfigurierten bzw. definierten Ticket-Nr wird versucht diese Tickets via Jira-api zu ermitteln um beim Stoppen einer Aufzeichnung davon auswählen zu können
- Starten der Anwendung direkt via .jar, falls das nicht klappt via .bat-File bzw. exe-File
- Neben den Tickets vom aktuellen Sprint können in einem optionalen 'default-tickets.txt' weitere Ticket-Nr angegeben werden, welche dann beim Erfassen der Zeit zur verfügung stehen

Login:
- Rechtsklick auf den roten/grünen Kreis in der Info-Leiste und dann das Menu 'Login' auswählen. Anschliessend erfolgt die Verifzierung durch die Eingabe von Benutzername & Passwort

Aufzeichnung starten:
- Durch Klick auf den roten Kreis unten in der Info-Leiste
- Durch Drücken der Tastenkombination, welche im turbo-bucher.properties definiert werden kann (Property StartStopHotKey)
- Eine Aufzeichnung ist nicht möglich, sofern noch Aufzeichnung von einem anderen Tag vorhanden sind. Diese müssen zuerst gelöscht werden

Aufzeichnung stoppen:
- Durch Klick auf den grünen Kreis unten in der Info-Leiste
- Durch Drücken der Tastenkombination, welche im turbo-bucher.properties definiert werden kann (Property StartStopHotKey)
- Anschliessend kann die Ticket-Nr eingegeben werden. Wenn im turbo-bucher.properties der Name des Scrum-Boards angegeben werden, kann in der Drop-Down Liste aus den Tickets im aktuellen Sprint ausgewählt werden. Alternativ kann direkt im Eingabefeld getippt werden. In einem Popup werden Tickets angezeigt, welche zum eingetippten Text matchen. D.h. entweder nach Ticket-Nr oder nach der Beschreibung
- Eingabe der Start- bzw. Endzeit z.B. also 0915, 915 bzw. 09:15
- Story-Time Buchungen: Durch die Eingabe mehrerer Ticket-Nrs, welche durch Semikolons separiert sind, wird die gesammt Zeit auf die Anzahl einzelner Tickets aufgeteilt

Tickets des aktuellen Sprints
- Damit beim Stoppen der Aufzeichnung aus den Tickets im aktuellen Sprint ausgewählt werden kann, bedarf es einem Eintrag im Turbo-bucher.properties
- z.B. boardName=Contract Health um für das Board 'Contract Health' die zum Start-Zeitpunkt der App aktuellen Tickets zu ermitteln. Es kann ein beliebiger Boardname eingetragen werden, so wie er in Jira angezeigt wird
- Beim Stop einer Aufzeichnung kann aus der Dropdown-Liste rechts vom Eingabe Feld ein Ticket ausgewählt werden. Tickets die dir zugewiesen sind, werden fett dargestellt
- Diese Liste mit Tickets kann aktualisiert werden, indem mittels der rechten Maustaste auf den roten/grünen Kreis das Kontext-Menu geöffnet und dort auf 'Sprint-Tickets aktualisieren geklickt wird.

Aufzeichnung exportieren:
- Sollte ein Abbuchen nicht möglich sein, kann die aktuelle Aufzeichnung exportiert werden
- Dazu einfach via Kontext-Menü 'Zeige Arbeitsstunden' auf die Übersichtsseite navigieren und von dor aus auf 'Exportieren' klicken
- Export erfolgt auf den Desktop
- Ebenfalls erfolgt automatisch ein Export auf den Desktop wenn die App beendet wird und Aufzeichnungen vorhanden sind. Damit soll ein ungewollter Datenverlust z.B. beim Herunterfahren des PCs vorgebeugt werden

Leistungsarten:
- Per Default sind folgende Leistungsarten unterstützt:
  - 100 (Analyse)
  - 111 (Meeting)
  - 113 (Umsetzung/Dokumentation)
  - 122 (Qualtitätssicherung)
  - 141 (Allg. Verwaltungsarbeiten)
  - 164 (bezahlte Abwesenheiten)
- in einem optionalen leistungsarten.properties können beliebige andere Leistungsarten definiert werden

Aufzeichnung importieren:
- Eine exportierte Aufzeichnung kann via Kontext-Menü 'Vorhandene Aufzeichnung importieren' importiert weden
- Hinweis: Alle in dieser Aufzeichnung verwendeten Leistungsarten müssen der Zeiterfassungs-App, welche den Import vornimmt, bekannt sein

Genauigkeit der Aufzeichnung:
- Die Genauigkeit der Aufzeichnung kann vier stufig definiert werden:
- 1, 5, 10 & 15 Minuten genau. Entweder im turbo-bucher.properties (Property Rundungseinstellungen) oder mittels Kontext-Menü 'Rundungseinstellungen' auf den roten/grünen Kreis

Abbuchen der Stunden
- Die Stunden können direkt über den Kontextmenu-Eintrag 'Abbuchen' oder von der Übersichtsseite her abgebucht werden

Anzeige und Ändern von aufgezeichneten Stunden
- Via Kontextmenue 'Zeige Arbeitsstunden' kann auf die Übersichtsseite navigiert werden
- Hier kann durch einen Doppelklick auf einen Eintrag folgende Elemente geändert werden:
  - Die Ticket-Nr
  - Der Buchungstext
  - Die Gesamtdauer des Eintrages ('Anzahl Stunden'). Wird diese geändert, passt sich die Endzeit des letzten Zeitabschnittes so an, dass die Summe aller Zeitabschnitte der gewünschten Dauer entspricht
  - Start- und Endzeit der vorhandenen Zeitabschnitten
  - Die Leistungsart aller Zeitabschnitten
- Klick auf 'Abbuchen' startet das Abbuchen
- Klick auf 'Exportieren' startet den Export auf den Desktop
- Klick auf 'Alles Löschen' löscht die Aufzeichnung ohne Vorwahrnung
