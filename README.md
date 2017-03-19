# cw-cm
Nathan Hanak Coursework 3 Repo

**Information**

This repository contains Nathan Hanak's attempt at Coursework 3 for the PiJ class
for the Birkbeck MSc Computer Science program. Contributing GitHub accounts are BBK-PiJ-2016-06 and NateHan, both of whom are the same person.

**Project Description**

The program "Contact Manager" contains a mock scheduling program intended for use by
a small business. It contains Meetings past and present as well as contacts who attend said meetings.
The program saves and stores all the information, also allowing notes to be stored and continuously added
to all meetings.

Program also contains a Flush feature which allows the program to save all information locally
to an external file. As long as that file stays intact without external influence, 
it will reload all previously stored information upon re-opening the program. The program will
also check to see if any Future meetings have "expired" or passed their deadlines and promptly 
convert them in to a past meeting.

Due to the heavy reliance on Java collection classes, many methods were completed using Java 8 techniques.
Since many of ContactManager's interface methods were very similar, several additional Private
methods were created to uphold the DRY principle. 

**SetUp**

Project was set up in the "gradle" format. 

**Addendum**

While all code is my own, at several points I did ask for assistance on StackOverflow for help with understanding 
some programming concepts and to help clear through syntax errors preventing me from getting the intended functionality.
In the effort of maintaining academic honesty and attempting to avoid plagiarism - I am listing those questions below:

http://stackoverflow.com/questions/41536667/tranversing-and-filtering-a-set-comparing-its-objects-getters-to-an-array-using
http://stackoverflow.com/questions/41402956/using-lambda-and-foreach-to-find-an-object-in-a-set-and-triggering-a-boolean
http://stackoverflow.com/questions/41420566/filter-loses-access-to-internal-objects-methods-when-passed-through-a-generic-p
http://stackoverflow.com/questions/41499428/generic-parameter-returns-listchildinterface-in-stream-but-direct-method-call
http://stackoverflow.com/questions/41517841/can-a-vararg-parameter-be-of-type-super-object
http://stackoverflow.com/questions/41536667/tranversing-and-filtering-a-set-comparing-its-objects-getters-to-an-array-using