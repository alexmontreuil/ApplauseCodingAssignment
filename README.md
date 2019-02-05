# ApplauseCodingAssignment
Applause Coding Assignment

Requirements:
minimum java version: JAVA 8

Directions:
1) create the following directory on the c:\ drive: C:\alexmontreuil
2) place all .csv files in the above directory
3) retrieve the zipped executable jar file ( codingassignment-1.zip ) located in the ApplauseCodingAssignment project under the "target" folder
4) unzip it in any folder on the c:\ drive
5) open a DOS console and cd to the folder where the file was unzipped
6) type the following command to run the springboot app:
"java -jar codingassignment-1.jar"

**note: make sure that port 8080 is not being used by another application

7) open a browser and type the following url in the address bar to open the swagger page:
http://localhost:8080/swagger-ui.html#/tester-matching
8)click on "tester-matching" to open the tester-matching operation
9) click on "try out" to execute the operation
10) enter your request payload, and click "Execute" to run the query

sample request payload:

{
  "countries": "ALL",
  "devices": "ALL"
}

{
  "countries": "iPhone 5,iPhone 4S",
  "devices": "US,JP"
}


