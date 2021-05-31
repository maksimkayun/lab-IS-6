package rut.imdt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static List<Integer> key = new ArrayList<>();
    private static List<Integer> support = new ArrayList<>();
    private static List<Integer> keyTwo = new ArrayList<>();
    private static List<String[][]> tableList = new ArrayList<>();


    public static void createTables(String message, int n, int m) {
        int length = message.length();
        int quantity = length / (n * m - (keyTwo.size() / 2)); // определяем количетсво таблиц, учитывая количетсво
        // забаненых ячеек
        if (length % (n * m - (keyTwo.size() / 2)) != 0) { // Если остались ещё символы в сообщении,
            quantity++;                                    // то это ещё +1 таблица
        }
        for (int i = 0; i < quantity; i++) {
            tableList.add(new String[n][m]);
        }
    }

    public static void setSupport(String message, int n, int m) {
        for (int k = 0; k < tableList.size(); k++) {
            // Если это не последняя таблица и она не единственная
            if (k != tableList.size() - 1 && tableList.size() > 1) {
                for (int i = 0; i < m; i++) {
                    int counter = 0; // количетсво забаненых ячеек в столбце
                    for (int j = 1; j < keyTwo.size(); j=j+2) {
                        if(keyTwo.get(j)==i){
                            counter=counter+1;
                        }
                    }
                    support.add(n - counter); // сколько возможно букв в столбце
                }
            }
            else { // действия для последней (или единственной) таблицы
                int quantity = (n * m - (keyTwo.size() / 2)) - (message.length() -
                        (tableList.size() - 1)*(n * m - (keyTwo.size() / 2))); // количество оставшихся пустых ячеек
                String[][] buffer = new String[n][m];
                for (int j = 0; j < keyTwo.size(); j += 2) {
                    buffer[keyTwo.get(j)][keyTwo.get(j+1)] = String.valueOf((char) 142); // заполняем 142-м символом
                }
                for (int i = n - 1; i > -1 && quantity > 0; i--) {
                    for (int j = m - 1; j > -1 && quantity > 0; j--) {
                        // Оставшиеся с конца ячейки заполняем пустыми строками ""
                        if (buffer[i][j] == null) {
                            buffer[i][j] = "";
                            quantity--;
                        }
                    }
                }
                // Считаем возможное количетсво букв в каждом столбце этой таблицы
                for (int i = 0; i < m; i++) {
                    int counter = 0;
                    for (int j = 0; j < n; j++) {
                        if (buffer[j][i] == null) {
                            counter++;
                        }
                    }
                    support.add(counter);
                }
                tableList.set(tableList.size() - 1,  buffer); // подмена
            }
        }
    }

    public static void setKeyTwo() {
        for (int i = 0; i < tableList.size(); i++) {
            for (int j = 0; j < keyTwo.size(); j += 2) {
                tableList.get(i)[keyTwo.get(j)][keyTwo.get(j+1)] = String.valueOf((char) 142);
            }
        }
        return;
    }

    public static String enc(String message, int n, int m) {
        String output = "";
        createTables(message, n, m);
        setKeyTwo();

        int counter = 0;
        for (int i = 0; i < tableList.size() && counter < message.length(); i++) {
            for (int j = 0; j < n && counter < message.length(); j++) {
                for (int k = 0; k < m && counter < message.length(); k++) {
                    if (tableList.get(i)[j][k] == null) {
                        tableList.get(i)[j][k] = String.valueOf(message.charAt(counter));
                        counter++;
                    }
                    if (tableList.get(i)[j][k].equals(String.valueOf((char) 142))) {
                        continue;
                    }
                }
            }
        }

        for (int p = 0; p < tableList.size(); p++) {
            for (int i = 0; i < m; i++) {
                int j = findCol(i);
                for (int k = 0; k < n; k++) {
                    if (tableList.get(p)[k][j] == null)
                        break;
                    if (!tableList.get(p)[k][j].equals(String.valueOf((char) 142))) {
                        output += tableList.get(p)[k][j];
                    }
                }
            }
        }
        return output;
    }

    public static int findCol(int index) { // Функция возвращает № столбца, который нужно записывать в сообщение
        for (int i = 0; i < key.size(); i++) {
            if (key.get(i) == index)
                return i;
        }
        return 0;
    }

    public static String dec(String message, int n, int m) {
        String output = "";
        createTables(message, n, m);
        setKeyTwo();
        setSupport(message, n, m);

        for (int i = 0; i < tableList.size(); i++) {
            for (int j = 0,number=0; number < m; j++) {
                if (key.get(j) == number) {

                    String[] column = new String[support.get(j + i * m)];
                    for (int k = 0; k < support.get(j + i * m); k++) {
                        column[k] = String.valueOf(message.charAt(k));
                    }
                    message = message.substring(support.get(j + i * m));

                    for (int k = 0, s = 0; k < n && s < column.length; k++) {
                        if (tableList.get(i)[k][j] == null) {
                            tableList.get(i)[k][j] = column[s];
                            s++;
                        }
                    }
                    number++;
                }
                if(j==m-1){
                    j=-1;
                }
            }
        }
        for (int i = 0; i < tableList.size(); i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    if (tableList.get(i)[j][k].equals("")) {
                        return output;
                    }
                    if (tableList.get(i)[j][k].equals(String.valueOf((char) 142))) {
                        continue;
                    }
                    output += tableList.get(i)[j][k];
                }
            }
        }
        return output;
    }

    public static void main(String[] args) {
        //============//============//
        //           INPUT          //
        try {
            System.out.print("Введите кол-во столбцов таблиц: ");
            int m = Integer.parseInt(sc.nextLine());
            if (m < 1) {
                throw new NumberFormatException("Число столбцов m должно быть больше 0!");
            }

            System.out.print("Введите кол-во строк таблиц: ");
            int n = Integer.parseInt(sc.nextLine());
            if (n < 1) {
                throw new NumberFormatException("Число строк n должно быть больше 0!");
            }

            System.out.printf("Введите код для шифрования (количество = %d): ", m);
            String values = sc.nextLine();
            if (values.split(" ").length != m) {
                throw new NumberFormatException(String.format("Количетсво элементов ключа должно быть равно " + m));
            }
            for (String value:values.split(" ")) {
                if (Integer.parseInt(value) < 0 || Integer.parseInt(value) > m - 1) {
                    throw new NumberFormatException(String.format("Значения элементов ключа должны быть от " +
                            "0 до " + (m - 1) + " включительно"));
                }
            }
            for (int i = 0; i < values.split(" ").length - 1; i++) {
                for (int j = i + 1; j < values.split(" ").length; j++) {
                    if (values.split(" ")[i].equals(values.split(" ")[j])) {
                        throw new NumberFormatException(String.format("Ключ должен пробегать все значения " +
                                "от 0 до " + (m - 1)));
                    }
                }
            }
            key = Arrays.stream(values.split(" ")).map(Integer::parseInt).collect(Collectors.toList());

            System.out.printf("Введите координаты пустых ячеек (формат x,y x,y...) строки x < %d, " +
                    "столбцы y < %d, не более %d пар: ", n, m, n * m - 1);
            String[] inKeyTwo = sc.nextLine().split(" ");
            if (inKeyTwo.length > n * m - 1 && m != 1 && n != 1) {
                throw new NumberFormatException(String.format("Количетсво пар должно быть меньше %d", n * m));
            }
            for (int i = 0; i < inKeyTwo.length; i++) {
                keyTwo.add(Integer.parseInt(inKeyTwo[i].split(",")[0]));
                keyTwo.add(Integer.parseInt(inKeyTwo[i].split(",")[1]));
                if (Integer.parseInt(inKeyTwo[i].split(",")[0]) >= n) {
                    throw new NumberFormatException(String.format("Значение строки не должно превышать %d", n - 1));
                }
                if (Integer.parseInt(inKeyTwo[i].split(",")[1]) >= m) {
                    throw new NumberFormatException(String.format("Значение столбца не должно превышать %d", m - 1));
                }
            }
            System.out.print("Введите сообщение для шифрования: ");
            String message = sc.nextLine();
            //          END INPUT       //
            //============//============//

            message = enc(message, n, m);
            System.out.printf("Зашифрованное сообщение: %s", message);
            tableList.clear();

            message = dec(message, n, m);
            System.out.printf("\nРасшифрованное сообщение: %s", message);
        }
        catch (NumberFormatException e) {
            System.out.println("Ошибка! " + e.getMessage());
            System.out.println("Перезапустите программу и попытайтесь снова.");
        }
        catch (IndexOutOfBoundsException e) {
            System.out.println("Ошибка! " + e.getMessage());
            System.out.println("Перезапустите программу и попытайтесь снова.");
        }
    }
}