@echo off
setlocal

:: Provera da li postoji JAR fajl
if not exist "YouTubeConverter.jar" (
    echo [INFO] YouTubeConverter.jar nije pronadjen. Pokusavam kompilaciju...
    
    :: Provera da li postoji Java compiler
    javac -version >nul 2>&1
    if %errorlevel% neq 0 (
        echo [GRESKA] Java (JDK) nije instaliran ili nije u PATH-u.
        echo Molimo instalirajte JDK da biste pokrenuli aplikaciju iz koda.
        pause
        exit /b 1
    )

    :: Kompilacija
    echo [INFO] Kompajliranje YouTubeConverter.java...
    javac -encoding UTF-8 YouTubeConverter.java
    
    :: Kreiranje manifesta ako ne postoji
    if not exist "manifest.txt" (
        echo Main-Class: YouTubeConverter > manifest.txt
    )

    :: Pakovanje u JAR
    echo [INFO] Kreiranje YouTubeConverter.jar...
    jar cfm YouTubeConverter.jar manifest.txt *.class
)

:: Pokretanje aplikacije
echo [INFO] Pokretanje aplikacije...
start javaw -jar YouTubeConverter.jar

exit
