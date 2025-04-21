# ProjectPhonton
Software Engineering Project 

--------- How to Run ---------

This project requires the use of a either a virual or local machine to run. A virtual machine can be found and downloaded from the internet with instructions. Gradle is used to connection to the database. To install gradle, first homebrew needs to be installed on you linux machine. Go to https://brew.sh/ or copy the following commands into your command line:
1. /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
2. echo >> /home/student/.bashrc
3. echo 'eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"' >> /home/student/.bashrc
4. eval "$(/home/linuxbrew/.linuxbrew/bin/brew shellenv)"
5. brew install gradle
   
To make sure everthing gets installed correctly run the following commands in your command line:
1. javac -version
2. java -version
3. gradle --version

Once this project's repoistory is copied to the desired machine and it located in a folder, the user can go through the terminal and run the command ./gradle.bash to activate the main of the program. You might have to give the gradle.bash file permissions to run. The python script needs to be ran in a seperate terminal before game start countdown finishes.

The main script will prompt the user to see if there should be a change of the newtowrk where broadcast will take place (answer with Y/N) and then load a player enerty screen that will allow for users to put in playerID, codename, and equipment numbers. Once the desired feilds are entered the user will hit. The save button will save the playerID's to the database and send them to be put into the game. Clear will clear all the entries in the player entry screen. Once ready hit the start game button to signal the server to begin the game where a 30 second countdown to start the game happens and then the game progress screen will pop up to show players and there scores updating.

There is an issue with the .wav files that when compressed they lose all of their data. To get music functionality working, downloading the .wav files seperately and put them into the photon_tracks folder.



--------- Team Members ---------
EthanGreen-16:       Ethan Green
havegonetoget:       Russell Van Norman
SethL77:             Seth Lea
Nwherring:           Nicholas Herring
hunterb516:          Hunter Braddy


