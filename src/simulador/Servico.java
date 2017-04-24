package simulador;

import java.util.*;

// Classe que representa um serviço com uma fila de espera associada
public abstract class Servico {
    protected int tamanho;
    protected int estado; // Variável que regista o estado do serviço: 0 - livre; 1ou+ - ocupado
    protected int atendidos; // Número de clientes atendidos até ao momento
    private double temp_ult, soma_temp_esp, soma_temp_serv; // Variáveis para cálculos estatísticos
    protected Vector<Cliente> fila; // Fila de espera do serviço
    protected Simulador s; // Referência para o simulador a que pertence o serviço

    // Construtor
    Servico(Simulador s,int tamanho) {
        this.tamanho=tamanho;
        this.s = s;
        fila = new Vector<Cliente>(); // Cria fila de espera
        estado = 0; // Livre
        temp_ult = s.getInstante(); // Tempo que passou desde o último evento. Neste caso 0, porque a simulação ainda não começou.
        atendidos = 0;  // Inicialização de variáveis
        soma_temp_esp = 0;
        soma_temp_serv = 0;
    }

    // Método que insere cliente (c) no serviço
    abstract void insereServico(Cliente c);

    // Método que remove cliente do serviço
    abstract void removeServico();

    // Método que calcula valores para estatísticas, em cada passo da simulação ou evento
    public void act_stats() {
        // Calcula tempo que passou desde o último evento
        double temp_desde_ult = s.getInstante() - temp_ult;
        // Actualiza variável para o próximo passo/evento
        temp_ult = s.getInstante();
        // Contabiliza tempo de espera na fila
        // para todos os clientes que estiveram na fila durante o intervalo
        soma_temp_esp += fila.size() * temp_desde_ult;
        // Contabiliza tempo de atendimento
        soma_temp_serv += estado * temp_desde_ult;
    }

    // Método que calcula valores finais estatísticos
    public String[] relat() {
        String[] dados=new String[6];
        // Tempo médio de espera na fila
        double temp_med_fila = soma_temp_esp / (atendidos + fila.size());
        // Comprimento médio da fila de espera
        // s.getInstante() neste momento é o valor do tempo de simulação,
        // uma vez que a simulação começou em 0 e este método só é chamdo no fim da simulação
        double comp_med_fila = soma_temp_esp / s.getInstante();
        // Tempo médio de atendimento no serviço
        double utilizacao_serv = soma_temp_serv / s.getInstante();
        // Apresenta resultados
        dados[0]="Tempo médio de espera:\t" + String.format("%.2f",temp_med_fila);
        dados[1]="\nComp. médio da fila:\t" + String.format("%.2f",comp_med_fila);
        dados[2]="\nUtilização do serviço:\t" + String.format("%.2f",utilizacao_serv/tamanho);
        dados[3]="\nTempo de simulação:\t" + String.format("%.2f",s.getInstante()); // Valor actual
        dados[4]="\nNº de clientes atendidos:\t" + atendidos;
        dados[5]="\nNº de clientes na fila:\t" + fila.size(); // Valor actual
        
        return dados;
    }

    // Método que devolve o número de clientes atendidos no serviço até ao momento
    public int getAtendidos() {
        return atendidos;
    }

}
