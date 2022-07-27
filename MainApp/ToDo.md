# To Do List

## app.java

## Command.java

- #### Bug1 : Invalid Command with 1 character
  - When using a name with a single character, like 'I'm w Ammad', it will throw an error since it detects 'w' as an invalid command. Only occurs if the single character is not beside a single quote
- #### Dev1 : Implement verification for **ls** command
  - Determine whether regex expressions should be used instead of current methods
  - Regex Methods > slower, lesser complexity, less error prone
  - Current Implementation > faster, error prone

## CreateEvent.java

- #### Dev1 : Conflict Notification
  - If event timing conflicts with other events, notfiy user what events are having conflicts
  - Look into verifyTimeInput() where it checks the startTime and endTime
  - Events that are labelled as **All-Day** should be displayed too
- #### Dev2 : Time Inputs should only be 15-minute intervals :heavy_check_mark:

## DBAccess.java

## Event.java

## ListEvent.java

- #### Dev1 : Conflicting Events output
  - in relation with [CreateEvent.java > Dev1](#createeventjava)

## Other
