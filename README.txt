# Calendar - Group 250 - CSC207H1S 2020 - University of Toronto

## How to use:
Run Main.main() to access the options to perform on the user and its calendar's activities. Throughout the TextUI, you will be given choices to operate.
The first display is the option to create a user/logIn/exit. After logging in, further options on Alerts, Events and Memos will be displayed.
NOTE: Any invalid input will be alerted (such as logging in to a non-existed username/ input option number does not exist)

## Data Structures: Main class and 3 layers: entities, controllers, and useCases

### Entities: consisting of 4 classes  Event, Memo, Alert, User
These classses handle initiating and performing basic operations of the 4 different types of objects that are used in Calendar
For example: set a Memo associated to an Event

### Controllers: consisting of 5 classes: IO, LoggingIO, CalendarIO, LoggingUI, CalendarUI
These classes handle the User Interaction and its IO input

IO is the mother class of LoggingIO and CalendarIO, responsible for basic reading, appending and overwriting to a file
CalendarIO stores data inside a CalendarManager object in a text file when the program closes and retrieves the data from text file to the CalendarManager object
LoggingIO checks and writes logIn information of users to text database file

LoggingUI allows user to create new username, login and verify username
CalendarUI accesses the useCases and displaying choices for the user to operate on it


### useCases: consisting of CalendarManager class
CalendarManager is the platform to store and perform all options on entities for a given user


## Author:
Andrew Gurges
Terrence Tabi Kesse Amponsah
Kangming Gu
Xida Wang
Ahmad Sameh Rehan
Minh Duc Hoang

## Missing Feature:
New feature 3: Multiple users can have the same event in their calendar.
Optional Features 4:  user can postpone/reschedule an event and also duplicate it. (method implemented, but not in GUI)
