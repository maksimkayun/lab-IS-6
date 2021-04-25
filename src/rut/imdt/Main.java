package rut.imdt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    private static Scanner sc = new Scanner(System.in);
    private static List<Integer> key = new ArrayList<>();
    private static List<Integer> keyTwo = new ArrayList<>();
    private static List<String[][]> tableList = new ArrayList<>();

    public static void createTables(String message, int n, int m) {
        int length = message.length();
        int quantity = length / (n * m);
        if (length % (n * m) != 0) {
            quantity++;
        }
        quantity++;
        for (int i = 0; i < quantity; i++) {
            tableList.add(new String[n][m]);
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
    }

    public static String enc(String message, int n, int m) {
        String output = "";
        createTables(message, m, n);
        setKeyTwo();

        return output;
    }

    public static String dec(String message, int n, int m) {
        String output = "";
        createTables(message, m, n);
        setKeyTwo();

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
