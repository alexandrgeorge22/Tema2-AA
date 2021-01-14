// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task2
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task2 extends Task {

    private Integer familiesNo;
    private Integer cliqueSize;
    private Integer edgeNo;
    private Integer[][] graph;
    private String ok;

    @Override
    public void solve() throws IOException, InterruptedException {
        readProblemData();
        formulateOracleQuestion();
        askOracle();
        decipherOracleAnswer();
        writeAnswer();

    }

    @Override
    public void readProblemData() throws IOException {
        File in = new File(this.inFilename);
        Scanner reader = new Scanner(in);
        String[] line = reader.nextLine().split(" ");
        this.familiesNo = Integer.valueOf(line[0]);
        this.edgeNo = Integer.valueOf(line[1]);
        this.cliqueSize = Integer.valueOf(line[2]);
        this.graph = new Integer[this.familiesNo + 1][this.familiesNo + 1];
        // Construire matricea de adiacenta a grafului si setarea elementelor pe 0
        for (int i = 0; i <= this.familiesNo; i++) {
            for (int j = 0; j <= this.familiesNo; j++) {
                graph[i][j] = 0;
            }
        }
        // Setarea muchiilor din graf
        for (int i = 0; i < this.edgeNo; i++) {
            line = reader.nextLine().split(" ");
            this.graph[Integer.parseInt(line[0])][Integer.parseInt(line[1])] = 1;
            this.graph[Integer.parseInt(line[1])][Integer.parseInt(line[0])] = 1;
        }
        reader.close();
    }

    /**
     * Scrie toate combinarile de n luate cate r dintr-un set
     *
     * @param set          setul de unde se aleg combinarile
     * @param n            lungimea setului
     * @param r            numarul de elemente dintr-o combinatie
     * @param index        indexul curent din lista de combinari
     * @param combinations lista de combinari generate
     * @param i            indexul elementului curent ce trebuie adaugat in combinatia actuala
     * @throws IOException
     */
    public void combination(Integer[] set, Integer n, Integer r,
                            Integer index, Integer[] combinations,
                            Integer i, ArrayList<String> clauses) {
        if (index.equals(r)) {
            String clause = "";
            for (int j = 0; j < r; j++) {
                clause = clause + combinations[j] + " ";

            }
            clause = clause + "0\n";
            clauses.add(clause);
            return;
        }

        if (i >= n) {
            return;
        }
        combinations[index] = set[i];
        combination(set, n, r, index + 1, combinations, i + 1, clauses);
        combination(set, n, r, index, combinations, i + 1, clauses);
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        FileWriter fileWriter = new FileWriter(oracleInFilename);
        ArrayList<String> clauses = new ArrayList<>();
        // Clauze de tip 1
        for (int i = 1; i < this.familiesNo; i++) {
            for (int j = i + 1; j <= this.familiesNo; j++) {
                if (graph[i][j].equals(0)) {
                    for (int k = 1; k <= this.cliqueSize; k++) {
                        for (int t = 1; t <= this.cliqueSize; t++) {
                            if (k != t) {
                                String clause;
                                clause = ((i - 1) * this.cliqueSize + k) * (-1) + " ";
                                clause = clause + ((j - 1) * this.cliqueSize + t) * (-1) + " 0\n";
                                clauses.add(clause);
                            }
                        }
                    }
                }
            }
        }
        // Clauze de tip 2
        for (int i = 1; i <= this.familiesNo; i++) {
            Integer[] set = new Integer[this.cliqueSize];
            for (int j = 1; j <= this.cliqueSize; j++) {
                set[j - 1] = (-1) * ((i - 1) * this.cliqueSize + j);
            }
            Integer[] combinations = new Integer[this.cliqueSize];
            combination(set, this.cliqueSize, 2, 0, combinations, 0, clauses);
        }
//      //Clauze de tip 3
        for (int i = 1; i <= this.cliqueSize; i++) {
            String clause = "";
            for (int j = 1; j <= this.familiesNo; j++) {
                clause = clause + ((j - 1) * this.cliqueSize + i) + " ";
            }
            clause = clause + "0\n";
            clauses.add(clause);
        }
        fileWriter.write("p cnf " + this.cliqueSize * this.familiesNo + " "
                + clauses.size() + "\n");
        for (String clause : clauses) {
            fileWriter.write(String.valueOf(clause));
        }

        fileWriter.close();
    }

    @Override
    public void decipherOracleAnswer() throws IOException {
        File in = new File(this.oracleOutFilename);
        Scanner reader = new Scanner(in);
        this.ok = reader.nextLine();
        if (ok.equals("True")) {
            reader.nextLine();
            String[] answear = reader.nextLine().split(" ");
            for (int i = 1; i <= this.familiesNo; i++) {
                for (int j = 0; j < this.cliqueSize; j++) {
                    // calculare numar nod din clica si verificare daca acesta face parte din clica
                    if (Integer.parseInt(answear[(i - 1) * this.cliqueSize + j]) > 0) {
                        // prima coloana din matrice este rezervata pentru a asigna fiecarei familii
                        // cifra 1 daca aceasta face parte din familia extinsa
                        this.graph[i][0] = 1;
                    }
                }
            }
        }
        reader.close();
    }

    @Override
    public void writeAnswer() throws IOException {
        FileWriter fileWriter = new FileWriter(outFilename);
        if (this.ok.equals("True")) {
            fileWriter.write(this.ok + "\n");
            for (int i = 1; i <= this.familiesNo; i++) {
                if (this.graph[i][0].equals(1)) {
                    fileWriter.write(i + " ");
                }
            }
        } else {
            fileWriter.write(this.ok);
        }
        fileWriter.close();
    }
}

