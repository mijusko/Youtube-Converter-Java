import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class YouTubeConverter extends JFrame {
    private JPanel cardPanel;
    private CardLayout cardLayout;

    // Colors
    private final Color primaryColor = new Color(255, 0, 0); // YouTube Red
    private final Color backgroundColor = new Color(24, 24, 24); // Dark Background
    private final Color textColor = Color.WHITE;
    private final Color secondaryColor = new Color(45, 45, 45); // Darker gray for inputs

    // First Screen Components
    private JTextField linkField;
    private JComboBox<String> formatBox;
    private JComboBox<String> resolutionBox;
    private JButton nextButton;

    // Second Screen Components
    private JLabel infoLabel;
    private JProgressBar progressBar;
    private JButton selectFolderButton;
    private JButton downloadButton;
    private String selectedPath = "";

    public YouTubeConverter() {
        setTitle("Java YT converter");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(backgroundColor);

        setupFirstScreen();
        setupSecondScreen();

        add(cardPanel);
        setVisible(true);
    }

    private void setupFirstScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Java YT converter");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel linkLabel = createStyledLabel("Unesite YouTube link:");
        linkField = createStyledTextField();

        JLabel formatLabel = createStyledLabel("Izaberite format:");
        String[] formats = {"MP4", "MP3"};
        formatBox = createStyledComboBox(formats);

        JLabel resLabel = createStyledLabel("Izaberite maksimalnu rezoluciju:");
        String[] resolutions = {"360p", "480p", "720p", "1080p", "2k", "4k"};
        resolutionBox = createStyledComboBox(resolutions);
        resolutionBox.setSelectedItem("1080p");

        nextButton = createStyledButton("DALJE", primaryColor);
        nextButton.addActionListener(e -> {
            if (linkField.getText().trim().isEmpty()) {
                showError("Molimo unesite YouTube link!");
                return;
            }
            cardLayout.show(cardPanel, "second");
        });

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(linkLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(linkField);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(formatLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(formatBox);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(resLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(resolutionBox);
        panel.add(Box.createVerticalGlue());
        panel.add(nextButton);

        cardPanel.add(panel, "first");
    }

    private void setupSecondScreen() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColor);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JLabel titleLabel = new JLabel("Čuvanje fajla");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(primaryColor);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        infoLabel = createStyledLabel("Izaberite lokaciju na računaru");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        selectFolderButton = createStyledButton("IZABERI FOLDER", secondaryColor);
        selectFolderButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedPath = chooser.getSelectedFile().getAbsolutePath();
                infoLabel.setText("Lokacija: " + truncatePath(selectedPath));
                infoLabel.setToolTipText(selectedPath);
            }
        });

        progressBar = new JProgressBar();
        progressBar.setMaximumSize(new Dimension(400, 25));
        progressBar.setBackground(secondaryColor);
        progressBar.setForeground(primaryColor);
        progressBar.setBorderPainted(false);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);

        downloadButton = createStyledButton("PREUZMI", primaryColor);
        downloadButton.addActionListener(e -> startDownload());

        JButton backButton = createStyledButton("NAZAD", secondaryColor);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "first"));

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(infoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(selectFolderButton);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(progressBar);
        panel.add(Box.createVerticalGlue());
        panel.add(downloadButton);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(backButton);

        cardPanel.add(panel, "second");
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(textColor);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(400, 40));
        field.setBackground(secondaryColor);
        field.setForeground(textColor);
        field.setCaretColor(textColor);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(secondaryColor.brighter(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setMaximumSize(new Dimension(400, 40));
        box.setBackground(Color.WHITE);
        box.setForeground(Color.BLACK);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return box;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(400, 50));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    private void startDownload() {
        if (selectedPath.isEmpty()) {
            showError("Molimo izaberite lokaciju za čuvanje!");
            return;
        }

        String link = linkField.getText().trim();
        String format = (String) formatBox.getSelectedItem();
        String resolution = (String) resolutionBox.getSelectedItem();

        setControlsEnabled(false);
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        infoLabel.setText("Preuzimanje je pokrenuto...");

        new Thread(() -> {
            try {
                String ytDlpCmd = "yt-dlp";
                if (new File("yt-dlp.exe").exists()) {
                    ytDlpCmd = new File("yt-dlp.exe").getAbsolutePath();
                }

                List<String> command = new ArrayList<>();
                command.add(ytDlpCmd);

                File ffmpegExe = new File("ffmpeg.exe");
                if (ffmpegExe.exists()) {
                    command.add("--ffmpeg-location");
                    command.add(ffmpegExe.getAbsoluteFile().getParent());
                } else if (!format.equals("MP3")) {
                    // If FFmpeg is missing and we want MP4, warn the user
                    SwingUtilities.invokeLater(() -> {
                        showError("Upozorenje: 'ffmpeg.exe' nije pronađen!\n" +
                                 "Bez njega, MP4 video visoke rezolucije neće imati zvuk.\n" +
                                 "Molimo ubacite ffmpeg.exe u folder aplikacije.");
                    });
                }

                if (format.equals("MP3")) {
                    command.add("-x");
                    command.add("--audio-format");
                    command.add("mp3");
                    command.add("--audio-quality");
                    command.add("0");
                } else {
                    String resHeight = resolution.replace("p", "");
                    if (resHeight.equals("2k")) resHeight = "1440";
                    if (resHeight.equals("4k")) resHeight = "2160";
                    
                    // Improved format string: try to get best MP4 compatible video + audio
                    command.add("-f");
                    command.add(String.format("bestvideo[height<=%s][ext=mp4]+bestaudio[ext=m4a]/best[height<=%s]/best", resHeight, resHeight));
                    command.add("--merge-output-format");
                    command.add("mp4");
                }

                command.add("--yes-playlist");
                command.add("-o");
                command.add(selectedPath + "/%(title)s.%(ext)s");
                command.add(link);

                ProcessBuilder pb = new ProcessBuilder(command);
                pb.redirectErrorStream(true);
                Process p = pb.start();

                Scanner s = new Scanner(p.getInputStream());
                while (s.hasNextLine()) {
                    String line = s.nextLine();
                    // Optional: parse percentage from line to update progress bar
                    if (line.contains("%")) {
                        try {
                            String[] parts = line.split("%")[0].split(" ");
                            String lastPart = parts[parts.length - 1].trim();
                            if (lastPart.startsWith("[") || lastPart.isEmpty()) {
                                // some formats of output have [download]  10%
                                lastPart = parts[parts.length - 1];
                            }
                            double progress = Double.parseDouble(lastPart);
                            SwingUtilities.invokeLater(() -> {
                                progressBar.setIndeterminate(false);
                                progressBar.setValue((int) progress);
                            });
                        } catch (Exception ignored) {}
                    }
                }

                int exitCode = p.waitFor();
                SwingUtilities.invokeLater(() -> {
                    setControlsEnabled(true);
                    progressBar.setVisible(false);
                    if (exitCode == 0) {
                        infoLabel.setText("Završeno! Fajlovi su sačuvani.");
                        JOptionPane.showMessageDialog(this, "Uspešno preuzeto!", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        infoLabel.setText("Došlo je do greške.");
                        showError("Greška pri preuzimanju. Proverite link ili internet konekciju.");
                    }
                });

            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    setControlsEnabled(true);
                    progressBar.setVisible(false);
                    showError("Sistemska greška: " + ex.getMessage());
                });
            }
        }).start();
    }

    private void setControlsEnabled(boolean enabled) {
        downloadButton.setEnabled(enabled);
        selectFolderButton.setEnabled(enabled);
        nextButton.setEnabled(enabled);
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Greška", JOptionPane.ERROR_MESSAGE);
    }

    private String truncatePath(String path) {
        if (path.length() > 40) {
            return "..." + path.substring(path.length() - 37);
        }
        return path;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(YouTubeConverter::new);
    }
}
