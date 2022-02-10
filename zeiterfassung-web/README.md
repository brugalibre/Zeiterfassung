
Projekt aufsezten
1. Import in Eclipse:

Projekt kann als normales Gradle-7.x-projekt importiert werden.

- Projekt in Eclipse importieren (via import -> gradle project -> zeiterfassung-zeiterfassung auswählen
- Rechtslick auf zeiterfassung-root -> gradle -> refresh gradle project
- Durch diesen gradle task werden die .classpath von zeiterfassung-ui & zeiterfassung-spring-boot "verwurstelt".
- d.h. gewisse Klassen mit 'fx' imports können anschliessend nicht mehr kompiliert werden
	-> wechsle zu git staging -> alles anwählen -> replace with head
- gradle auf java 11 umstellen, grundsätzlich gradle.properties
  Bei mir hat allerdings nur folgender Command geholfen (der muss nach einem Neustart erneut eingegeben werden!):
  set JAVA_HOME="C:\Program Files\Java\jdk-11"

2. Install node.js:
- Für Windows muss zuerst den node-package-manager (npm) installiert werden:
  - https://docs.microsoft.com/en-us/windows/dev-environment/javascript/nodejs-on-windows
	oder alternativ hier gucken was wie wo: https://nodejs.org/en/download/
  - https://docs.microsoft.com/en-us/windows/dev-environment/javascript/vue-on-windows
  - https://codedocu.de/Details_Mobile?d=3156&a=11&f=501&l=0&v=m&t=Fehlermeldung:-Die-Datei-kann-nicht-geladen-werden.-Die-Datei-ist-npm-ng.ps1-nicht-digital-signiert

- Dann starte eine Konsole und navigiere zu 'C:\Dein\Pfad\\zum\git\folder\git\Zeiterfassung\zeiterfassung-web\src\main\ui' und
  setze folgende Befehle ab
  - npm install
  - npm update
  - npm install vue
  - npm install -g @vue/cli@latest und oder npm i @vue/cli-service
  - (npm update --force kann auch helfen wenns eswo hackt)
  - npm audit fix --force (immer mal wieder wenn ein update oder install gefailed ist)


Backdropfilter firefox:
- navigiere nach about:config
- suche nach 'layout.css.backdrop-filter.enabled' -> klick darauf um auf 'true' zu setzen


Starten des entwickler-servers für Vue-js
- Auf der Konsole nach '..\zeiterfassung-web\src\main\ui' navigieren und ein
  npm run serve
  ausführen
- Damit kann via localhost:3000 die Website betrachtet werden, wobei Änderungen an vue-js files direkt & ohne Neustart
  übertragen werden


Bauen eines startfähigen jars inkl. web resourcen

- Zum Bauen der Applikation inkl. Frontend im das 'build.bat' die Variable 'zeiterfassungBaseDir' an die Verzeichnis Struktur anpassen
- Wenn java-home bereits auf jdk-11 zeigt, kann die Zeile 'set JAVA_HOME=%pathToJdk%' entfernt werden. Andernfalls den Pfad zum jdk-11 anpassen
- und dann hobblaschorsch