import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import model.Question;
import model.QuizQuestion;

/**
 * This class is the entry point of the program.
 */
public class Main {
  /**
   * Main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    ArrayList<Question> questions = new ArrayList<>(Arrays.asList(
        new Question(1, "Combien de frontière possède la France ?", "8",
            new ArrayList<>(List.of("7", "6", "5"))
        ), new Question(2, "Quel est le symbole atomique du potassium ?", "K",
            new ArrayList<>(List.of("P", "Po", "Li"))
        ), new Question(3, "Combien de vaccins sont obligatoires en France ?", "11",
            new ArrayList<>(List.of("10", "9", "8"))
        ), new Question(4, "Combien d’os comporte le corps humain ?", "206",
            new ArrayList<>(List.of("200", "212", "194"))
        ), new Question(5, "En quelle année environ a été créé le premier ordinateur ?", "1943",
            new ArrayList<>(List.of("1940", "1946", "1937"))
        ), new Question(6,
            "Comment appelle-t-on les 40 élus de l’académie française décidant de la manière dont"
            + " on " + "emplois le français ?", "Les Immortels",
            new ArrayList<>(List.of("Les Académitiens", "Les Vieux", "Les 40"))
        ), new Question(7, "Combien de pays sont reconnus par l’ONU ?", "195",
            new ArrayList<>(List.of("200", "190", "185"))
        ), new Question(8, "Quel est le score parfait au bowling ?", "300",
            new ArrayList<>(List.of("200", "400", "500"))
        ), new Question(9, "Quel est le nom complet de Mozart ?", "Wolfgang Amadeus Mozart",
            new ArrayList<>(List.of("Amadeus Wolfgang Mozart", "Amadeus Mozart Wolfgang",
                "Wolfgang Mozart Amadeus"
            ))
        ), new Question(10,
            "Laquelle de ces propositions n’est pas une unité du système international ?", "Gramme",
            new ArrayList<>(List.of("Candela", "Kelvin", "Mole"))
        ), new Question(11,
            "Lequel de ces instruments ne fait pas partie de l’orchestre symphonique ?",
            "La Flûte à bec",
            new ArrayList<>(List.of("Le Bassons", "Le Saxophone", "Le Glockenspiel"))
        ), new Question(12, "De combien de pays le français est-il la langue officielle ?", "29",
            new ArrayList<>(List.of("30", "31", "32"))
        ), new Question(13,
            "A quel ordre de grandeur numérique appartient le préfixe du Système international "
            + "d'unité “péta” ?", "10^15", new ArrayList<>(List.of("10^12", "10^17", "10^19"))
        ), new Question(14, "Laquelle de ces figures de styles n'inclut aucune répétition ?",
            "Métalepse", new ArrayList<>(List.of("Allitération", "Épizeuxe", "Anadiplose"))
        ), new Question(15, "Combien de secondes y a-t-il dans une journée ?", "86400",
            new ArrayList<>(List.of("86200", "74600", "84800"))
        ), new Question(16, "Quelle est la distance Terre-Lune ?", "384.000 km",
            new ArrayList<>(List.of("3.840.000 km", "38.400 km", "38.400.000 km"))
        ), new Question(17, "En quelle année l’Homme a-t-il marché sur la lune ?", "1969",
            new ArrayList<>(List.of("1970", "1963", "1967"))
        ), new Question(18, "Quel objet céleste appelle-t-on “l’étoile du berger” ?", "Venus",
            new ArrayList<>(List.of("Proxima du centaure", "Un astéroïde du la ceinture de Kuiper",
                "Le Soleil"
            ))
        ),
        new Question(19, "Quel élément chimique compose majoritairement la photosphère du soleil ?",
            "Hydrogène", new ArrayList<>(List.of("Polonium", "Hydrogène", "Oxygène"))
        ), new Question(20, "Quelles sont les 5 premières décimales de pi ?", "14159",
            new ArrayList<>(List.of("14519", "14915", "15194"))
        )
    ));
    
    Collections.shuffle(questions);
    int numberOfQuestions = 10;
    int numberOfChoices = 4;
    questions.subList(numberOfQuestions, questions.size()).clear();
    ArrayList<QuizQuestion> quizQuestions = new ArrayList<>();
    questions.forEach(question -> quizQuestions.add(new QuizQuestion(question, numberOfChoices)));
    int score = 0;
    for (QuizQuestion question : quizQuestions) {
      System.out.println(question.getStatement());
      question.getChoices().forEach(
          choice -> System.out.println(question.getChoices().indexOf(choice) + 1 + "_ " + choice));
      System.out.println("Votre réponse : ");
      Scanner scanner = new Scanner(System.in);
      int answer = getUserAnswer(numberOfChoices, scanner);
      System.out.println("Vous avez répondu : " + answer);
      System.out.println("La bonne réponse était : " + (question.getAnswerId() + 1));
      if (answer == question.getAnswerId() + 1) {
        score++;
      }
      System.out.println("Appuyez sur entrée pour continuer");
      try {
        System.in.read();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      System.out.println("--------------------------------------------------");
    }
    System.out.println("Your Score: " + score);
  }
  
  private static int getUserAnswer(int numberOfChoices, Scanner scanner) {
    String answer;
    while (true) {
      answer = scanner.next();
      try {
        int answerInt = Integer.parseInt(answer);
        if (answerInt > 0 && answerInt <= numberOfChoices) {
          return answerInt;
        }
        System.out.println("Veuillez entrer un nombre valide");
      } catch (NumberFormatException e) {
        System.out.println("Veuillez entrer un nombre");
      }
    }
    
  }
}
