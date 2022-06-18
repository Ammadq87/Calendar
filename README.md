# List of Current Commands

### Create an Event ✔️

- Use `event ce 'Birthday'` to create a new event
- When creating an event, a name for an event must be provided
- An event has optional attributes like date and time. If none are specified, that event is assumed to be an all-day event on the day it is created. If your event has a date or time, you can add those as such:
  - `event ce 'Birthday' -d '07-07-2002' -t '0000-2359'`
  - `event ce 'Birthday' -d '07-07-2002'`
  - `event ce 'Birthday' -t '0000-2359'`

### List Events

- Use `ls` to list events
- If no arguments are supplied, `ls` lists the events of the current date, whether if there are any
- The list command takes both date and time arguments, showing you what events appear at a certain time and date interval
  - `ls -d '07-07-2022'` : Lists all the events for the date supplied
  - `ls -t '0100-1500'` : Lists the current day's events within the supplied time slot
  - `ls -d '07-07-2022-07-08-2022'` Lists the events within the date interval
  - `ls -d -07-07-2022' -t '0100-1500'` or `ls -t '0100-1500' -d '07-07-2022-07-08-2022' ` : Lists the events within the current time and date interval

### Remove Events

### Edit Events

### Find Events
