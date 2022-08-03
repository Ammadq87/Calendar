# To Do List

## app.java

## Command.java

- #### Bug1 : Invalid Command with 1 character
  - When using a name with a single character, like 'I'm w Ammad', it will throw an error since it detects 'w' as an invalid command. Only occurs if the single character is not beside a single quote
- #### Bug2 : Invalid Time and Date error messages

  - Determine what error messages should be displayed for when the time and date input doesn't pass the regex
  - in relation with [Message.java > Bug1](#bug1-pairing-optional-and-exisitng-error-messages)

- #### Dev1 : Implement verification for **ls** command
  - Determine whether regex expressions should be used instead of current methods
  - Regex Methods > slower, lesser complexity, less error prone
  - Current Implementation > faster, error prone

## CreateEvent.java

- #### Dev1 : Conflict Notification
  - If event timing conflicts with other events, notfiy user what events are having conflicts
  - Look into verifyTimeInput() where it checks the startTime and endTime
  - Events that are labelled as **All-Day** should be displayed too
  - in relation with [ListEvent.java > Dev1](#dev1--conflicting-events-output)
- #### Dev2 : Time Inputs should only be 15-minute intervals :heavy_check_mark:

## DBAccess.java

## Event.java

## FindEvent.java

- #### Feature: Display Editing Options for Found Queries

## ListEvent.java

- ### NOTE :
  - ListEvent.java should be the last feature to be worked on as more features will affect this. Currently, a basic query for getting events based on time and date will do the job. Update and Delete functions should be implemented from now on, then Read function can continue development
- #### Dev1 : Conflicting Events output
  - in relation with [CreateEvent.java > Dev1](#dev1--conflict-notification)
- #### Dev2 : Time Flag Implementation
  - when time flag is part of input, the time interval of the current date should be displayed with the events that occur within that day

## Messages.java

- #### Bug1 :Pairing Optional and Exisitng error messages
  - in relation with [Command.java > Bug2](#bug2--invalid-time-and-date-error-messages)

## New Features

- Request Booking

  - Users can request a booking to another user (ex: Doctor Appt)
  - example cases:
    - event req 'Doc Appt' -t '1200-1515' -d '7-7-2022' -email 'doctor@office.com'
    - event req 'Doc Appt' -d '7-7-2022' -email 'doctor@office.com'
      - This would be an <All-Day> event
    - event req 'Doc Appt' -t '1200-1515' -email 'doctor@office.com'
      - The date would be the date it was sent
    - Subject and Optional Message should be implemented too
  - <-email> tag can only be used if it exists as a contact

- Create Contact

  - Users can create a contact to book meetings to
  - example cases:
    - event contact 'Doctor'
    - event contact 'Doctor' -name 'Ammad' -email 'ammad@mail.com'
    - name and email tag are optional, but if user is requesting a booking, email tag should be included

- Login
  - There should be user accounts, login acess, etc
  - example cases:
    - event login -user 'ammad' -pass '123'
    - no commands cannot be executed if user is not logged in
    - NOTE: figure how to make the password blurred
