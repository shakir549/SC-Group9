package com.university.controller;

import com.university.dao.*;
import com.university.model.*;
import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class UserController {
      private static UserController instance;
    private final UserDAO userDAO;
    private final SubmissionDAO submissionDAO;
    private final NotificationDAO notificationDAO;
    private final ScheduleDAO scheduleDAO;
    private final ProjectVersionDAO projectVersionDAO;
    private final DeadlineDAO deadlineDAO;
    private final ProjectDAO projectDAO;
    private User currentUser;
    
    

    // Private constructor for Singleton pattern
    private UserController() {
        this.userDAO = new UserDAO();
        this.submissionDAO = new SubmissionDAO();
        this.notificationDAO = new NotificationDAO();
          this.scheduleDAO = new ScheduleDAO();
            this.projectVersionDAO = new ProjectVersionDAO();
        this.deadlineDAO = new DeadlineDAO();
         this.projectDAO = new ProjectDAO(); 



         
    }

    
    public static synchronized UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public User login(String username, String password, Role role) {
        User user = userDAO.getUserByUsernameAndRole(username, role);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return user;
        }
        return null;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public User register(String username, String password, String fullName, String email, Role role) {
        if (userDAO.getUserByUsernameAndRole(username, role) != null) {
            return null;
        }
        User newUser = new User(0, username, password, fullName, email, role);
        int generatedId = userDAO.createUser(newUser);
        if (generatedId > 0) {
            newUser.setId(generatedId);
            return newUser;
        }
        return null;
    }

    public List<User> getUsersByRole(Role role) {
        return userDAO.getUsersByRole(role);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }


public boolean uploadWork(int studentId, int projectId, String filePath, String fileType, String description, File file) {
    // Create a new Submission object using the default constructor
    Submission submission = new Submission();
    
    // Set the properties of the submission object
    submission.setStudent(new User()); // Initialize the student object
    submission.getStudent().setId(studentId);  // Set student ID
    
    submission.setProject(new Project()); // Initialize the project object
    submission.getProject().setId(projectId); // Set project ID
    
    submission.setFilePath(filePath);
    submission.setDescription(description);
    submission.setVersion(1);  // Set the default version
    submission.setStatus(Submission.Status.PENDING);  // Set default status
    submission.setSubmissionDate(LocalDateTime.now());  // Set the current date and time
    
    // Call the SubmissionDAO to insert the submission into the database
    return submissionDAO.insertSubmission(submission, file);
}


 public void sendNotification(int recipientId, String subject, String message, String importance) {
    // Create a new Notification object
    Notification notification = new Notification(
            currentUser.getId(), // senderId from the current user (logged-in user)
            recipientId, // recipientId passed as the method parameter
            subject, // subject passed as the method parameter
            message, // message passed as the method parameter
            new Timestamp(System.currentTimeMillis()), // current timestamp
            importance, // importance passed as the method parameter
            "No" // Default read status
    );

    // Send the notification via NotificationDAO
    notificationDAO.sendNotification(notification);
}


public boolean submitProject(User loggedInUser, Object projectFromForm, File file) {
    if (loggedInUser == null || projectFromForm == null || file == null) {
        return false;
    }

    Submission submission = new Submission();
    submission.setProject((Project) projectFromForm); // Cast projectFromForm to Project
    submission.setStudent(loggedInUser);
    submission.setDescription("My first draft");
    submission.setVersion(1);
    submission.setStatus(Submission.Status.PENDING);
    submission.setFilePath(file.getAbsolutePath());

    boolean result = submissionDAO.insertSubmission(submission, file);
    
    if (result) {
      
        // Call the sendN   otification method, passing the supervisor's ID, subject, message, and importance
       sendNotification(submission.getProject().getSupervisor().getId(),
                 "New submission for project: " + submission.getProject().getTitle(),
                 "Submission uploaded by " + loggedInUser.getFullName(), "Normal");

    }
    
    return result;
}

public void defineDeadline(Deadline deadline) {
    boolean isSaved = deadlineDAO.addDeadline(deadline);  // Call DeadlineDAO to save the deadline
    if (isSaved) {
        System.out.println("Deadline defined successfully!");
    } else {
        System.out.println("Failed to define deadline.");
    }
}
   

    public List<Notification> getNotifications(int userId) {
        return notificationDAO.getUserNotifications(userId);
    }

  public List<Schedule> getSchedulesByProject(int projectId) {
        return scheduleDAO.getSchedulesByProject(projectId);
    }

    public List<ProjectVersion> getProjectVersionsBySubmission(int submissionId) {
        return projectVersionDAO.getVersionsBySubmission(submissionId);
    
    }

    public List<ProjectVersion> getProjectVersions(int submissionId) {
        return projectVersionDAO.getVersionsBySubmission(submissionId);
    
    }

    public int getActiveSupervisors() {
    return userDAO.getActiveSupervisors();
}

public int getActiveStudents() {
    return userDAO.getActiveStudents();
}

public int getTotalUsers() {
    return userDAO.getTotalUsers();
}


public List<Activity> getRecentActivity() {
    return new ActivityDAO().getRecentActivity();
}

public int getProjectsInProgressCount() {
    return projectDAO.getProjectsInProgressCount();
}

// In UserController.java
public List<Notification> getSentNotifications(int senderId) {
    return new NotificationDAO().getSentNotifications(senderId);
}


    
  public List<Deadline> getDeadlinesByProject(int projectId) {
        return deadlineDAO.getDeadlinesByProject(projectId);
    }


    public List<User> getAllUsersWithProjects() {
    return userDAO.getAllUsersWithProjects();
}


 



}
