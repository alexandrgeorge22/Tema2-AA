// Copyright 2020
// Author: Matei Simtinică

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Task3
 * This being an optimization problem, the solve method's logic has to work differently.
 * You have to search for the minimum number of arrests by successively querying the oracle.
 * Hint: it might be easier to reduce the current task to a previously solved task
 */
public class Task3 extends Task {
    String task2InFilename;
    String task2OutFilename;

    private Integer familiesNo;
    private Integer cliqueSize;
    private Integer edgeNo;
    private Integer[][] graph;
    ArrayList<String> edges;
    private String ok = "False";

    @Override
    public void solve() throws IOException, InterruptedException {
        task2InFilename = inFilename + "_t2";
        task2OutFilename = outFilename + "_t2";
        Task2 task2Solver = new Task2();
        task2Solver.addFiles(task2InFilename, oracleInFilename, oracleOutFilename, task2OutFilename);
        readProblemData();
        this.cliqueSize = this.familiesNo;
        reduceToTask2();
        task2Solver.solve();
        extractAnswerFromTask2();
        // Cauta clica de ordin maxim
        while (this.ok.equals("False")) {
            this.cliqueSize--;
            reduceToTask2();
            task2Solver.solve();
            extractAnswerFromTask2();
        }
        writeAnswer();
    }

    @Override
    public void readProblemData() throws IOException {
        File in = new File(this.inFilename);
        Scanner reader = new Scanner(in);
        String[] line = reader.nextLine().split(" ");
        this.familiesNo = Integer.valueOf(line[0]);
        this.edgeNo = Integer.valueOf(line[1]);
        this.graph = new Integer[this.familiesNo + 1][this.familiesNo + 1];
        // Construieste complementul grafului initial
        for (int i = 0; i <= this.familiesNo; i++) {
            for (int j = 0; j <= this.familiesNo; j++) {
                graph[i][j] = 1;
            }
        }
        for (int i = 0; i < this.edgeNo; i++) {
            line = reader.nextLine().split(" ");
            this.graph[Integer.parseInt(line[0])][Integer.parseInt(line[1])] = 0;
            this.graph[Integer.parseInt(line[1])][Integer.parseInt(line[0])] = 0;
        }
        reader.close();
        // Construieste o lista cu toate muchiile din graful generat anterior
        this.edges = new ArrayList<>();
        for (int i = 1; i < this.familiesNo; i++) {
            for (int j = i + 1; j <= this.familiesNo; j++) {
                if (graph[i][j].equals(1)) {
                    String edge = i + " " + j + "\n";
                    this.edges.add(edge);
                }
            }
        }
    }

    /**
     * Scrie input-ul problemei in acelasi format cu cel de la task2
     *
     * @throws IOException
     */
    public void reduceToTask2() throws IOException {
        FileWriter fileWriter = new FileWriter(this.task2InFilename);
        fileWriter.write(this.familiesNo + " " + this.edges.size()
                + " " + this.cliqueSize + "\n");
        for (String edge : this.edges) {
            fileWriter.write(edge);
        }
        fileWriter.close();
    }

    public void extractAnswerFromTask2() throws FileNotFoundException {
        File in = new File(this.task2OutFilename);
        Scanner reader = new Scanner(in);
        this.ok = reader.nextLine();
        if (this.ok.equals("True")) {
            String[] answear = reader.nextLine().split(" ");
            for (String vertex : answear) {
                this.graph[Integer.parseInt(vertex)][0] = 0;
            }
        }
        reader.close();
    }

    @Override
    public void writeAnswer() throws IOException {
        FileWriter fileWriter = new FileWriter(outFilename);
        if (this.ok.equals("True")) {
            for (int i = 1; i <= this.familiesNo; i++) {
                if (this.graph[i][0].equals(1)) {
                    fileWriter.write(i + " ");
                }
            }
        }
        fileWriter.close();
    }
}
