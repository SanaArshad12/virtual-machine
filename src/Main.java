import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

 class CoolEnhancedVM extends JFrame {

    private Map<String, Integer> registers = new HashMap<>();  // Registers (R1, R2, R3)
    private int[] memory = new int[256];  // Memory storage

    // GUI components
    private JTextArea instructionArea;
    private JTextArea outputArea;
    private JButton executeButton;
    private JButton resetButton;
    private JLabel statusLabel;

    public CoolEnhancedVM() {
        // Initialize registers
        for (int i = 1; i <= 3; i++) {
            registers.put("R" + i, 0);
        }

        // Set up the GUI
        setTitle("Cool Enhanced Virtual Machine");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set up fonts and colors for a modern look
        Font font = new Font("Arial", Font.PLAIN, 16);
        Color bgColor = new Color(30, 30, 30);
        Color fgColor = new Color(220, 220, 220);
        Color btnColor = new Color(70, 130, 180);
        Color btnHoverColor = new Color(100, 150, 200);

        // Instruction area setup
        instructionArea = new JTextArea(12, 40);
        instructionArea.setFont(font);
        instructionArea.setBackground(bgColor);
        instructionArea.setForeground(fgColor);
        instructionArea.setCaretColor(fgColor);
        instructionArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fgColor, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        instructionArea.setLineWrap(true);
        instructionArea.setWrapStyleWord(true);

        // Output area setup
        outputArea = new JTextArea(12, 40);
        outputArea.setFont(font);
        outputArea.setBackground(bgColor);
        outputArea.setForeground(fgColor);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fgColor, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);

        // Buttons setup
        executeButton = new JButton("Execute");
        resetButton = new JButton("Reset");
        executeButton.setFont(font);
        resetButton.setFont(font);
        executeButton.setBackground(btnColor);
        executeButton.setForeground(fgColor);
        resetButton.setBackground(btnColor);
        resetButton.setForeground(fgColor);
        executeButton.setOpaque(true);
        resetButton.setOpaque(true);
        executeButton.setBorder(BorderFactory.createEmptyBorder());
        resetButton.setBorder(BorderFactory.createEmptyBorder());
        executeButton.setFocusPainted(false);
        resetButton.setFocusPainted(false);

        // Add hover effect to buttons
        executeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                executeButton.setBackground(btnHoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                executeButton.setBackground(btnColor);
            }
        });
        resetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(btnHoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                resetButton.setBackground(btnColor);
            }
        });

        // Status label setup
        statusLabel = new JLabel("Enter instructions and click 'Execute'.");
        statusLabel.setFont(font);
        statusLabel.setForeground(fgColor);

        // Panel for controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        controls.setBackground(bgColor);
        controls.add(executeButton);
        controls.add(resetButton);

        // Layout setup
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.add(new JScrollPane(instructionArea), BorderLayout.WEST);
        mainPanel.add(new JScrollPane(outputArea), BorderLayout.EAST);
        mainPanel.add(controls, BorderLayout.SOUTH);
        mainPanel.add(statusLabel, BorderLayout.NORTH);

        add(mainPanel);

        // Instruction input guidance
        instructionArea.setText(
                "Enter instructions here, one per line.\n" +
                        "Example:\n" +
                        "LOAD R1 5   // Load 5 into R1\n" +
                        "LOAD R2 10  // Load 10 into R2\n" +
                        "ADD R1 R2   // Add R2 to R1\n" +
                        "PRINT R1    // Print the value in R1\n" +
                        "PRINT R2    // Print the value in R2\n"
        );

        // Button actions
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executeProgram();
            }
        });

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetVM();
            }
        });
    }

    // Executes the entire program
    private void executeProgram() {
        resetVM();
        String[] instructions = instructionArea.getText().split("\n");
        for (String instruction : instructions) {
            execute(instruction.trim());
        }
        statusLabel.setText("Execution completed.");
    }

    // Resets the virtual machine
    private void resetVM() {
        for (int i = 1; i <= 3; i++) {
            registers.put("R" + i, 0);
        }
        outputArea.setText("VM reset. Enter new instructions.\n");
        statusLabel.setText("VM reset. Ready for new instructions.");
    }

    // Executes a single instruction
    private void execute(String instruction) {
        if (instruction.isEmpty()) return;

        String[] parts = instruction.split(" ");
        String command = parts[0];

        outputArea.append("Executing: " + instruction + "\n");

        switch (command) {
            case "LOAD":
                load(parts[1], Integer.parseInt(parts[2]));
                outputArea.append("Loaded value " + parts[2] + " into " + parts[1] + "\n\n");
                break;
            case "ADD":
                add(parts[1], parts[2]);
                outputArea.append("Added value from " + parts[2] + " to " + parts[1] + "\n\n");
                break;
            case "PRINT":
                print(parts[1]);
                outputArea.append("Value in " + parts[1] + ": " + registers.get(parts[1]) + "\n\n");
                break;
            default:
                outputArea.append("Unknown instruction: " + command + "\n\n");
        }
    }

    // Load a value into a register
    private void load(String register, int value) {
        registers.put(register, value);
    }

    // Add the value of one register to another
    private void add(String register1, String register2) {
        int value1 = registers.get(register1);
        int value2 = registers.get(register2);
        registers.put(register1, value1 + value2);
    }

    // Print the value of a register
    private void print(String register) {
        outputArea.append("Value in " + register + ": " + registers.get(register) + "\n");
    }

    // Main method to run the VM
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CoolEnhancedVM().setVisible(true);
            }
        });
    }
}
