::Disable command echoes...
@ECHO OFF
::Enable UTF-8
chcp 65001
::Enable the support for ANSI colors on this Windows CMD script
for /f "tokens=2 delims=[" %%a in ('ver') do for /f "tokens=1 delims=]" %%b in ("%%a") do set "winver=%%b"
for /f "tokens=1,2 delims=." %%a in ("%winver%") do set "major=%%a" & set "minor=%%b"
::Defines the invisible ESC character
for /f %%a in ('echo prompt $E ^| cmd') do set "ESC=%%a"
::Set the formation variables
set "GRAY=%ESC%[90m"
set "RED=%ESC%[91m"
set "GREEN=%ESC%[92m"
set "YELLOW=%ESC%[93m"
set "BLUE=%ESC%[94m"
set "CYAN=%ESC%[96m"
set "RESET=%ESC%[0m"
::Clear the current logs
cls



::Warn about Git installation check
echo ===================================================================
echo - Checking Git installation on Windows system...

::If the Git is not installed, stop here...
where git >nul 2>nul
if %errorlevel% neq 0 (
    cls
    echo:
    echo %RED%ERROR!%RESET% It's not possible to find Git installed on your system. Please install it and try again.
    echo        If Git is already installed, try restarting your computer.
    echo:
    pause
    exit
)

::Get the installed Git version in safe way
for /f "delims=" %%a in ('"git version"') do set "gitCheckResult=%%a"
set "gitVersionNumber=%gitCheckResult:git version =%"
::Inform the found version of Git
echo - A Git installation was found: "%GRAY%%gitVersionNumber%%RESET%".
::Show the Repository path of this context
echo - Context repository: "%GRAY%%cd%%RESET%".

::Get the Hooks present in the ".github/hooks" inside this repository, and inject it in the ".git/hooks" of this
::Repository. This way, the Hook Scripts will run locally for this repository when some events of Git is fired.
echo - Injecting Hooks, locally in the "%GRAY%.git%RESET%" folder of this Repository.
copy /Y ".github\hooks\post-checkout" ".git\hooks\" >nul
::If was found a error, warn it!
if %errorlevel% equ 0 (
    echo - Hooks successfully injected into "%GRAY%.git/hooks%RESET%" of this Repository.
) else (
    echo %RED%ERROR!%RESET% Failed to inject the Hook Scripts. Is this the root of the repository?
    pause
    exit
)

::Send the final message
for %%I in ("%cd%") do set "CURRENT_DIR=%%~nxI"
echo:
echo [%GREEN%DONE%RESET%] This repository (%GRAY%%CURRENT_DIR%%RESET%) is now, locally,
echo        configured to run useful Hook Scripts for this repository.
echo        The injected Hook Scripts in this local repository, can be
echo        found on the directory of "%GRAY%.github/hooks%RESET%" inside this
echo        repository. Now whenever the currently active Branch is
echo        locally changed on this repository, the Hook Scripts in the
echo        directory of "%GRAY%.github/hooks%RESET%" will be runned by Git.
echo:
echo %BLUE%This ".BAT" don't need to be executed again while this repository
echo %BLUE%exists locally in this machine.%RESET%
echo ===================================================================
echo:




::Pause the script before exit
pause