package util;

import dataObj.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe para análise e consulta dos dados do programa
 */

public class DataAnalyzer {

    /**
     * Checa os N últimos empréstimos iniciados
     * @param n quantidade empréstimos
     * @return Lista com os empréstimos
     */
    public static ArrayList<BorrowData> checkLastestBorrows(int n){
        ArrayList<BorrowData> list = new ArrayList<>();
        BorrowData[] data = BorrowData.getBorrowDataById().values().toArray(new BorrowData[0]);
        for(int i = 0; i < data.length; i++){
            boolean added = false;
            for(int j = 0; j < list.size(); j++){
                if(data[i].getTakenDate().isAfter(list.get(j).getTakenDate())){
                    list.add(j, data[i]);
                    added = true;
                    if(list.size() > n) list.remove(n-1);
                    break;
                }
            }
            if(i < n && !added) {
                list.add(data[i]);
            }
        }
        printResults(list);
        return list;
    }

    /**
     * Checa os empréstimos em aberto iniciados mais de N dias atrás
     * @param n quantidade de dias
     * @return Lista com os empréstimos
     */
    public static ArrayList<BorrowData> checkOpenBorrowsPastDays(int n){
        ArrayList<BorrowData> list = new ArrayList<>();
        BorrowData[] data = BorrowData.getBorrowDataById().values().toArray(new BorrowData[0]);
        LocalDate today = Tools.getDate();
        for(int i = 0; i < data.length; i++){
            if(data[i].getBroughtDate() != null) continue;
            if(data[i].getTakenDate().toLocalDate().isBefore(today.minusDays(n))){
                list.add(data[i]);
            }
        }
        printResults(list);
        return list;
    }

    /**
     * Checa os empréstimos fechados completados mais de N dias atrás
     * @param n quantidade de dias
     * @return Lista com os empréstimos
     */
    public static ArrayList<BorrowData> checkClosedBorrowsPastDays(int n){
        ArrayList<BorrowData> list = new ArrayList<>();
        BorrowData[] data = BorrowData.getBorrowDataById().values().toArray(new BorrowData[0]);
        LocalDate today = Tools.getDate();
        for(int i = 0; i < data.length; i++){
            if(data[i].getBroughtDate() == null) continue;
            if(data[i].getBroughtDate().toLocalDate().isBefore(today.minusDays(n))){
                list.add(data[i]);
            }
        }
        printResults(list);
        return list;
    }

    /**
     * Checa os empréstimos (abertos e fechados) iniciados mais de N dias atrás
     * @param n quantidade de dias
     * @return Lista com os empréstimos
     */
    public static ArrayList<BorrowData> checkAllBorrowsPastDays(int n){
        ArrayList<BorrowData> list = new ArrayList<>();
        BorrowData[] data = BorrowData.getBorrowDataById().values().toArray(new BorrowData[0]);
        LocalDate today = Tools.getDate();
        for(int i = 0; i < data.length; i++){
            if(data[i].getTakenDate().toLocalDate().isBefore(today.minusDays(n))){
                list.add(data[i]);
            }
        }
        printResults(list);
        return list;
    }

    /**
     * Checa os N alunos com maior número de empréstimos
     * @param n número de alunos
     * @return lista com os alunos e seus números de empréstimos
     */
    public static ArrayList<Map.Entry<? extends FormattableToCSV, Integer>> checkStudentsWithMostBorrows(int n){
        HashMap<Student, Integer> borrowsByStudent = new HashMap<>();
        for(BorrowData borrow : BorrowData.getBorrowDataById().values()){
            Student student = Student.getStudentById().get(borrow.getStudentId());
            if(borrowsByStudent.containsKey(student)){
                int currentBorrows = borrowsByStudent.get(student);
                borrowsByStudent.put(student, currentBorrows + 1);
            }
            else
                borrowsByStudent.put(student, 1);
        }
        ArrayList<Map.Entry<? extends FormattableToCSV, Integer>> list = new ArrayList<>();
        int i = 0;
        for(Map.Entry pair : borrowsByStudent.entrySet()){
            boolean added = false;
            for(int j = 0; j < list.size(); j++){
                if((int)pair.getValue() > (int)list.get(j).getValue()){
                    list.add(j, pair);
                    added = true;
                    if(list.size() > n) list.remove(n-1);
                    break;
                }
            }
            if(i < n && !added) {
                list.add(pair);
            }
            i++;
        }
        printPair(list);
        return list;
    }
    /**
     * Checa os N livros com maior número de empréstimos
     * @param n número de livros
     * @return lista com os livros e seus números de empréstimos
     */
    public static ArrayList<Map.Entry<? extends FormattableToCSV, Integer>> checkBooksWithMostBorrows(int n){
        HashMap<Book, Integer> borrowsByBook = new HashMap<>();
        for(BorrowData borrow : BorrowData.getBorrowDataById().values()){
            Book book = Book.getBookById().get(borrow.getBookId());
            if(borrowsByBook.containsKey(book)){
                int currentBorrows = borrowsByBook.get(book);
                borrowsByBook.put(book, currentBorrows + 1);
            }
            else
                borrowsByBook.put(book, 1);
        }
        ArrayList<Map.Entry<? extends FormattableToCSV, Integer>> list = new ArrayList<>();
        int i = 0;
        for(Map.Entry pair : borrowsByBook.entrySet()){
            boolean added = false;
            for(int j = 0; j < list.size(); j++){
                if((int)pair.getValue() > (int)list.get(j).getValue()){
                    list.add(j, pair);
                    added = true;
                    if(list.size() > n) list.remove(n-1);
                    break;
                }
            }
            if(i < n && !added) {
                list.add(pair);
            }
            i++;
        }
        printPair(list);
        return list;
    }
    /**
     * Checa os N autores com maior número de empréstimos feitos por alunos em livros escritos em seu nome
     * @param n número de autores
     * @return lista com os autores e seus números de empréstimos
     */
    public static ArrayList<Map.Entry<? extends FormattableToCSV, Integer>> checkAuthorsWithMostBorrows(int n){
        HashMap<Author, Integer> borrowsByAuthor = new HashMap<>();
        for(BorrowData borrow : BorrowData.getBorrowDataById().values()){
            Author author = Author.getAuthorById().get(Book.getBookById().get(borrow.getBookId()).getAuthorId());
            if(borrowsByAuthor.containsKey(author)){
                int currentBorrows = borrowsByAuthor.get(author);
                borrowsByAuthor.put(author, currentBorrows + 1);
            }
            else
                borrowsByAuthor.put(author, 1);
        }
        ArrayList<Map.Entry<? extends FormattableToCSV, Integer>> list = new ArrayList<>();
        int i = 0;
        for(Map.Entry pair : borrowsByAuthor.entrySet()){
            boolean added = false;
            for(int j = 0; j < list.size(); j++){
                if((int)pair.getValue() > (int)list.get(j).getValue()){
                    list.add(j, pair);
                    added = true;
                    if(list.size() > n) list.remove(n-1);
                    break;
                }
            }
            if(i < n && !added) {
                list.add(pair);
            }
            i++;
        }
        printPair(list);
        return list;
    }

    /**
     * Checa os N estilos literários com maior número de empréstimos
     * @param n número de estilos
     * @return lista com os estilos e seus números de empréstimos
     */
    public static ArrayList<Map.Entry<Genre, Integer>> checkGenreWithMostBorrows(int n){
        HashMap<Genre, Integer> borrowsByGenre = new HashMap<>();
        for(BorrowData borrow : BorrowData.getBorrowDataById().values()){
            Genre genre = Book.getBookById().get(borrow.getBookId()).getGenre();
            if(borrowsByGenre.containsKey(genre)){
                int currentBorrows = borrowsByGenre.get(genre);
                borrowsByGenre.put(genre, currentBorrows + 1);
            }
            else
                borrowsByGenre.put(genre, 1);
        }
        ArrayList<Map.Entry<Genre, Integer>> list = new ArrayList<>();
        int i = 0;
        for(Map.Entry pair : borrowsByGenre.entrySet()){
            boolean added = false;
            for(int j = 0; j < list.size(); j++){
                if((int)pair.getValue() > (int)list.get(j).getValue()){
                    list.add(j, pair);
                    added = true;
                    if(list.size() > n) list.remove(n-1);
                    break;
                }
            }
            if(i < n && !added) {
                list.add(pair);
            }
            i++;
        }
        for(Map.Entry pair : list){
            System.out.println(pair.getKey().toString());
            System.out.println("Valor: " + pair.getValue());
        }
        return list;
    }







    public static void printResults(ArrayList<? extends FormattableToCSV> list){
        for (FormattableToCSV item : list){
            System.out.println(item.formatToCSV());
        }
    }

    public static void printPair(ArrayList<Map.Entry<? extends FormattableToCSV, Integer>> pairList){
        for (Map.Entry pair : pairList){
            FormattableToCSV formattable = (FormattableToCSV) pair.getKey();
            System.out.println(formattable.formatToCSV() + "Valor: " + pair.getValue());
        }
    }
}
