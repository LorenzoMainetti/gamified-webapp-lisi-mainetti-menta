# Data Bases 2 Project - a.y. 2020-2021
The goal of the project is to implement a web app that deals with gamified consumer data collection.
- **User View**: a user can access the homepage where the product of the day is published, and he can complete the related questionnaire. The application computes the gamification points of each user that can be checked in the leaderboard.
- **Admin View**: an admin can access a reserved homepage where he is able to create a new questionnaire for the day of for a future date, inspect or delete a past questionnaire.

More infos on the specifications here -> [specs](https://github.com/PrivateAbstractAleLisi/gamified-webapp-lisi-mainetti-menta/tree/main/specs)

## Technical specifications
The application has been realized in a JEE (Java Enterprise Edition) environment, particularly exploiting JPA, EJBs and Servlets.
The following technologies are employed:
- MySQL DBMS
- TomEE JEE application/web server
- Triggers and constraints for database-level business constraints and rules.
#### Backend:
- JPA for object relational mapping and transaction management
- EJB (stateless) for the business objects
- Java servlet for the client components
#### Frontend:
- HTML5 for the static structure
- JavaScript for dynamically modifying the pages

## Documentation
The ER Diagram of the project and other design choices are accurately described here -> [design](https://github.com/PrivateAbstractAleLisi/gamified-webapp-lisi-mainetti-menta/tree/main/design)

## Screenshots
Here are some screenshots from the web app, showing the user view and the admin view.
#### User View
<kbd>
  <img src="https://i.ibb.co/QmVzJwV/User-Homepage.png">
</kbd>
<br/><br/>
<kbd>
  <img src="https://i.ibb.co/9hcy25j/User-Leaderboard.png">
</kbd>
<br/><br/>
<kbd>
  <img src="https://i.ibb.co/PYqpWnR/User-Questionnaire2.png">
</kbd>

#### Admin View
<kbd>
  <img src="https://i.ibb.co/VYxLvkt/Admin-Homepage.png">
</kbd>
<br/><br/>
<kbd>
  <img src="https://i.ibb.co/jLgVfL3/Admin-Past-Questionnaires.png">
</kbd>
<br/><br/>
<kbd>
  <img src="https://i.ibb.co/g6JVgJP/Admin-Inspection-Page.png">
</kbd>

## Group Members
- [__Alessandro Lisi__](https://github.com/PrivateAbstractAleLisi)
- [__Lorenzo Mainetti__](https://github.com/LorenzoMainetti)
- [__Andrea Menta__](https://github.com/Menta99)
