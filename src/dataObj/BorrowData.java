package dataObj;

import event.EventManager;
import util.Tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Classe para representar informações de um empréstimo realizado
 */
public class BorrowData implements FormattableToCSV{
    private int borrowId;
    private int studentId;
    private int bookId;
    private LocalDateTime takenDate;
    private LocalDateTime broughtDate;

    private static HashMap<Integer, BorrowData> borrowDataById = new HashMap<>();

    public BorrowData(int borrowId, int studentId, int bookId, LocalDateTime takenDate, LocalDateTime broughtDate) {
        this.setBorrowId(borrowId);
        this.setStudentId(studentId);
        this.setBookId(bookId);
        this.setTakenDate(takenDate);
        this.setBroughtDate(broughtDate);
    }
    /**
     * Carrega o empréstimo em memória a partir de uma string lida externamente
     * @param str String representando o empréstimo
     * @return objeto empréstimo interpretado da String fornecida
     */
    public static BorrowData createFromString(String str){

        String[] lines = str.split("\",");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace("\"", "");
            lines[i] = lines[i].trim();
        }

        int borrowId = Integer.parseInt(lines[0]);
        int studentId = Integer.parseInt(lines[1]);
        int bookId = Integer.parseInt(lines[2]);
        LocalDateTime takenDate = LocalDateTime.parse(lines[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime broughtDate;
        if(lines[4].isBlank()) broughtDate = null;
        else broughtDate = LocalDateTime.parse(lines[4], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        BorrowData borrowData = new BorrowData(borrowId, studentId, bookId, takenDate, broughtDate);
        getBorrowDataById().put(borrowId, borrowData);
        if(broughtDate == null){
            Book.getBookById().get(bookId).getCurrentBorrows().add(borrowData);
            Student.getStudentById().get(studentId).getCurrentBorrows().add(borrowData);
        }

        return borrowData;
    }

    /**
     * Tenta realizar um empréstimo com os valores fornecidos
     * @return As informações do empréstimo, se realizado
     */
    public static BorrowData attemptToBorrow(){
        System.out.println("Criando novo empréstimo.");
        int borrowId = getMaxId() + 1;
        System.out.println("Digite o nome do aluno.");
        String studentName = Tools.getString();
        System.out.println("Digite o sobrenome do aluno.");
        String studentSurname = Tools.getString();
        int studentId = Student.findStudentId(studentName, studentSurname);
        if(studentId == -1){
            System.out.println("Aluno não encontrado.");
            return null;
        }
        Student student = Student.getStudentById().get(studentId);
        if(student.getCurrentBorrows().size() == 2) {
            System.out.println("O aluno já tem 2 empréstimos no momento.");
            return null;
        }
        System.out.println("Digite o nome do livro.");
        String nameBook = Tools.getString();
        int bookId = Book.findBookId(nameBook);
        if(bookId == -1){
            System.out.println("Livro não encontrado.");
            return null;
        }
        Book book = Book.getBookById().get(bookId);
        if(student.getCurrentBorrows().size() == 1) {
            if(student.getCurrentBorrows().get(0).getBookId() == bookId){
                System.out.println("O aluno já fez o empréstimo de uma cópia desse livro.");
                return null;
            }
        }
        if(book.getCurrentBorrows().size() >= 2) {
            System.out.println("Todas as cópias desse livro já foram emprestadas.");
            System.out.println("Entrando na fila para empréstimo.");
            //book.borrowQueue.add(student);
            book.addToQueue(studentId);
            return null;
        }

        LocalDateTime takenDate = Tools.getDateTime();
        LocalDateTime broughtDate = null;


        BorrowData borrowData = new BorrowData(borrowId, studentId, bookId, takenDate, broughtDate);
        getBorrowDataById().put(borrowId, borrowData);

        EventManager.getInstance().getNewBorrowEvent().trigger(borrowData);
        return borrowData;
    }

    /**
     * Tenta realizar a devolução de um empréstimo do livro
     * @return as informações atualizadas do empréstimo
     */
    public static BorrowData returnBook(){
        System.out.println("Retornando livro.");
        System.out.println("Digite o nome do aluno.");
        String studentName = Tools.getString();
        System.out.println("Digite o sobrenome do aluno.");
        String studentSurname = Tools.getString();
        int studentId = Student.findStudentId(studentName, studentSurname);
        if(studentId == -1){
            System.out.println("Aluno não encontrado.");
            return null;
        }
        Student student = Student.getStudentById().get(studentId);

        System.out.println("Digite o nome do livro.");
        String nameBook = Tools.getString();
        int bookId = Book.findBookId(nameBook);
        if(bookId == -1){
            System.out.println("Livro não encontrado.");
            return null;
        }
        Book book = Book.getBookById().get(bookId);

        boolean found = false;
        for(BorrowData borrowData : student.getCurrentBorrows()){
            if(borrowData.getBookId() == bookId){
                found = true;

            }
        }
        for(BorrowData borrowData : book.getCurrentBorrows()){
            if(borrowData.getStudentId() == studentId){
                found = true;
            }
        }
        if(!found){
            System.out.println("O aluno não tem um empréstimo em aberto desse livro.");
            return null;
        }


        int borrowDataId = BorrowData.findBorrowDataOpenId(studentId, bookId);
        if(borrowDataId == -1){
            System.out.println("Empréstimo não encontrado.");
            return null;
        }
        BorrowData borrowData = BorrowData.getBorrowDataById().get(borrowDataId);
        LocalDateTime now = Tools.getDateTime();

        borrowData.setBroughtDate(now);


        EventManager.getInstance().getReturnBorrowEvent().trigger(borrowData);

        return borrowData;
    }

    /**
     * Cria um novo empréstimo com os dados fornecidos
     * @param studentId id do estudante
     * @param bookId id do livro
     * @param dateTime data atual
     * @return objeto com as informações do empréstimo
     */
    public static BorrowData createBorrowFromIds(int studentId, int bookId, LocalDateTime dateTime){
        int borrowId = getMaxId() + 1;

        LocalDateTime now = dateTime;

        BorrowData borrowData = new BorrowData(borrowId, studentId, bookId, now, null);
        getBorrowDataById().put(borrowId, borrowData);

        EventManager.getInstance().getNewBorrowEvent().trigger(borrowData);
        //data.Student.studentById.get(studentId).registerBorrow(borrowData);
        //data.Book.bookById.get(bookId).registerBorrow(borrowData);

        return borrowData;
    }

    /**
     * Procura e retorna o ID de um empréstimo em aberto com os dados fornecidos, ou -1 se não for encontrado
     * @param studentId id do estudante
     * @param bookId id do livro
     * @return o ID de um empréstimo em aberto com os dados fornecidos, ou -1 se não for encontrado
     */
    public static int findBorrowDataOpenId(int studentId, int bookId){
        Iterator iterator = getBorrowDataById().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            BorrowData borrowData = (BorrowData) pair.getValue();
            if(borrowData.getBroughtDate() != null) continue;
            if((borrowData.getStudentId() == studentId) && borrowData.getBookId() == bookId) {
                return (int) pair.getKey();
            }
        }
        return -1;
    }


    @Override
    public String formatToCSV(){
        if(getBroughtDate() == null) System.out.println("BroughtDate == null : " + this.getBorrowId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        String string = "\"" + this.getBorrowId() + "\",\"" + this.getStudentId() + "\",\"" + this.getBookId() + "\",\"" + getTakenDate().format(formatter);
        if(getBroughtDate() != null) string += "\",\"" + getBroughtDate().format(formatter);
        else string += "\",\"" + "";
        string += "\"\n";
        return string;
    }

    /**
     * retorna o maior ID atual entre todos os empréstimos
     * @return o maior ID atual entre todos os empréstimos
     */
    public static int getMaxId(){
        return Collections.max(getBorrowDataById().keySet());
    }


    //getters e setters

    public static HashMap<Integer, BorrowData> getBorrowDataById() {
        return borrowDataById;
    }

    public static void setBorrowDataById(HashMap<Integer, BorrowData> borrowDataById) {
        BorrowData.borrowDataById = borrowDataById;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getTakenDate() {
        return takenDate;
    }

    public void setTakenDate(LocalDateTime takenDate) {
        this.takenDate = takenDate;
    }

    public LocalDateTime getBroughtDate() {
        return broughtDate;
    }

    public void setBroughtDate(LocalDateTime broughtDate) {
        this.broughtDate = broughtDate;
    }
}
