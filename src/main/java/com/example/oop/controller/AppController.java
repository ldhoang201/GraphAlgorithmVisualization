package com.example.oop.controller;

import com.example.oop.algorithm.DFS;
import com.example.oop.algorithm.DP;
import com.example.oop.algorithm.Dijkstra;
import com.example.oop.model.Edge;
import com.example.oop.model.Graph;
import com.example.oop.model.Vertex;
import com.example.oop.step.PseudoStep;
import com.example.oop.step.soloStep;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.robot.Robot;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class AppController implements Initializable {
    @FXML
    public Label textOfShowPanel;
    @FXML
    public Button showPanel;
    @FXML
    public Button codeTraceButton;
    @FXML
    public Button statusButton;
    @FXML
    public VBox panel;
    @FXML
    public Label textOfShowStatus;
    @FXML
    public Label textOfShowCodeTrace;
    @FXML
    public TextFlow status;
    @FXML
    public TextFlow codeTrace;
    @FXML
    public AnchorPane main;
    @FXML
    public Button newGraph;
    @FXML
    public Button dfs;
    @FXML
    public Button db;
    @FXML
    public Button djs;
    @FXML
    public Button exitButton;
    @FXML
    public ToolBar DFSBox;
    @FXML
    public ToolBar DPBox;
    @FXML
    public ToolBar djsBox;
    @FXML
    public TextArea DFSGo;
    @FXML
    public TextArea DPGo;
    @FXML
    public TextArea djsGo;
    @FXML
    public TextFlow message;
    @FXML
    public Button clearButton;
    @FXML
    public Slider sliderSpeed;
    @FXML
    public Label messageSpeed;
    @FXML
    public Button pauseButton;

    ArrayList<PseudoStep> pseudoStepArrayList = new ArrayList<>();

    private boolean panelVisable = false;
    private boolean isExample;
    private boolean isPause = false;
    private boolean isNext = false;
    private boolean isBack = false;

    Graph graph = new Graph();
    private boolean isDrag = false;
    private boolean creatButton = false;
    Robot robot = new Robot();
    Edge currentLine;
    long timeSpeed = 1000;
    int currentStep, sizeStep;
    ArrayList<PseudoStep> pseudoSteps = new ArrayList<>();

    static class InitMenu {
        TranslateTransition translateTransition;
        FadeTransition fadeTransition;
        RotateTransition rotateTransition;

        Node container;
        boolean isShow = false;


        public InitMenu(Node container, Label label) {
            this.container = container;
            translateTransition = new TranslateTransition(Duration.millis(100), container);
            fadeTransition = new FadeTransition(Duration.millis(100), container);
            rotateTransition = new RotateTransition(Duration.millis(100), label);
        }

        public InitMenu(Node container) {
            this.container = container;
            translateTransition = new TranslateTransition(Duration.millis(100), container);
            fadeTransition = new FadeTransition(Duration.millis(100), container);
        }

        public void play(int byX) {
            if (!isShow) {
                container.setVisible(true);
                translateTransition.setByX(byX);
                fadeTransition.setFromValue(0);
                fadeTransition.setToValue(1);
                rotateTransition.setByAngle(180);
                translateTransition.play();
                fadeTransition.play();
                rotateTransition.play();
                isShow = true;
            } else {
                translateTransition.setByX(-byX);
                fadeTransition.setFromValue(1);
                fadeTransition.setToValue(0);
                rotateTransition.setByAngle(-180);
                translateTransition.play();
                fadeTransition.play();
                rotateTransition.play();
                PauseTransition p = new PauseTransition(Duration.millis(100));
                p.setOnFinished(actionEvent -> container.setVisible(false));
                p.play();
                isShow = false;
            }
        }
    }

    InitMenu initPanel, initStatus, initCodeTrace;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initPanel = new InitMenu(panel, textOfShowPanel);
        initStatus = new InitMenu(status, textOfShowStatus);
        initCodeTrace = new InitMenu(codeTrace, textOfShowCodeTrace);
        main.setFocusTraversable(true);
    }

    public void showPanel() {
        initPanel.play(20);
        panelVisable = !panelVisable;
        if (!panelVisable) {
            DFSBox.setVisible(false);
            DPBox.setVisible(false);
            djsBox.setVisible(false);
        }

    }

    public void showStatus() {
        initStatus.play(-50);
    }

    public void showCodeTrace() {
        initCodeTrace.play(-50);
    }

    public void showDfsBox() {
        message.getChildren().clear();
        if (isEmpty())
            return;
        clearButton.setVisible(false);
        newGraph.setText("On Draw");
        creatButton = false;
        DFSBox.setVisible(!DFSBox.isVisible());
        DPBox.setVisible(false);
        djsBox.setVisible(false);
    }

    public void showDPBox() {
        message.getChildren().clear();
        if (isEmpty())
            return;
        clearButton.setVisible(false);
        newGraph.setText("On Draw");
        creatButton = false;
        DPBox.setVisible(!DPBox.isVisible());
        DFSBox.setVisible(false);
        djsBox.setVisible(false);
    }

    public void showDijBox() {
        message.getChildren().clear();
        if (isEmpty())
            return;
        clearButton.setVisible(false);
        newGraph.setText("On Draw");
        creatButton = false;
        djsBox.setVisible(!djsBox.isVisible());
        DFSBox.setVisible(false);
        DPBox.setVisible(false);
    }


    public void addOrLink(MouseEvent mouseEvent) {
        if (!creatButton)
            return;
        Node cur = mouseEvent.getPickResult().getIntersectedNode();
        System.out.println(cur);
        if (cur == main && !isDrag) {
            Vertex node = new Vertex();
            double x = robot.getMouseX() - main.localToScreen(main.getBoundsInLocal()).getMinX() - 22;
            double y = robot.getMouseY() - main.localToScreen(main.getBoundsInLocal()).getMinY() - 22;
            x = Math.min(x, main.getPrefWidth() - 44);
            y = Math.min(y, main.getPrefHeight() - 44);
            node.setLayoutX(x);
            node.setLayoutY(y);
            node.setId(graph.getVertices().size());
            main.getChildren().add(node);
            graph.addVertex(node);

            System.out.println(x + " " + y);

            node.setOnMouseEntered(mouseEvent1 -> node.requestFocus());
            node.setOnKeyPressed(keyEvent -> {
                        if (keyEvent.getCode() == KeyCode.DELETE && creatButton && !isDrag) {
                            graph.removeVertex(node, main);
                            refreshNode();
                        }
                    }
            );
        } else {
            Vertex currentNode;
            if (!isDrag && (cur instanceof Circle || cur instanceof Text)) {
                isDrag = true;
                currentNode = checkNode(mouseEvent);
                currentLine = new Edge();
                currentLine.setVisible(true);
                currentLine.setStartX(currentNode.getLayoutX() + 22);
                currentLine.setStartY(currentNode.getLayoutY() + 22);
                currentLine.setEndX(currentLine.getStartX());
                currentLine.setEndY(currentLine.getStartY());
                currentLine.setFrom(currentNode);
                main.getChildren().add(currentLine);

            } else if (isDrag && (cur instanceof Circle || cur instanceof Text)) {
                isDrag = false;
                currentNode = checkNode(mouseEvent);
                currentLine.setEndX(currentNode.getLayoutX() + 22);
                currentLine.setEndY(currentNode.getLayoutY() + 22);
                graph.addEdge(currentLine);
                currentLine.setTo(currentNode);
                int fromX = (int) currentLine.getFrom().getLayoutX();
                int fromY = (int) currentLine.getFrom().getLayoutY();
                int toX = (int) currentLine.getTo().getLayoutX();
                int toY = (int) currentLine.getTo().getLayoutY();
                int length = (int) Math.sqrt(Math.pow(fromX - toX, 2) + Math.pow(fromY - toY, 2)) / 50;
                currentLine.setLength(length, main);
                graph.addEdge(currentLine);
                System.out.println(graph.getEdges().isEmpty());
                System.out.println("from " + currentLine.getFrom().getID() + " to " + currentLine.getTo().getID() + " length " + length);
            }
        }
    }

    private void refreshNode() {
        graph.getVertices().forEach(stackPane -> {
                    for (Edge e : graph.getEdges()) {
                        if (e.getFrom().getID() == stackPane.getID())
                            e.getFrom().setId(graph.getVertices().indexOf(stackPane));
                        if (e.getTo().getID() == stackPane.getID())
                            e.getTo().setId(graph.getVertices().indexOf(stackPane));
                    }
                    stackPane.setId(graph.getVertices().indexOf(stackPane));

                }
        );
        for (Edge e : graph.getEdges()) {
            System.out.println("form " + e.getFrom().getID() + " to " + e.getTo().getID() + " lenght " + e.getLabel());
        }
    }

    private int checkIdNode(MouseEvent mouseEvent) {
        Parent parent = mouseEvent.getPickResult().getIntersectedNode().getParent();
        return Integer.parseInt(parent instanceof Label ? ((Label) parent).getText() :
                ((Label) ((StackPane) parent).getChildren().get(1)).getText());
    }

    private Vertex checkNode(MouseEvent mouseEvent) {
        int currId = checkIdNode(mouseEvent);
        return graph.getVertices().get(currId);
    }

    public void drawLine() {
        if (isDrag) {
            double x = robot.getMouseX() - main.getLayoutX() - main.getParent().getScene().getWindow().getX();
            double y = robot.getMouseY() - main.getLayoutY() - main.getParent().getScene().getWindow().getY();
            x = Math.min(x, main.getPrefWidth() - 1);
            y = Math.min(y, main.getPrefHeight() - 1);
            currentLine.setEndX(x);
            currentLine.setEndY(y);
        }
    }

    public boolean isEmpty() {
        int size = graph.getVertices().size();
        Text text;
        if (size == 0) {
            text = new Text("Graph cannot be empty.");
            text.setStyle("-fx-font-size: 16px; -fx-fill: gold;");
            message.getChildren().clear();
            message.getChildren().add(text);
            return true;
        }
        return false;
    }


    @FXML
    private void CreateGraph() {
        if (isDrag)
            return;
        if (isEmpty() && creatButton)
            return;
        if (isExample && !creatButton)
            clearAction();
        isExample = false;
        message.getChildren().clear();
        graph.clearHighlight();
        newGraph.setText(creatButton ? "On Draw" : "Off Draw");
        creatButton = !creatButton;
        if (creatButton)
            clearButton.setVisible(true);
        else
            clearButton.setVisible(false);
        DFSBox.setVisible(false);
        DPBox.setVisible(false);
        djsBox.setVisible(false);
    }

    public void clearAction() {
        main.getChildren().clear();
        graph.getVertices().removeAll(graph.getVertices());
        graph.getEdges().removeAll(graph.getEdges());
    }

    public void pauseAction() {
        pauseButton.setText(isPause ? "Pause" : "Remuse");
        isPause = !isPause;
        if (!isPause)
            remuse();
    }

    public void setSpeed() {
        double x = 1000 / sliderSpeed.getValue();
        timeSpeed = (long) x;
        System.out.println("x" + sliderSpeed.getValue() + " " + timeSpeed);
        messageSpeed.setText(String.format("x%.1f", sliderSpeed.getValue()));
    }

    public void moveSpeed() {
        sliderSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                messageSpeed.setText(String.format("x%.1f", (Double) t1));
            }
        });
    }

    public void backAction() {
        graph.clearHighlight();
        isPause = true;
        pauseButton.setText("Remuse");
        if (currentStep > 0)
            currentStep--;
        for (int i = 0; i <= currentStep; i++) {
            PseudoStep step = pseudoSteps.get(i);
            codeTrace.getChildren().forEach(node -> node.setStyle("-fx-font-weight: normal"));
            int idPseudo = Integer.parseInt(step.getText());
            if (idPseudo != -1)
                codeTrace.getChildren().get(idPseudo).setStyle("-fx-font-weight: bold");
            for (soloStep s : step.getDetail()) {
                if (s.getText().length() > 0) {
                    status.getChildren().clear();
                    status.getChildren().add(new Text(s.getText()));
                }
                s.run();
            }
        }
    }

    public void nextAction() {
        isPause = true;
        pauseButton.setText("Remuse");
        next();
    }


    public void next() {
        graph.clearHighlight();
        if (currentStep < pseudoSteps.size() - 1)
            currentStep++;
        for (int i = 0; i <= currentStep; i++) {
            PseudoStep step = pseudoSteps.get(i);
            codeTrace.getChildren().forEach(node -> node.setStyle("-fx-font-weight: normal"));
            int idPseudo = Integer.parseInt(step.getText());
            if (idPseudo != -1)
                codeTrace.getChildren().get(idPseudo).setStyle("-fx-font-weight: bold");
            for (soloStep s : step.getDetail()) {
                if (s.getText().length() > 0) {
                    status.getChildren().clear();
                    status.getChildren().add(new Text(s.getText()));
                }
                s.run();
            }
        }
    }

    public void remuse() {
        Task<Void> task = new Task<>() {
            @Override
            public Void call() throws Exception {
                while (currentStep < pseudoSteps.size() - 1) {
                    if (creatButton || isPause)
                        break;
                    Platform.runLater(() -> next());
                    Thread.sleep(timeSpeed);
                }
                return null;
            }
        };
        new Thread(task).start();
    }

    public void executeDFS() {
        graph.clearHighlight();
        isPause = true;
        int getSourceDfs = Integer.parseInt(DFSGo.getText());
        int size = graph.getVertices().size();
        Text text;
        if (getSourceDfs >= size || getSourceDfs < 0) {
            text = new Text("This vertex does not exist in the graph. Please select another source vertex.");
            text.setStyle("-fx-font-size: 16px; -fx-fill: gold;");
            message.getChildren().add(text);
            return;
        }
        message.getChildren().clear();
        Vertex temp = new Vertex();
        temp.setId(getSourceDfs);
        currentStep = -1;
        exploreDFS(temp);
    }


    public void exploreDFS(Vertex vertex) {
        isNext = isBack = false;
        DFS dfs = new DFS();
        dfs.setData(graph);
        dfs.falseAll();
        dfs.explore(vertex);
        pseudoSteps.clear();
        for (PseudoStep p : dfs.getPseudoSteps()) {
            pseudoSteps.add(p);
        }
        for (int i = 0; i < dfs.getPseudoCodes().size(); i++) {
            Text text = new Text(dfs.getPseudoCodes().get(i));
            text.setStyle("-fx-font-size: 16px;");
            Platform.runLater(() -> codeTrace.getChildren().add(text));
        }
        codeTrace.getChildren().clear();
        if (!initCodeTrace.isShow)
            showCodeTrace();
        if (!initStatus.isShow)
            showStatus();
        isPause = false;
        remuse();

    }

    public void executeDP() {
        message.getChildren().clear();
        isPause = true;
        int getSourceDp = Integer.parseInt(DPGo.getText());
        int size = graph.getVertices().size();
        Text text;
        if (getSourceDp >= size || getSourceDp < 0) {
            text = new Text("This vertex does not exist in the graph. Please select another source vertex.");
            text.setStyle("-fx-font-size: 16px; -fx-fill: gold;");
            message.getChildren().add(text);
            return;
        }
        message.getChildren().clear();
        Vertex temp2 = new Vertex();
        temp2.setId(getSourceDp);
        currentStep = -1;
        exploreDP(temp2);
        graph.clearHighlight();
    }

    public void exploreDP(Vertex v) {
        DP dp = new DP();
        dp.setData(graph);
        dp.falseAll();
        if (dp.isDAG()) {
            dp.topoSort();
            dp.explore(v);
            pseudoSteps.clear();
            for (PseudoStep p : dp.getPseudoSteps()) {
                pseudoSteps.add(p);
            }
            codeTrace.getChildren().clear();
            if (!initCodeTrace.isShow)
                showCodeTrace();
            if (!initStatus.isShow)
                showStatus();
            for (int i = 0; i < dp.getPseudoCodes().size(); i++) {
                Text text = new Text(dp.getPseudoCodes().get(i));
                text.setStyle("-fx-font-size: 16px");
                Platform.runLater(() -> codeTrace.getChildren().add(text));
            }
            isPause = false;
            remuse();
        } else {
            Text text;
            text = new Text("Graph is not a Directed Acyclic Graph.");
            text.setStyle("-fx-font-size: 16px; -fx-fill: gold;");
            message.getChildren().add(text);
        }
    }

    public void executeDijkstra() {
        graph.clearHighlight();
        isPause = true;
        int getSourceDij = Integer.parseInt(djsGo.getText());
        int size = graph.getVertices().size();
        Text text;
        if (getSourceDij >= size || getSourceDij < 0) {
            text = new Text("This vertex does not exist in the graph. Please select another source vertex.");
            text.setStyle("-fx-font-size: 16px; -fx-fill: gold;");
            message.getChildren().add(text);
            return;
        }
        message.getChildren().clear();
        Vertex temp = new Vertex();
        temp.setId(getSourceDij);
        isPause = false;
        currentStep = -1;
        exploreDij(temp);
    }

    public void exploreDij(Vertex vertex) {
        Dijkstra dijkstra = new Dijkstra();
//        graph.highlightSource(vertex);
        dijkstra.setData(graph);
        dijkstra.explore(vertex);
        pseudoSteps.clear();
        for (PseudoStep p : dijkstra.getPseudoSteps()) {
            pseudoSteps.add(p);
        }
        codeTrace.getChildren().clear();
        if (!initCodeTrace.isShow)
            showCodeTrace();
        if (!initStatus.isShow)
            showStatus();
        for (int i = 0; i < dijkstra.getPseudoCodes().size(); i++) {
            Text text = new Text(dijkstra.getPseudoCodes().get(i));
            text.setStyle("-fx-font-size: 16px;");
            Platform.runLater(() -> codeTrace.getChildren().add(text));
        }
        isPause = false;
        remuse();

    }

    public void showExample() {
        clearAction();
        isExample = true;
        creatButton = false;
        newGraph.setText("On Draw");
        clearButton.setVisible(false);
        double random = Math.random();
        random = random * 2 + 1;
        int randomInt = (int) random;
        System.out.println(randomInt);
        if (randomInt == 1) {
            Vertex node1 = new Vertex();
            Vertex node2 = new Vertex();
            Vertex node3 = new Vertex();
            Vertex node4 = new Vertex();
            Vertex node5 = new Vertex();
            Vertex node6 = new Vertex();
            Vertex node7 = new Vertex();

            node1.setLayoutX(471.0);
            node1.setLayoutY(44.0);
            node1.setId(graph.getVertices().size());
            main.getChildren().add(node1);
            graph.addVertex(node1.getID());

            node2.setLayoutX(371.800048828125);
            node2.setLayoutY(189.60000610351562);
            node2.setId(graph.getVertices().size());
            main.getChildren().add(node2);
            graph.addVertex(node2.getID());

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node1.getLayoutX() + 22);
            currentLine.setStartY(node1.getLayoutY() + 22);
            currentLine.setEndX(node2.getLayoutX() + 22);
            currentLine.setEndY(node2.getLayoutY() + 22);
            currentLine.setFrom(node1);
            currentLine.setTo(node2);
            int fromX = (int) currentLine.getFrom().getLayoutX();
            int fromY = (int) currentLine.getFrom().getLayoutY();
            int toX = (int) currentLine.getTo().getLayoutX();
            int toY = (int) currentLine.getTo().getLayoutY();
            int length = (int) Math.sqrt(Math.pow(fromX - toX, 2) + Math.pow(fromY - toY, 2)) / 50;
            currentLine.setLength(length, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);


            node3.setLayoutX(576.5999755859375);
            node3.setLayoutY(190.4000244140625);
            node3.setId(graph.getVertices().size());
            main.getChildren().add(node3);
            graph.addVertex(node3.getID());

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node1.getLayoutX() + 22);
            currentLine.setStartY(node1.getLayoutY() + 22);
            currentLine.setEndX(node3.getLayoutX() + 22);
            currentLine.setEndY(node3.getLayoutY() + 22);
            currentLine.setFrom(node1);
            currentLine.setTo(node3);
            int fromX1 = (int) currentLine.getFrom().getLayoutX();
            int fromY1 = (int) currentLine.getFrom().getLayoutY();
            int toX1 = (int) currentLine.getTo().getLayoutX();
            int toY1 = (int) currentLine.getTo().getLayoutY();
            int length1 = (int) Math.sqrt(Math.pow(fromX1 - toX1, 2) + Math.pow(fromY1 - toY1, 2)) / 50;
            currentLine.setLength(length1, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            node4.setLayoutX(292.5999755859375);
            node4.setLayoutY(380.79998779296875);
            node4.setId(graph.getVertices().size());
            main.getChildren().add(node4);
            graph.addVertex(node4.getID());

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node2.getLayoutX() + 22);
            currentLine.setStartY(node2.getLayoutY() + 22);
            currentLine.setEndX(node4.getLayoutX() + 22);
            currentLine.setEndY(node4.getLayoutY() + 22);
            currentLine.setFrom(node2);
            currentLine.setTo(node4);
            int fromX2 = (int) currentLine.getFrom().getLayoutX();
            int fromY2 = (int) currentLine.getFrom().getLayoutY();
            int toX2 = (int) currentLine.getTo().getLayoutX();
            int toY2 = (int) currentLine.getTo().getLayoutY();
            int length2 = (int) Math.sqrt(Math.pow(fromX2 - toX2, 2) + Math.pow(fromY2 - toY2, 2)) / 50;
            currentLine.setLength(length, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            node5.setLayoutX(407.0);
            node5.setLayoutY(380.79998779296875);
            node5.setId(graph.getVertices().size());
            main.getChildren().add(node5);
            graph.addVertex(node5.getID());

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node2.getLayoutX() + 22);
            currentLine.setStartY(node2.getLayoutY() + 22);
            currentLine.setEndX(node5.getLayoutX() + 22);
            currentLine.setEndY(node5.getLayoutY() + 22);
            currentLine.setFrom(node2);
            currentLine.setTo(node5);
            int fromX3 = (int) currentLine.getFrom().getLayoutX();
            int fromY3 = (int) currentLine.getFrom().getLayoutY();
            int toX3 = (int) currentLine.getTo().getLayoutX();
            int toY3 = (int) currentLine.getTo().getLayoutY();
            int length3 = (int) Math.sqrt(Math.pow(fromX3 - toX3, 2) + Math.pow(fromY3 - toY3, 2)) / 50;
            currentLine.setLength(length3, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            node6.setLayoutX(601.4000244140625);
            node6.setLayoutY(376.8000183105469);
            node6.setId(graph.getVertices().size());
            main.getChildren().add(node6);
            graph.addVertex(node6.getID());

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node3.getLayoutX() + 22);
            currentLine.setStartY(node3.getLayoutY() + 22);
            currentLine.setEndX(node6.getLayoutX() + 22);
            currentLine.setEndY(node6.getLayoutY() + 22);
            currentLine.setFrom(node3);
            currentLine.setTo(node6);
            int fromX4 = (int) currentLine.getFrom().getLayoutX();
            int fromY4 = (int) currentLine.getFrom().getLayoutY();
            int toX4 = (int) currentLine.getTo().getLayoutX();
            int toY4 = (int) currentLine.getTo().getLayoutY();
            int length4 = (int) Math.sqrt(Math.pow(fromX4 - toX4, 2) + Math.pow(fromY4 - toY4, 2)) / 50;
            currentLine.setLength(length4, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            node7.setLayoutX(716.5999755859375);
            node7.setLayoutY(371.20001220703125);
            node7.setId(graph.getVertices().size());
            main.getChildren().add(node7);
            graph.addVertex(node7.getID());

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node3.getLayoutX() + 22);
            currentLine.setStartY(node3.getLayoutY() + 22);
            currentLine.setEndX(node7.getLayoutX() + 22);
            currentLine.setEndY(node7.getLayoutY() + 22);
            currentLine.setFrom(node3);
            currentLine.setTo(node7);
            int fromX5 = (int) currentLine.getFrom().getLayoutX();
            int fromY5 = (int) currentLine.getFrom().getLayoutY();
            int toX5 = (int) currentLine.getTo().getLayoutX();
            int toY5 = (int) currentLine.getTo().getLayoutY();
            int length5 = (int) Math.sqrt(Math.pow(fromX5 - toX5, 2) + Math.pow(fromY5 - toY5, 2)) / 50;
            currentLine.setLength(length5, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);
        } else {
            Vertex node1 = new Vertex();
            Vertex node2 = new Vertex();
            Vertex node3 = new Vertex();
            Vertex node4 = new Vertex();
            Vertex node5 = new Vertex();


            node1.setLayoutX(439.79998779296875);
            node1.setLayoutY(20.000000000000007);
            node1.setId(graph.getVertices().size());
            main.getChildren().add(node1);
            graph.addVertex(node1.getID());

            node2.setLayoutX(309.4000244140625);
            node2.setLayoutY(192.0);
            node2.setId(graph.getVertices().size());
            main.getChildren().add(node2);
            graph.addVertex(node2.getID());

            node3.setLayoutX(572.5999755859375);
            node3.setLayoutY(186.39999389648438);
            node3.setId(graph.getVertices().size());
            main.getChildren().add(node3);
            graph.addVertex(node3.getID());

            node4.setLayoutX(446.199951171875);
            node4.setLayoutY(358.3999938964844);
            node4.setId(graph.getVertices().size());
            main.getChildren().add(node4);
            graph.addVertex(node4.getID());

            node5.setLayoutX(678.2000122070312);
            node5.setLayoutY(349.5999755859375);
            node5.setId(graph.getVertices().size());
            main.getChildren().add(node5);
            graph.addVertex(node5.getID());

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node1.getLayoutX() + 22);
            currentLine.setStartY(node1.getLayoutY() + 22);
            currentLine.setEndX(node2.getLayoutX() + 22);
            currentLine.setEndY(node2.getLayoutY() + 22);
            currentLine.setFrom(node1);
            currentLine.setTo(node2);
            int fromX = (int) currentLine.getFrom().getLayoutX();
            int fromY = (int) currentLine.getFrom().getLayoutY();
            int toX = (int) currentLine.getTo().getLayoutX();
            int toY = (int) currentLine.getTo().getLayoutY();
            int length = (int) Math.sqrt(Math.pow(fromX - toX, 2) + Math.pow(fromY - toY, 2)) / 50;
            currentLine.setLength(length, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node1.getLayoutX() + 22);
            currentLine.setStartY(node1.getLayoutY() + 22);
            currentLine.setEndX(node3.getLayoutX() + 22);
            currentLine.setEndY(node3.getLayoutY() + 22);
            currentLine.setFrom(node1);
            currentLine.setTo(node3);
            int fromX1 = (int) currentLine.getFrom().getLayoutX();
            int fromY1 = (int) currentLine.getFrom().getLayoutY();
            int toX1 = (int) currentLine.getTo().getLayoutX();
            int toY1 = (int) currentLine.getTo().getLayoutY();
            int length1 = (int) Math.sqrt(Math.pow(fromX1 - toX1, 2) + Math.pow(fromY1 - toY1, 2)) / 50;
            currentLine.setLength(length1, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node3.getLayoutX() + 22);
            currentLine.setStartY(node3.getLayoutY() + 22);
            currentLine.setEndX(node2.getLayoutX() + 22);
            currentLine.setEndY(node2.getLayoutY() + 22);
            currentLine.setFrom(node3);
            currentLine.setTo(node2);
            int fromX2 = (int) currentLine.getFrom().getLayoutX();
            int fromY2 = (int) currentLine.getFrom().getLayoutY();
            int toX2 = (int) currentLine.getTo().getLayoutX();
            int toY2 = (int) currentLine.getTo().getLayoutY();
            int length2 = (int) Math.sqrt(Math.pow(fromX2 - toX2, 2) + Math.pow(fromY2 - toY2, 2)) / 50;
            currentLine.setLength(length2, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node3.getLayoutX() + 22);
            currentLine.setStartY(node3.getLayoutY() + 22);
            currentLine.setEndX(node4.getLayoutX() + 22);
            currentLine.setEndY(node4.getLayoutY() + 22);
            currentLine.setFrom(node3);
            currentLine.setTo(node4);
            int fromX3 = (int) currentLine.getFrom().getLayoutX();
            int fromY3 = (int) currentLine.getFrom().getLayoutY();
            int toX3 = (int) currentLine.getTo().getLayoutX();
            int toY3 = (int) currentLine.getTo().getLayoutY();
            int length3 = (int) Math.sqrt(Math.pow(fromX3 - toX3, 2) + Math.pow(fromY3 - toY3, 2)) / 50;
            currentLine.setLength(length3, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node3.getLayoutX() + 22);
            currentLine.setStartY(node3.getLayoutY() + 22);
            currentLine.setEndX(node5.getLayoutX() + 22);
            currentLine.setEndY(node5.getLayoutY() + 22);
            currentLine.setFrom(node3);
            currentLine.setTo(node5);
            int fromX4 = (int) currentLine.getFrom().getLayoutX();
            int fromY4 = (int) currentLine.getFrom().getLayoutY();
            int toX4 = (int) currentLine.getTo().getLayoutX();
            int toY4 = (int) currentLine.getTo().getLayoutY();
            int length4 = (int) Math.sqrt(Math.pow(fromX4 - toX4, 2) + Math.pow(fromY4 - toY4, 2)) / 50;
            currentLine.setLength(length4, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node2.getLayoutX() + 22);
            currentLine.setStartY(node2.getLayoutY() + 22);
            currentLine.setEndX(node4.getLayoutX() + 22);
            currentLine.setEndY(node4.getLayoutY() + 22);
            currentLine.setFrom(node2);
            currentLine.setTo(node4);
            int fromX5 = (int) currentLine.getFrom().getLayoutX();
            int fromY5 = (int) currentLine.getFrom().getLayoutY();
            int toX5 = (int) currentLine.getTo().getLayoutX();
            int toY5 = (int) currentLine.getTo().getLayoutY();
            int length5 = (int) Math.sqrt(Math.pow(fromX5 - toX5, 2) + Math.pow(fromY5 - toY5, 2)) / 50;
            currentLine.setLength(length5, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);

            currentLine = new Edge();
            currentLine.setVisible(true);
            currentLine.setStartX(node5.getLayoutX() + 22);
            currentLine.setStartY(node5.getLayoutY() + 22);
            currentLine.setEndX(node4.getLayoutX() + 22);
            currentLine.setEndY(node4.getLayoutY() + 22);
            currentLine.setFrom(node5);
            currentLine.setTo(node4);
            int fromX6 = (int) currentLine.getFrom().getLayoutX();
            int fromY6 = (int) currentLine.getFrom().getLayoutY();
            int toX6 = (int) currentLine.getTo().getLayoutX();
            int toY6 = (int) currentLine.getTo().getLayoutY();
            int length6 = (int) Math.sqrt(Math.pow(fromX6 - toX6, 2) + Math.pow(fromY6 - toY2, 2)) / 50;
            currentLine.setLength(length6, main);
            main.getChildren().add(currentLine);
            graph.addEdge(currentLine);
        }
    }

    public void exitEvent(MouseEvent mouseEvent) {
        Node cur = mouseEvent.getPickResult().getIntersectedNode();
        if (cur == exitButton)
            exitButton.setStyle("-fx-background-radius: 10px; -fx-background-color: #ff0000;");
        else
            exitButton.setStyle("-fx-background-radius: 10px; -fx-background-color: #222426");
    }

    public void exit() {
        System.exit(0);
    }

}
