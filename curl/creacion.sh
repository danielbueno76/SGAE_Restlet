#!/bin/bash
clear
#Formato
mediaType="application/x-www-form-urlencoded"
######################
#Crear Persona DNI = 99999999A
printf 'Crear persona\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" http://localhost:8111/personas/99999999A --data 'NOMBRE=Athena&APELLIDOS=Griego&FECHANACIMIENTO=05-06-2002'
#Crear grupo CIF = X0123456X
printf '\n\nCrear grupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/ --data 'NOMBRE=Olimpo&FECHACREACION=20-11-2017'
#Añadir miembro al grupo CIF = X0123456X
printf '\n\nAñadir miembro\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/ --data 'DNI=99999999A'	#Va raro
#Crear album al grupo CIF = X0123456X
printf '\n\nAñadir album\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/albumes/ --data 'TITULO=Dioses&FECHAPUBLICACION=21-12-2017'	
#Crear pista al album con ID=a0 del grupo CIF = X0123456X
printf '\n\nAñadir pista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/albumes/a0/pistas/ --data 'NOMBRE=Titanes&DURACION=300'	


#################################
#Fallos
#################################
printf '\n\n\n -------------------FALLOS-------------\n'
#Crear grupo con POST
printf '\n\nCrear grupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/ --data 'NOMBRE=Olimpo&FECHACREACION=20-11-2018'
