
 #!/bin/bash          
mediaType=text/plain
echo $mediaType
curl -X GET -H "Accept: $mediaType" -i http://localhost:8111/personas/
