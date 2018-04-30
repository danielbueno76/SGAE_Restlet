#!/bin/bash
clear
uri=http://localhost:8111
######################
#Persona
printf 'Borrar persona\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/personas/11111111A
#Pista
printf '\n\nBorrar pista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/grupos/D0123456D/albumes/a1/pistas/p0
#Album
printf '\n\nBorrar album\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/grupos/D0123456D/albumes/a1/
#Grupo
printf '\n\nBorrar grupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/grupos/E0123456E/
#################################
#Fallos
#################################
#Delete no permitido
printf '\n\n\n -------------------FALLOS-------------\n'
printf '\n\nBorrar personas\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/personas/
#Grupos
printf '\n\nBorrar grupos\n'
curl -w "\nCodigo respuesta: %{htt p_code}" -X DELETE -H "Accept: text/plain" $uri/grupos/
#Albumes
printf '\n\nBorrar albumes\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/grupos/E0123456E/albumes/
#Pistas
printf '\n\nBorrar pistas\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/grupos/E0123456E/albumes/a1/pistas/
#Miembros
printf '\n\nBorrar miembros\n'
curl -w "\nCodigo respuesta: %{http_code}" -X DELETE -H "Accept: text/plain" $uri/grupos/miembros
printf '\n\n'
