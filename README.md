Battle-Simulator
================

Simulates a basic battle. 

Classes-

Actor: 

Abstract class, contains shared variables for all Actor types.

Elf, Hobbit, Orc, Wizard: 

Subclasses of Actor, and adds all unique effects and methods unique to that Actor type. For example, a wizard has a hasStaff variable.

Actor Factory:

Wrapper that encapsulates tools needed for the automatic generation of Actor subclass objects. 

Army:

Allows for quick creation of large ArrayLists of Actors of various types and factions.

Simulator:

Contains simulator controls like suspend, populate, and run. 

FXLauncher:

Controls visual effects, menus, and Stage. 
