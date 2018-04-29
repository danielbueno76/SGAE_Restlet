#!/bin/bash
clear
######################
#Persona
printf 'Borrar persona\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" http://localhost:8111/personas/11111111A
#Pista
printf '\n\nBorrar pista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" http://localhost:8111/grupos/D0123456D/albumes/a1/pistas/p0
#Album
printf '\n\nBorrar album\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" http://localhost:8111/grupos/E0123456E/albumes/a1/
#Grupo
printf '\n\nBorrar grupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" http://localhost:8111/grupos/E0123456E/
#################################
#Fallos
#################################
#Delete no permitido
#Persona
printf '\n\nBorrar personas\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" http://localhost:8111/personas/
printf '\n\n'
