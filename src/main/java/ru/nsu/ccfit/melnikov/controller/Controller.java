package ru.nsu.ccfit.melnikov.controller;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.ccfit.melnikov.model.Drafter;
import ru.nsu.ccfit.melnikov.model.Tools;
import ru.nsu.ccfit.melnikov.view.Canvas;
import ru.nsu.ccfit.melnikov.view.components.FileChooser.ImageLoader;
import ru.nsu.ccfit.melnikov.view.components.FileChooser.ImageSaver;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


@Getter
@Setter
public class Controller {
    private Color currentColor = Color.BLACK;
    private Tools currentTool = Tools.PEN;
    private int thickness = 1;
    private int numOfAngles = 5;
    private int radius = 70;
    private int rotation = 0;

    public void loadImage(Canvas canvas) {
        var loader = new ImageLoader();
        int returnVal = loader.showOpenDialog(loader);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                var newImage = ImageIO.read(loader.getSelectedFile());
                if (newImage != null)
                    canvas.setImage(newImage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveImage(Canvas canvas) {
        var saver = new ImageSaver();
        var returnVal = saver.showSaveDialog(saver);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                var filePath = saver.getSelectedFile().getAbsolutePath();
                var file = filePath.endsWith(".png") ? saver.getSelectedFile() : new File(filePath + ".png");
                ImageIO.write(canvas.getImage(), "png", file);
                JOptionPane.showMessageDialog(saver,"File " + file.getPath() + " saved");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void fill(BufferedImage image, Point seedPoint) {
        Drafter.fill(image, getCurrentColor(), seedPoint.x, seedPoint.y);
    }

    public void drawLine(BufferedImage image, Point from, Point to) {
        if (thickness == 1) {
            Drafter.drawLine(image,
                    currentColor,
                    from.x, from.y,
                    to.x, to.y);
        } else {
            var g2d = (Graphics2D) image.getGraphics();
            g2d.setColor(currentColor);
            g2d.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.drawLine(from.x, from.y, to.x, to.y);
        }
    }

    public void drawPolygon(BufferedImage image, Point centerPoint) {
        Point[] vertex = new Point[numOfAngles];
        double k = 2 * Math.PI / numOfAngles;

        for (int i = 0; i < numOfAngles; ++i) {
            var vertexAngle = Math.toRadians(rotation) + i * k;
            var x = (int) (centerPoint.x + radius * Math.cos(vertexAngle));
            var y = (int) (centerPoint.y + radius * Math.sin(vertexAngle));
            vertex[i] = new Point(x, y);
        }

        for (int i = 0; i < numOfAngles; ++i)
            drawLine(image, vertex[i], vertex[(i + 1) % numOfAngles]);
    }

    public void drawStar(BufferedImage image, Point centerPoint) {
        Point[] vertex = new Point[2 * numOfAngles];
        double k = Math.PI / numOfAngles;

        for (int i = 0; i < 2 * numOfAngles; ++i) {
            int x, y;
            var vertexAngle = Math.toRadians(rotation) + i * k;
            if (i % 2 == 0) {
                x = (int) (centerPoint.x + radius * Math.cos(vertexAngle));
                y = (int) (centerPoint.y + radius * Math.sin(vertexAngle));
            } else {
                x = (int) (centerPoint.x + radius / 2.5 * Math.cos(vertexAngle));
                y = (int) (centerPoint.y + radius / 2.5 * Math.sin(vertexAngle));
            }
            vertex[i] = new Point(x, y);
        }

        for (int i = 0; i < 2 * numOfAngles; ++i)
            drawLine(image, vertex[i], vertex[(i + 1) % (2 * numOfAngles)]);
    }
}
