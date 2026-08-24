@echo off


ECHO "--------------------------"
ECHO "STARTING DBA2LibrarySystem"
ECHO "--------------------------"

ECHO "Starting dockers containers"
docker compose up -d



ECHO "Starting Backend..."
start "DBA2 Backend" cmd /c "cd backend && mvn spring-boot:run"


ECHO "Starting Frontend..."
start "DBA2 Frontend" cmd "cd frontend && npm install && npm run dev"

ECHO "-------------------------------------------------"
ECHO "  Navigate to: http://localhost:5173" 
ECHO "-------------------------------------------------"

PAUSE
