import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        // Przykładowe użycie klasyfikatora języków
        String[] languages = {"English", "French", "German"};
        LanguageClassifier classifier = new LanguageClassifier(languages, 0.1);
        // Trenowanie klasyfikatora na przykładowych danych
        classifier.train("training_data");

        boolean endProgram = false;
        while (!endProgram) {
            Scanner scanner = new Scanner(System.in);
            String text1 = scanner.nextLine();

            System.out.println("Input text: " + text1);
            String predictedLanguage1 = classifier.classify(text1);
            System.out.println("Predicted language: " + predictedLanguage1);

            System.out.println("Lets try again.\n1.Yes 2.Finish 3.test");
            int input2 = scanner.nextInt();

            if (input2 == 2) {
                endProgram = true;
            }
            // Aktualizacja wag na podstawie błędów klasyfikacji
            String actualLanguage1 = predictedLanguage1;
            double[] inputVector1 = new double[128]; //przechowywanie informacji o wystapieniach poszczegolnych liter w podanym tekscie

            for (char c : text1.toCharArray()) {
                if (c >= 'a' && c <= 'z') {
                    inputVector1[c]++; // jezeli zmienna zostala znaleziona to wartosc w wektorze indeksu jest ++ co oznacza ze zostala znaleziona
                }
            }
            //tworzymy wektor ktory ma wartosci dla kazdego znaku w kodzie ascii zwiekszane w petli powyzej na podstawie wystapien w aktualnym tekscie

            classifier.updateWeights(actualLanguage1, inputVector1); //uczenie sie wektora na podstawie aktualnych danych
            //uczenie sie -> aktualizujemy wagi dla aktualnego jezyka i jego wartosci na podstawie tego wektora ktory przeanalizowal nam wpisany tekst
            // uzywamy do tego delty
        }
    }
}
////examples fr:
///*
//Est-il déjà arrivé?
//Est-elle partie en France?
//Va-t-elle rester?
//Est-ce qu'elle aime la musique?
// */