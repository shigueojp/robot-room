package robo;

/*********************************************************************/
/** ACH 2002 - Introducao à Análise de Algoritmos                   **/
/** EACH-USP - Segundo Semestre de 2018                             **/
/**                                                                 **/
/** <Victor Shigueo Okuhama> <10724052>                             **/
/**                                                                 **/
/*********************************************************************/

/**
	Classe que implementa os movimentos do robô.
*/
public class Robo implements IRobo {
	/** Coordenada x de início da busca */
	private static int x = ISala.X_INICIO_ARM;
	
	/** Coordenada y de início da busca */
	private static int y = ISala.Y_FIM_ARM+1;
	
	/** Mensageiro do robô **/
	public Mensageiro mensageiro;
        
        public Sala sala;
	
	/** Construtor padrão para o robô **/
	public Robo() {
            mensageiro = new Mensageiro();
            sala = new Sala();
	}
	
	// Aqui você deve completar seu código
	
	
	/**
		Retorna instância do mensageiro do robô
	*/
	public Mensageiro mensageiro() {
		return(this.mensageiro);
	}

        
   
        @Override
        public void adicionaBloco(int x, int y) {
            sala.posicao[x][y] = Sala.BLOCO_PRESENTE;
        }

        @Override
        public void adicionaObstaculo(int x, int y) {
            sala.posicao[x][y] = Sala.OBSTACULO_PRESENTE;
        }

        @Override
        public boolean buscaBloco(int x, int y) {    
            System.out.println("Xa:" + x + " Y:" + y);
            System.out.println(sala.posicao[x][y]);
            if (sala.marcadorEm(x, y) == Sala.POSICAO_VAZIA){
                mensageiro.mensagem(Mensageiro.BUSCA, x, y);
                sala.posicao[x][y] = Sala.MARCA_PRESENTE;               
//                System.out.println("X:" + x + " Y:" + y);
                return false;
            }
            
            if (sala.marcadorEm(x, y) == Sala.OBSTACULO_PRESENTE){
                mensageiro.mensagem(Mensageiro.OBSTACULO, x, y);
                
                return false;
            }           
            
            if (sala.marcadorEm(x, y) == Sala.BLOCO_PRESENTE){
                System.out.println("ABCV");
                sala.posicao[x][y] = Sala.MARCA_PRESENTE;   
                mensageiro.mensagem(Mensageiro.BUSCA, x, y);
                mensageiro.mensagem(Mensageiro.CAPTURA, x, y);
                
                return true;
            }             
                    
            return false;
        }

        @Override
        public void buscaBlocos() {               
//                while(sala.posicao[x][y] == Sala.POSICAO_VAZIA){
//                    System.out.println(sala.posicao[x][y] == Sala.POSICAO_VAZIA);
//                    sala.posicao[x][y] = Sala.MARCA_PRESENTE;
//                    mensageiro.mensagem(Mensageiro.BUSCA, x, y);
//                    y = y+1;
//                }
//                buscaBloco(x, y);

            while(!buscaBloco(x, y)){
//                Verificar se pode ir para o norte, se nao ir para o leste se possivel, se nao ir para sul se possivel ou entao ir a esquerda;
                if (podeIrNorte()){                        
                    y++;
//                    System.out.println("X:" + x + " Y:" + y);
                } else if (podeIrLeste()){
                    
                    x++;
//                    System.out.println("X:" + x + " Y:" + y);
                } else if (podeIrSul()){
                    
                    y--;

                } else if (podeIrOeste()){
                    
                    x--;
//                    System.out.println("X:" + x + " Y:" + y);
                }
            }
            
            System.out.println("Caiu aqui: " + sala.posicao[x][y]);

        }

        @Override
        public boolean guardaBloco() {
            return true;
        }

        @Override
        public void novaBusca() {
        }

    private boolean podeIrNorte() {
        return sala.posicaoBuscaValida(x, y+1) && (sala.marcadorEm(x, y+1) == Sala.POSICAO_VAZIA || sala.marcadorEm(x, y+1) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrLeste() {    
        System.out.println(sala.posicaoBuscaValida(x+1, y));
        return sala.posicaoBuscaValida(x+1, y) && (sala.marcadorEm(x+1, y) == Sala.POSICAO_VAZIA || sala.marcadorEm(x+1, y) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrSul() {
        return sala.posicaoBuscaValida(x, y-1) && (sala.marcadorEm(x, y-1) == Sala.POSICAO_VAZIA || sala.marcadorEm(x, y-1) == Sala.BLOCO_PRESENTE);
    }

    private boolean podeIrOeste() {
        return sala.posicaoBuscaValida(x-1, y) && (sala.marcadorEm(x-1, y) == Sala.POSICAO_VAZIA || sala.marcadorEm(x-1, y) == Sala.BLOCO_PRESENTE);
    }
}
