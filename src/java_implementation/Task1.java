// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Task1
 * You have to implement 4 methods:
 * readProblemData         - read the problem input and store it however you see fit
 * formulateOracleQuestion - transform the current problem instance into a SAT instance and write the oracle input
 * decipherOracleAnswer    - transform the SAT answer back to the current problem's answer
 * writeAnswer             - write the current problem's answer
 */
public class Task1 extends Task {

    private Integer familiesNo;
    private Integer spiesNo;
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
        this.spiesNo = Integer.valueOf(line[2]);

        // Construire matricea de adiacenta a grafului si setarea elementelor pe 0
        this.graph = new Integer[this.familiesNo + 1][this.familiesNo + 1];
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
     * @param fileWriter
     * @throws IOException
     */
    public void combination(Integer[] set, Integer n, Integer r,
                            Integer index, Integer[] combinations,
                            Integer i, FileWriter fileWriter) throws IOException {
        if (index.equals(r)) {
            for (int j = 0; j < r; j++) {
                fileWriter.write(combinations[j] + " ");
            }
            fileWriter.write("0\n");
            return;
        }

        if (i >= n) {
            return;
        }

        combinations[index] = set[i];
        combination(set, n, r, index + 1, combinations, i + 1, fileWriter);
        combination(set, n, r, index, combinations, i + 1, fileWriter);
    }

    @Override
    public void formulateOracleQuestion() throws IOException {
        FileWriter fileWriter = new FileWriter(this.oracleInFilename);
        int clausesNo = this.familiesNo * (1 + spiesNo) + this.edgeNo * this.spiesNo;
        int variablesNo = this.familiesNo * this.spiesNo;
        fileWriter.write("p cnf " + variablesNo + " " + clausesNo + "\n");

        // Clauze tip 1 si 2
        for (int i = 1; i <= this.familiesNo; i++) {
            Integer[] set = new Integer[this.spiesNo];
            for (int j = 1; j <= this.spiesNo; j++) {
                set[j - 1] = (-1) * ((i - 1) * this.spiesNo + j);
                fileWriter.write(((i - 1) * this.spiesNo + j) + " ");
            }
            fileWriter.write("0\n");
            Integer[] combinations = new Integer[this.spiesNo];
            combination(set, this.spiesNo, 2, 0, combinations, 0, fileWriter);
        }
        // Clauze de tip 3
        for (int i = 1; i < this.familiesNo; i++) {
            for (int j = i + 1; j <= this.familiesNo; j++) {
                if (graph[i][j] == 1) {
                    for (int k = 1; k <= this.spiesNo; k++) {
                        fileWriter.write(((i - 1) * this.spiesNo + k) * (-1) + " ");
                        fileWriter.write(((j - 1) * this.spiesNo + k) * (-1) + " 0\n");
                    }
                }
            }
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
                for (int j = 0; j < this.spiesNo; j++) {
                    // calculare numar spion si verificare daca acesta trebuie plantat in familia i
                    if (Integer.parseInt(answear[(i - 1) * this.spiesNo + j]) > 0) {
                        // prima coloana din matrice este rezervata pentru a asigna fiecarei familii
                        // un spion
                        this.graph[i][0] = j + 1;
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
                fileWriter.write(this.graph[i][0] + " ");
            }
        } else {
            fileWriter.write(this.ok);
        }
        fileWriter.close();
    }
}
