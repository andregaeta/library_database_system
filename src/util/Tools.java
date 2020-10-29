package util;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;


/**
 *
 * Classe para ajudar com funções auxiliares
 *
 */

public class Tools{

    /**
     *
     * Valida e retorna um float.
     *
     */

    public static float getFloat(){
        boolean sucesso = false;
        float f = 0;
        while (!sucesso) {
            try {
                Scanner scanner = new Scanner(System.in);
                f = scanner.nextFloat();
                sucesso = true;
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Input invalido, tente novamente. Float esperado.");
            }
        }
        return f;
    }

    /**
     *
     * Valida e retorna um int.
     *
     */
    public static int getInt(){
        boolean sucesso = false;
        int i = 0;
        while (!sucesso) {
            try {
                Scanner scanner = new Scanner(System.in);
                i = scanner.nextInt();
                sucesso = true;
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Input invalido, tente novamente. Int esperado.");
            }
        }
        return i;
    }
    /**
     *
     * Valida e retorna uma string.
     *
     */
    public static String getString(){
        boolean sucesso = false;
        String s = null;
        while (!sucesso) {
            try {
                Scanner scanner = new Scanner(System.in);
                s = scanner.nextLine();
                sucesso = true;
            } catch (InputMismatchException e) {
                System.out.println("Input invalido, tente novamente. String esperada.");
            }
        }
        return s;
    }

    /**
     * Valida e retorna uma data e hora
     * @return data e hora em questão
     */
    public static LocalDateTime getDateTime(){
        System.out.println("Digite a data e hora. Formato esperado: \"yyyy-MM-dd HH:mm\".");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = null;

        while(date == null) {
            try {
                String string = getString();
                date = LocalDateTime.parse(string, formatter);
            }
            catch (DateTimeParseException e){
                System.out.println("Data inválida. Tente novamente. Formato esperado: \"yyyy-MM-dd HH:mm\".");
            }
        }
        return date;
    }

    /**
     * Valida e retorna uma data
     * @return data em questão
     */
    public static LocalDate getDate(){
        System.out.println("Digite a data. Formato esperado: \"yyyy-MM-dd\".");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = null;
        while(date == null) {
            try {
                String string = getString();
                date = LocalDate.parse(string, formatter);
            }
            catch (DateTimeParseException e){
                System.out.println("Data inválida. Tente novamente. Formato esperado: \"yyyy-MM-dd\".");
            }
        }
        return date;
    }


}