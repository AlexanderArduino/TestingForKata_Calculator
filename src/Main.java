import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, NumberFormatException {
        String str;
        Scanner sc = new Scanner(System.in);

        while(true){
            if(sc.hasNext()){
                str = sc.nextLine();
                System.out.println("Результат:  " + calc(str));
            }
        }
    }


    public static String calc(String input) throws IOException, NumberFormatException {
        String znak = null;     // Переменная для хранения знака операции;
        String[] operandyString = new String[0]; // Массив для хранения операндов после их разделения
        int tmpResult; // Временное хранение результата операции для последующего перевода с арабских в римские
        String result = new String(); // Переменная для итогового результата вычислений калькулятора

        System.out.println("Введенное значение: " + input);

        input = input.toUpperCase(); // Все к одному регистру

        //Проверка на наличие знаков и разделение на операнды
        if (proverkaNaNalichieZnakov(input)) {
            znak = kakoiZnak(input);
//            System.out.println("ZNAK: " + znak);
            operandyString = razdelenieNaOperandy(input);

            System.out.println("Оператор 1: " + operandyString[0]);
            System.out.println("Оператор 2: " + operandyString[1]);
        }

        // Проверка на использование одинаковых систем счисления для обоих операндов
        int system = kakayaSistema(operandyString[0], operandyString[1]);
        int op1 = 0, op2 = 0;

        // Обработка арабской системы счисления
        if (system == 0) {
            try {
                op1 = Integer.parseInt(operandyString[0]);
            } catch (NumberFormatException e) {
                System.out.println("ОШИБКА!!! Не введено первое число");
            }
            try {
                op2 = Integer.parseInt(operandyString[1]);
            } catch (Exception e) {
                System.out.println("Не второе первое число");
            }
            if (checkDiapazone(op1) & checkDiapazone(op2)) {
                tmpResult = goMath(op1, op2, znak);
                result = Integer.toString(tmpResult);
                System.out.println("Результат операции " + znak + " :" + tmpResult);
            }

            // Обработка рисмкой системы счисления
        } else if (system == 1) {
            op1 = convertRomanToArabian(operandyString[0]);
            op2 = convertRomanToArabian(operandyString[1]);
            if (checkDiapazone(op1) & checkDiapazone(op2)) {
                tmpResult = goMath(op1, op2, znak);
                result = Integer.toString(tmpResult);
                System.out.println("Результат операции " + znak + " :" + tmpResult);
            } else
                throw new NumberFormatException("Введенные числа не соответствуют диапазону от 1 до 10 включительно");
            tmpResult = goMath(op1, op2, znak);
            if (tmpResult < 0) {
                throw new IOException("Результат операции - отрицательное число. Такого нет в римской системе");
            } else {
                result = convertArabianToRoman(tmpResult);
            }
        }
        return result;
    }


    /**
     * Проверяется наличие операторов сложения, вычитания, деления, умножения.
     * Так же производится подсчет их количества. Если их количество равно 1,
     * то вызывается метод razdelenieNaOperandy(String s)
     *
     * @param s - входная строка для проверки.
     * @return - массив операндов
     * @throws IOException - выбрасывается если нет операторов или операторов более одного
     */
    static boolean proverkaNaNalichieZnakov(String s) throws IOException {
        int count = 0;
        char[] chars = s.toCharArray();
        if (!s.isEmpty()) { //[s.length() != 0] предложил заменить на !s.isEmpty()
            for (char c : chars) {
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    count += 1;
                }
            }
        }
        for (char c : chars) {
            if (c == '.' || c == ',') {
                throw new IOException("Зафиксирован возможный ввод дробного числа (наличие точки или запятой)");
            }
        }
        if (count == 0) {
            throw new IOException("Операторы отсутствуют. Это не математическая операция");
        } else if (count > 1) {
            throw new IOException("Больше одного оператора. Не соответствует условиям ввода");
        } else {
            return true;
        }
    }

    /**
     * Производит разделение входной строки на массив строк. Разделителями служат операторы "+" "-" "*" "/"
     *
     * @param s - входная строка для разделения
     * @return - массив строк
     */
    static String[] razdelenieNaOperandy(String s) {
        String[] strings = new String[0];
        strings = s.split("[-+*/]");
        strings[0] = trimAllSpaces(strings[0]);
        strings[1] = trimAllSpaces(strings[1]);
        return strings;
    }

    /**
     * Используется для вычисления какой конкретно знак используется в строке
     * На вход получает строку. Поочередно сравнивает каждый символ строки со знаками "+", "-", "*", "/";
     *
     * @param s - выражение в виде строки
     * @return - строку и символом оператора
     */
    static String kakoiZnak(String s) {
        String znak = null;
        char[] chars = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            switch (chars[i]) {
                case '+':
                    znak = "+";
                    break;
                case '-':
                    znak = "-";
                    break;
                case '*':
                    znak = "*";
                    break;
                case '/':
                    znak = "/";
                    break;
//                default:
//                    znak = null;
            }
        }
        return znak;
    }

    /**
     * Удаляются лишние пробелы (если они есть) в начале, в середине и в конце строки
     *
     * @param str - входящая строка
     * @return - возвращается строка
     */
    static String trimAllSpaces(String str) {
        StringBuilder sb = new StringBuilder(str);
//        System.out.println(sb); //==========================================================
        int i = -1;
        for (int j = 0; j < sb.length(); j++) {
            i = sb.indexOf(" ");
            if (i != -1) {
                sb.deleteCharAt(i);
            } else {
                break;
            }
        }
        return sb.toString();
    }

    /**
     * Проверка строки на наличие ТОЛЬКО символо от 0 до 9 (арабские числа)
     *
     * @param s - входящая строка
     * @return - true: если ТОЛЬКО символы от 0 до 9, false - если есть хотя бы один любой другой символ
     */
    private static boolean isArabian(String s) {
        int count = 0;
        char[] array = new char[0];
        array = s.toCharArray();

        for (int i = 0; i < s.length(); i++) {
            if (array[i] == '0' || array[i] == '1' || array[i] == '2' || array[i] == '3' || array[i] == '4' || array[i] == '5' || array[i] == '6' || array[i] == '7' || array[i] == '8' || array[i] == '9') {
                count++;
            }
        }
        if (count == s.length()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Проверка строки на наличие ТОЛЬКО символов I, V, X, L, C, D, M (Римские числа).
     *
     * @param s - входящая строка.
     * @return - true: если ТОЛЬКО символы I, V, X, L, C, D, M    false - если есть хотя бы один любой другой символ.
     */
    private static boolean isRoman(String s) {
        int count = 0;
        char[] array = s.toCharArray();

        for (int i = 0; i < s.length(); i++) {
            if (array[i] == 'I' || array[i] == 'V' || array[i] == 'X' || array[i] == 'L' || array[i] == 'C' || array[i] == 'D' || array[i] == 'M') {
                count++;
            }
        }
        if (count == s.length()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Определяет какая система счисления у чисел на входе. На вход подается два числа.
     *
     * @param s1 - число 1.
     * @param s2 - число 2.
     * @return - 0 - оба числа арабские, 1 - оба числа римские.
     * @throws IOException - исключение выбрасывается, если числа разных систем.
     */
    static int kakayaSistema(String s1, String s2) throws IOException {
        int result;
        if (isArabian(s1) && isArabian(s2)) {
            result = 0;
        } else if (isRoman(s1) && isRoman(s2)) {
            result = 1;
        } else {
            throw new IOException("Вы используете различные системы исчисления");
        }
        return result;
    }

    /**
     * Конвертирует число из римского написания в эквивалентное арабское число для удобства проведения вычислений
     *
     * @param s - строка с числом, представленным в римской системе исчисления
     * @return - число типа int
     */
    static int convertRomanToArabian(String s) {
        char previos = '0';
        int total = 0;
        char[] array = s.toCharArray();

        for (int i = array.length - 1; i >= 0; i--) {
            if (array[i] == 'I') {
                if (previos == '0' || previos == 'I') {
                    total += 1;
                    previos = 'I';
                } else if (previos == 'V' || previos == 'X') {
                    total -= 1;
                    previos = 'I';
                }
            }
            if (array[i] == 'V') {
                if (previos == '0' || previos == 'I') {
                    total += 5;
                    previos = 'V';
                }
            }
            if (array[i] == 'X') {
                if (previos == '0' || previos == 'I' || previos == 'V' || previos == 'X') {
                    total += 10;
                    previos = 'X';
                } else if (previos == 'L' || previos == 'C') {
                    total -= 10;
                    previos = 'X';
                }
            }
            if (array[i] == 'L') {
                if (previos == '0' || previos == 'I' || previos == 'V' || previos == 'X') {
                    total += 50;
                    previos = 'L';
                } else {
                    throw new NumberFormatException("Подряд 2 раза писать L нельзя. Вместо этого напишите C. Продумайте правильность написания числа");
                }
            }
            if (array[i] == 'C') {
                if (previos == '0' || previos == 'I' || previos == 'V' || previos == 'X' || previos == 'L' || previos == 'C') {
                    total += 100;
                    previos = 'C';
                } else if (previos == 'D' || previos == 'M') {
                    total -= 100;
                    previos = 'C';
                }
            }
            if (array[i] == 'D') {
                if (previos == '0' || previos == 'I' || previos == 'V' || previos == 'X' || previos == 'L' || previos == 'C') {
                    total += 500;
                    previos = 'D';
                } else {
                    throw new NumberFormatException("Подряд 2 раза писать D нельзя. Продумайте правильность написания числа");
                }
            }
            if (array[i] == 'M') {
                if (previos == '0' || previos == 'M') {
                    total += 1000;
                    previos = 'M';
                }
            }
        }
        return total;
    }

    /**
     * Проверка диапазона (1-10) введенного числа.
     * @param op - введенное число
     * @return - true - если число в диапазоне, false - если число вне диапазона
     */
    static boolean checkDiapazone(int op) {
        if ((op > 0 & op <= 10)) {
            return true;
        } else throw new NumberFormatException("Введенные числа не соответствуют диапазону от 1 до 10 включительно");
    }

    /**
     * Проведение математических операций в зависимости от знака оператора.
     * @param op1 - число 1
     * @param op2 - число 2
     * @param znak - знак оператора
     * @return - результат в формате int.
     */
    static int goMath(int op1, int op2, String znak) {
        int result = 0;
        switch (znak) {
            case "+":
                result = op1 + op2;
                break;
            case "-":
                result = op1 - op2;
                break;
            case "*":
                result = op1 * op2;
                break;
            case "/":
                result = op1 / op2;
                break;

        }
        return result;
    }

    /**
     * Перевод числа из арабской в римскую систему счисления
     * @param num - число
     * @return - римская запись арабского числа
     */
    static String convertArabianToRoman(int num) {
        StringBuilder sb = new StringBuilder();
        int count, temp;
        count = num / 1000;
        if (count > 0) {
            for (int i = 1; i <= count; i++) {
                sb.append("M");
            }
        }
        count = num % 1000 / 100;
        if (count > 0) {
            switch (count) {
                case 1:
                    sb.append("C");
                    break;
                case 2:
                    sb.append("CC");
                    break;
                case 3:
                    sb.append("CCC");
                    break;
                case 4:
                    sb.append("CD");
                    break;
                case 5:
                    sb.append("D");
                    break;
                case 6:
                    sb.append("DC");
                    break;
                case 7:
                    sb.append("DCC");
                    break;
                case 8:
                    sb.append("DCCC");
                    break;
                case 9:
                    sb.append("CM");
                    break;
            }
        }
        count = num % 1000 % 100 / 10;
        if (count > 0) {
            switch (count) {
                case 1:
                    sb.append("X");
                    break;
                case 2:
                    sb.append("XX");
                    break;
                case 3:
                    sb.append("XXX");
                    break;
                case 4:
                    sb.append("XL");
                    break;
                case 5:
                    sb.append("L");
                    break;
                case 6:
                    sb.append("LX");
                    break;
                case 7:
                    sb.append("LXX");
                    break;
                case 8:
                    sb.append("LXXX");
                    break;
                case 9:
                    sb.append("XC");
                    break;
            }
        }
        count = num % 1000 % 100 % 10 / 1;
        if (count >= 0) {
            switch (count) {
                case 1:
                    sb.append("I");
                    break;
                case 2:
                    sb.append("II");
                    break;
                case 3:
                    sb.append("III");
                    break;
                case 4:
                    sb.append("IV");
                    break;
                case 5:
                    sb.append("V");
                    break;
                case 6:
                    sb.append("VI");
                    break;
                case 7:
                    sb.append("VII");
                    break;
                case 8:
                    sb.append("VIII");
                    break;
                case 9:
                    sb.append("IX");
                    break;
                case 0:
                    sb.append("");
            }
        }
        return sb.toString();
    }
}