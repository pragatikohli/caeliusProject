# caeliusProject
# Chatbot Project

This is a chatbot project implemented using Java Swing for the user interface and 
MySQL database hosted on AWS RDS for storing data.

## Features

- Interactive chatbot interface using Java Swing GUI.
- Database connectivity to MySQL database hosted on AWS RDS.
- User input processing and response generation based on predefined rules.
- Integration with external services for additional functionalities (e.g., Google search for unknown queries).

## Technologies Used

- Java Swing: Used for building the graphical user interface (GUI) of the chatbot.
- MySQL: Used for storing and retrieving chatbot data from the database.
- AWS RDS: Hosted the MySQL database in the cloud for easy access and scalability.

## Setup and Installation

1. Clone the repository:
`git clone https://github.com/your-username/caeliusProject.git`

2. Import the project into your Java IDE (e.g., Eclipse, IntelliJ) 
and set up the mysql connector dependency. Add the mysql-connector.jar file to the project.

3. Configure the MySQL database connection settings in the project (if you want to add your own database connection):

- Open the `chatbot.java` file.
- Update the `url`, `username`, and `password` properties with your AWS RDS MySQL database connection details.

4. Build and run the project.

## Usage

- Launch the chatbot application.
- Enter your queries or messages in the chat input field.
- The chatbot will process the input and generate responses based on the predefined rules and database content.

## License

This project is licensed under the [MIT License](LICENSE).



