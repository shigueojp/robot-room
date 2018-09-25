package robo;

import java.util.LinkedList;

/*********************************************************************/
/** ACH 2002 - Introducao à Análise de Algoritmos                   **/
/** EACH-USP - Segundo Semestre de 2018                             **/
/**                                                                 **/
/** Victor Shigueo Okuhama 10724052                                 **/
/**                                                                 **/
/*********************************************************************/

/**
	Classe que implementa os movimentos do robô.
*/
public class Robo implements IRobo
{
    private static int x = ISala.X_INICIO_ARM;

    private static int y = ISala.Y_FIM_ARM + 1;

    /**
     *
     */
    public Mensageiro mensageiro;

    /**
     *
     */
    public Sala sala;
    
    private boolean backtracked = false;
    
    private int teste;
    
    private LinkedList<java.lang.String> backtracking;

    /**
     *
     */
    public Robo()
    {
        mensageiro = new Mensageiro();
        sala = new Sala();
        backtracking = new LinkedList();
    }

    /**
     *
     * @return
     */
    @Override
    public Mensageiro mensageiro()
    {
        return (this.mensageiro);
    }

    /**
     *
     * @param x
     * @param y
     */
    @Override
    public void adicionaBloco(int x, int y)
    {
        sala.posicao[x][y] = Sala.BLOCO_PRESENTE;
    }

    /**
     *
     * @param x
     * @param y
     */
    @Override
    public void adicionaObstaculo(int x, int y)
    {
        sala.posicao[x][y] = Sala.OBSTACULO_PRESENTE;
    }

    /**
     *
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean buscaBloco(int x, int y)
    {   
        /* Ao buscar o bloco na posicao[x][y] verifica se esta vazia*/
        if (sala.marcadorEm(x, y) == Sala.POSICAO_VAZIA) {
            mensageiro.mensagem(Mensageiro.BUSCA, x, y);
            sala.posicao[x][y] = Sala.MARCA_PRESENTE;
            backtracking.add(Mensageiro.BUSCA + "," + x + ","+ y);
            sala.marcaPosicaoBusca(x, y, Sala.MARCA_PRESENTE);
            teste = 0;
            
            return false;
        }
        
        /* Ao buscar o bloco na posicao[x][y] verifica se existe o bloco*/
        if (sala.marcadorEm(x, y) == Sala.BLOCO_PRESENTE) {
            sala.posicao[x][y] = Sala.MARCA_PRESENTE;
//            sala.marcaPosicaoBusca(x, y, Sala.MARCA_PRESENTE);
            mensageiro.mensagem(Mensageiro.BUSCA, x, y);
            backtracking.add(Mensageiro.BUSCA + "," + x + ","+ y);
            mensageiro.mensagem(Mensageiro.CAPTURA, x, y);
//            System.out.println("PERCORRI NA POSICAO X:" + x + " Y:" + y + " E ACHEI E CAPTUREI UM BLOCO");
            
            return true;
        }

        return false;
    }

    /**
     *
     */
    @Override
    public void buscaBlocos()
    {
        while (!buscaBloco(x, y)) {
            //Verificar se pode ir para o norte, 
            //se nao ir para o leste se possivel, 
            //se nao ir para sul se possivel ou entao ir a esquerda; 
            //se nao backtrack
            if (podeIrNorte()) {
                y++;
            } else if (podeIrLeste()) {
                x++;
            } else if (podeIrSul()) {
                y--;
            } else if (podeIrOeste()) {
                x--;                  
            } else {
                if (!backtracking()){
                    mensageiro.msgNaoAchou();
                    
                    return;
                }          
            }
        }
            retornarBase();
            
            if (guardaBloco())
                novaBusca();
            else
                mensageiro.msgFim();
    }

    /**
     *
     * @return
     */
    @Override
    public boolean guardaBloco()
    {
        LinkedList armazenagemX = new LinkedList();
        LinkedList armazenagemY = new LinkedList();
        
        for (int row = 0; row <= Sala.X_FIM_ARM; row ++) {
            for (int col = 0; col <= Sala.Y_FIM_ARM; col++) {
                if (sala.posicao[col][row] == Sala.POSICAO_VAZIA) {                    
                    armazenagemX.add(col);
                    armazenagemY.add(row);
                }
            }
        }

        if(armazenagemX.isEmpty() && armazenagemY.isEmpty())
            return false;
        
        int xARM = (int) armazenagemX.pop();
        int yARM = (int) armazenagemY.pop();
        mensageiro.mensagem(Mensageiro.ARMAZENAGEM, xARM, yARM);
        sala.marcaPosicaoArmazenagem(xARM, yARM);
        
        return !(armazenagemX.isEmpty() && armazenagemY.isEmpty()); 
    }

    /**
     *
     */
    @Override
    public void novaBusca()
    {
        sala.removeMarcador(Sala.MARCA_PRESENTE);
        buscaBlocos();
    }

    private boolean podeIrNorte()
    {
        /*Se tiver obstaculo faça isso */
        if (sala.posicaoBuscaValida(x, y + 1) 
                && sala.marcadorEm(x, y + 1) == Sala.OBSTACULO_PRESENTE) {
            if (!backtracked) {
                mensageiro.mensagem(Mensageiro.OBSTACULO, x, y + 1);
            }
            
            return false;
        }
        
        /*Se tiver posicao vaga faça isso */
        if (sala.posicaoBuscaValida(x, y + 1) 
                && (sala.marcadorEm(x, y + 1) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x, y + 1) == Sala.BLOCO_PRESENTE)){
            backtracked = false;
//            System.out.println("Fui para NORTE (Y+1) X: " + x + "Y: " + y);
            return true;
        }
        
        return sala.posicaoBuscaValida(x, y + 1) 
                && (sala.marcadorEm(x, y + 1) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x, y + 1) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrLeste()
    {
        /*Se tiver obstaculo faça isso */
        if (sala.posicaoBuscaValida(x + 1, y) 
                && sala.marcadorEm(x + 1, y) == Sala.OBSTACULO_PRESENTE) {
            if (!backtracked) {  
                mensageiro.mensagem(Mensageiro.OBSTACULO, x + 1, y);             
            }
            
            if (backtracked && teste < 3) {
                mensageiro.mensagem(Mensageiro.OBSTACULO, x + 1, y);
                teste++;
            }
            
            return false;
        }
        /*Se tiver posicao vaga faça isso */
        if (sala.posicaoBuscaValida(x + 1, y) 
                && (sala.marcadorEm(x + 1, y) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x + 1, y) == Sala.BLOCO_PRESENTE)){
            backtracked = false;
//            System.out.println("Fui para Leste (x+1) X: " + x + "Y: " + y);
            return true;
        }
        
        
        
        return sala.posicaoBuscaValida(x + 1, y) 
                && (sala.marcadorEm(x + 1, y) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x + 1, y) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrSul()
    {
        /*Se tiver obstaculo faça isso */
        if (sala.posicaoBuscaValida(x, y - 1) 
                && sala.marcadorEm(x, y - 1) == Sala.OBSTACULO_PRESENTE) {
            if (!backtracked) {
               mensageiro.mensagem(Mensageiro.OBSTACULO, x, y - 1);
            }
            
            if (backtracked && teste < 3) {
                mensageiro.mensagem(Mensageiro.OBSTACULO, x, y - 1);
                teste++;
            }

            return false;
        }
        /*Se tiver posicao vaga faça isso */
        if (sala.posicaoBuscaValida(x, y - 1) 
                && (sala.marcadorEm(x, y - 1) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x, y - 1) == Sala.BLOCO_PRESENTE)){
            backtracked = false;
//            System.out.println("Fui para OESTE (x-1) X: " + x + "Y: " + y);
            return true;
        }
        
        return sala.posicaoBuscaValida(x, y - 1) 
                && (sala.marcadorEm(x, y - 1) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x, y - 1) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrOeste()
    {
        /*Se tiver obstaculo faça isso */
        if (sala.posicaoBuscaValida(x - 1, y) 
                && sala.marcadorEm(x - 1, y) == Sala.OBSTACULO_PRESENTE ) {
            if (!backtracked) {
                mensageiro.mensagem(Mensageiro.OBSTACULO, x - 1, y);
            }
            
            if (backtracked && teste < 3) {
                mensageiro.mensagem(Mensageiro.OBSTACULO, x - 1, y);
                teste++;
            }

            return false;
        }      
        /*Se tiver posicao vaga faça isso */
        if (sala.posicaoBuscaValida(x - 1, y) 
                && (sala.marcadorEm(x - 1, y) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x - 1, y) == Sala.BLOCO_PRESENTE)){
            backtracked = false;
            
            return true;
        }
        
        return sala.posicaoBuscaValida(x - 1, y) 
                && (sala.marcadorEm(x - 1, y) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x - 1, y) == Sala.BLOCO_PRESENTE);
    }

    private void retornarBase() 
    { 
//        System.out.println("x:" + x + " y:" + y);
        while ( x != ISala.X_INICIO_ARM || y != ISala.Y_FIM_ARM + 1) {
//            System.out.println("x:" + x + " y:" + y);
            if (!backtracking.isEmpty() 
                    && backtracking.getLast().contentEquals(Mensageiro.BUSCA + "," + x + "," + y)) {
                backtracking.pollLast();
                String penultimoPasso = backtracking.pollLast();
                x = Character.getNumericValue(penultimoPasso.charAt(2));
                y = Character.getNumericValue(penultimoPasso.charAt(4));
//                System.out.println("Voltando para posicao x:" + x + " y:" + y);    
                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
            } else if (!backtracking.isEmpty()) {
//                System.out.println(backtracking);
                String penultimoPasso = backtracking.pollLast();
                x = Character.getNumericValue(penultimoPasso.charAt(2));
                y = Character.getNumericValue(penultimoPasso.charAt(4));
//                System.out.println("Voltando para posicao x:" + x + " y:" + y);        
                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
            }
        }
    }
    
    private boolean backtracking()
    {
//        System.out.println("RETORNO ADS X: " + x + "Y: " + y);
        System.out.println(backtracking.getLast());
        if (!backtracking.isEmpty() 
                && backtracking.getLast().contentEquals(Mensageiro.BUSCA + "," + x + "," + y)) {
            backtracking.pollLast();
            
            if (backtracking.isEmpty())
                return false;
            
            String penultimoPasso = backtracking.pollLast();
            backtracked = true;
            
            x = Character.getNumericValue(penultimoPasso.charAt(2));
            y = Character.getNumericValue(penultimoPasso.charAt(4));
//            backtracking.add(1 + "," + x + ","+ y);
//            mensageiro.mensagem(Mensageiro.RETORNO, x, y);           
        } else if (!backtracking.isEmpty()) {
//                System.out.println(backtracking);
            String penultimoPasso = backtracking.pollLast();

            if (backtracking.isEmpty()){
                    return false;
            }

            backtracked = true;
            x = Character.getNumericValue(penultimoPasso.charAt(2));
            y = Character.getNumericValue(penultimoPasso.charAt(4));
//            System.out.println("DASDASDASDSD x:" + x + " y:" + y);
//                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
        }   
        
        return true;
    }
}
