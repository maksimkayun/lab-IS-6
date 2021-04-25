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
        int quantity = length / (n * m - (keyTwo.size() / 2));
        if (length % (n * m - (keyTwo.size() / 2)) != 0) {
            quantity++;
        }
        for (int i = 0; i < quantity; i++) {
            tableList.add(new String[n][m]);
        }
    }

    public static void setSupport(String message, int n, int m) {
        int len = message.length();
        for (int k = 0; k < tableList.size(); k++) {
            if (k != tableList.size() - 1 && tableList.size() > 1) {
                int counter = 0;
                for (int i = 0; i < m; i++) {
                    for (int j = 1; j < keyTwo.size(); j=j+2) {
                        if(keyTwo.get(j)==i){
                            counter=counter+1;
                        }
                    }
                }
                len -= n * m - (keyTwo.size() / 2);
                support.add(n - counter);
            }
            else {
                int quantity = n * m - (keyTwo.size() / 2) - len; // количество оставшихся пустых ячеек
                String[][] buffer = new String[n][m];
                for (int j = 0; j < keyTwo.size(); j += 2) {
                    buffer[keyTwo.get(j)][keyTwo.get(j+1)] = String.valueOf((char) 142);
                }
                for (int i = n - 1; i > -1 && quantity > 0; i--) {
                    for (int j = m - 1; j > -1 && quantity > 0; j--) {
                        if (buffer[i][j] == null) {
                            buffer[i][j] = "";
                            quantity--;
                        }
                    }
                }
                for (int i = 0; i < m; i++) {
                    int counter = 0;
                    for (int j = 0; j < n; j++) {
                        if (buffer[j][i] == null) {
                            counter++;
                        }
                    }
                    support.add(counter);
                }

                tableList.set(tableList.size() - 1,  buffer);
            }
        }
    }

    public static void setKeyTwo() {
        char symbol = 142;
        String ch = String.valueOf(symbol);

        for (int i = 0; i < tableList.size(); i++) {
            for (int j = 0; j < keyTwo.size(); j += 2) {
                tableList.get(i)[keyTwo.get(j)][keyTwo.get(j+1)] = ch;
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
            //String[][] table = new String[n][m];
            for (int j = 0,number=1; number <= m; j++) {
                if(key.get(j)==number){
                    String[] columTable = new String[support.get(j+i*m)];
                    for (int k = 0; k < support.get(j+i*m); k++) {
                        columTable[k]= String.valueOf(message.charAt(k));
                    }
                    message = message.substring(support.get(j+i*m));
                    for (int k = 0, s = 0; s < columTable.length; k++) {
                        if (tableList.get(i)[k][j] == null) {
                            tableList.get(i)[k][j]=columTable[s];
                            s++;
                        }
                        if (tableList.get(i)[k][j].equals(String.valueOf((char) 142))) {
                            continue;
                        }
                        if (tableList.get(i)[k][j].equals("")) {
                            break;
                        }
                    }
                    number++;

                }
                if(j==m-1){
                    j=-1;
                }
            }
            //tabletList.add(table);
        }
        for (int i = 0; i < tableList.size(); i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < m; k++) {
                    if (tableList.get(i)[k][j].equals(String.valueOf((char) 142)))
                        continue;
                    output += tableList.get(i)[j][k];
                }
            }
        }

        return output;
    }

    public static void main(String[] args) {
        //============//============//
        //           INPUT          //
        System.out.print("Введите кол-во столбцов таблиц: ");
        int m = Integer.parseInt(sc.nextLine());

        System.out.print("Введите кол-во строк таблиц: ");
        int n = Integer.parseInt(sc.nextLine());

        System.out.printf("Введите код для шифрования (количество = %d): ", m);
        String values = sc.nextLine();
        key = Arrays.stream(values.split(" ")).map(Integer::parseInt).collect(Collectors.toList());

        System.out.printf("Введите координаты пустых ячеек (формат x,y x,y...) строки x < %d, " +
                "столбцы y < %d, не более %d пар: ", n - 1, m - 1, n * m);
        String[] inKeyTwo = sc.nextLine().split(" ");
        for (int i = 0; i < inKeyTwo.length; i++) {
            keyTwo.add(Integer.parseInt(inKeyTwo[i].split(",")[0]));
            keyTwo.add(Integer.parseInt(inKeyTwo[i].split(",")[1]));
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
}