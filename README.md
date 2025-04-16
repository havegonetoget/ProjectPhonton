# ProjectPhonton
Software Engineering Project 

--------- How to Run ---------

This project requires the use of a either a virual or local machine to run. A virtual machine can be found and downloaded from the internet with instructions. Gradle is used to connection to the database. To install gradle, first homebrew needs to be installed on you linux machine. Go to https://brew.sh/ or copy the following code into your command line " /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)" " and follow the instructions to get it set up properly. Then once you make sure homebrew gets installed correctly, run "brew install gradle" in the command window which will install everything needed to run the program including java. Once this project's repoistory is copied to the desired machine and it located in a folder, the user can go through the terminal and run the command ./gradle.bash to activate the main of the program. You might have to give the gradle.bash file permissions to run. Must run python generator script (must run python script first) and main script spereately in two terminals. 

The main script will prompt the user to see if there should be a change of the newtowrk where broadcast will take place (answer with Y/N) and then load a player enerty screen that will allow for users to put in playerID, codename, and equipment numbers. Once the desired feilds are entered the user will hit. The save button will save the playerID's to the database and get them for future games. Clear will clear all the entries inthe player entry screen. Once ready hit the start game button to signal the server to begin the game where a 30 second countdown to start the game happens and then the game progress screen will pop up to show players and there scores.

There is an issue with the .wav files that when compressed they lose all of their data. To get Sound functionality working, downloading the .wav files seperately and put them into the photon_tracks folder.



--------- Team Members ---------
EthanGreen-16:       Ethan Green
havegonetoget:       Russell Van Norman
SethL77:             Seth Lea
Nwherring:           Nicholas Herring
hunterb516:          Hunter Braddy


