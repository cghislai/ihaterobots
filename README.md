# I Hate Robots

A 2D platform game, remake of the 1993 Software Creattions' Jetpack DOS classic.

This was done in 2011 for a Slick2D (java 2D game library) competition. It was hosted on google code, but
I neglected to export it while I had still time. 

![Screenshot](http://i56.tinypic.com/14e9flw.png)

Looking at the source, it used ant and was built on netbeans. I commit everything just in case.

## Getting started
- Download the release tarball
- Create a `.iHateRobots` directory in your home (there is a bug, the game wont create it for you but rather throw an exception)
- Run `java -Djava.library.path="lib/native" -jar IHateRobots.jar` from the extracted directory.

## How to play
- Move around using arrows
- Use left CTRL to trigger the digging tool.
Some tiles may be dug, and will fold back after some time. You can try to 
capture enemies by making them move onto the dug tile right when it is about to refold.
- Collect all green gems
- Move to the escape door that will open.

All of that by avoiding ennemies of course.

And dont try to collect all the gold, you greedy capitalist, you will regret it.


## Links
- https://code.google.com/archive/p/ihaterobots/ The google code archive
- https://gamejolt.com/games/i-hate-robots/5276 The released version was published there
- http://slick.ninjacave.com/forum/viewtopic.php?t=3241 The slick competion game topic
- There was also a gameplay vid but it seems flash is required to play it: http://tinypic.com/r/1z216h2/7


## License
Original work was GPLv3 https://www.gnu.org/licenses/gpl-3.0.en.html. 
Im not sure I can relicense it to a more permissive one because of the libraries used.

Notice the excellent work on assets, sprites drawn using paint, and 
sound recordings of my own voice or some paper scratching :)

