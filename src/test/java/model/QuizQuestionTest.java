package model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.InvalidNumberOfChoices;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class QuizQuestionTest {
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
      ),
      new Question(11, "Lequel de ces instruments ne fait pas partie de l’orchestre symphonique ?",
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
          new ArrayList<>(
              List.of("Proxima du centaure", "Un astéroïde du la ceinture de Kuiper", "Le Soleil"))
      ),
      new Question(19, "Quel élément chimique compose majoritairement la photosphère du soleil ?",
          "Hydrogène", new ArrayList<>(List.of("Polonium", "Hydrogène", "Oxygène"))
      ), new Question(20, "Quelles sont les 5 premières décimales de pi ?", "14159",
          new ArrayList<>(List.of("14519", "14915", "15194"))
      )
  ));
  
  @Test
  void should_the_question_choices_be_random() {
    //Given
    Question question = questions.get(0);
    int numberOfChoices = 4;
  
    //When
    QuizQuestion question1 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question2 = new QuizQuestion(question, numberOfChoices);
    //Then
    assertThat(question1.getChoices()).containsAll(question2.getChoices());
    assertThat(question1.getChoices()).isNotEqualTo(question2.getChoices());
  }
  
  @Test
  void should_the_question_answer_be_at_a_random_position() {
    //Given
    Question question = questions.get(0);
    int numberOfChoices = 4;
  
    //When
    QuizQuestion question1 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question2 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question3 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question4 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question5 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question6 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question7 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question8 = new QuizQuestion(question, numberOfChoices);
    QuizQuestion question9 = new QuizQuestion(question, numberOfChoices);
  
    //Then
    List<Integer> test1 = List.of(question1.getChoices().size(), question2.getChoices().size(),
        question3.getChoices().size(), question4.getChoices().size(), question5.getChoices().size(),
        question6.getChoices().size(), question7.getChoices().size(), question8.getChoices().size(),
        question9.getChoices().size()
    );
    List<Integer> test =
        List.of(question1.getAnswerId(), question2.getAnswerId(), question3.getAnswerId(),
            question4.getAnswerId(), question5.getAnswerId(), question6.getAnswerId(),
            question7.getAnswerId(), question8.getAnswerId(), question9.getAnswerId()
        );
  
    float avgNumberOfChoices = 0;
    for (int x : test1) {
      avgNumberOfChoices += x;
    }
    avgNumberOfChoices /= test1.size();
    float numberOfCombinaison = 0;
    float numberOfMatchingPaire = 0;
    for (int x = 0; x < test.size() - 1; x++) {
      for (int y = x + 1; y < test.size() - 1; y++) {
        numberOfCombinaison++;
        if (test.get(x).equals(test.get(y))) {
          numberOfMatchingPaire++;
        }
      }
    }
  
    assertThat(numberOfMatchingPaire * 100 / numberOfCombinaison).isLessThanOrEqualTo(
        100 / avgNumberOfChoices);
  }
  
  @Test
  void should_choices_contain_the_maximum_amount_of_choices_if_the_ask_amount_is_greater_than_max() {
    //Given
    Question question = questions.get(0);
    int numberOfChoices = Integer.MAX_VALUE;
    int expectedChoicesLength = question.getChoices().size() + 1;
    //When
    QuizQuestion question1 = new QuizQuestion(question, numberOfChoices);
  
    //Then
    assertThat(question1.getChoices().size()).isEqualTo(expectedChoicesLength);
  }
  
  @Test
  void should_return_error_if_the_number_of_choices_ask_is_strictly_less_than_2() {
    //Given
    Question question = questions.get(0);
    
    //When
    
    //Then
    assertThrows(InvalidNumberOfChoices.class, () -> new QuizQuestion(question, 1));
    assertThrows(InvalidNumberOfChoices.class, () -> new QuizQuestion(question, 0));
    assertThrows(InvalidNumberOfChoices.class, () -> new QuizQuestion(question, -1));
  }
}