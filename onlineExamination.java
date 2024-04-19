import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

class User
{
    private String username;
    private String password;
    private String fullName;

    public User(String username, String password, String fullName) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }
}

class Question {
    private String questionText;
    private List<String> options; 
    private int correctOptionIndex;

    public Question(String questionText, List<String> options, int correctOptionIndex) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

public class onlineExamination {
    private static User currentUser;
    private static List<Question> questions;
    private static List<Integer> selectedAnswers;
    private static Timer timer;
    private static int remainingTimeInSeconds = 1800; 

    public static void main(String[] args)
    {
        initializeQuestions();
        login();
    }

    private static void initializeQuestions() {
        questions = new ArrayList<>();
        questions.add(new Question("In what galaxy is our solar system located?", List.of("Andromeda", "Milky Way", "Antennae.", "Circinus"),2));
        questions.add(new Question("What type of gas is absorbed by plants?", List.of("Nitrogen", "Oxygen", "Carbon dioxide", "Hitrogen"),3));
        questions.add(new Question("When is India aiming to land an astronaut on the moon, as revealed by ISRO Chief S Somanath?", List.of("2040", "2035", "2049", "2050"),1));
        questions.add(new Question("What is the gravitational pull of the Earth?", List.of("10 m/s2", "9.8 m/s2", "1.62 m/s2", "12.54 m/s2"),1));
        questions.add(new Question("World's 1st Wooden Satellite will be Launched by _____ and ______.?", List.of("ESA and ISRO", "NASA and JAXA","CNSA and DLR", "OSCOSMOS and ASI"),2));
        questions.add(new Question("What is considered the sillicon valley of India?", List.of("Mumbai", "Hyderabad", "Madurai", "Bengaluru"),3));
        questions.add(new Question("What is largest country in the world?", List.of("china", "Russia", "India", "America"),1));
        questions.add(new Question("The sun rises in the…?", List.of("East", "Wast", "North", "South"),1));
        questions.add(new Question("Who invented the TELESCOPE?", List.of("Issac Newton", "Albert Einstein", "Galileo Galilei", "Aristotle"),2));
        questions.add(new Question("which country won the world cup in 1983?", List.of("Australia", "England", "Pakistan", "India"),3));
    }

    private static void login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        currentUser = new User(username, password, "Mohamed Asif");

        if (currentUser != null) {
            showMainMenu(sc);
        }
        else {
            System.out.println("Login failed. Please try again.");
            login();
        }
    }

    private static void showMainMenu(Scanner scanner) {
        System.out.println("Welcome, " + currentUser.getFullName() + "!!");
        System.out.println("1. Start Exam");
        System.out.println("2. Update Profile");
        System.out.println("3. Change Password");
        System.out.println("4. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                startExam(scanner);
                break;
            case 2:
                updateProfile(scanner);
                break;
            case 3:
                changePassword(scanner);
                break;
            case 4:
                logout(scanner);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                showMainMenu(scanner);
                break;
        }
    }

    private static void startExam(Scanner scanner) {
        selectedAnswers = new ArrayList<>();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                remainingTimeInSeconds--;
                if (remainingTimeInSeconds <= 0) 
                {
                    autoSubmit();
                }
            }
        }, 2000, 2000);

        System.out.println("You have 60 minutes to complete the exam.");
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            System.out.println("Question " + (i + 1) + ": " + question.getQuestionText());
            List<String> options = question.getOptions();
            for (int j = 0; j < options.size(); j++) {
                System.out.println((j + 1) + ". " + options.get(j));
            }
            System.out.print("Select an answer (1-" + options.size() + "): ");
            int answer = scanner.nextInt();
            selectedAnswers.add(answer - 1);
        }
        autoSubmit();
    }

    private static void autoSubmit() {
        if (timer != null)
        {
            timer.cancel();
        }
        System.out.println("Time's up! Submitting your answers.");
        showResult();
    }

    private static void showResult() 
    {
        int score = 0;
        for (int i = 0; i < questions.size(); i++)
        {
            Question question = questions.get(i);
            int selectedAnswerIndex = selectedAnswers.get(i);
            if (selectedAnswerIndex == question.getCorrectOptionIndex()) 
            {
                score++;
            }
        }

        System.out.println("You scored " + score + " out of " + questions.size() + " questions."); 
        logout(new Scanner(System.in)); 
    }

    private static void updateProfile(Scanner scanner) 
    {
        System.out.print("Enter your new full name: ");
        String newFullName = scanner.nextLine();
        currentUser = new User(currentUser.getUsername(), currentUser.getPassword(), newFullName);
        System.out.println("Profile updated successfully.");
        showMainMenu(scanner);
    }

    private static void changePassword(Scanner scanner) 
    {
        System.out.print("Enter your current password: ");
        String currentPassword = scanner.nextLine();
        if (currentPassword.equals(currentUser.getPassword())) 
        {
            System.out.print("Enter your new password: ");
            String newPassword = scanner.nextLine();
            currentUser = new User(currentUser.getUsername(), newPassword, currentUser.getFullName());
            System.out.println("Password changed successfully.");
        }
        else
        {
            System.out.println("Incorrect current password. Please try again.");
        }
        showMainMenu(scanner);
    }

    private static void logout(Scanner scanner)
    {
        System.out.println("Logging out... Bye!");
        scanner.close();
        System.exit(0);
    }
}
