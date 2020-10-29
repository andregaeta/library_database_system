package dataObj;

import event.EventManager;
import util.Tools;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Classe para representar um autor
 *
 */
public class Author implements FormattableToCSV{
    private int authorId;

    private String name;
    private String surname;

    private static HashMap<Integer, Author> authorById = new HashMap<>();

    public Author(int authorId, String name, String surname) {
        this.setAuthorId(authorId);
        this.setName(name);
        this.setSurname(surname);

    }

    /**
     * Carrega o autor em memória a partir de uma string lida externamente
     * @param str String representando o autor
     * @return objeto autor interpretado da String fornecida
     */
    public static Author createFromString(String str){
        String[] lines = str.split("\",");
        for (int i = 0; i < lines.length; i++) {
            lines[i] = lines[i].replace("\"", "");
            lines[i] = lines[i].trim();
        }

        int id = Integer.parseInt(lines[0]);
        String name = lines[1];
        String surname = lines[2];

        Author author = new Author(id, name, surname);
        getAuthorById().put(id, author);

        return author;
    }

    /**
     * Cria um novo autor, pedindo suas informações pro usuário
     * @return objeto autor criado baseado na entrada fornecida
     */
    public static Author createFromScratch(){
        System.out.println("Registrando novo autor.");
        int id = getMaxId() + 1;
        System.out.println("Digite o nome.");
        String name = Tools.getString();
        System.out.println("Digite o sobrenome.");
        String surname = Tools.getString();

        Author author = new Author(id, name, surname);
        getAuthorById().put(id, author);

        EventManager.getInstance().getNewAuthorEvent().trigger(author);

        return author;
    }

    /**
     * Cria um autor a partir de um nome e sobrenome
     * @param name nome do autor
     * @param surname sobrenome do autor
     * @return objeto autor criado
     */
    public static Author createAuthorFromName(String name, String surname){
        int id = getMaxId() + 1;

        Author author = new Author(id, name, surname);
        getAuthorById().put(id, author);

        EventManager.getInstance().getNewAuthorEvent().trigger(author);

        return author;
    }

    /**
     * retorna o maior ID atual entre todos os autores
     * @return o maior ID atual entre todos os autores
     */
    public static int getMaxId(){
        return Collections.max(getAuthorById().keySet());
    }

    /**
     * Procura e retorna o ID de um autor a partir de um nome e sobrenome. Retorna -1 caso não seja encontrado
     * @param name nome do autor
     * @param surname sobrenome do autor
     * @return o id do autor encontrado ou -1 se não for encontrado
     */
    public static int findAuthorId(String name, String surname){
        Iterator iterator = getAuthorById().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry pair = (Map.Entry)iterator.next();
            Author author = (Author) pair.getValue();
            if(author.getName().toLowerCase().replace(" ", "").equals(name.toLowerCase().replace(" ", "")) &&
               author.getSurname().toLowerCase().replace(" ", "").equals(surname.toLowerCase().replace(" ", ""))) {
                return (int) pair.getKey();
            }
        }
        return -1;
    }



    @Override
    public String formatToCSV(){
        String string = "\"" + this.getAuthorId() + "\"," + "\"" + this.getName() + "\"," + "\"" + this.getSurname() + "\"" + "\n";
        return string;
    }

    //getters e setters

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public static HashMap<Integer, Author> getAuthorById() {
        return authorById;
    }

    public static void setAuthorById(HashMap<Integer, Author> authorById) {
        Author.authorById = authorById;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
