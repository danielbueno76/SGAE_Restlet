#!/bin/bash
clear
#Formato para personas/grupos/sistema/miembros
mediaType="text/plain"
#mediaType="application/xml"
#Formato para pistas/albumes
mediaType2="text/plain"
#mediaType2="text/html"
######################
#Sistema
printf 'Sistema\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111
#Personas
printf '\n\nPersonas\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/personas/
#Persona DNI = 00000000A
printf '\n\nPersona\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/personas/00000000A
#Grupos
printf '\n\nGrupos\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/grupos/
#Grupo CIF = D0123456D
printf '\n\nGrupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/grupos/D0123456D/
#Albumes CIF = D0123456D
printf '\n\nAlbumes\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" http://localhost:8111/grupos/D0123456D/albumes/
#Album del grupo CIF = D0123456D y album ID= a1
printf '\n\nAlbum\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" http://localhost:8111/grupos/D0123456D/albumes/a1/
#Pistas del grupo CIF = D0123456D y album ID= a1
printf '\n\nPistas\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" http://localhost:8111/grupos/D0123456D/albumes/a1/pistas/
#Pista del grupo CIF = D0123456D, album ID= a1 y pista ID = p0
printf '\n\nPista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" http://localhost:8111/grupos/D0123456D/albumes/a1/pistas/p0
#Miembros CIF = D0123456D
printf '\n\nMiembros\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/grupos/D0123456D/miembros

#################################
#Fallos
#################################
#URL inexistente
printf '\n\n\n -------------------FALLOS-------------\n'
printf '\n\nPersonas\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/personas
#Persona no introducida en el sistema
printf '\n\nPersona\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/personas/90000000A
#Grupo no añadido
printf '\n\nGrupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" http://localhost:8111/grupos/D0123456F/
#Album no añadido
printf '\n\nAlbum\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" http://localhost:8111/grupos/D0123456D/albumes/a10/
#Pista no añadida
printf '\n\nPista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" http://localhost:8111/grupos/D0123456D/albumes/a1/pistas/p10
printf '\n\n'




