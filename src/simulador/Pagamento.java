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
public class Pagamento extends Evento {
    
    //Construtor
    Pagamento(double i, Simulador s) {
        super(i, s);
    }
    
    // Método que executa as acções correspondentes ao pagamento de um cliente
    void executa(Servico serv) {
        // Coloca cliente no serviço - na fila ou a ser atendido, conforme o caso
        serv.insereServico(new Cliente());
    }


    // Método que descreve o evento.
    // Para ser usado na listagem da lista de eventos.
    public String toString() {
        return "Pagamento em " + instante;
    }
}
