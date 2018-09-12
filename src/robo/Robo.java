package robo;

import java.util.LinkedList;

/**
 * ******************************************************************
 */
/**
 * ACH 2002 - Introducao à Análise de Algoritmos                   *
 */
/**
 * EACH-USP - Segundo Semestre de 2018                             *
 */
/**
 * **//*** <Victor Shigueo Okuhama> <10724052>                             *
 */
/**
 * *
 */
/**
 * ******************************************************************
 */
/**
 * Classe que implementa os movimentos do robô.
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

    /**
     * Construtor padrão para o robô *
     */
    public Robo()
    {
        mensageiro = new Mensageiro();
        sala = new Sala();
    }

    // Aqui você deve completar seu código
    /**
     * Retorna instância do mensageiro do robô
     */
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
        if (sala.marcadorEm(x, y) == Sala.POSICAO_VAZIA) {
            mensageiro.mensagem(Mensageiro.BUSCA, x, y);
            sala.posicao[x][y] = Sala.MARCA_PRESENTE;

            return false;
        }

        if (sala.marcadorEm(x, y) == Sala.BLOCO_PRESENTE) {
            sala.posicao[x][y] = Sala.MARCA_PRESENTE;
            mensageiro.mensagem(Mensageiro.BUSCA, x, y);
            mensageiro.mensagem(Mensageiro.CAPTURA, x, y);
             System.out.println("BLOC PRESENTE X:" + x + " Y:" + y);
//            retornarBase();

            return true;
        }

        return false;
    }

    @Override
    public void buscaBlocos()
    {
        while (!buscaBloco(x, y)) {
//                Verificar se pode ir para o norte, se nao ir para o leste se possivel, se nao ir para sul se possivel ou entao ir a esquerda;
            if (podeIrNorte()) {
                y++;
            } else if (podeIrLeste()) {
                x++;
            } else if (podeIrSul()) {
                y--;
            } else if (podeIrOeste()) {
                x--;                  
            } else {
                backtracking();
//                mensageiro.msgNaoAchou();
//                return;
            }
        }
            retornarBase();
            
            if ( guardaBloco() )
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
        if (sala.posicaoBuscaValida(x, y + 1) 
                && sala.marcadorEm(x, y + 1) == Sala.OBSTACULO_PRESENTE) {
            if (backtracked) {
                backtracked = false;
            }else{
            mensageiro.mensagem(Mensageiro.OBSTACULO, x, y + 1);
            backtracked = false;
            }
            return false;
        }
        
        backtracked = false;
        
        return sala.posicaoBuscaValida(x, y + 1) 
                && (sala.marcadorEm(x, y + 1) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x, y + 1) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrLeste()
    {
        if (sala.posicaoBuscaValida(x + 1, y) 
                && sala.marcadorEm(x + 1, y) == Sala.OBSTACULO_PRESENTE) {
            if (backtracked) {
                backtracked = false;
            }else{
            mensageiro.mensagem(Mensageiro.OBSTACULO, x + 1, y);
            backtracked = false;
            }
            return false;
        }
        
        backtracked = false;
        
        return sala.posicaoBuscaValida(x + 1, y) 
                && (sala.marcadorEm(x + 1, y) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x + 1, y) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrSul()
    {
        if (sala.posicaoBuscaValida(x, y - 1) 
                && sala.marcadorEm(x, y - 1) == Sala.OBSTACULO_PRESENTE) {
            if (backtracked) {
                backtracked = false;
            }else{
            mensageiro.mensagem(Mensageiro.OBSTACULO, x, y - 1);
            backtracked = false;
            }
        }
        
        backtracked = false;
        
        return sala.posicaoBuscaValida(x, y - 1) 
                && (sala.marcadorEm(x, y - 1) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x, y - 1) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrOeste()
    {
        if (sala.posicaoBuscaValida(x - 1, y) 
                && sala.marcadorEm(x - 1, y) == Sala.OBSTACULO_PRESENTE ) {
            if (backtracked) {
                backtracked = false;
            }else{
            mensageiro.mensagem(Mensageiro.OBSTACULO, x - 1, y);
            backtracked = false;
            }
        }
        
        backtracked = false;
        
        return sala.posicaoBuscaValida(x - 1, y) 
                && (sala.marcadorEm(x - 1, y) == Sala.POSICAO_VAZIA 
                || sala.marcadorEm(x - 1, y) == Sala.BLOCO_PRESENTE);
    }

    private void retornarBase() 
    {
        LinkedList<String> backtracking = mensageiro.mensagens();
        
        while ((x != ISala.X_INICIO_ARM) || (y != ISala.Y_FIM_ARM + 1)) {          
            if (!backtracking.isEmpty() 
                    && backtracking.getLast().contentEquals("1," + x + "," + y)) {
                backtracking.pollLast();
                String penultimoPasso = backtracking.pollLast();
                x = Character.getNumericValue(penultimoPasso.charAt(2));
                y = Character.getNumericValue(penultimoPasso.charAt(4));

                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
            } else if (!backtracking.isEmpty()) {
                String penultimoPasso = backtracking.pollLast();
                x = Character.getNumericValue(penultimoPasso.charAt(2));
                y = Character.getNumericValue(penultimoPasso.charAt(4));

                mensageiro.mensagem(Mensageiro.RETORNO, x, y);
            }
        }
    }
    
    private void backtracking()
    {
        LinkedList<String> backtracking = mensageiro.mensagens();
        backtracked = true;
        if (!backtracking.isEmpty() 
                && backtracking.getLast().contentEquals("1," + x + "," + y)) {
            backtracking.pollLast();
            String penultimoPasso = backtracking.pollLast();
            x = Character.getNumericValue(penultimoPasso.charAt(2));
            y = Character.getNumericValue(penultimoPasso.charAt(4));
            backtracking.add(1 + "," + x + ","+ y);
            mensageiro.mensagem(Mensageiro.RETORNO, x, y);
        } else if (!backtracking.isEmpty()) {
            String penultimoPasso = backtracking.pollLast();
            x = Character.getNumericValue(penultimoPasso.charAt(2));
            y = Character.getNumericValue(penultimoPasso.charAt(4));
            backtracking.add(1 + "," + x + ","+ y);
            mensageiro.mensagem(Mensageiro.RETORNO, x, y);
        }      
    }
}
