package robo;

import java.util.Iterator;
import java.util.LinkedList;

public class Mensageiro
{
    static int ARMAZENAGEM = 4;
    static int BUSCA = 0;
    static int CAPTURA = 3;
    static int OBSTACULO = 2;
    static int RETORNO = 1;

    private LinkedList log;
    private LinkedList backtracking;
    
    public Mensageiro()
    {
        log = new LinkedList();
        backtracking = new LinkedList();
    }

    public java.util.LinkedList<java.lang.String> getBacktracking() {
        return backtracking;
    }
    
    
    public void imprimeMensagens()
    {
        Iterator iter1 = log.iterator();
        while (iter1.hasNext()) {
                System.out.println(iter1.next());
        }       
    }
    
    void limpaMensagens()
    {
        
    }
    
    void mensagem(int tipo, int x, int y)
    {
        if (tipo == BUSCA) { 
            log.add("Busca em " + "(" + x + "," + y + ")");
            backtracking.add(tipo + "," + x + ","+ y);
        }
        
        if (tipo == OBSTACULO) {
            log.add("Obstáculo detectado em " + "(" + x + "," + y + ")");
        }
        
        if (tipo == CAPTURA) {
            log.add("Bloco recolhido em " + "(" + x + "," + y + ")");
        }
        
        if (tipo == ARMAZENAGEM) {
            log.add("Bloco armazenado em " + "(" + x + "," + y + ")");
        }
        
        if (tipo == RETORNO) {
            log.add("Retorno a " + "(" + x + "," + y + ")");
        }
        
    }
    
    java.util.LinkedList<java.lang.String> mensagens()
    {          
        return backtracking;
    }
    
    void msgFim()
    {
        log.add("Busca terminada. Blocos recuperados.");
    }
    
    void msgNaoAchou()
    {
        log.add("Mais nenhum bloco foi encontrado.");
        
    }

}
