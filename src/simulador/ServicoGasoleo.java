/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador;

/**
 *
 * @author Pedro
 */
public class ServicoGasoleo extends Servico{
    private double mediaServ;
    
    public ServicoGasoleo(Simulador s,int tamanho){
        super(s, tamanho);
    }
    
    // Método que insere cliente (c) no serviço
    public void insereServico(Cliente c) {
        if (estado < tamanho) { // Se serviço livre,
            estado++;     // fica ocupado e
            // agenda saída do cliente c para daqui a s.getMedia_serv() instantes
            mediaServ=s.getAbast().distNormal(s.getMedia_serv(), s.getDesvioA());
           
            s.insereEvento(new SaidaGasoleo(s.getInstante() + mediaServ, s));
            s.insereEvento(new Pagamento(s.getInstante() + mediaServ, s));
        } else {
            fila.addElement(c); // Se serviço ocupado, o cliente vai para a fila de espera
        }
    }
    
    // Método que remove cliente do serviço
    public void removeServico() {
        atendidos++; // Regista que acabou de atender + 1 cliente
        if (fila.size() == 0) {
            estado--; // Se a fila está vazia, liberta o serviço
        } else { // Se não,
            // vai buscar próximo cliente à fila de espera e
            // Cliente c = (Cliente)fila.firstElement();
            fila.removeElementAt(0);
            // agenda a sua saida para daqui a s.getMedia_serv() instantes
            mediaServ=s.getAbast().distNormal(s.getMedia_serv(), s.getDesvioA());
            
            s.insereEvento(new SaidaGasoleo(s.getInstante() + mediaServ, s));
            s.insereEvento(new Pagamento(s.getInstante() + mediaServ, s));
        }
    }
}
