# Coronavirus game

## Introduction
This is a coronavirus game,which takes place when Coronavirus is infecting the world, in
Israel Kibutz.
The player has limited time. His goal is to buy enough food and return home, within allocated time.
He needs to gather money and to buy food. He can buy bike, it will double his speed. There are infected people
in Kibutz and each time player touches them he losses life.
If player reaches home with foods within an allocated time - he wins. Otherwise, he is loss. 

## Students
Shiran Golbar, 313196974
Lev Levin, 342480456

## How to run
Run Launcher.java which is class responsible for starting the game.


## Packages and their responsibilites
1. Utils - helper classes like timers, Drawable2d, audioPlayer that helped to implemet some features and do not
fit to other packages.
2. game - General Game related classes ,like CoronavirusGame(pass between one level to another, showing game
related messages etc.)
3. game.level - level related classes. Connects other more specific classes to implement the logic and physical engine
for levels.
4. game.factory - responsible for generating levels.
5. game.gameObjects - responsible for game objects and their relation to the physical world of the game(they don't
implement logic within)
6. game.gameWorld - responsible for physical 3d world generation, using game objects from game.gameObjects package.
7. game.level.logic - responsible for level rules and logic.
8. math- math related classes.
9. external loaders - classes from the internet that helped to load '.obj' objects.
10. events- classes that are responsable for event handling in the game.
