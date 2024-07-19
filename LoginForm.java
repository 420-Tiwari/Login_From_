import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;


public class LoginForm extends JFrame {
    final private Font mainFont = new Font("Segoe print", Font.BOLD, 18);
    JTextField tfEmail;
    JPasswordField pfPassword;
    
    public void initialize(){

        JLabel lbLoginForm = new JLabel("Login Form ",SwingConstants.CENTER);
        lbLoginForm.setFont(mainFont);

        JLabel lbEmail = new JLabel("Email");
        lbEmail.setFont(mainFont);

        tfEmail = new JTextField();
        lbEmail.setFont(mainFont);

        JLabel lbPassword = new JLabel("Email");
        lbEmail.setFont(mainFont);

        pfPassword = new JPasswordField();
        pfPassword.setFont(mainFont);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.add(lbLoginForm);
        formPanel.add(lbEmail);
        formPanel.add(tfEmail);
        formPanel.add(lbPassword);
        formPanel.add(pfPassword);

        JButton btnLogin = new JButton("Login");
        btnLogin.setFont(mainFont);
        btnLogin.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                String email = tfEmail.getText();
                String password = String.valueOf(pfPassword.getPassword()); 

                User user = getAuthentticatedUser(email, password);

                if(user != null){
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.initilize(user);
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this,
                     "Emai or password enter correctly", 
                     "try again ", JOptionPane.ERROR_MESSAGE);
                }
            }
            
        });

        JButton btnCancle = new JButton("Cancle");
        btnCancle.setFont(mainFont);
        btnCancle.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                dispose();
            }
            
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1,2,10,0));
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnCancle);

        add(formPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        setTitle("Login form");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(400,500);
        setMinimumSize(new Dimension(300,500));
        setLocationRelativeTo(null);
        setVisible(true);
    }

     private User getAuthentticatedUser(String email, String password){
        User user = null; 

            final String DB_URL = "jdbc:mysql://localhost/mydb?serverTimezone=UTC";
            final String USERNAME = "root";
            final String PASSWORD = "";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.phone = resultSet.getString("phone");
                user.address = resultSet.getString("address");
                user.password = resultSet.getString("password");
            }

            preparedStatement.close();
            conn.close();
        }catch(Exception e){
            e.printStackTrace(); // Print the stack trace of the exception
            System.out.println("Database Connection Failed: " + e.getMessage()); // Print a custom message
        }
        

        return user;
    }
    public static void main(String ags[]){
        LoginForm loginForm = new LoginForm();
        loginForm.initialize();
    }

}