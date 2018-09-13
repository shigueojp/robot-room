package robo;

/**
	Classe que define a sala por onde o robô passeia. A sala é uma matriz quadrada de largura ISala.TAMANHO_SALA.
	
	Essa classe deve ser implementada pelo aluno
*/
public class Sala implements ISala
{   
    int [][] posicao;
    
    public Sala()
    {
        posicao = new int[TAMANHO_SALA][TAMANHO_SALA];

        for (int row = 0; row < TAMANHO_SALA; row ++){
            for (int col = 0; col < TAMANHO_SALA; col++){
                posicao[row][col] = POSICAO_VAZIA;
            }
        }
    }

    @Override
    public boolean areaArmazenagem(int x, int y)
    {
            return x <= 1 && y <= 1;
    }

    @Override
    public int marcadorEm(int x, int y)
    {
        return posicao[x][y];
    }

    @Override
    public boolean marcaPosicaoArmazenagem(int x, int y)
    {
        if (areaArmazenagem(x, y) && posicao[x][y] == POSICAO_VAZIA) {
            posicao[x][y] = BLOCO_PRESENTE;         
            
            return true;
        }
        
        return false;
    }

    @Override
    public boolean marcaPosicaoBusca(int x, int y, int marcador)
    {
        if (posicaoBuscaValida(x, y)) {
            posicao[x][y] = marcador; 
            
            return true;
        } else return false;
    }

    @Override
    public boolean posicaoBuscaValida(int x, int y)
    {
        return ((x >= 0 && x < 10) && (y >= 0 && y < 10) && !areaArmazenagem(x,y));
    }

    @Override
    public void removeMarcador(int marcador)
    {
        for (int row = 0; row < TAMANHO_SALA; row ++) {
            for (int col = 0; col < TAMANHO_SALA; col++) {
                if (posicao[row][col] == marcador)
                    posicao[row][col] = POSICAO_VAZIA;
            }
        } 
    }

    @Override
    public void removeMarcador(int x, int y)
    {
        if (posicao[x][y] != OBSTACULO_PRESENTE && posicaoBuscaValida(x, y)) {
            posicao[x][y] = POSICAO_VAZIA;
        }
            
    }
    
}
