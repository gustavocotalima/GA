import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static Individuo cruzamento(Individuo pai, Individuo mae, double taxaMutacao) {

        BitSet gene1 = new BitSet();
        BitSet gene2 = new BitSet();

        for (int i = 0; i < 18; i++) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                mutacao(pai, taxaMutacao, gene1, gene2, i);
            } else{
                mutacao(mae, taxaMutacao, gene1, gene2, i);
            }
        }

        return new Individuo(gene1, gene2);
    }

    public static void mutacao(Individuo progenitor, double taxaMutacao, BitSet gene1, BitSet gene2, int i) {
        if (ThreadLocalRandom.current().nextDouble(100) < taxaMutacao) {
            gene1.set(i, !progenitor.getGene1().get(i));
            gene2.set(i, !progenitor.getGene2().get(i));
        }
        else {
            gene1.set(i, progenitor.getGene1().get(i));
            gene2.set(i, progenitor.getGene2().get(i));
        }
    }

    public static int escolherProgenitor(int tamPopulacao){
        int prob = ThreadLocalRandom.current().nextInt(0,tamPopulacao*((tamPopulacao+1)/2));
        int soma = 0;

        for (int i = tamPopulacao, j = 0; i > 0; i--, j++) {
            soma += i;
            if (soma > prob)
                return j;
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);
        int tamPopulacao, numGeracao;
        double taxaMutacao;

        System.out.print("Insira o tamanho da população: ");
        tamPopulacao = scn.nextInt();

        System.out.print("Insira o número de gerações: ");
        numGeracao = scn.nextInt();

        System.out.print("Insira a taxa de mutacao (0 - 100): ");
        taxaMutacao = scn.nextDouble();

        double[]  media = new double[numGeracao];

        ArrayList<ArrayList<Individuo>> individuos = new ArrayList<ArrayList<Individuo>>(numGeracao);
        ArrayList<Individuo> primeiraGeracao = new ArrayList<Individuo>(tamPopulacao);


        //criação da primeira geração

        double soma = 0;
        for (int i = 0; i < tamPopulacao; i++) {
            double x1 = ThreadLocalRandom.current().nextInt(-100000, 100000)/10000.0;
            double x2 = ThreadLocalRandom.current().nextInt(-100000, 100000)/10000.0;
            primeiraGeracao.add(new Individuo(x1,x2));
            soma += primeiraGeracao.get(i).getFitness();
        }
        media[0]=soma/tamPopulacao;

        primeiraGeracao.sort(Individuo::compareTo);

        individuos.add(primeiraGeracao);

        //outras gerações

        for (int i = 1; i < numGeracao; i++) {
            ArrayList<Individuo> geracaoIndividuos = new ArrayList<Individuo>(tamPopulacao);
            geracaoIndividuos.add(individuos.get(i-1).get(0));

            soma = 0;

            for (int j = 1; j < tamPopulacao; j++) {

                int nMae, nPai = escolherProgenitor(tamPopulacao);

                do {
                    nMae = escolherProgenitor(tamPopulacao);
                } while (nMae==nPai);

                geracaoIndividuos.add(cruzamento(individuos.get(i-1).get(nPai),individuos.get(i-1).get(nMae),taxaMutacao));

                soma += geracaoIndividuos.get(j).getFitness();
            }
            media[i]=soma/tamPopulacao;
            geracaoIndividuos.sort(Individuo::compareTo);
            individuos.add(geracaoIndividuos);
        }

        for (int i = 0; i < numGeracao; i++) {
            System.out.println("Melhor: "+individuos.get(i).get(0).getFitness()+"  Média: "+media[i]+"  Pior: "+individuos.get(i).get(tamPopulacao-1).getFitness());
        }
    }

}