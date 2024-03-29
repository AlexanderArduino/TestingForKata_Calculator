import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //        String str = " X I + I X ";
//        String str = " ";
        String str = "12 3 -X I";
//        String str = "123/123*125+5";
//        String str = "123/ii+ xI";
        String znak = null;
        String[] operandy = new String[0];

        System.out.println("Введенное значение" + str);
        if (proverkaNaNalichieZnakov(str)) {
            znak = kakoiZnak(str);
            operandy = razdelenieNaOperandy(str);
            for (String s : operandy) {
                trimAllSpaces(s);
                System.out.println(s);
            }
        }
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
     * На вход получает строку. Поочередно сравнивает каждый символ строки с знаками "+", "-", "*", "/";
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
                default:
                    znak = null;
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
    static boolean isArabian(String s) {
        int count = 0;
        char[] array = new char[0];
        array = s.toCharArray();

        for (int i = 0; i < s.length(); i++) {
            if (array[i] == '0' || array[i] == '1' || array[i] == '2' || array[i] == '3' || array[i] == '4' ||
                    array[i] == '5' || array[i] == '6' || array[i] == '7' || array[i] == '8' || array[i] == '9') {
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
     * Проверка строки на наличие ТОЛЬКО символов I, V, X, L, C, D, M (Римские числа)
     *
     * @param s - входящая строка
     * @return - true: если ТОЛЬКО символы I, V, X, L, C, D, M    false - если есть хотя бы один любой другой символ
     */
    static boolean isRoman(String s) {
        int count = 0;
        char[] array = s.toCharArray();

        for (int i = 0; i < s.length(); i++) {
            if (array[i] == 'I' || array[i] == 'V' || array[i] == 'X' || array[i] == 'L' || array[i] == 'C' ||
                    array[i] == 'D' || array[i] == 'M') {
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
     * На вход подается два числа
     * @param s1 - число 1
     * @param s2 - число 2
     * @return - число. Если 0 - оба числа арабские, 1 - оба числа римские
     * @throws IOException - исключение выбрасывается, если числа разных систем.
     */
    static int kakayaSistema(String s1, String s2) throws IOException{
        int result;
        if(isArabian(s1) && isArabian(s2)){
            result = 0;
        } else if(isRoman(s1) && isRoman(s2)){
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
        System.out.println(array);

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

}