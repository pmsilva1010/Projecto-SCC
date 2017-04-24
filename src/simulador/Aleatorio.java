package simulador;

// Classe para geração de números aleatórios segundos várias distribuições
// Apenas a distribuição exponencial negativa está definida
public class Aleatorio {
    private String cache=null;
    
    //Gera um numero de 0 a 1
    static public double discreto(){
        return new RandomGenerator().rand64(1);
    }
    
    // Gera um número segundo uma distribuição exponencial negativa de média m
    static public double exponencial(double m) {
        return (-m * Math.log(new RandomGenerator().rand64(1)));
    }
    
    public double distNormal(double m, double d){
        if(cache!=null){
            String aux=cache;
            cache=null;
            return Double.parseDouble(aux);
        }
        else{
            RandomGenerator rand=new RandomGenerator();
            double v1,v2,w,y1,y2,x1,x2;

            do{
                do{
                    v1=2*rand.rand64(1)-1;
                    v2=2*rand.rand64(1)-1;

                    w=Math.pow(v1, 2)+Math.pow(v2, 2);
                }while(w>1);

                y1=v1*Math.sqrt((-2*Math.log(w)/w));
                x1=m+y1*d;

                y2=v2*Math.sqrt((-2*Math.log(w)/w));
                x2=m+y2*d;
            }while(x1<0 || x2<0);
            
            cache=""+x2;
            return x1;
        }
    }
}
