#!/bin/bash
clear
#Formato
mediaType=application/x-www-form-urlencoded
uri=http://localhost:8111
######################
#Crear Persona DNI = 99999999A
printf 'Crear persona\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept:$mediaType" $uri/personas/99999999A --data 'NOMBRE=Athenea&APELLIDOS=Griego&FECHANACIMIENTO=05-06-2002'
#Crear grupo CIF = X0123456X
printf '\n\nCrear grupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept:$mediaType" $uri/grupos/X0123456X/ --data 'NOMBRE=Olimpo&FECHACREACION=20-11-2017'
#Añadir miembro al grupo CIF = X0123456X
printf '\n\nAñadir miembro\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept:$mediaType" $uri/grupos/X0123456X/ --data 'DNI=99999999A'
#Crear album al grupo CIF = X0123456X
printf '\n\nAñadir album\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept:application/x-www-form-urlencoded" $uri/grupos/X0123456X/albumes/ --data 'TITULO=Dioses&FECHAPUBLICACION=21-12-2017&EJEMPLARESVENDIDOS=900'	
#Crear pista al album con ID=a0 del grupo CIF = X0123456X
printf '\n\nAñadir pista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept:application/x-www-form-urlencoded" $uri/grupos/X0123456X/albumes/a0/pistas/ --data 'NOMBRE=Titanes&DURACION=300'	


#################################
#Fallos
#################################
printf '\n\n\n -------------------FALLOS-------------\n'
#Crear Persona con un campo en blanco
printf '\nCrear persona sin apellidos\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/personas/99999998A --data 'NOMBRE=Zeus&APELLIDOS=&FECHANACIMIENTO=05-06-2002'
#Crear Persona con todos los campos en blanco
printf '\nCrear persona data vacio\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/personas/99999997A --data ''
#Crear grupo con POST
printf '\n\nCrear grupo con metodo POST\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" $uri/grupos/X0123456X/ --data 'NOMBRE=Olimpo&FECHACREACION=20-11-2018'
#Creacion con algun campo vacio
printf '\n\nCrear grupo con nombre vacio\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/grupos/X0123456z/ --data 'NOMBRE=&FECHACREACION=20-11-2018'
#Crear album campo vacio
printf '\n\nAñadir album con un campo vacio\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" $uri/grupos/X0123456X/albumes/ --data 'TITULO=Dioses&FECHAPUBLICACION='	
#Crear pista duracion blanco
printf '\n\nAñadir pista con duracion en blanco\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" $uri/grupos/X0123456X/albumes/a0/pistas/ --data 'NOMBRE=Titanes&DURACION='
printf '\n\n'	
