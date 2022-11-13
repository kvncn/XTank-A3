# XTank-A3
CSC335 Fall 22 A3 Project (Kate Nixon and Kevin Nisterenko)

**EPILEPSY WARNING:**
WHEN MULTIPLE TANKS ARE AT PLAY, THE TANKS CAN FLICKER A LOT.
Please proceed at your own caution. 

BASIC CONTROLS:

  -Movement is basic ASWD, not arrow keys 
  
  -shoot by pressing the space bar (also press another key right after it, so the animation is sure to start)
  
  
  
RUN TIME FLEXIBLITY:

  -can change type of tank when a new client starts (terminal std input)
  
    "standard" for the standard tank with 100 health and 50 damage. Represented by light blue color
    
    "bomb" for the bomb tank with 100 health and 100 damage. Represented by a dark red color
    
    "turtle" for the turtle tank with 200 health and 50 damage. Represented by a dark green color
     
  -can select different type maze at server start (terminal std input)


TIPS FOR TESTING:

  -We have found it useful to move the first tank up a bit and then introduce the second client. When the tanks shots, it may take a second and/or require some clicking on keys

EXTRA: 

  -We understand the reason for the latency, it is because we do constant read/writes, but that ensures we never have out of date frames on clients, even if there is latency. This also ensures our bullet doesn't need anything special to move (like perhaps constantly flickering mouse over the screen)
