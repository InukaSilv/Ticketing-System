@echo off

:: Start the backend from the correct directory
start cmd /k "cd /d %~dp0\ticketing-backend && mvnw spring-boot:run"

:: Start the frontend
start cmd /k "cd /d %~dp0\ticketing-frontend\src && npm run dev"
