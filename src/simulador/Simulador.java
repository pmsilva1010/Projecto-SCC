package simulador;

public class Simulador {

    // Relógio de simulação - variável que contém o valor do tempo em cada instante
    private double instante;
    // Médias das distribuições de chegadas e de atendimento no serviço
    private double media_cheg, media_servAbast, media_servPagar, desvioA, desvioP;
    // Serviço - pode haver mais do que um num simulador
    private Servico servicoAbast, servicoPagar, servicoAbastGasoleo;
    // Lista de eventos - onde ficam registados todos os eventos que vão ocorrer na simulação
    // Cada simulador só tem uma
    private ListaEventos lista;
    //Instanciacoes diferentes de aleatorios para obter diferente dist. normais
    Aleatorio abast, pagam;
    //Tempo de simulacao
    double tempoSimulacao;
    //Despesas e lucros
    int ligado,nEmpregados;
    double investimento, ordenado, valorCliente;

    // Construtor
    public Simulador(double mediaChegada,double mediaAbast, double mediaPagamento, double desvioA,double desvioP,int nPostosGasolina,int nPostosGasoleo,int nCaixas,double tempoSimulacao,int ligado,double investimento,double valorCliente,int nEmpregados,double ordenado) {
        // Inicialização de parâmetros do simulador
        media_cheg = mediaChegada;
        media_servAbast = mediaAbast;
        media_servPagar = mediaPagamento;
        this.desvioA=desvioA;
        this.desvioP=desvioP;
        // Inicialização do relógio de simulação
        instante = 0;
        // Criação dos serviços
        servicoAbast = new ServicoGasolina(this,nPostosGasolina);
        servicoAbastGasoleo= new ServicoGasoleo(this,nPostosGasoleo);
        servicoPagar =new ServicoPagamento(this,nCaixas);
        // Criação da lista de eventos
        lista = new ListaEventos(this);
        // Agendamento da primeira chegada
        // Se não for feito, o simulador não tem eventos para simular
        insereEvento(new Chegada(instante, this));
        //Iniciaçao de aleatorios para distribuicoes Normais
        abast=new Aleatorio();
        pagam=new Aleatorio();
        //tempo de simulacao
        this.tempoSimulacao=tempoSimulacao;
        //Lucros
        this.ligado=ligado;
        this.investimento=investimento;
        this.nEmpregados=nEmpregados;
        this.ordenado=ordenado;
        this.valorCliente=valorCliente;
    }

    // Método que insere o evento e1 na lista de eventos
    void insereEvento(Evento e1) {
        lista.insereEvento(e1);
    }

    // Método que actualiza os valores estatísticos do simulador
    private void act_stats() {
        servicoAbast.act_stats();
        servicoAbastGasoleo.act_stats();
        servicoPagar.act_stats();
    }

    // Método que apresenta os resultados de simulação finais
    private void relat() {
        String[] dadosGasolina=servicoAbast.relat(); 
        String[] dadosGasoleo=servicoAbastGasoleo.relat(); 
        String[] dadosPagamento=servicoPagar.relat();
        String[] dadosLucros=pagamentos();
        
        ResultadosUI results=new ResultadosUI(dadosGasolina, dadosGasoleo, dadosPagamento, dadosLucros);
        results.setVisible(true);
    }
    
    //Medtodos para calcular lucros e despesas
    private String[] pagamentos(){
        String[] dados=new String[5];
        
        //valor que cada cliente contribui menos os ordenados dos funcionarios
        double valorFinal=servicoPagar.getAtendidos()*valorCliente-((nEmpregados*ordenado)*(tempoSimulacao/(30*24*60)));
        double lucros=valorFinal-investimento;
        
        dados[0]=""+ligado;
        dados[1]="Duraçao:\t"+String.format("%.2f",tempoSimulacao/(30*24*60))+" meses";
        dados[2]="\nInvestimento:\t"+investimento+" €";
        dados[3]="\nFacturação:\t"+valorFinal+" €";
        dados[4]="\nLucro:\t"+lucros+" €";
        
        return dados;
    }
    
    // Método executivo do simulador
    public void executa() {
        Evento e1;
        
        // Enquanto não atender todos os clientes
        while (instante < tempoSimulacao) {               // Tempo simulacao
            //lista.print();                                // Mostra lista de eventos - desnecessário; é apenas informativo
            e1 = (Evento) (lista.removeFirst());            // Retira primeiro evento (é o mais iminente) da lista de eventos
            instante = e1.getInstante();                    // Actualiza relógio de simulação
            act_stats();                                    // Actualiza valores estatísticos
            
            //Executa o evento
            if(e1 instanceof Chegada){
                double aux=new Aleatorio().discreto();
                
                if(aux<=0.2)
                    e1.executa(servicoAbastGasoleo);
                else
                    e1.executa(servicoAbast);
            }
            else if(e1 instanceof SaidaGasolina){
                e1.executa(servicoAbast);
            }
            else if(e1 instanceof SaidaGasoleo){
                e1.executa(servicoAbastGasoleo);
            }
            else if(e1 instanceof Pagamento){
                e1.executa(servicoPagar);
            }
            else if(e1 instanceof Saida){
                e1.executa(servicoPagar);
            }
            else
                System.out.println("Erro nos eventos!");
        }
        relat();  // Apresenta resultados de simulação finais
    }

    // Método que devolve o instante de simulação corrente
    public double getInstante() {
        return instante;
    }

    // Método que devolve a média dos intervalos de chegada
    public double getMedia_cheg() {
        return media_cheg;
    }

    // Método que devolve a média dos tempos de serviço
    public double getMedia_serv() {
        return media_servAbast;
    }
    
    public double getMediaPag(){
        return media_servPagar;
    }
    
    public double getDesvioA(){
        return desvioA;
    }
    
    public double getDesvioP(){
        return desvioP;
    }
    
    //Metodos que devolvem a instancia Aleatoria para dist.Normais
    public Aleatorio getAbast(){
        return abast;
    }
    
    public Aleatorio getPagam(){
        return pagam;
    }
}
