package com.university.view;

import com.university.controller.UserController;
import com.university.model.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Dashboard for student users.
 * Provides access to student-specific functionalities.
 * Includes responsive design to adapt to different screen sizes and devices.
 * Enhanced with visual summaries and better UI elements.
 */
public class StudentDashboard extends JFrame {
   
    private UserController userController;  
    private User currentUser;  // Declare currentUser for storing the logged-in user details
    private JTextField filePathField;
    private JTextArea descriptionField;
    private JComboBox<String> projectComboBox;
    private JButton submitButton;

    private JPanel contentPanel;
    private JPanel sidebarPanel;
private static final int MIN_WIDTH = 1000;  // Set the minimum width of the window
private static final int MIN_HEIGHT = 800; // Set the minimum height of the window

    // Sidebar width variables
    private static final int SIDEBAR_EXPANDED_WIDTH = 220;
    private static final int SIDEBAR_COLLAPSED_WIDTH = 70;
    private boolean sidebarCollapsed = false;
    
    // Color scheme
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Blue
    private static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Lighter blue
    private static final Color ACCENT_COLOR = new Color(46, 204, 113); // Green
    private static final Color WARNING_COLOR = new Color(243, 156, 18); // Orange
    private static final Color DANGER_COLOR = new Color(231, 76, 60); // Red
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color CARD_BACKGROUND = new Color(255, 255, 255); // White
    private static final Color TEXT_COLOR = new Color(44, 62, 80); // Dark blue/gray
    private static final Color LIGHT_TEXT = new Color(236, 240, 241); // Off-white
    
    // Round corner radius
    private static final int CORNER_RADIUS = 10;
    
 public StudentDashboard(User user) {
    this.currentUser = user;
    this.userController = UserController.getInstance();  // Initialize userController

    // Initialize contentPanel and layout
    contentPanel = new JPanel(new CardLayout());  // Initialize contentPanel here
    sidebarPanel = createSidebarPanel();  // Create sidebar panel
    JPanel headerPanel = createHeaderPanel();  // Create header panel

    // UI components setup
    filePathField = new JTextField(20);
    descriptionField = new JTextArea(5, 20);
    projectComboBox = new JComboBox<>(new String[]{"Project A", "Project B", "Project C"});
    submitButton = new JButton("Submit");

    // Action listener for the submit button
    submitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String filePath = filePathField.getText();
            String description = descriptionField.getText();

            // Example: Assume studentId is 1 and projectId is mapped based on selection
            int studentId = 1;  // This would ideally come from the logged-in user's info
            int projectId = projectComboBox.getSelectedIndex() + 1;

            // Call the controller to upload the work
            File file = new File(filePath); // Create a file object from the file path
            boolean isUploaded = userController.uploadWork(studentId, projectId, filePath, "PDF", description, file);

            if (isUploaded) {
                JOptionPane.showMessageDialog(null, "Work uploaded successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to upload work.");
            }
        }
    });

    // Add the panels to the frame
    setLayout(new BorderLayout());  // Set the layout of the main frame
    add(headerPanel, BorderLayout.NORTH);  // Add header
    add(sidebarPanel, BorderLayout.WEST);  // Add sidebar
    add(contentPanel, BorderLayout.CENTER);  // Add content area

    // Show welcome panel initially
    CardLayout cl = (CardLayout) (contentPanel.getLayout());
    cl.show(contentPanel, "welcome");

    // Initialize UI components for the content
    setupContentPanels();  // Setup your content panels (e.g., "welcome", "uploadWork")

    // Adjust UI for the screen size
    adjustUIForScreenSize();
    
    // Call adjustUIForScreenSize() when the window size changes
    addComponentListener(new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent e) {
            adjustUIForScreenSize();
        }
    });
}

private void setupContentPanels() {
    contentPanel.add(createWelcomePanel(), "welcome");
    contentPanel.add(createUploadWorkPanel(), "uploadWork");
    contentPanel.add(createViewStatusPanel(), "viewStatus");
    contentPanel.add(createViewFeedbackPanel(), "viewFeedback");
    contentPanel.add(createSubmitVersionsPanel(), "submitVersions");
}

    
    /**
     * Adjusts UI components based on the current window size
     */
  private void adjustUIForScreenSize() {
    int width = getWidth();
    int height = getHeight();
    
    // Prevent the window from getting too small
    if (width < MIN_WIDTH) {
        setSize(MIN_WIDTH, height);  // Set to minimum width if the window is too small
    }

    if (height < MIN_HEIGHT) {
        setSize(width, MIN_HEIGHT);  // Set to minimum height if the window is too small
    }

    // Auto-collapse sidebar on small screens
    if (width < 600 && !sidebarCollapsed) {
        collapseSidebar();
    } else if (width >= 600 && sidebarCollapsed) {
        expandSidebar();
    }

    // Update fonts and component sizes
    updateFontSizes();

    revalidate();
    repaint();
}
    
    /**
     * Updates font sizes based on screen size
     */
    private void updateFontSizes() {
        int width = getWidth();
        
        Font headerFont;
        Font regularFont;
        Font smallFont;
        
        if (width < 600) {
            headerFont = new Font("SansSerif", Font.BOLD, 16);
            regularFont = new Font("SansSerif", Font.PLAIN, 13);
            smallFont = new Font("SansSerif", Font.PLAIN, 11);
        } else if (width < 900) {
            headerFont = new Font("SansSerif", Font.BOLD, 18);
            regularFont = new Font("SansSerif", Font.PLAIN, 14);
            smallFont = new Font("SansSerif", Font.PLAIN, 12);
        } else {
            headerFont = new Font("SansSerif", Font.BOLD, 20);
            regularFont = new Font("SansSerif", Font.PLAIN, 15);
            smallFont = new Font("SansSerif", Font.PLAIN, 13);
        }
        
        // Update component fonts throughout the UI
        updateComponentFonts(this, headerFont, regularFont, smallFont);
    }
    
    /**
     * Recursively updates fonts for component tree
     */
    private void updateComponentFonts(Component comp, Font headerFont, Font regularFont, Font smallFont) {
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            if (label.getFont().isBold()) {
                label.setFont(headerFont);
            } else {
                label.setFont(regularFont);
            }
        } else if (comp instanceof JButton) {
            comp.setFont(regularFont);
        } else if (comp instanceof JTextField || comp instanceof JTextArea) {
            comp.setFont(regularFont);
        } else if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            table.setFont(regularFont);
            table.getTableHeader().setFont(smallFont);
            table.setRowHeight(28); // Increase row height for better touch targets
        } else if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                updateComponentFonts(child, headerFont, regularFont, smallFont);
            }
        }
    }
    
    /**
     * Collapses the sidebar to icon-only mode
     */
    private void collapseSidebar() {
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_COLLAPSED_WIDTH, getHeight()));
        sidebarCollapsed = true;
        
        // Hide text on buttons, show only icons
        for (Component comp : sidebarPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setText("");
                button.setToolTipText(button.getActionCommand()); // Show text as tooltip
            }
        }
        
        revalidate();
    }
    
    /**
     * Expands the sidebar to show text and icons
     */
    private void expandSidebar() {
        sidebarPanel.setPreferredSize(new Dimension(SIDEBAR_EXPANDED_WIDTH, getHeight()));
        sidebarCollapsed = false;
        
        // Restore button text
        Component[] components = sidebarPanel.getComponents();
        String[] buttonTexts = {"Dashboard", "Upload Work", "View Status", "View Feedback", "Submit Versions"};
        
        for (int i = 0; i < components.length && i < buttonTexts.length; i++) {
            if (components[i] instanceof JButton) {
                JButton button = (JButton) components[i];
                button.setText(buttonTexts[i]);
                button.setToolTipText(null); // Remove tooltip
            }
        }
        
        revalidate();
    }
    
    /**
     * Creates a custom rounded border
     */
    private Border createRoundedBorder(Color color, int thickness, int radius) {
        return BorderFactory.createCompoundBorder(
            new Border() {
                @Override
                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(color);
                    g2d.setStroke(new BasicStroke(thickness));
                    g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
                    g2d.dispose();
                }

                @Override
                public Insets getBorderInsets(Component c) {
                    return new Insets(thickness, thickness, thickness, thickness);
                }

                @Override
                public boolean isBorderOpaque() {
                    return false;
                }
            },
            new EmptyBorder(4, 8, 4, 8)
        );
    }
    
    /**
     * Creates the header panel with user info and logout button.
     */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(52, 73, 94)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        // User info label with icon
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userInfoPanel.setOpaque(false);
        
        // Add user icon
        ImageIcon userIcon = createUserAvatar(currentUser.getFullName());
        JLabel iconLabel = new JLabel(userIcon);
        userInfoPanel.add(iconLabel);
        
        // User info text
        JLabel userInfoLabel = new JLabel("Logged in as: " + currentUser.getFullName() + " (Student)");
        userInfoLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        userInfoLabel.setForeground(LIGHT_TEXT);
        userInfoPanel.add(userInfoLabel);
        
        panel.add(userInfoPanel, BorderLayout.WEST);
        
        // Right side panel with buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);
        
        // Notifications button with icon
        JButton notificationsButton = createHeaderButton("Notifications", createIcon("notification", 16));
        rightPanel.add(notificationsButton);
        
        // Logout button with icon
        JButton logoutButton = createHeaderButton("Logout", createIcon("logout", 16));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        rightPanel.add(logoutButton);
        
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Creates a custom header button with icon and text
     */
    private JButton createHeaderButton(String text, Icon icon) {
        JButton button = new JButton(text);
        button.setIcon(icon);
        button.setForeground(LIGHT_TEXT);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setIconTextGap(8);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 255, 255));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(LIGHT_TEXT);
            }
        });
        
        return button;
    }
    
    /**
     * Creates a user avatar with initials
     */
    private ImageIcon createUserAvatar(String name) {
        int size = 32;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background circle
        g2d.setColor(ACCENT_COLOR);
        g2d.fillOval(0, 0, size, size);
        
        // Get initials from name
        String initials = "";
        if (name != null && !name.isEmpty()) {
            String[] parts = name.split(" ");
            if (parts.length > 0 && parts[0].length() > 0) {
                initials += parts[0].charAt(0);
            }
            if (parts.length > 1 && parts[1].length() > 0) {
                initials += parts[1].charAt(0);
            }
        }
        
        // Draw initials
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (size - metrics.stringWidth(initials)) / 2;
        int y = ((size - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(initials, x, y);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Creates a simple icon for buttons
     */
    private ImageIcon createIcon(String type, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.WHITE);
        
        switch (type) {
            case "dashboard":
                g2d.fillRect(3, 3, size-6, size-6);
                break;
            case "upload":
                // Arrow pointing up
                int[] xPoints = {size/2, size/4, 3*size/4};
                int[] yPoints = {size/4, 3*size/4, 3*size/4};
                g2d.fillPolygon(xPoints, yPoints, 3);
                g2d.fillRect(size/3, size/2, size/3, size/3);
                break;
            case "status":
                g2d.drawOval(2, 2, size-4, size-4);
                g2d.drawLine(size/2, size/2, size/2+size/4, size/2);
                break;
            case "feedback":
                g2d.drawRoundRect(2, 2, size-4, size-4, 3, 3);
                g2d.drawLine(5, 7, size-5, 7);
                g2d.drawLine(5, 11, size-5, 11);
                g2d.drawLine(5, 15, size-8, 15);
                break;
            case "versions":
                g2d.drawRect(3, 3, size-6, size-6);
                g2d.drawLine(3, 7, size-3, 7);
                break;
            case "notification":
                g2d.drawArc(3, 3, size-6, size-6, 0, 300);
                g2d.fillOval(size-6, 3, 4, 4);
                break;
            case "logout":
                g2d.drawLine(5, size/2, size-5, size/2);
                g2d.drawLine(size-8, size/2-3, size-5, size/2);
                g2d.drawLine(size-8, size/2+3, size-5, size/2);
                break;
        }
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Creates the sidebar panel with navigation buttons.
     */
    private JPanel createSidebarPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1, 0, 15));
        panel.setBackground(new Color(52, 73, 94)); // Dark blue-gray
        panel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panel.setPreferredSize(new Dimension(SIDEBAR_EXPANDED_WIDTH, getHeight()));
        
        // Create navigation buttons with icons
        JButton dashboardButton = createNavButton("Dashboard", "welcome", createIcon("dashboard", 20));
        JButton uploadWorkButton = createNavButton("Upload Work", "uploadWork", createIcon("upload", 20));
        JButton viewStatusButton = createNavButton("View Status", "viewStatus", createIcon("status", 20));
        JButton viewFeedbackButton = createNavButton("View Feedback", "viewFeedback", createIcon("feedback", 20));
        JButton submitVersionsButton = createNavButton("Submit Versions", "submitVersions", createIcon("versions", 20));
        
        // Add buttons to panel
        panel.add(dashboardButton);
        panel.add(uploadWorkButton);
        panel.add(viewStatusButton);
        panel.add(viewFeedbackButton);
        panel.add(submitVersionsButton);
        
        return panel;
    }
    
    /**
     * Creates a stylized navigation button with the given label, action, and icon.
     */
    private JButton createNavButton(String label, String panelName, Icon icon) {
        JButton button = new JButton(label);
        button.setIcon(icon);
        button.setActionCommand(label);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
        
        // Style the button
        button.setForeground(LIGHT_TEXT);
        button.setBackground(new Color(52, 73, 94)); // Match sidebar color
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(44, 62, 80)); // Darker on hover
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94)); // Back to normal
            }
        });
        
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) (contentPanel.getLayout());
                cl.show(contentPanel, panelName);
            }
        });
        
        return button;
    }
    
    /**
     * Creates the welcome panel with user information and visual dashboard summaries.
     */
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Welcome header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        welcomeLabel.setForeground(TEXT_COLOR);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        
        // Add current date/time
       /*  JLabel dateTimeLabel = new JLabel(new java.util.Date().toString());
        dateTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dateTimeLabel.setForeground(new Color(127, 140, 141)); // Subtle gray
        headerPanel.add(dateTimeLabel, BorderLayout.EAST);*/
        
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content with cards
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        contentPanel.setOpaque(false);
        
        // Left column: User info card
        JPanel userInfoCard = createDashboardCard("Your Profile", null);
        
        JPanel userInfoContent = new JPanel();
        userInfoContent.setLayout(new BoxLayout(userInfoContent, BoxLayout.Y_AXIS));
        userInfoContent.setOpaque(false);
        userInfoContent.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Profile picture (larger avatar)
        JLabel avatarLabel = new JLabel(createUserAvatar(currentUser.getFullName(), 80));
        avatarLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userInfoContent.add(avatarLabel);
        userInfoContent.add(Box.createVerticalStrut(15));
        
        // User details with icons
        userInfoContent.add(createInfoRow("User: ", currentUser.getUsername()));
        userInfoContent.add(Box.createVerticalStrut(10));
        userInfoContent.add(createInfoRow("Email: ", currentUser.getEmail()));
        userInfoContent.add(Box.createVerticalStrut(10));
        userInfoContent.add(createInfoRow("Role: ", "Student"));
        
        userInfoCard.add(userInfoContent, BorderLayout.CENTER);
        
        // Right column: Dashboard summary cards
        JPanel rightColumn = new JPanel();
        rightColumn.setLayout(new BoxLayout(rightColumn, BoxLayout.Y_AXIS));
        rightColumn.setOpaque(false);
        
        // Activity summary
        JPanel activityCard = createDashboardCard("Activity Summary", SECONDARY_COLOR);
        
        // Create progress charts panel
        JPanel chartPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        chartPanel.setOpaque(false);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add progress summaries
        chartPanel.add(createProgressSummary("Projects", 2, 5, ACCENT_COLOR));
        chartPanel.add(createProgressSummary("Submissions", 4, 4, PRIMARY_COLOR));
        chartPanel.add(createProgressSummary("Feedback", 3, 4, WARNING_COLOR));
        chartPanel.add(createProgressSummary("Tasks", 6, 10, DANGER_COLOR));
        
        activityCard.add(chartPanel, BorderLayout.CENTER);
        
        // Quick actions card
       // JPanel quickActionsCard = createDashboardCard("Quick Actions", null);
        JPanel buttonsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        buttonsPanel.setOpaque(false);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
       // buttonsPanel.add(createQuickActionButton("Upload New Work", "uploadWork"));
        //buttonsPanel.add(createQuickActionButton("Check Status", "viewStatus"));
        //buttonsPanel.add(createQuickActionButton("View Feedback", "viewFeedback"));
        //buttonsPanel.add(createQuickActionButton("Submit Version", "submitVersions"));
        
        //quickActionsCard.add(buttonsPanel, BorderLayout.CENTER);
        
        // Add the activity card with fixed height
        activityCard.setPreferredSize(new Dimension(100, 200));
        rightColumn.add(activityCard);
        rightColumn.add(Box.createVerticalStrut(20)); // Space between cards
        
        // Add quick actions card
       // quickActionsCard.setPreferredSize(new Dimension(100, 150));
        //rightColumn.add(quickActionsCard);
        
        // Add both columns to the content panel
        contentPanel.add(userInfoCard);
        contentPanel.add(rightColumn);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates an info row with a label and value
     */
    private JPanel createInfoRow(String label, String value) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("SansSerif", Font.BOLD, 14));
        labelComponent.setForeground(TEXT_COLOR);
        panel.add(labelComponent, BorderLayout.WEST);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("SansSerif", Font.PLAIN, 14));
        valueComponent.setForeground(TEXT_COLOR);
        panel.add(valueComponent, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates a card panel for dashboard elements with title and optional color header
     */
    private JPanel createDashboardCard(String title, Color headerColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            createRoundedBorder(new Color(220, 220, 220), 1, CORNER_RADIUS),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Add card title with header background if specified
        JPanel titlePanel = new JPanel(new BorderLayout());
        if (headerColor != null) {
            titlePanel.setBackground(headerColor);
            titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            titleLabel.setForeground(Color.WHITE);
            titlePanel.add(titleLabel, BorderLayout.WEST);
        } else {
            titlePanel.setBackground(new Color(245, 245, 245));
            titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JLabel titleLabel = new JLabel(title);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            titleLabel.setForeground(TEXT_COLOR);
            titlePanel.add(titleLabel, BorderLayout.WEST);
        }
        
        card.add(titlePanel, BorderLayout.NORTH);
        
        return card;
    }
    
    /**
     * Creates a progress summary with circular visualization
     */
    private JPanel createProgressSummary(String title, int current, int total, Color color) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setOpaque(false);
        
        // Create the circular progress indicator
        JPanel progressPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                
                // Draw background circle
                g2d.setColor(new Color(230, 230, 230));
                g2d.fillOval(x, y, size, size);
                
                // Draw progress arc
                float percentage = (float) current / total;
                g2d.setColor(color);
                g2d.fillArc(x, y, size, size, 90, -(int)(360 * percentage));
                
                // Draw inner circle to create donut
                int innerSize = size - 20;
                g2d.setColor(CARD_BACKGROUND);
                g2d.fillOval(x + 10, y + 10, innerSize, innerSize);
                
                // Draw text in the center
                String text = current + "/" + total;
                g2d.setColor(TEXT_COLOR);
                g2d.setFont(new Font("SansSerif", Font.BOLD, 14));
                
                FontMetrics metrics = g2d.getFontMetrics();
                int textX = x + (size - metrics.stringWidth(text)) / 2;
                int textY = y + (size - metrics.getHeight()) / 2 + metrics.getAscent();
                
                g2d.drawString(text, textX, textY);
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(80, 80);
            }
        };
        
        // Add title label
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_COLOR);
        
        panel.add(progressPanel, BorderLayout.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates a quick action button with hover effects
     */
    /* private JButton createQuickActionButton(String text, String panelName) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setForeground(TEXT_COLOR);
        button.setBackground(new Color(240, 240, 240));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            createRoundedBorder(new Color(200, 200, 200), 1, 5),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(230, 230, 230));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
            }
        });
        
        button.addActionListener(e -> {
            CardLayout cl = (CardLayout) (contentPanel.getLayout());
            cl.show(contentPanel, panelName);
        });
        
        return button;
    }*/
    
    /**
     * Creates a user avatar with initials and specified size
     */
    private ImageIcon createUserAvatar(String name, int size) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw background circle
        g2d.setColor(ACCENT_COLOR);
        g2d.fillOval(0, 0, size, size);
        
        // Get initials from name
        String initials = "";
        if (name != null && !name.isEmpty()) {
            String[] parts = name.split(" ");
            if (parts.length > 0 && parts[0].length() > 0) {
                initials += parts[0].charAt(0);
            }
            if (parts.length > 1 && parts[1].length() > 0) {
                initials += parts[1].charAt(0);
            }
        }
        
        // Draw initials
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("SansSerif", Font.BOLD, size / 3));
        FontMetrics metrics = g2d.getFontMetrics();
        int x = (size - metrics.stringWidth(initials)) / 2;
        int y = ((size - metrics.getHeight()) / 2) + metrics.getAscent();
        g2d.drawString(initials, x, y);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    /**
     * Creates the upload work panel.
     */
    private JPanel createUploadWorkPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Upload Work");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Project selection
        JLabel projectLabel = new JLabel("Project:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(projectLabel, gbc);
        
        JComboBox<String> projectComboBox = new JComboBox<>(new String[]{"Final Year Project", "Research Paper", "Group Assignment"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(projectComboBox, gbc);
        
        // File selection
        JLabel fileLabel = new JLabel("File:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(fileLabel, gbc);
        
        JPanel filePanel = new JPanel(new BorderLayout());
        JTextField filePathField = new JTextField(20);
        filePathField.setEditable(false);
        filePanel.add(filePathField, BorderLayout.CENTER);
        
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(StudentDashboard.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    filePathField.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });
        filePanel.add(browseButton, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(filePanel, gbc);
        
        // Description
        JLabel descriptionLabel = new JLabel("Description:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(descriptionLabel, gbc);
        
        JTextArea descriptionArea = new JTextArea(5, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);
        
        // Submit button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filePathField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(StudentDashboard.this, 
                        "Please select a file to upload", 
                        "Upload Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                JOptionPane.showMessageDialog(StudentDashboard.this, 
                    "Work submitted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                projectComboBox.setSelectedIndex(0);
                filePathField.setText("");
                descriptionArea.setText("");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(submitButton, gbc);
        
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Creates the view status panel with enhanced search functionality.
     */
    private JPanel createViewStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("View Submission Status");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        // Create a header panel with title
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel searchLabel = new JLabel("Search:");
        searchPanel.add(searchLabel);
        
        JTextField searchField = new JTextField(15);
        searchPanel.add(searchField);
        
        JLabel filterLabel = new JLabel("Status:");
        searchPanel.add(filterLabel);
        
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All", "Under Review", "Approved", "Rejected"});
        searchPanel.add(statusFilter);
        
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        
        headerPanel.add(searchPanel, BorderLayout.CENTER);
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Create table for submission data
        String[] columnNames = {"Project", "Submit Date", "Version", "Status"};
        
        // Sample data
        Object[][] data = {
            {"Final Year Project", "2023-05-10", "1.0", "Under Review"},
            {"Research Paper", "2023-04-15", "2.0", "Approved"},
            {"Group Assignment", "2023-03-20", "1.0", "Rejected"}
        };
        
        // Create table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setPreferredScrollableViewportSize(new Dimension(500, 200));
        table.setFillsViewportHeight(true);
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Create details panel
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createTitledBorder("Submission Details"));
        
        JTextArea detailsArea = new JTextArea(5, 30);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        detailsArea.setText("Select a submission to view details");
        
        JScrollPane detailsScrollPane = new JScrollPane(detailsArea);
        detailsPanel.add(detailsScrollPane, BorderLayout.CENTER);
        
        panel.add(detailsPanel, BorderLayout.SOUTH);
        
        // Add selection listener to update details
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                String project = (String) table.getValueAt(row, 0);
                String date = (String) table.getValueAt(row, 1);
                String version = (String) table.getValueAt(row, 2);
                String status = (String) table.getValueAt(row, 3);
                
                // Update details text area with selected submission info
                detailsArea.setText(
                    "Project: " + project + "\n" +
                    "Submission Date: " + date + "\n" +
                    "Version: " + version + "\n" +
                    "Status: " + status + "\n\n" +
                    "Supervisor Comments: " + getCommentsForProject(project, status)
                );
            }
        });
        
        // Implement search functionality
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchTerm = searchField.getText().toLowerCase();
                String selectedStatus = statusFilter.getSelectedItem().toString();
                
                // Clear the current table data
                model.setRowCount(0);
                
                // Filter data based on search criteria
                for (Object[] row : data) {
                    String project = ((String) row[0]).toLowerCase();
                    String status = (String) row[3];
                    
                    boolean matchesSearch = searchTerm.isEmpty() || project.contains(searchTerm);
                    boolean matchesStatus = selectedStatus.equals("All") || status.equals(selectedStatus);
                    
                    if (matchesSearch && matchesStatus) {
                        model.addRow(row);
                    }
                }
                
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(StudentDashboard.this,
                        "No submissions found matching your search criteria.",
                        "Search Results",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Clear details area when no results found
                    detailsArea.setText("No submissions match your search criteria");
                }
            }
        });
        
        // Add button panel with action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton viewButton = new JButton("View Details");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    String project = (String) table.getValueAt(selectedRow, 0);
                    String date = (String) table.getValueAt(selectedRow, 1);
                    String version = (String) table.getValueAt(selectedRow, 2);
                    String status = (String) table.getValueAt(selectedRow, 3);
                    
                    JOptionPane.showMessageDialog(StudentDashboard.this,
                        "Submission Details\n\n" +
                        "Project: " + project + "\n" +
                        "Date: " + date + "\n" +
                        "Version: " + version + "\n" +
                        "Status: " + status + "\n\n" +
                        "Supervisor Comments: " + getCommentsForProject(project, status),
                        "View Submission",
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(StudentDashboard.this,
                        "Please select a submission to view details",
                        "No Selection",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Reset search field and filter
                searchField.setText("");
                statusFilter.setSelectedItem("All");
                
                // Reload all data
                model.setRowCount(0);
                for (Object[] row : data) {
                    model.addRow(row);
                }
                
                // Clear details
                detailsArea.setText("Select a submission to view details");
                
                JOptionPane.showMessageDialog(StudentDashboard.this, 
                    "Submission list refreshed", 
                    "Refresh", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        buttonPanel.add(viewButton);
        buttonPanel.add(refreshButton);
        detailsPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Helper method to get comments for a project based on its status
     */
    private String getCommentsForProject(String project, String status) {
        // In a real application, this would query the database
        // For now, we return hardcoded comments based on project and status
        if (project.equals("Final Year Project") && status.equals("Under Review")) {
            return "Currently being reviewed. Expected feedback by next week.";
        } else if (project.equals("Research Paper") && status.equals("Approved")) {
            return "Excellent work! The methodology section is particularly strong.";
        } else if (project.equals("Group Assignment") && status.equals("Rejected")) {
            return "Needs significant improvement in the analysis section. Please revise and resubmit.";
        }
        return "No comments available.";
    }
    
    /**
     * Creates the view feedback panel.
     */
    private JPanel createViewFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("View Feedback");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Project selection
        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.add(new JLabel("Select Project:"));
        
        JComboBox<String> projectComboBox = new JComboBox<>(new String[]{"Final Year Project", "Research Paper", "Group Assignment"});
        projectComboBox.setPreferredSize(new Dimension(200, 25));
        selectionPanel.add(projectComboBox);
        
        panel.add(selectionPanel, BorderLayout.CENTER);
        
        // Feedback display panel
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        JTextArea feedbackArea = new JTextArea(10, 30);
        feedbackArea.setEditable(false);
        feedbackArea.setText("No feedback available for this project yet.");
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(feedbackArea);
        feedbackPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Grade display
        JPanel gradePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        gradePanel.add(new JLabel("Grade:"));
        
        JTextField gradeField = new JTextField("N/A");
        gradeField.setEditable(false);
        gradeField.setPreferredSize(new Dimension(100, 25));
        gradePanel.add(gradeField);
        
        feedbackPanel.add(gradePanel, BorderLayout.SOUTH);
        
        panel.add(feedbackPanel, BorderLayout.SOUTH);
        
        // Add action listener to the project combo box
        projectComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProject = (String) projectComboBox.getSelectedItem();
                if ("Research Paper".equals(selectedProject)) {
                    feedbackArea.setText("Good work overall. The literature review is thorough, but the methodology section needs more detail. Please revise and resubmit.");
                    gradeField.setText("75/100");
                } else if ("Group Assignment".equals(selectedProject)) {
                    feedbackArea.setText("The assignment does not meet the requirements. Please review the assignment guidelines and resubmit.");
                    gradeField.setText("45/100");
                } else {
                    feedbackArea.setText("No feedback available for this project yet.");
                    gradeField.setText("N/A");
                }
            }
        });
        
        return panel;
    }
    
    /**
     * Creates the submit versions panel.
     */
    private JPanel createSubmitVersionsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Submit New Version");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Project selection
        JLabel projectLabel = new JLabel("Project:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(projectLabel, gbc);
        
        JComboBox<String> projectComboBox = new JComboBox<>(new String[]{"Final Year Project", "Research Paper", "Group Assignment"});
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(projectComboBox, gbc);
        
        // Version number
        JLabel versionLabel = new JLabel("Version:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(versionLabel, gbc);
        
        JTextField versionField = new JTextField("2.0");
        versionField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(versionField, gbc);
        
        // File selection
        JLabel fileLabel = new JLabel("File:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(fileLabel, gbc);
        
        JPanel filePanel = new JPanel(new BorderLayout());
        JTextField filePathField = new JTextField(20);
        filePathField.setEditable(false);
        filePanel.add(filePathField, BorderLayout.CENTER);
        
        JButton browseButton = new JButton("Browse");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(StudentDashboard.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    filePathField.setText(fileChooser.getSelectedFile().getPath());
                }
            }
        });
        filePanel.add(browseButton, BorderLayout.EAST);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(filePanel, gbc);
        
        // Changes made
        JLabel changesLabel = new JLabel("Changes Made:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(changesLabel, gbc);
        
        JTextArea changesArea = new JTextArea(5, 20);
        changesArea.setLineWrap(true);
        changesArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(changesArea);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollPane, gbc);
        
        // Submit button
        JButton submitButton = new JButton("Submit New Version");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (filePathField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(StudentDashboard.this, 
                        "Please select a file to upload", 
                        "Upload Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (changesArea.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(StudentDashboard.this, 
                        "Please describe the changes made in this version", 
                        "Upload Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                JOptionPane.showMessageDialog(StudentDashboard.this, 
                    "New version submitted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                projectComboBox.setSelectedIndex(0);
                filePathField.setText("");
                changesArea.setText("");
            }
        });
        
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(submitButton, gbc);
        
        // Add the form panel to the main panel
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Add action listener to the project combo box to update version number
        projectComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedProject = (String) projectComboBox.getSelectedItem();
                if ("Research Paper".equals(selectedProject)) {
                    versionField.setText("3.0");
                } else if ("Group Assignment".equals(selectedProject)) {
                    versionField.setText("2.0");
                } else {
                    versionField.setText("2.0");
                }
            }
        });
        
        return panel;
    }
    
    /**
     * Handles the logout process.
     */
    private void logout() {
        // Logout the user
        UserController.getInstance().logout();
        
        // Close the dashboard
        this.dispose();
        
        // Show the login form
        LoginForm loginForm = new LoginForm();
        loginForm.setVisible(true);
    }
}
