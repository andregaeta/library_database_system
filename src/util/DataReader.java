package util;

import dataObj.*;

import java.io.*;

/**
 * Classe para leitura e carregamento em memória dos dados CSV
 */
public class DataReader {
    private static final String authorsDataPath = "./src/resources/authorsFull.csv";
    private static final String booksDataPath = "./src/resources/books.csv";
    private static final String borrowsDataPath = "./src/resources/borrows.csv";
    private static final String studentsDataPath = "./src/resources/students.csv";
    private static final String typesDataPath = "./src/resources/types.csv";
    private static final String queueDataPath = "./src/resources/queue.csv";

    /**
     * Lê e carrega os autores em memória
     */
    public static void loadAuthors(){
        try(BufferedReader reader = new BufferedReader(new FileReader(authorsDataPath))){
            reader.readLine();
            String line;
            while((line = reader.readLine()) != null){
                Author.createFromString(line);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
    }
    /**
     * Lê e carrega os alunos em memória
     */
    public static void loadStudents(){
        try(BufferedReader reader = new BufferedReader(new FileReader(studentsDataPath))){
            reader.readLine();
            String line;
            while((line = reader.readLine()) != null){
                Student.createFromString(line);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
    }
    /**
     * Lê e carrega os livros em memória
     */
    public static void loadBooks(){
        try(BufferedReader reader = new BufferedReader(new FileReader(booksDataPath))){
            reader.readLine();
            String line;
            while((line = reader.readLine()) != null){
                Book.createFromString(line);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
    }
    /**
     * Lê e carrega os empréstimos em memória
     */
    public static void loadBorrowData(){
        try(BufferedReader reader = new BufferedReader(new FileReader(borrowsDataPath))){
            reader.readLine();
            String line;
            while((line = reader.readLine()) != null){
                BorrowData.createFromString(line);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
    }
    /**
     * Lê e carrega as filas de empréstimo em memória
     */
    public static void loadQueue(){
        try(BufferedReader reader = new BufferedReader(new FileReader(queueDataPath))){
            reader.readLine();
            String line;
            while((line = reader.readLine()) != null){
                BorrowQueue.createFromString(line);
            }
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
        catch (IOException e){
            e.printStackTrace();
            System.out.println("Erro ao ler o arquivo.");
        }
    }
    /**
     * Lê e carrega todos os dados
     */
    public static void loadAll(){
        loadBooks();
        loadStudents();
        loadAuthors();
        loadBorrowData();
        loadQueue();
    }

}
