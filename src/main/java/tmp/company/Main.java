package tmp.company;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
  public static void main(String[] args) {
    Pattern pattern = Pattern.compile(
      "[" +                   //начало списка допустимых символов
        "a-zA-ZäÄöÖüÜß" +    //буквы русского алфавита
        "\\d" +         //цифры
        "\\s" +         //знаки-разделители (пробел, табуляция и т.д.)
        "\\p{Punct}" +  //знаки пунктуации
        "]" +                   //конец списка допустимых символов
        "*");
    //допускается наличие указанных символов в любом количестве
    Matcher matcher = pattern.matcher("Посоветуйте, как это осуществить максимально быстро");
    System.out.println(matcher.matches());//true

    matcher = pattern.matcher("lorem ipsum dolor ü sit amet");
    System.out.println(matcher.matches());//false
  }
}
