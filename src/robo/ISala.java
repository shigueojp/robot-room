package robo;

public interface ISala
{
    static int BLOCO_PRESENTE = 3;
    static int MARCA_PRESENTE = 1;
    static int OBSTACULO_PRESENTE = 2;
    static int POSICAO_VAZIA = 0;
    static int TAMANHO_SALA = 10;
    static int X_INICIO_ARM = 0;
    static int X_FIM_ARM = 1;
    static int Y_INICIO_ARM = 0;
    static int Y_FIM_ARM = 1;
    
    boolean areaArmazenagem(int x, int y);
    
    int marcadorEm(int x, int y);
    
    boolean marcaPosicaoArmazenagem(int x, int y);
    
    boolean marcaPosicaoBusca(int x, int y, int marcador);
    
    boolean posicaoBuscaValida(int x, int y);
    
    void removeMarcador(int marcador);
    
    void removeMarcador(int x, int y);
    
}
