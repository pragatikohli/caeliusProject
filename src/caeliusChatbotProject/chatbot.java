package caeliusChatbotProject;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Stack;

//java swing imports
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class chatbot extends JFrame implements ActionListener {
    static JTextArea area = new JTextArea();
    JTextField field = new JTextField();
    JTextField train_field = new JTextField();
    JScrollPane sp;
    JButton send;
    JButton train;
    LocalTime time = LocalTime.now();
    LocalDate date = LocalDate.now();

    public chatbot(String title) {
        super(title);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        getContentPane().setBackground(Color.black);
        field = new JTextField();
        // for button
        send = new JButton("Ask");
        send.setFont(new Font("Times New Roman", Font.ITALIC, 15));
        send.setBackground(Color.white);
        send.setBounds(715, 495, 65, 35);
        send.setSize(80, 33);
        add(send);
        
        train = new JButton("Train");
        train.setFont(new Font("Times New Roman", Font.ITALIC, 15));
        train.setBackground(Color.white);
        train.setBounds(725, 525, 65, 35);
        train.setSize(80, 35);
        add(train);
        
        // For Text area
        area.setEditable(false);
        area.setBackground(Color.white);
        add(area);
        area.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        // scrollbar
        sp = new JScrollPane(area, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sp.setBounds(10, 20, 775, 470);
        add(sp);

        // For TextField
        field.setSize(715, 35);
        field.setLocation(10, 495);
        field.setForeground(Color.black);
        field.setFont(new Font("Times New Roman", Font.ITALIC, 15));
        add(field);
     
        train_field.setSize(715, 35);
        train_field.setLocation(10, 525);
        train_field.setForeground(Color.black);
        train_field.setFont(new Font("Times New Roman", Font.ITALIC, 15));
        add(train_field);
        send.addActionListener(this);
        train.addActionListener(this);
        getRootPane().setDefaultButton(send);

    }

    // triggered when ASK button is clicked
    public void actionPerformed(ActionEvent e) {
        String message = field.getText().toLowerCase();
        if(e.getSource() == send) {
        area.append("You : " + field.getText() + "\n");
        field.setText("");
        
            String answer = getFromDatabase(message);
            if (!answer.isEmpty()) {
                bot(answer);
            } else if (isMath(message)) {
                try {
                    double result = evaluateMath(message);
                    bot("The result is: " + result);
                } catch (Exception exception) {
                    String ans = exception.toString();
                    bot(ans);
                    bot("Invalid mathematical expression.");
                }
            } else if (message.contains("date") || message.contains("month") || message.contains("year")
                    || message.contains("day")) {

                String cdate = new String();
                cdate = cdate + date.getDayOfWeek() + "," + date.getDayOfMonth() + " " + date.getMonth() + " "
                        + date.getYear();
                bot(cdate);

            } else if (message.contains("time")) {

                String ctime = new String();
                if (time.getHour() > 12) {
                    int hour = time.getHour() - 12;
                    ctime = ctime + hour + ":" + time.getMinute() + ":" + time.getSecond() + " PM";
                }

                else {

                    ctime = ctime + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond() + " AM";
                }
                bot(ctime);

            } else {
                search(message);
            }
        }
        else {
        	boolean flag = false;
        	String trainAns = train_field.getText().toLowerCase();
        	try {
				flag = insertIntoDatabase(message,trainAns);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        	if(flag) {
        		area.append("You : " + train_field.getText() + "\n");
        		bot("Thankyou, for adding to my knowledge.");
                field.setText("");
                train_field.setText("");
        	}else {
        		field.setText("");
                train_field.setText("");
                bot("Sorry, Try Again.");
        	}
        }
        
    }

    // retrieves answer from database
    public String getFromDatabase(String message) {
    	String url = "jdbc:mysql://localhost:3306/chatbot_info";
        String username = "root";
        String password = "pb08em6199";

        try (Connection connection = DriverManager.getConnection(url, username, password);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement
                        .executeQuery("SELECT answer FROM quesans WHERE question = '" + message + "'")) {

            if (resultSet.next()) {
                return resultSet.getString("answer");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "";
    }
    
    // insert into database
    public boolean insertIntoDatabase(String message, String trainAns) throws Exception {
        String url = "jdbc:mysql://localhost:3306/chatbot_info";
        String username = "root";
        String password = "pb08em6199";
        boolean flag = false;

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO quesans (question, answer) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, message);
            statement.setString(2, trainAns);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                flag = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return flag;
    }

    // performs google search for the given message if not in the database
    public void search(String message) {
        try {
            URL url = new URL("https://google.com/search?q=" + message.replace(" ", "+"));
            java.awt.Desktop.getDesktop().browse(url.toURI());
            bot("Sorry, I don't know about this. But here are some google searches for you.");
        } catch (Exception e) {
            bot("Please connect to the internet.");
        }
    }

    // for displaying the bot response in the GUI
    public static void bot(String message) {
        area.append("Bot : " + message + "\n");
    }

    // checks if the given message is mathematical expression
    public static boolean isMath(String message) {
        return message.matches("[0-9+\\-*/\\s]+");
    }

    // evaluates the expression and returns the result
    public static double evaluateMath(String expression) {
        String[] tokens = expression.split("\\s");
        Stack<Double> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (token.matches("[+-/*]")) {
                operators.push(token);
            } else {
                double number = Double.parseDouble(token);
                numbers.push(number);
            }

            while (numbers.size() >= 2 && operators.size() >= 1) {
                double operand2 = numbers.pop();
                double operand1 = numbers.pop();
                String operator = operators.pop();
                double result = perform(operand1, operator, operand2);
                numbers.push(result);
            }
        }

        return numbers.pop();
    }

    // perform the operation according to the operand
    public static double perform(double operand1, String operator, double operand2) {
        switch (operator) {
            case "+":
                return operand1 + operand2;
            case "-":
                return operand1 - operand2;
            case "*":
                return operand1 * operand2;
            case "/":
                return operand1 / operand2;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

    // main method
    public static void main(String[] args) {
        chatbot cb = new chatbot("ChatBot - Let's Chat");
        cb.setSize(810, 615);
        cb.setLocation(50, 50);
    }
}
