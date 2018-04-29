#!/bin/bash
clear
#Formato
mediaType="application/x-www-form-urlencoded"
######################
#Cambiar un miembro al grupo CIF = X0123456X
printf '\n\nAñadir miembro\n'
curl -w "\nCodigo respuesta: %{http_code}" -X PUT -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/ --data 'DNI= '	#Va raro
#Modificar el album al grupo CIF = X0123456X
printf '\n\nAñadir album\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/albumes/a0/ --data 'TITULO=Semidioses&FECHAPUBLICACION=25-01-2018'	
#Crear pista al album con ID=a0 del grupo CIF = X0123456X
printf '\n\nAñadir pista\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/albumes/a0/pistas/p0 --data 'NOMBRE=Tartaro&DURACION=600'	


#################################
#Fallos
#################################
printf '\n\n\n -------------------FALLOS-------------\n'
#Modificar grupo con PUT
printf '\n\nCrear grupo\n'
curl -w "\nCodigo respuesta: %{http_code}" -X POST -H "Accept: $mediaType" http://localhost:8111/grupos/X0123456X/ --data 'NOMBRE=Tierra&FECHACREACION=20-11-2018'
