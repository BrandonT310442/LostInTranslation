package translation;

import javax.swing.*;
import java.awt.event.*;


// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel countryPanel = new JPanel();
            JTextField countryField = new JTextField(10);
            countryField.setText("Canada");
            countryField.setEditable(true);
            countryPanel.add(new JLabel("Country:"));
            countryPanel.add(countryField);

            JPanel languagePanel = new JPanel();
            JTextField languageField = new JTextField(10);
            languagePanel.add(new JLabel("Language:"));
            languagePanel.add(languageField);

            JPanel buttonPanel = new JPanel();
            JButton submit = new JButton("Submit");
            buttonPanel.add(submit);

            JLabel resultLabelText = new JLabel("Translation:");
            buttonPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            buttonPanel.add(resultLabel);


            // adding listener for when the user clicks the submit button
            submit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String languageInput = languageField.getText();
                    String countryInput = countryField.getText();
                    
                    CountryCodeConverter countryConverter = new CountryCodeConverter();
                    LanguageCodeConverter languageConverter = new LanguageCodeConverter();
                    
                    // Handle country input - could be name or code
                    String countryCode = countryConverter.fromCountry(countryInput);
                    if (countryCode == null) {
                        // If not found as country name, try using input directly as code
                        countryCode = countryInput.toLowerCase();
                    }
                    
                    // Handle language input - could be name or code
                    String languageCode = languageConverter.fromLanguage(languageInput);
                    if (languageCode == null) {
                        // If not found as language name, try using input directly as code
                        languageCode = languageInput.toLowerCase();
                    }
                    
                    Translator translator = new JSONTranslator();

                    String result = translator.translate(countryCode, languageCode);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);

                }

            });

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(countryPanel);
            mainPanel.add(languagePanel);
            mainPanel.add(buttonPanel);

            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);


        });
    }
}
