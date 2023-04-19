import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LanguageClassifier {
    private Map<String, double[]> languageWeights;
    private String[] languages;
    private double learningRate;

    public LanguageClassifier(String[] languages, double learningRate) {
        this.languages = languages;
        this.languageWeights = new HashMap<>();
        for (String language : languages) {
            this.languageWeights.put(language, new double[128]);
        }
        this.learningRate = learningRate;
    }

    public void train(String trainingFolder) throws IOException {
        File folder = new File(trainingFolder);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("Training folder path is not a directory.");
        }

        for (String language : languages) {
            File languageFolder = new File(folder, language);
            if (!languageFolder.exists() || !languageFolder.isDirectory()) {
                throw new IllegalArgumentException("Training folder does not contain subfolder for language: " + language);
            }

            File[] files = languageFolder.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.toLowerCase();
                        for (char c : line.toCharArray()) {
                            if (c >= 'a' && c <= 'z') {
                                languageWeights.get(language)[c]++;
                            }
                        }
                    }
                    br.close();
                }
            }

            double[] weights = languageWeights.get(language);
            double sum = 0.0;
            for (double weight : weights) {
                sum += weight;
            }
            for (int i = 0; i < weights.length; i++) {
                weights[i] /= sum;
            }
        }
    }

    public String classify(String input) {
        double[] inputVector = new double[128];
        input = input.toLowerCase();
        for (char c : input.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                inputVector[c]++;
            }
        }

        String bestLanguage = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (String language : languages) {
            double[] weights = languageWeights.get(language);
            double score = 0.0;
            for (int i = 0; i < inputVector.length; i++) {
                score += inputVector[i] * weights[i];
            }
            if (score > bestScore) {
                bestScore = score;
                bestLanguage = language;
            }
        }

        return bestLanguage;
    }

    public void updateWeights(String language, double[] inputVector) {
        double[] weights = languageWeights.get(language); //wagi przypisane do danego jezyka
        for (int i = 0; i < inputVector.length; i++) {
            weights[i] += learningRate * inputVector[i] * (1 - weights[i]); //delta
        }
    }
}
