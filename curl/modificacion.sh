#!/bin/bash
clear
#Formato
mediaType=application/x-www-form-urlencoded
uri=http://localhost:8111
######################
#Cambiar un miembro al grupo CIF = X0123456X
printf '\n\nAñadir miembro\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/grupos/X0123456X/ --data 'DNI= '	
#Modificar el album al grupo CIF = X0123456X
printf '\n\nModificar album\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/grupos/X0123456X/albumes/a0/ --data 'TITULO=Semidioses&FECHAPUBLICACION=25-01-2018&EJEMPLARESVENDIDOS=90909'	

#################################
#Fallos
#################################
printf '\n\n\n -------------------FALLOS-------------\n'
#Modificar grupo con PUT
printf '\n\nModificar grupo con POST\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" $uri/grupos/X0123456X/ --data 'NOMBRE=Tierra&FECHACREACION=20-11-2018'
#Modificar el album con un titulo vacio
printf '\n\nModificar el album con un titulo vacio\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/grupos/X0123456X/albumes/a0/ --data 'TITULO=&FECHAPUBLICACION=25-01-2018&EJEMPLARESVENDIDOS=90909'	
#Modificar el album con una fecha vacia
printf '\n\n#Modificar el album con una fecha vacia\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/grupos/X0123456X/albumes/a0/ --data 'TITULO=&FECHAPUBLICACION=&EJEMPLARESVENDIDOS=90909'
#Modificar pista al album con ID=a0 del grupo CIF = X0123456X
printf '\n\nModificar pista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" $uri/grupos/X0123456X/albumes/a0/pistas/p0 --data 'NOMBRE=Tartaro&DURACION=600'
printf '\n\n'
