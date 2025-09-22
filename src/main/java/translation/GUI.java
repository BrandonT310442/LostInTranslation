package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

// TODO Task D: Update the GUI for the program to align with UI shown in the README example.
//            Currently, the program only uses the CanadaTranslator and the user has
//            to manually enter the language code they want to use for the translation.
//            See the examples package for some code snippets that may be useful when updating
//            the GUI.
public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Initialize services
            Translator translator = new JSONTranslator();
            CountryCodeConverter countryConverter = new CountryCodeConverter();
            LanguageCodeConverter languageConverter = new LanguageCodeConverter();
            
            // Create top panel with language dropdown
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(new JLabel("Language:"));
            
            // Create language dropdown (JComboBox) with language names
            JComboBox<String> languageComboBox = new JComboBox<>();
            List<String> languageCodes = translator.getLanguageCodes();
            for (String code : languageCodes) {
                String languageName = languageConverter.fromLanguageCode(code);
                // Use the language name if available, otherwise use the code
                languageComboBox.addItem(languageName != null ? languageName : code);
            }
            languageComboBox.setPreferredSize(new Dimension(300, 30));
            topPanel.add(languageComboBox);
            
            // Create country list (JList) with country names
            List<String> countryCodes = translator.getCountryCodes();
            String[] countryNames = new String[countryCodes.size()];
            for (int i = 0; i < countryCodes.size(); i++) {
                String code = countryCodes.get(i);
                String countryName = countryConverter.fromCountryCode(code);
                countryNames[i] = countryName != null ? countryName : code;
            }
            JList<String> countryList = new JList<>(countryNames);
            countryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            countryList.setVisibleRowCount(15);
            
            // Put the country list in a scroll pane that will take up the full bottom area
            JScrollPane countryScrollPane = new JScrollPane(countryList);
            countryScrollPane.setPreferredSize(new Dimension(600, 350));
            
            // Create translation display area at the top right
            JPanel translationPanel = new JPanel();
            translationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            translationPanel.add(new JLabel("Translation: "));
            JLabel translationResult = new JLabel("");
            translationResult.setFont(new Font("Arial", Font.PLAIN, 20));
            translationPanel.add(translationResult);
            
            // Combine language dropdown and translation in top panel
            JPanel fullTopPanel = new JPanel();
            fullTopPanel.setLayout(new BorderLayout());
            fullTopPanel.add(topPanel, BorderLayout.WEST);
            fullTopPanel.add(translationPanel, BorderLayout.CENTER);
            fullTopPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            // Add listener for country selection
            countryList.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        String selectedCountry = countryList.getSelectedValue();
                        String selectedLanguage = (String) languageComboBox.getSelectedItem();
                        
                        if (selectedCountry != null && selectedLanguage != null) {
                            // Convert country name to code
                            String countryCode = countryConverter.fromCountry(selectedCountry);
                            if (countryCode == null) {
                                countryCode = selectedCountry.toLowerCase();
                            }
                            
                            // Convert language name to code
                            String languageCode = languageConverter.fromLanguage(selectedLanguage);
                            if (languageCode == null) {
                                languageCode = selectedLanguage.toLowerCase();
                            }
                            
                            // Get translation
                            String translation = translator.translate(countryCode, languageCode);
                            if (translation != null) {
                                translationResult.setText(translation);
                            } else {
                                translationResult.setText("Translation not available");
                            }
                        }
                    }
                }
            });
            
            // Add listener for language selection
            languageComboBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        String selectedCountry = countryList.getSelectedValue();
                        String selectedLanguage = (String) languageComboBox.getSelectedItem();
                        
                        if (selectedCountry != null && selectedLanguage != null) {
                            // Convert country name to code
                            String countryCode = countryConverter.fromCountry(selectedCountry);
                            if (countryCode == null) {
                                countryCode = selectedCountry.toLowerCase();
                            }
                            
                            // Convert language name to code
                            String languageCode = languageConverter.fromLanguage(selectedLanguage);
                            if (languageCode == null) {
                                languageCode = selectedLanguage.toLowerCase();
                            }
                            
                            // Get translation
                            String translation = translator.translate(countryCode, languageCode);
                            if (translation != null) {
                                translationResult.setText(translation);
                            } else {
                                translationResult.setText("Translation not available");
                            }
                        }
                    }
                }
            });
            
            // Create main panel with the full top panel and country list
            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BorderLayout());
            mainPanel.add(fullTopPanel, BorderLayout.NORTH);
            mainPanel.add(countryScrollPane, BorderLayout.CENTER);
            
            // Create and configure frame
            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}