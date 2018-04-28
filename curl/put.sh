#!/bin/bash
#Formato para personas/grupos/sistema/miembros
mediaType = text/plain
#Formato para pistas/albumes
mediaType2 =text/html
######################
#Crear Persona DNI = 99999999A
printf '\n\nCreacion de una persona'
curl -X PUT -H 'Accept: text/plain' -i http://localhost:8111/personas/99999999A --data 'NOMBRE=Athena&APELLIDOS=Olimpo&FECHANACIMIENTO=05-06-2013'


#Grupos
printf '\n\nGrupos'
curl -X GET -H "Accept: $mediaType" -i http://localhost:8111/grupos/
#Grupo CIF = D0123456D
printf '\n\nGrupo'
curl -X GET -H "Accept: $mediaType" -i http://localhost:8111/grupos/D0123456D/
#Albumes CIF = D0123456D
printf '\n\nAlbumes'
curl -X GET -H "Accept: $mediaType2" -i http://localhost:8111/grupos/D0123456D/albumes/
#Album del grupo CIF = D0123456D y album ID= a1
printf '\n\nAlbum'
curl -X GET -H "Accept: $mediaType2" -i http://localhost:8111/grupos/D0123456D/albumes/a1/
#Pistas del grupo CIF = D0123456D y album ID= a1
printf '\n\nPistas'
curl -X GET -H "Accept: $mediaType2" -i http://localhost:8111/grupos/D0123456D/albumes/a1/pistas/
#Pista del grupo CIF = D0123456D, album ID= a1 y pista ID = p0
printf '\n\nPista'
curl -X GET -H "Accept: $mediaType2" -i http://localhost:8111/grupos/D0123456D/albumes/a1/pistas/p0
#Miembros CIF = D0123456D
printf '\n\nMiembros'
curl -X GET -H "Accept: $mediaType" -i http://localhost:8111/grupos/D0123456D/miembros

