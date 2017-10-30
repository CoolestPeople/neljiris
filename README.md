# neljiris

Neljiris -- from the Inari Sami _nelji_, meaning _four_, is a reinforcement learning-based program that learns to play Tetris.

By Andrew Gritsevskiy and Louie Golowich
AP Computer Science, June 2016

## prerequisites

* Apache commons lang (Download from [here](https://commons.apache.org/proper/commons-lang/download_lang.cgi))
* Intellij (preferred) (Download from [here](https://www.jetbrains.com/idea/))


## setup instructions

1. Get the code.
2. Open IntelliJ
3. Go to File > Project Structure
4. Go to Project Settings > Libraries
5. Press the + button
6. Select 'Java'
7. Select the Apache Commons Lang library
8. Add it to neljiris.
9. Setup the SDK if it isn't set up


## running instructions

1. Edit the configuration file to whatever you would like to do.
    * For the first run, set
        * LEARN_GAME to true
        * DISPLAY_GAME to false
        * SAVE_BEST_GAME to false
        * TOTAL_RUNS to approximately 100000
        * SAVE_CHUNK to approximately 10000
    * If you already taught it to play sufficiently well and you want to watch it, set
        * LEARN_GAME to false
        * DISPLAY_GAME to true
        * SAVE_BEST_GAME to true
        * TOTAL_RUNS to anything you want
        * SAVE_CHUNK to anything you want
2. Press 'run'!
