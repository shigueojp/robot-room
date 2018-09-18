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
    /**
     * Coordenada x de início da busca
     */
    private static int x = ISala.X_INICIO_ARM;

    /**
     * Coordenada y de início da busca
     */
    private static int y = ISala.Y_FIM_ARM + 1;

    /**
     * Mensageiro do robô *
     */
    public Mensageiro mensageiro;

    public Sala sala;
    
    private boolean backtracked = false;
    private int teste;

    /**
     * Construtor padrão para o robô *
     */
    public Robo()
    {
        mensageiro = new Mensageiro();
        sala = new Sala();
    }

    /**
     * Retorna instância do mensageiro do robô
     * @return 
     */
    @Override
    public Mensageiro mensageiro()
    {
        return (this.mensageiro);
    }

    @Override
    public void adicionaBloco(int x, int y)
    {
        sala.posicao[x][y] = Sala.BLOCO_PRESENTE;
    }

    @Override
    public void adicionaObstaculo(int x, int y)
    {
        sala.posicao[x][y] = Sala.OBSTACULO_PRESENTE;
    }

    @Override
    public boolean buscaBloco(int x, int y)
    {   
        /* Ao buscar o bloco na posicao[x][y] verifica se esta vazia*/
        if (sala.marcadorEm(x, y) == Sala.POSICAO_VAZIA) {
            mensageiro.mensagem(Mensageiro.BUSCA, x, y);
            sala.posicao[x][y] = Sala.MARCA_PRESENTE;
            teste = 0;
            
            return false;
        }
        
        /* Ao buscar o bloco na posicao[x][y] verifica se existe o bloco*/
        if (sala.marcadorEm(x, y) == Sala.BLOCO_PRESENTE) {
            sala.posicao[x][y] = Sala.MARCA_PRESENTE;
            mensageiro.mensagem(Mensageiro.BUSCA, x, y);
            mensageiro.mensagem(Mensageiro.CAPTURA, x, y);
            System.out.println("PERCORRI NA POSICAO X:" + x + " Y:" + y + " E ACHEI E CAPTUREI UM BLOCO");
            
            return true;
        }

        return false;
    }

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
            
//            if (backtracked && teste < 3) {
//                mensageiro.mensagem(Mensageiro.OBSTACULO, x, y + 1);
//                teste++;
//            }
            
            return false;
        }
        
        /*Se tiver posicao vaga faça isso */
        if (sala.posicaoBuscaValida(x, y + 1) 
                && (sala.marcadorEm(x, y + 1) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x, y + 1) == Sala.BLOCO_PRESENTE)){
            backtracked = false;
            System.out.println("Fui para NORTE (Y+1) X: " + x + "Y: " + y);
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
            System.out.println("Fui para Leste (x+1) X: " + x + "Y: " + y);
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
            System.out.println("Fui para OESTE (x-1) X: " + x + "Y: " + y);
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
        LinkedList<java.lang.String> backtracking = mensageiro.getBacktracking();
        System.out.println("x:" + x + " y:" + y);
        while ( x != ISala.X_INICIO_ARM || y != ISala.Y_FIM_ARM + 1) {          
            if (!backtracking.isEmpty() 
                    && backtracking.getLast().contentEquals("0," + x + "," + y)) {
                backtracking.pollLast();
                String penultimoPasso = backtracking.pollLast();
                x = Character.getNumericValue(penultimoPasso.charAt(2));
                y = Character.getNumericValue(penultimoPasso.charAt(4));
                System.out.println("Voltando para posicao x:" + x + " y:" + y);    
                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
            } else if (!backtracking.isEmpty()) {
                System.out.println(backtracking);
                String penultimoPasso = backtracking.pollLast();
                x = Character.getNumericValue(penultimoPasso.charAt(2));
                y = Character.getNumericValue(penultimoPasso.charAt(4));
                System.out.println("Voltando para posicao x:" + x + " y:" + y);        
                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
            }
        }
    }
    
    private boolean backtracking()
    {
        LinkedList<String> backtracking = mensageiro.mensagens();       
        System.out.println("RETORNO ADS X: " + x + "Y: " + y);
        System.out.println(backtracking.getLast());
        if (!backtracking.isEmpty() 
                && backtracking.getLast().contentEquals("0," + x + "," + y)) {           
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
                
                if (backtracking.isEmpty())
                    return false;
                
                x = Character.getNumericValue(penultimoPasso.charAt(2));
                y = Character.getNumericValue(penultimoPasso.charAt(4));
                System.out.println("Voltando para posicao x:" + x + " y:" + y);        
//                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
            }   
        
        return true;
    }
}
