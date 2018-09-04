package robo;

public interface IRobo {
    static int N_BLOCOS = 100;
    
    void adicionaBloco(int x, int y);
    
    void adicionaObstaculo(int x,int y);
    
    boolean buscaBloco(int x, int y);
   
    void buscaBlocos();
    
    boolean guardaBloco();
    
    Mensageiro mensageiro();
    
    void novaBusca();
}
