PiedPipers

Command-line argument: java piedpipers.sim.Piedpipers arg1 arg2 ....

arg1: group name 
arg2: number of pipers
arg3: number of rats
arg4: graphic interface (true-enable, false-disable)
arg5: the seed for random distribution of the rats(you could set it to any integer)
arg6: the size of the field (eg. If it is 100*100, then the input is 100)

Example: Play a game with 5 pipers, 10 rats, Display GUI, the seed for random generator is 6, the size is 50*50

java piedpipers.sim.Piedpipers dumb1 5 10 true 6 50


Parallel Pied Pipers

The legend of the Pied Piper of Hamelin states that he was a rat-catcher hired by the city of Hamelin to eliminate its rat population. The Pied Piper played his flute, which attracted the rats, and drew them to the river where they drowned. When the city did not pay the piper, he is said to have lured the city's children away.

For this project, we'll be more concerned with the first part of the legend, i.e., eliminating rats by luring them with music. You will control a team of pipers in a field of rats. Your goal is to lure every rat from one side of the field to the other, where a grim death awaits them. You will write the code for a piper so that he can effectively and efficiently lure rats, both alone and in teams.

The extermination take place in a large square field of side length F meters, that is partitioned in half by a fence. Precisely in the middle of the fence is a 2m wide gate. A number S of rats are randomly (uniformly) distributed in one half of the field and the other half is empty. The basic task is to move all of the rats through the gate to the other half of the field. Pipers can be in one of two states: (a) playing music, in which case they attract rats, or (b) silent, in which case they do not attract rats, but can run faster through the field.

Rats are frantic creatures, and will wander randomly at 1m/s if left alone. Rats ignore silent pipers. However, when a piper playing music comes close, they react by moving towards the closest playing piper. When a piper is within 10m of a rat, but further than 2m, the rat walks at 1m/s directly towards the closest piper. When a rat is within 2m of a playing piper it stays put, mesmerized by the music. Pipers can move at up to 1m/s when playing music, or can run at 5m/s when not playing music.

The simulator is discrete rather than continuous, and operates at 0.1s granularity. The new positions of the rats are calculated based on the positions of the rats and pipers at the current time. The new positions of the piper(s) are specified by your program, and may be any position up to 0.1m away from the current position (if the piper is playing) or 0.5m away (if the piper is not playing). You also specify whether the piper plays or not on the coming turn.

A rat that collides with a fence (either in the middle of the field, or on its perimeter) will bounce off as if it were a ray of light (angle of incidence equals angle of reflection) within a single simulator timestep.

Pipers and rats can get arbitrarily close to one another, and climb over one another as necessary.

You have d pipers, each of which is executing an instance of the same code, but instantiated with a unique piper-id parameter (between 1 and d). The pipers start in the empty half of the field on the far fence, spread out uniformly. So if the line defining the far fence stretches from (0,0) to (0,F), the d pipers start at $(0,F/(d+1)),(0,2F/(d+1)), \ldots, (0,dF/(d+1))$. Pipers have complete access to all positional information, but do not explicitly communicate with other pipers.

The simulator will detect when the goal is achieved and you will be ranked based on the time taken to achieve the goal. There will be a timeout to handle cases where the last few pesky rats simply can't be convinced to go where you want them. In such a case, you will be ranked by the number of rats transferred. We will run some tournaments at the end of class with various values for the parameters F, S, and d.

Some things to think about:

Given no explicit communication between pipers, how might you achieve coordinated behavior?
This project is similar to the Sheepdog Trials project given in PPS 2013. In that project, sheepdogs repelled rather than attracted the animals they were trying to herd. What similarities and differences are there between the two projects? Might there be something to learn from last year's reports?
Note that this is a single-player game. Each group will be operating in their own private field.
