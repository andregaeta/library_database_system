package dataObj;

import event.EventListener;
import event.EventManager;
import event.EventType;
import util.Tools;

import java.time.Duration;
import java.util.*;

/**
 * Classe para representação de um livro
 */

public class Book implements EventListener, FormattableToCSV{
    private int bookId;
    private String name;
    private int pageCount;
    private int points;
    private int authorId;
    private Genre genre;

    private ArrayList<BorrowData> currentBorrows;
    private BorrowQueue queue;

    private static HashMap<Integer, Book> bookById = new HashMap<>();

    public Book(int bookId, String name, int pageCount, int points, int authorId, Genre genre) {
        this.setBookId(bookId);
        this.setName(name);
        this.setPageCount(pageCount);
        this.setPoints(points);
        this.setAuthorId(authorId);
        this.setGenre(genre);
        this.setCurrentBorrows(new ArrayList<>());
        EventManager.getInstance().getNewBorrowEvent().subscribe(this);
        EventManager.getInstance().getReturnBorrowEvent().subscribe(this);
    }

    /**
     * Carrega o livro em memória a partir de uma string lida externamente
     * @param str String representando o livro
     * @return objeto livro interpretado da String fornecida
     */
    public static Book createFromString(String str) {
        String[] lines = str.split("\",");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace("\"", "");
            lines[i] = lines[i].trim();
        }

        int id = Integer.parseInt(lines[0]);
        String name = lines[1];
        int pageCount = Integer.parseInt(lines[2]);
        int points = Integer.parseInt(lines[3]);
        int authorId = Integer.parseInt(lines[4]);
        Genre genre = Genre.values()[Integer.parseInt(lines[5])];

        Book book = new Book(id, name, pageCount, points, authorId, genre);
        getBookById().put(id, book);

        return book;
    }

    /**
     * Cria um novo livro, pedindo suas informações pro usuário
     * @return objeto livro criado baseado na entrada fornecida
     */
    public static Book createFromScratch(){
        System.out.println("Registrando novo livro.");
        int id = getMaxId() + 1;
        System.out.println("Digite o nome.");
        String name = Tools.getString();
        System.out.println("Digite o número de páginas.");
        int pageCount = Tools.getInt();
        int points = 0;
        System.out.println("Digite o nome do autor.");
        String authorName = Tools.getString();
        System.out.println("Digite o sobrenome do autor.");
        String authorSurname = Tools.getString();
        int authorId = Author.findAuthorId(authorName, authorSurname);
        if(authorId == -1){
            System.out.println("Autor não reconhecido no sistema. Registrando " + authorName + " " + authorSurname + " como novo autor.");
            Author author = Author.createAuthorFromName(authorName, authorSurname);
            authorId = author.getAuthorId();
        }
        System.out.println("Digite o id do gênero.");
        Genre genre = Genre.values()[Tools.getInt()];

        Book book = new Book(id, name, pageCount, points, authorId, genre);
        getBookById().put(id, book);

        EventManager.getInstance().getNewBookEvent().trigger(book);

        return book;
    }
    /**
     * retorna o maior ID atual entre todos os livros
     * @return o maior ID atual entre todos os livros
     */
    public static int getMaxId(){
        return Collections.max(getBookById().keySet());
    }
    /**
     * Procura e retorna o ID de um livro a partir de seu nome. Retorna -1 caso não seja encontrado
     * @param name nome do livro
     * @return o id do livro encontrado ou -1 se não for encontrado
     */
    public static int findBookId(String name){
        Iterator iterator = getBookById().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Book book = (Book) pair.getValue();
            if(book.getName().toLowerCase().replace(" ", "").equals(name.toLowerCase().replace(" ", ""))) {
                return (int) pair.getKey();
            }
        }
        return -1;
    }

    /**
     * Registra um novo empréstimo
     * @param data informações do empréstimo
     */
    private void registerBorrow(BorrowData data){
        if(data.getBookId() != getBookId()) return;
        getCurrentBorrows().add(data);
    }

    /**
     * Adiciona aluno à fila de empréstimos do livro
     * @param studentId o id do aluno
     */
    public void addToQueue(int studentId){
        if(getQueue() == null){
            setQueue(BorrowQueue.createQueueForBook(this.getBookId()));
        }
        getQueue().addStudentToQueue(studentId);
    }

    /**
     * Remove aluno da fila de empréstimos do livro
     * @param studentId o id do aluno
     */
    private void removeFromQueue(int studentId){
        if(getQueue() == null) return;
        getQueue().removeStudentFromQueue(studentId);
        if(getQueue().getStudentIds().size() == 0) resetQueue();
    }

    /**
     * Reseta o à fila atual de empréstimos do livro
     */
    private void resetQueue(){
        getQueue().removeQueue();
        setQueue(null);
    }

    /**
     * Calcula os pontos que serão adicionados ao livro após seu retorno
     * @param data informações do empréstimo
     */
    private void addPointsOnReturn(BorrowData data){
        int days = (int) Duration.between(data.getTakenDate(), data.getBroughtDate()).toDays();
        this.setPoints(this.getPoints() + days);
    }


    @Override
    public String formatToCSV(){
        String string = "\"" + this.getBookId() + "\",\"" + this.getName() + "\",\"" + this.getPageCount() + "\",\"" + this.getPoints() + "\",\"" + this.getAuthorId() + "\",\"" + this.getGenre().ordinal() + "\"\n";

        return string;
    }

    @Override
    public <T> void onTrigger(EventType type, T data) {
        BorrowData borrowData = (BorrowData) data;
        switch(type) {
            case NEW_BORROW:
                if(borrowData.getBookId() != this.getBookId()) return;
                registerBorrow(borrowData);
                break;
            case RETURN_BORROW:
                if(borrowData.getBookId() != this.getBookId()) return;
                addPointsOnReturn(borrowData);
                if(getQueue() == null) return;
                if(getQueue().getStudentIds().get(0) != null){
                    BorrowData.createBorrowFromIds(getQueue().getStudentIds().get(0), getBookId(), borrowData.getBroughtDate());
                    removeFromQueue(getQueue().getStudentIds().get(0));
                }
                break;

        }
    }

    //getters e setters

    public static HashMap<Integer, Book> getBookById() {
        return bookById;
    }

    public static void setBookById(HashMap<Integer, Book> bookById) {
        Book.bookById = bookById;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public ArrayList<BorrowData> getCurrentBorrows() {
        return currentBorrows;
    }

    public void setCurrentBorrows(ArrayList<BorrowData> currentBorrows) {
        this.currentBorrows = currentBorrows;
    }

    public BorrowQueue getQueue() {
        return queue;
    }

    public void setQueue(BorrowQueue queue) {
        this.queue = queue;
    }
}
