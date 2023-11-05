import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class TextFileSearch extends JFrame {
    private JTextArea originalTextArea, filteredTextArea;
    private JTextField searchField;
    private JButton loadFileButton, searchButton, quitButton;
    private Path filePath;

    public TextFileSearch() {
        setTitle("Text File Search");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        originalTextArea = new JTextArea(20, 40);
        filteredTextArea = new JTextArea(20, 40);
        searchField = new JTextField(20);
        loadFileButton = new JButton("Load File");
        searchButton = new JButton("Search");
        quitButton = new JButton("Quit");
        searchButton.setEnabled(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(originalTextArea), new JScrollPane(filteredTextArea));
        splitPane.setResizeWeight(0.5);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(searchField, BorderLayout.NORTH);
        rightPanel.add(searchButton, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(loadFileButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(quitButton);

        loadFileButton.addActionListener(e -> loadFile());
        searchButton.addActionListener(e -> searchFile());
        quitButton.addActionListener(e -> System.exit(0));

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(splitPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }
        private void loadFile() {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                filePath = fileChooser.getSelectedFile().toPath();
                try (Stream<String> lines = Files.lines(filePath)) {
                    originalTextArea.setText("");
                    lines.forEach(line -> originalTextArea.append(line + "\n"));
                    searchButton.setEnabled(true);
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        private void searchFile() {
            String searchString = searchField.getText();
            if (filePath != null && !searchString.isEmpty()) {
                try (Stream<String> lines = Files.lines(filePath)) {
                    File filteredFile = new File("filtered_File.txt");
                    try (PrintWriter writer = new PrintWriter(new FileWriter(filteredFile))) {
                        lines.filter(line -> line.contains(searchString))
                                .forEach(line -> {
                                    writer.println(line);
                                    filteredTextArea.append(line + "\n");
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error writing the filtered file", "Filter Error", JOptionPane.ERROR_MESSAGE);
                    }
                }catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error loading file.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

            }
        }
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            TextFileSearch app = new TextFileSearch();
            app.setVisible(true);
        });
    }
}