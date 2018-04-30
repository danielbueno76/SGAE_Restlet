#!/bin/bash
clear
#Formato para personas/grupos/sistema/miembros
mediaType=text/plain
#mediaType=application/xml
#Formato para pistas/albumes
mediaType2=text/plain
#mediaType2=text/html
uri=http://localhost:8111
######################
#Sistema
printf 'Get a Sistema\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri
#Personas
printf '\n\nGet: ./Personas/\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/personas/
#Persona DNI = 00000000A
printf '\n\nGet: .Personas/Persona\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/personas/00000000A
#Grupos
printf '\n\nGet: ./Grupos/\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/grupos/
#Grupo CIF = D0123456D
printf '\n\nGet: ./Grupos/Grupo/\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/grupos/D0123456D/
#Albumes CIF = D0123456D
printf '\n\nGet: ./Grupos/Grupo/Albumes/\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" $uri/grupos/D0123456D/albumes/
#Album del grupo CIF = D0123456D y album ID= a1
printf '\n\nGet: ./Grupos/Grupo/Albumes/Album/\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" $uri/grupos/D0123456D/albumes/a1/
#Pistas del grupo CIF = D0123456D y album ID= a1
printf '\n\nGet: ./Grupos/Grupo/Albumes/Album/Pistas/\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" $uri/grupos/D0123456D/albumes/a1/pistas/
#Pista del grupo CIF = D0123456D, album ID= a1 y pista ID = p0
printf '\n\nGet: ./Grupos/Grupo/Albumes/Album/Pistas/Pista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" $uri/grupos/D0123456D/albumes/a1/pistas/p0
#Miembros CIF = D0123456D
printf '\n\nGet: ./Grupos/Grupos/Miembros\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/grupos/D0123456D/miembros

#################################
#Fallos
#################################
#URL inexistente
printf '\n\n\n -------------------FALLOS-------------\n'
printf '\n\nGet: /Personas\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/personas
#Persona no introducida en el sistema
printf '\n\nGet: Persona no añadida\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/personas/90000000A
#Grupo no añadido
printf '\n\nGet: Grupo no añadido\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType" $uri/grupos/D0123456F/
#Album no añadido
printf '\n\nGet: Album no añadido\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" $uri/grupos/D0123456D/albumes/a10/
#Pista no añadida
printf '\n\nGet: Pista no añadida\n'
curl -w "\nCodigo respuesta: %{http_code}" -X GET -H "Accept: $mediaType2" $uri/grupos/D0123456D/albumes/a1/pistas/p10
printf '\n\n'




