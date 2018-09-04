package robo;

import java.util.Iterator;
import java.util.LinkedList;

public class Mensageiro {
    static int ARMAZENAGEM;
    static int BUSCA;
    static int CAPTURA;
    static int OBSTACULO;
    static int RETORNO;

    private LinkedList log;
    
    public Mensageiro()
    {
    }
    
    public void imprimeMensagens()
    {
        Iterator iter1 = log.iterator();
        while(iter1.hasNext()){
                System.out.println(iter1.next());
        }
        
    }
    
    void limpaMensagens()
    {
        
    }
    
    void mensagem(int tipo, int x, int y)
    {
        if (tipo == BUSCA){ 
            log.add("Busca em " + "(" + x + "," + y + ")");
        }
        
        if (tipo == OBSTACULO){
            log.add("Obstáculo detectado em " + "(" + x + "," + y + ")");
        }
        
        if (tipo == CAPTURA){
            log.add("Bloco recolhido em " + "(" + x + "," + y + ")");
        }
        
        if (tipo == ARMAZENAGEM){
            log.add("Bloco armazenado em " + "(" + x + "," + y + ")");
        }
        
        if (tipo == RETORNO){
            log.add("Retorno a " + "(" + x + "," + y + ")");
        }
        
    }
    
    java.util.LinkedList<java.lang.String> mensagens()
    {
        LinkedList list = new LinkedList();
        list.add(log);
        
        return list;
    }
    
    void msgFim()
    {
        
    }
    
    void msgNaoAchou()
    {
        
    }

}
