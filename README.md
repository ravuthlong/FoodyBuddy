Video demo: https://www.youtube.com/watch?v=cx1P8BzFGpY

Android APK file: https://www.dropbox.com/s/wlvj9tfekwdyfgx/Foody_Buddy.apk?dl=0

Android repo: https://github.com/ravuthlong/FoodyBuddy

REST-API repo: Please refer to the foody-buddy-server/ dir contains in the zip file.

*Authors' note: We do not publish the REST-API server repo on github due to the reason it contains our Database credentials and URL.

# Background
A lot of people want to try different food but do not have willingness to do so. One reason might be because their friends are not interested in trying different food, or they do not like the restaurant they are planning to go to. The food lover can choose to go alone, but why go alone when there are plenty of like-minded people out there who loves to try different food just as much as them? This is also a chance to get to know more people of different backgrounds.

Foody Buddy is an Android mobile app that allows people who love the same type of food to find each other and eat together. We build this app based on a simple idea: we want to help people to break their routine lifestyle, to create more possibilities in life, by connect people with their favorite food. Users can invite others to eat together by send broadcast message to other users nearby, and those who joined the same event can start to chat with each other. Users can comment or follow the event they interested in, as well as get friend recommendation based on the food type they interested in.

# Installation

## Backend
The backend includes 2 main parts: REST API server and PostgreSQL database. The server and database should be put on the cloud with any service provider, in order to work as Internet reachable endpoints. The installation steps are as follow:

### AWS - Server and Database setup
- Create or login with your account on aws.amazon.com

- Ubuntu Server: From Compute > EC2, spawn a free-tier Ubuntu server from AWS console. Go to Security-Group and enable TCP port 22 (for SSH), and TCP port 8080, which is the port that nodejs server listens for incoming API request

- PostgreSQL server: From Database > RDS, spawn a free-tier PostgreSQL following the instruction from Amazon. Name the db "foodybuddydb". Record the publicURL, the username and password for future use.

### REST API server
Extract or copy all the code from /foody-buddy-server to a Ubuntu server on the cloud.
Edit using your favorite editor, replace with your Database URL and credential from AWS

```
vim foody-buddy-server/server/setting/dbURL.js
url: 'postgres://<username>:<password>@<your-url>.rds.amazonaws.com:5432/foodybuddydb'

```
In the main dir, execute the following command to starts the server and make it listen to port 8080


### PostgreSQL Database
- Login to PostgreSQL server on AWS with any tool or workbench using your credential.
- Open /foody-buddy-server/db-tables-init.txt. Here will have all the code for initialize the database with core tables.
- Using terminal or query to create tables based on the db-tables-init.txt file.

## Frontend
- The frontend, which the user interacts with, is compatible with any Android device running Android Kitkat 4.4 and above. 
- The installation for Android is simple and straightforward. This will use the Foody_Buddy.apk includes in the project zip file.
 	+ Copy the Foody_Buddy.apk to Android storage, either phone storage or SD card.
 	+ Open File Explorer on Android, navigate and double tap on the Foody_Buddy.apk file. 
- After the installation, the app will be ready to run.


# Developers
Ravuth Long [Github](https://github.com/ravuthlong) 

Linh Phan [Github](https://github.com/paulphoenix01) 

Lingyan Xie [Github](https://github.com/emilylxie)
