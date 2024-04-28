package ru.nsu.ccfit.melnikov.controllers;

import lombok.AllArgsConstructor;
import ru.nsu.ccfit.melnikov.exceptions.ValueException;
import ru.nsu.ccfit.melnikov.gui.MainPanel;
import ru.nsu.ccfit.melnikov.logic.Constants;
import ru.nsu.ccfit.melnikov.logic.algorithms.BSpline;
import ru.nsu.ccfit.melnikov.logic.utils.Point;
import ru.nsu.ccfit.melnikov.gui.frames.InfoDialog;
import ru.nsu.ccfit.melnikov.logic.Config;
import ru.nsu.ccfit.melnikov.exceptions.FileContentException;
import ru.nsu.ccfit.melnikov.exceptions.FileExtException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileController {
    private JFrame frame;
    private JPanel mainPanel;

    private class Save {
        public int k;
        public int n;
        public int m;
        public int m1;
        public double zn;
        public double thetaX;
        public double thetaY;
        public double thetaZ;
        public BSpline bSpline;

        public Save() {
            k = Config.getK();
            n = Config.getN();
            m = Config.getM();
            m1 = Config.getM1();
            zn = Config.getZn();
            thetaX = Config.getThetaX();
            thetaY = Config.getThetaY();
            thetaZ = Config.getThetaZ();
            bSpline = Config.getBSpline();
        }
    }

    public FileController(JFrame frame, JPanel panel){
        this.frame = frame;
        this.mainPanel = panel;
    }

    public void openFile(){
        try {
            FileDialog fd = new FileDialog(frame, "Open file", FileDialog.LOAD);
            fd.setResizable(true);
            fd.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".wrf"));
            fd.setFile("*.wrf");
            fd.setVisible(true);
            if (fd.getFile() == null)
                return;
            File inputFile = (fd.getFiles()[0]);
            checkFileExt(inputFile.getName());
            readData(inputFile);
            ((MainPanel)mainPanel).update();
        }
        catch(FileExtException | FileContentException | ValueException e){
            new InfoDialog(e.getMessage());
        } catch(IOException e){
        }
    }

    public void saveFile() {
        try {
            FileDialog fd = new FileDialog(frame, "Save", FileDialog.SAVE);
            fd.setResizable(true);
            fd.setFilenameFilter((dir, name) -> name.toLowerCase().endsWith(".wrf"));
            fd.setFile("Untitled.wrf");
            fd.setVisible(true);
            if (fd.getFile() == null) {
                return;
            }
            File outputFile = (fd.getFiles()[0]);
            checkFileExt(outputFile.getName());
            writeData(outputFile);
        }
        catch(FileExtException e){
            new InfoDialog(e.getMessage());
        } catch(IOException e){
        }
    }

    public void openInfoDialog(){
        new InfoDialog();
    }

    private void checkFileExt(String fileName) throws FileExtException {
        if(!fileName.matches(".+\\.wrf")){
            throw new FileExtException("invalid file extension");
        }
    }

    private void writeData(File file) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        var save = new Save();
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(save));
        }
    }

    private void readData(File file) throws IOException, FileContentException, ValueException {
        Save save;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Reader reader = new FileReader(file)) {
            save = gson.fromJson(reader, Save.class);
        }
        checkReading(save);
    }

    private void checkReading(Save save) throws ValueException {
        if (save.k < Constants.MIN_K || save.k > Constants.MAX_K)
            throw new ValueException("K", save.k, Constants.MIN_K, Constants.MAX_K);
        if (save.n < Constants.MIN_N || save.n > Constants.MAX_N)
            throw new ValueException("N", save.n, Constants.MIN_N, Constants.MAX_N);
        if (save.m < Constants.MIN_M || save.m > Constants.MAX_M)
            throw new ValueException("M", save.m, Constants.MIN_M, Constants.MAX_M);
        if (save.m1 < Constants.MIN_M1 || save.m1 > Constants.MAX_M1)
            throw new ValueException("M1", save.m1, Constants.MIN_M1, Constants.MAX_M1);

        Config.setK(save.k);
        Config.setN(save.n);
        Config.setM(save.m);
        Config.setM1(save.m1);
        Config.setZn(save.zn);
        Config.setThetaX(save.thetaX);
        Config.setThetaY(save.thetaY);
        Config.setThetaZ(save.thetaZ);
        Config.setBSpline(save.bSpline);
    }
}
