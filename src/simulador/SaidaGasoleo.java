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
public class SaidaGasoleo extends Evento {

    //Construtor
    SaidaGasoleo(double i, Simulador s) {
        super(i, s);
    }

    // Método que executa as acções correspondentes à saída de um cliente
    void executa(Servico serv) {
        // Retira cliente do serviço
        serv.removeServico();
    }

    // Método que descreve o evento.
    // Para ser usado na listagem da lista de eventos.
    public String toString() {
        return "Saída em " + instante;
    }

}