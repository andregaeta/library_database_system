package util;

import dataObj.*;
import event.EventListener;
import event.EventManager;
import event.EventType;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Classe para exportação e atualização de dados CSV baseado nas mudanças feitas em runtime
 */

public class DataWriter implements EventListener {
    private static DataWriter instance;

    private DataWriter(){
        EventManager.getInstance().getReturnBorrowEvent().subscribe(this);
        EventManager.getInstance().getNewBorrowEvent().subscribe(this);
        EventManager.getInstance().getNewAuthorEvent().subscribe(this);
        EventManager.getInstance().getNewBookEvent().subscribe(this);
        EventManager.getInstance().getNewStudentEvent().subscribe(this);
        EventManager.getInstance().getNewQueueEvent().subscribe(this);
        EventManager.getInstance().getUpdateQueueEvent().subscribe(this);
    }
    //singleton
    public static DataWriter getInstance(){
        if(instance == null)
            instance = new DataWriter();
        return instance;
    }
    private static final String authorsDataPath = "./src/resources/authorsFull.csv";
    private static final String booksDataPath = "./src/resources/books.csv";
    private static final String borrowsDataPath = "./src/resources/borrows.csv";
    private static final String studentsDataPath = "./src/resources/students.csv";
    private static final String typesDataPath = "./src/resources/types.csv";
    private static final String queuesDataPath = "./src/resources/queue.csv";


    /**
     * Gere o funcionamento da classe quando um evento ocorre
     * @param type tipo de evento
     * @param data informações do evento
     * @param <T> tipo das informações do evento
     */
    @Override
    public <T> void onTrigger(EventType type, T data) {

        switch (type){
            case NEW_AUTHOR:
                Author author = (Author) data;
                appendAuthor(author);
                break;
            case NEW_BOOK:
                Book book = (Book) data;
                appendBook(book);
                break;
            case NEW_STUDENT:
                Student student = (Student) data;
                appendStudent(student);
                break;
            case NEW_BORROW:
                BorrowData borrowData = (BorrowData) data;
                appendBorrow(borrowData);
                break;
            case NEW_QUEUE:
                BorrowQueue queue = (BorrowQueue) data;
                appendQueue(queue);
                break;
            case RETURN_BORROW:
                updateBorrow();
                break;
            case UPDATE_QUEUE:
                updateQueue();
                break;
        }
    }

    /**
     * Atualiza o CSV das filas de empréstimo
     */
    private void updateQueue(){
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(queuesDataPath))){
            StringBuilder sb = new StringBuilder("\"queueId\",\"BookId\",\"StudentId\",\"StudentId\"...\n");
            for(BorrowQueue queue : BorrowQueue.getQueueById().values()){
                sb.append(queue.formatToCSV());
            }
            bw.write(sb.toString());
        }
        catch (IOException e){
            System.out.println("Não foi possível gerar o arquivo de autores.");
        }
    }
    /**
     * Atualiza o CSV dos empréstimos
     */
    private void updateBorrow() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(borrowsDataPath))){
            StringBuilder sb = new StringBuilder("\"borrowId\",\"studentId\",\"bookId\",\"takenDate\",\"broughtDate\"");
            for(BorrowData borrow : BorrowData.getBorrowDataById().values()){
                sb.append(borrow.formatToCSV());
            }
            bw.write(sb.toString());
        }
        catch (IOException e){
            System.out.println("Não foi possível gerar o arquivo de autores.");
        }
    }
    /**
     * Adiciona uma nova fila de empréstimo ao CSV
     */
    private void appendQueue(BorrowQueue queue) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(queuesDataPath,true))){
            bw.write(queue.formatToCSV());
        }
        catch (IOException e){

        }
    }
    /**
     * Adiciona um novo empréstimo ao CSV
     */
    private void appendBorrow(BorrowData borrowData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(borrowsDataPath,true))){
            bw.write(borrowData.formatToCSV());
        }
        catch (IOException e){

        }
    }
    /**
     * Adiciona um novo aluno ao CSV
     */
    private void appendStudent(Student student) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(studentsDataPath,true))){
            bw.write(student.formatToCSV());
        }
        catch (IOException e){

        }
    }
    /**
     * Adiciona um novo livro ao CSV
     */
    private void appendBook(Book book) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(booksDataPath,true))){
            bw.write(book.formatToCSV());
        }
        catch (IOException e){

        }
    }
    /**
     * Adiciona um novo autor ao CSV
     */
    private void appendAuthor(Author author) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(authorsDataPath,true))){
            bw.write(author.formatToCSV());
        }
        catch (IOException e){

        }
    }


}
