package ru.nsu.ccfit.melnikov.view;

import ru.nsu.ccfit.melnikov.controller.Controller;
import ru.nsu.ccfit.melnikov.model.Tools;
import ru.nsu.ccfit.melnikov.view.components.buttons.ColoredButton;
import ru.nsu.ccfit.melnikov.view.components.buttons.IconButton;
import ru.nsu.ccfit.melnikov.view.components.ParametersDialog.FigureParametersDialog;
import ru.nsu.ccfit.melnikov.view.components.buttons.ToolButton;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String TITLE = "ICG Paint";
    private static final Dimension MINIMUM_SIZE = new Dimension(640, 480);
    private static final Color[] MAIN_PALETTE_COLORS =
            {Color.BLACK, Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.WHITE};
    private final Canvas canvas;
    private final Controller controller = new Controller();
    private final FigureParametersDialog parametersDialog = new FigureParametersDialog(controller);

    public MainFrame() {
        setTitle(TITLE);
        setMinimumSize(MINIMUM_SIZE);
        setSize(660, 580);
        setResizable(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setJMenuBar(createMenuBar());
        add(createToolBar(), BorderLayout.NORTH);

        canvas = new Canvas(controller, MINIMUM_SIZE);
        var scrollPane = new JScrollPane(canvas);
        canvas.setPreferredSize(MINIMUM_SIZE);
        add(scrollPane);
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createAboutMenu());

        return menuBar;
    }

    private JMenu createFileMenu() {
        var file = new JMenu("File");
        var open = new JMenuItem("Open");
        open.addActionListener(e -> controller.loadImage(canvas));
        var save = new JMenuItem("Save");
        save.addActionListener(e -> controller.saveImage(canvas));
        var settings = new JMenuItem("Settings") {
            {
                addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, parametersDialog,
                            "Options", JOptionPane.OK_CANCEL_OPTION);
                    if (JOptionPane.OK_OPTION == confirm) {
                        controller.setThickness(parametersDialog.getThickness());
                        controller.setNumOfAngles(parametersDialog.getNumOfAngles());
                        controller.setRadius(parametersDialog.getRadius());
                        controller.setRotation(parametersDialog.getRotation());
                    }
                });
            }
        };

        file.add(open);
        file.add(save);

        file.addSeparator();

        file.add(settings);

        file.addSeparator();

        file.add(new JMenuItem("Exit") {
            {
                addActionListener(e -> System.exit(0));
            }
        });

        return file;
    }

    private JMenu createViewMenu() {
        return new JMenu("View");
    }

    private JMenu createAboutMenu() {
        return new JMenu("About");
    }

    private JToolBar createToolBar() {
        var toolBar = new JToolBar();
        toolBar.setFloatable(false);

        var toolButtonGroup = new ButtonGroup();

        var eraser = new ToolButton(controller, Tools.ERASER);
        eraser.addActionListener(e -> canvas.setDefaultBackground());
        toolBar.add(eraser);
        toolButtonGroup.add(eraser);

        toolBar.addSeparator();

        var pen = new ToolButton(controller, Tools.PEN);
        pen.setSelected(true);
        toolBar.add(pen);
        toolButtonGroup.add(pen);
        var line = new ToolButton(controller, Tools.LINE);
        toolBar.add(line);
        toolButtonGroup.add(line);
        var fill = new ToolButton(controller, Tools.FILL);
        toolBar.add(fill);
        toolButtonGroup.add(fill);

        toolBar.addSeparator();

        var polygon = new ToolButton(controller, Tools.POLYGON);
        toolBar.add(polygon);
        toolButtonGroup.add(polygon);
        var star = new ToolButton(controller, Tools.STAR);
        toolBar.add(star);
        toolButtonGroup.add(star);

        toolBar.addSeparator();

        var colorButtonGroup = new ButtonGroup();
        for (var color : MAIN_PALETTE_COLORS) {
            var coloredButton = new ColoredButton(controller, color);
            toolBar.add(coloredButton);
            colorButtonGroup.add(coloredButton);
        }

        toolBar.addSeparator();

        var palette = new IconButton(Tools.PALETTE.getIconPath());
        palette.addActionListener(e -> {
            controller.setCurrentColor(
                    JColorChooser.showDialog(null, "Changing the palette", controller.getCurrentColor()));
            palette.setSelected(false);
        });
        toolBar.add(palette);

        return toolBar;
    }

    public void display() {
        setVisible(true);
    }
}
