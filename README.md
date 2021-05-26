Konfiguration & Set-up:
- Eine Kopie vom zeiterfassung.properties & turbo-bucher.properties in einem Ordner deines Vertrauens anlegen.
- Im turbo-bucher.properties braucht es drei Einträge: 'loginPage', 'baseURL' & 'browserName'. 
  - Für die ersten Einträge sind die gesetzten Werte so zu belassen. Letzterer Eintrag ist optional und definiert den Browser fürs abbuchen
- Die übrige Konfiguration wird im zeiterfassung.properties eingetragen
- Mit dem key 'boardName' kann ein Scrum-Board angegeben werden. Anhand diesem Boardnamen werden alle Tickets des aktuellen Sprints ermittelt
- Falls Tickets von weiteren Sprints angezeigt werden sollen kann mit dem key 'sprintNames' eine Liste von Semikolon separierten Sprint-Namen angegeben werden (z.B. sprintNames=sprintName1;sprintName2)
- Zusätzlich kann in einem optionalen 'default-tickets.txt' weitere Ticket-Nr angegeben werden, welche dann beim Erfassen der Zeit zur verfügung stehen. 
  Die Ticket-Nr in dieser Datei sind nicht Semikolon separiert, sondern können einfach untereinander eingetragen werden 
- Anhand aller konfigurierten bzw. definierten Ticket-Nr wird versucht diese Tickets via Jira-api zu ermitteln um beim Stoppen einer Aufzeichnung davon auswählen zu können
- Starten der Anwendung direkt via .jar (Mac) bzw. exe-File (Windows)

Login:
- Rechtsklick auf den roten/grünen/blauen Kreis in der Info-Leiste und dann das Menu 'Login' auswählen. Anschliessend erfolgt die Verifzierung durch die Eingabe von Benutzername & Passwort
- Die Authentifizierung geschieht via Jira. Ist das Login mehrfach fehlgeschlagen und funktioniert trotz korrekter Eingabe nicht, musst du das Login auf Jira zurück setzen:
  - Navigiere auf https://jira.adcubum.com/
  - Klicke oben rechts auf dein Benutzer-Avatar und dann ganz unten auf 'Abmelden'
  - Auf der nun erschienenen Seite kannst du auf 'Melden Sie sich ernaut an' klicken
  - Auf der Login Maske Benutzername & Passwort eingeben sowie das Captcha bestätigen
  - et voila

Erfassen der Stunden:
  - Aufzeichnen der Arbeitsstunden während dem Tag via Start/Stop. Auf diese Art werden die Arbeitsstunden laufend erfasst, sobald die Tätigkeit gewechselt wird
  - Oder alternativ via Aufzeichnen von 'Kommen & Gehen' bei Ankunft bzw. Verlassen des Arbeitsplatzes. Hierbei werden die Arbeitsstunden erst am Ende des Tages erfasst
  - Die beiden Modi können kombiniert werden. Allerdings kann keine Aufzeichnung gestartet werden wenn Kommen/Gehen aktiv ist und vice versa

Kommen/Gehen: Kommen:
- Rechtsklick auf den roten Kreis in der Info-Leiste und dann auf den Menu-Eintrag 'Kommen/Gehen' klicken
- Alternativ durch Drücken der Tastenkombination, welche im turbo-bucher.properties definiert werden kann (Property ComeOrGoHotKey)
- Dadurch wird ein 'Kommen' registriert und der rote Kreis verfäbt sich blau

Kommen/Gehen: Gehen:
- Rechtsklick auf den blauen Kreis in der Info-Leiste und dann auf den Menu-Eintrag 'Kommen/Gehen' klicken
- Alternativ durch Drücken der Tastenkombination
- Dadurch wird ein 'Gehen' registriert und der blaue Kreis verfäbt sich rot und das aktuelle 'Kommen/'Gehen' ist somit vollständig

Kommen/Gehen: Arbeitsstunden erfassen:
- Rechtsklick auf den roten Kreis in der Info-Leiste und dann auf den Menu-Eintrag 'Zeige Kommen/Gehen' klicken
- Dadurch wird eine Übersicht aller Kommen/Gehen angezeigt
- Durch einen Doppelklick auf einen Kommen bzw. Gehen-Eintrag kann dieser editiert werden. Mit 'Enter'/'Tab.' wird die Änderung übernommen. Durch 'Esc' wird sie verworfen
- Mit dem Button 'Starte Zeiteingabe' wird ein Wizzard gestartet welcher pro Kommen/Gehen eine Eingabe-Maske zeigt
- Auf dieser Maske werden alle nötigen Informationen des bearbeiteten Tickets eingetragen (Ticket-Nr, Leistungsart usw.)
- Der Begin kann jeweils nicht verändert werden
- Das Ende kann kleiner gewählt werden, nicht aber grösser als das Ende des 'Gehen'
- Alternativ zum 'Ende' kann auch direkt die Anzahl Stunden eingegeben werden
- Überschreitet diese Anzahl Stunden das Ende des 'Gehen' wird autmatisch dieses Ende gesetzt 
- Wird ein kleineres 'Ende' eingegeben und 'Weiter' geklickt startet der Dialog mit dem eingegebenen End-Datum als neuer Begin
- Dies wird so lange wiederholt bis die Arbeitsstunden für das letzte 'Kommen/Gehen' erfasst wurden.-
- Beim letzten Eintrag wird anstelle von 'Weiter' 'Fertig' angezeigt und beim Klick darauf wird der Dialog beendet und die erfassten Stunden sind wie gewohnt in
der Übersichtsseite ersichtlich
- Klick auf 'Abbrechen' verwirft die gemachten Erfassungen

Aufzeichnung starten:
- Durch Klick auf den roten Kreis unten in der Info-Leiste
- Durch Drücken der Tastenkombination, welche im turbo-bucher.properties definiert werden kann (Property StartStopHotKey)
- Eine Aufzeichnung ist nicht möglich, sofern noch Aufzeichnung von einem anderen Tag vorhanden sind. Diese müssen zuerst gelöscht werden

Aufzeichnung stoppen:
- Durch Klick auf den grünen Kreis unten in der Info-Leiste
- Durch Drücken der Tastenkombination, welche im turbo-bucher.properties definiert werden kann (Property StartStopHotKey)
- Anschliessend kann die Ticket-Nr eingegeben werden. Wenn im turbo-bucher.properties der Name des Scrum-Boards angegeben werden, 
  kann in der Drop-Down Liste aus den Tickets im aktuellen Sprint ausgewählt werden. Alternativ kann direkt im Eingabefeld getippt werden. 
  In einem Popup werden Tickets angezeigt, welche zum eingetippten Text matchen. D.h. entweder nach Ticket-Nr oder nach der Beschreibung
- Story-Time Buchungen: Durch die Eingabe mehrerer Ticket-Nrs, welche durch Semikolons separiert sind, wird die gesammt Zeit auf die Anzahl einzelner Tickets aufgeteilt
- Dieses Eingabefeld ist nur aktive, wenn das Eingabefeld 'Ticket-Nr.', in welchem ein einzelnes Ticket eingegeben werden kann, leer ist und vice versa.
- Eingabe der Start- bzw. Endzeit z.B. also 0915, 915 bzw. 09:15

Integriertes Ticketbacklog
- Damit beim Stoppen der Aufzeichnung aus den Tickets im aktuellen Sprint ausgewählt werden kann, bedarf es einem Eintrag im zeiterfassung.properties
- z.B. boardName=Contract Health um für das Board 'Contract Health' die zum Start-Zeitpunkt der App aktuellen Tickets zu ermitteln. Es kann ein beliebiger Boardname eingetragen werden, so wie er in Jira angezeigt wird
  - Die Syntas sieht folgendermassen aus: boardName=Mein Board Name
- Für weitere, nicht aktive Sprints kann mit dem Key 'sprintNames' weitere Sprints angegeben werden, welche noch aktiv sind (z.b. Fachexperte/Analyse-Sprints oder sonstige Backlogs)
  - Die Syntax benötigt keine Hochkommas o.ä. und sieht folgendermassen aus: sprintNames=sprintName1;sprintName2;sprint name drei
- Beim Stop einer Aufzeichnung kann aus der Dropdown-Liste rechts vom Eingabe Feld ein Ticket ausgewählt werden. Tickets die dir zugewiesen sind, werden fett dargestellt
- Diese Liste mit Tickets kann aktualisiert werden, indem mittels der rechten Maustaste auf den roten/grünen/blauen Kreis das Kontext-Menu geöffnet und dort auf 'Sprint-Tickets aktualisieren' geklickt wird.

Abbuchen der aufgezeichneten Stunden:
- Die Stunden können direkt über den Kontextmenu-Eintrag 'Abbuchen' oder von der Übersichtsseite her abgebucht werden. 
  - Rechtsklick auf den Kreis, Klick auf Menu-Eintrag 'Zeige Arbeitsstunden' und auf der sich nun öffnenden Seite auf 'Abbuchen' drücken
  - Anschliessend öffnet sich der konfigurierte Browser und verbucht die Zeiten in Abacus
  - Unterstützte Browser-Typen sind: Google Chrome, Firefox & Edge.
  - Zur Konfiguration des Browsers im turbo-bucher.properties mit dem Key 'browserName' den gewünschten Browser-Name eintragen 
  - firefox für Firefox, chrome für Google-Chrome oder edge für MS-Edge

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
- 1, 5, 10 & 15 Minuten genau. Entweder im turbo-bucher.properties (Property Rundungseinstellungen) oder mittels Kontext-Menü 'Rundungseinstellungen' auf den roten/grünen/blauen Kreis

Anzeige und Ändern von aufgezeichneten Stunden
- Via Kontextmenue 'Zeige Arbeitsstunden' kann auf die Übersichtsseite navigiert werden
- Hier kann durch einen Doppelklick auf einen Eintrag folgende Elemente geändert werden:
  - Die Ticket-Nr
  - Der Buchungstext
  - Die Gesamtdauer des Eintrages ('Anzahl Stunden'). Wird diese geändert, passt sich die Endzeit so an, dass die Summe der gewünschten Dauer entspricht
  - Start- und Endzeit der vorhandenen Zeitabschnitten
  - Die Leistungsart des erfassten Zeitabschnittes
- Ist für kein Entrag ein Buchungstext vorhanden kann mittels Rechtsklick auf die gewünschte Zeile ein Kontext-Menü angezeigt werden. In diesem klickst du auf 'Beschreibung hinzufügen'
- Klick auf 'Abbuchen' startet das Abbuchen
- Klick auf 'Exportieren' startet den Export auf den Desktop
- Klick auf 'Alles Löschen' löscht die Aufzeichnung ohne Vorwahrnung
