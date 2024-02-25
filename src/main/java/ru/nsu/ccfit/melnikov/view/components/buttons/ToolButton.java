package ru.nsu.ccfit.melnikov.view.components.buttons;

import ru.nsu.ccfit.melnikov.controller.Controller;
import ru.nsu.ccfit.melnikov.model.Tools;

public class ToolButton extends IconButton {
    public ToolButton(Controller controller, Tools tool) {
        super(tool.getIconPath());
        addActionListener(e -> controller.setCurrentTool(tool));
    }
}
