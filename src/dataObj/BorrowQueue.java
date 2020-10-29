package dataObj;

import event.EventManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class BorrowQueue implements FormattableToCSV{
    private static HashMap<Integer, BorrowQueue> queueById = new HashMap<>();
    private int queueId;
    private int bookId;
    private ArrayList<Integer> studentIds = new ArrayList<>();
    public BorrowQueue(int queueId, int bookId) {
        this.setBookId(bookId);
        this.setQueueId(queueId);
    }

    /**
     * Cria uma fila de espera para o livro fornecido
     * @param book livro em questão
     * @return objeto com informações da fila de espera
     */
    public static BorrowQueue createQueueForBook(int book){
        int bookId = book;
        int queueId;
        if(getQueueById().size() == 0) queueId = 1;
        else queueId = Collections.max(getQueueById().keySet()) + 1;
        BorrowQueue queue = new BorrowQueue(queueId, bookId);
        getQueueById().put(queueId, queue);

        EventManager.getInstance().getNewQueueEvent().trigger(queue);

        return queue;
    }

    /**
     * Carrega a fila em memória a partir de uma string lida externamente
     * @param str String representando a fila
     * @return objeto da fila interpretado da String fornecida
     */
    public static BorrowQueue createFromString(String str){
        String[] lines = str.split("\",");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace("\"", "");
            lines[i] = lines[i].trim();
        }

        int queueId = Integer.parseInt(lines[0]);
        int bookId = Integer.parseInt(lines[1]);
        BorrowQueue queue = new BorrowQueue(queueId, bookId);

        for(int i = 2; i < lines.length; i++){
            queue.getStudentIds().add(Integer.parseInt(lines[i]));
        }
        Book.getBookById().get(bookId).setQueue(queue);
        getQueueById().put(queueId, queue);
        return queue;
    }

    /**
     * Adiciona aluno à fila
     * @param studentId id do aluno
     */
    public void addStudentToQueue(int studentId){
        getStudentIds().add(studentId);
        EventManager.getInstance().getUpdateQueueEvent().trigger(null);
    }

    /**
     * Remove aluno da fila
     * @param studentId id do aluno
     */
    public void removeStudentFromQueue(int studentId){
        getStudentIds().remove(Integer.valueOf(studentId));
        EventManager.getInstance().getUpdateQueueEvent().trigger(null);
    }

    /**
     * Destrói a si mesmo
     */
    public void removeQueue(){
        getQueueById().remove(this);
        EventManager.getInstance().getUpdateQueueEvent().trigger(null);
    }

    @Override
    public String formatToCSV(){
        String string = "\"" + this.getQueueId() + "\",\"" + this.getBookId();
        for(int id : getStudentIds()){
            string += "\",\"" + id;
        }
        string += "\"\n";
        return string;
    }

    //getters e setters

    public static HashMap<Integer, BorrowQueue> getQueueById() {
        return queueById;
    }

    public static void setQueueById(HashMap<Integer, BorrowQueue> queueById) {
        BorrowQueue.queueById = queueById;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public ArrayList<Integer> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(ArrayList<Integer> studentIds) {
        this.studentIds = studentIds;
    }
}
