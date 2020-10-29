import dataObj.Author;
import dataObj.Book;
import dataObj.Student;
import util.DataAnalyzer;
import util.DataReader;
import util.DataWriter;
import util.Tools;

/**
 * @author André Gaeta <andremg@dcc.ufrj.br>
 */

public class Main {
    public static void main(String[] args) {
        DataWriter.getInstance();
        DataReader.loadAll();
        menuInicial();

    }


    private static void menuInicial(){
        System.out.println("Qual tipo de operação você deseja realizar?");
        System.out.println("1. Cadastrar");
        System.out.println("2. Consultar");


        int escolha = 0;
        escolha = Tools.getInt();
        while(escolha < 1 || escolha > 2){
            System.out.println("Escolha invalida. Digite um numero de 1 a 2.");
            escolha = Tools.getInt();
        }

        switch(escolha) {
            case 1:
                menuCadastro();
                break;
            case 2:
                menuConsulta();
                break;
        }
    }

    private static void menuCadastro() {
        System.out.println("O que deseja cadastrar?");
        System.out.println("1. Aluno");
        System.out.println("2. Livro");
        System.out.println("3. Autor");

        int escolha = 0;
        escolha = Tools.getInt();
        while(escolha < 1 || escolha > 3){
            System.out.println("Escolha invalida. Digite um numero de 1 a 3.");
            escolha = Tools.getInt();
        }

        switch(escolha) {
            case 1:
                Student.createFromScratch();
                break;
            case 2:
                Book.createFromScratch();
                break;
            case 3:
                Author.createFromScratch();
                break;
        }
        System.out.println("Voltando ao menu inicial.");
        menuInicial();
    }

    private static void menuConsulta() {
        System.out.println("O que deseja consultar?");
        System.out.println("1. Consulta aos N últimos empréstimos de livros.");
        System.out.println("2. Consulta aos empréstimos em aberto com mais de N dias.");
        System.out.println("3. Consulta aos empréstimos fechados mais de N dias atrás.");
        System.out.println("4. Consulta aos empréstimos (abertos e fechados) iniciados mais de N dias atrás.");
        System.out.println("5. Consulta aos N estudantes que pegaram mais livros emprestados.");
        System.out.println("6. Consulta aos N livros mais emprestados.");
        System.out.println("7. Consulta aos N autores mais populares.");
        System.out.println("8. Consulta aos estilos literários mais populares.");

        int escolha = 0;
        escolha = Tools.getInt();
        while(escolha < 1 || escolha > 8){
            System.out.println("Escolha invalida. Digite um numero de 1 a 8.");
            escolha = Tools.getInt();
        }

        System.out.println("Digite o valor para N.");
        int escolha2 = 0;
        escolha2 = Tools.getInt();
        while(escolha2 < 1){
            System.out.println("Escolha invalida. Digite um numero maior que 0.");
            escolha2 = Tools.getInt();
        }


        switch(escolha) {
            case 1:
                DataAnalyzer.checkLastestBorrows(escolha2);
                break;
            case 2:
                DataAnalyzer.checkOpenBorrowsPastDays(escolha2);
                break;
            case 3:
                DataAnalyzer.checkClosedBorrowsPastDays(escolha2);
                break;
            case 4:
                DataAnalyzer.checkAllBorrowsPastDays(escolha2);
                break;
            case 5:
                DataAnalyzer.checkStudentsWithMostBorrows(escolha2);
                break;
            case 6:
                DataAnalyzer.checkBooksWithMostBorrows(escolha2);
                break;
            case 7:
                DataAnalyzer.checkAuthorsWithMostBorrows(escolha2);
                break;
            case 8:
                DataAnalyzer.checkGenreWithMostBorrows(escolha2);
                break;
        }
        System.out.println("Voltando ao menu inicial.");
        menuInicial();
    }




}
