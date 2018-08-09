package engine;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;

public class GameSettings implements Serializable {
    private int target;
    private int boardNumRows;
    private int boardNumCols;
    private GameVariant gameVariant;
    private GameType gameType;
    private File settingsFile;
    private String settingsFilePath;
    private int numOfPlayers;

    GameSettings() {
        this.numOfPlayers = 2;
        this.settingsFilePath = settingsFilePath;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void setBoardNumRows (int numRows) {
        this.boardNumRows = numRows;
    }

    public void setBoardNumCols (int numCols) {
        this.boardNumCols = numCols;
    }

    public void setGameVariant(GameVariant gameVariant) {
        this.gameVariant = gameVariant;
    }

    public int getTarget() {
        return this.target;
    }

    public int getBoardNumRows() {
        return this.boardNumRows;
    }

    public int getBoardNumCols() {
        return this.boardNumCols;
    }

    public GameVariant getGameVariant() {
        return this.gameVariant;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void initGameSettings(String settingsFilePath) throws Exception {
        String extension = "";
        this.settingsFilePath = settingsFilePath;
        int i = settingsFilePath.lastIndexOf('.');

        if (i > 0 && settingsFilePath.substring(i).equals(".xml")) {
            File gameSettingsFile = new File(settingsFilePath);
            if (!gameSettingsFile.exists()) {
                throw new SettingsFileException("file: the file in the given path does not exist");
            }
            readGameInfoFromFile(gameSettingsFile);
        } else {
            throw new SettingsFileException("file: the given path is not of an XML file");
        }
    }

    private void readGameInfoFromFile(File gameSettingsFile) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(gameSettingsFile);
            initGameSetting(doc);
        } catch (SettingsFileException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private void initGameSetting(Node doc) throws SettingsFileException {
        if (doc.getNodeType() == Node.DOCUMENT_NODE) {
            if (doc.getChildNodes().getLength() == 0) {
                throw new SettingsFileException("xml: xml file has no elements!");
            }
            else if (doc.getChildNodes().getLength() > 1) {
                throw new SettingsFileException("xml: xml file has more than a single root element!");
            }
            else {
                Node rootElement = doc.getChildNodes().item(0);
                if (rootElement.getNodeType() == Node.ELEMENT_NODE && rootElement.getNodeName() == "GameDescriptor") {
                    NodeList mainElements = rootElement.getChildNodes();
                    if (mainElements != null) {
                        Node gameTypeNode = getNodeWithNameAndType(mainElements, Node.ELEMENT_NODE, "GameType");
                        parseGameTypeNode(gameTypeNode);

                        Node gameNode = getNodeWithNameAndType(mainElements, Node.ELEMENT_NODE, "Game");
                        parseGameNode(gameNode);
                    }
                    else {
                        throw new SettingsFileException("xml: GameDescriptor has no elements");
                    }
                }
                else {
                    throw new SettingsFileException("xml: root element in xml file is not GameDescriptor");
                }
            }

        }
    }

    private void parseGameNode(Node gameNode) throws SettingsFileException {
        if (gameNode != null) {
            try {
                // validating board's rows and columns number against the input target
                parseTargetAttr(gameNode);
                parseGameNodeChildElements(gameNode);
            } catch (SettingsFileException e) {
                throw e;
            }
        }
    }

    private void parseGameNodeChildElements(Node gameNode) throws SettingsFileException {
        NodeList gameNodeChildNodes = gameNode.getChildNodes();

        Node boardNode = getNodeWithNameAndType(gameNodeChildNodes, Node.ELEMENT_NODE, "Board");
        if (boardNode != null) {
            parseBoardNode(boardNode);
        }
        else {
            throw new SettingsFileException("xml: the Game element has no Board element");
        }

        Node variantNode = getNodeWithNameAndType(gameNodeChildNodes, Node.ELEMENT_NODE, "Variant");
        if (boardNode != null) {
            parseVariantNode(variantNode);
        }
        else {
            throw new SettingsFileException("xml: the Game element has no Variant element");
        }
    }

    private void parseVariantNode(Node variantNode) throws SettingsFileException {
        boolean isLegalGameVariantVal = false;
        NodeList gameVariantVal = variantNode.getChildNodes();
        if (gameVariantVal != null) {
            String vType = gameVariantVal.item(0).getTextContent();
            GameVariant gameVariants[] = GameVariant.values();
            for (GameVariant vt : gameVariants) {
                if (vt.name().equalsIgnoreCase(vType)) {
                    gameVariant = vt;
                    isLegalGameVariantVal = true;
                }
            }
            if (!isLegalGameVariantVal) {
                throw new SettingsFileException("xml: value of GameVariant is illegal");
            }
        }
        else {
            throw new SettingsFileException("xml: the Variant element has no value");
        }
    }

    private void parseBoardNode(Node boardNode) throws SettingsFileException {
        NamedNodeMap boardAttributes = boardNode.getAttributes();
        if (boardAttributes.getLength() != 0) {
            Node rowsNode = boardAttributes.getNamedItem("rows");
            parseBoardRows(rowsNode);

            Node colsNode = boardAttributes.getNamedItem("columns");
            parseBoardCols(colsNode);
        }
        else {
            throw new SettingsFileException("xml: Board element has no attributes");
        }
    }

    private void parseBoardRows(Node rowsAttr) throws SettingsFileException {
        if (rowsAttr != null) {
            try {
                int rowsNum = Integer.parseInt(rowsAttr.getTextContent());
                try {
                    if (rowsNum > target) {
                        if (rowsNum >= 5 && rowsNum <= 50) {
                            setBoardNumRows(rowsNum);
                        }
                        else {
                            throw new SettingsFileException("number of rows is out of valid range: " + rowsNum);
                        }
                    }
                    else {
                        throw new SettingsFileException("number of board rows is greater-equal to taget value");
                    }
                } catch (SettingsFileException e) {
                    throw e;
                }
            } catch (NumberFormatException e) {
                throw new SettingsFileException("xml: the value of rows attribute is not an int");
            }
        }
        else {
            throw new SettingsFileException("xml: Board element has no rows attribute");
        }
    }

    private void parseBoardCols(Node colsAttr) throws SettingsFileException {
        if (colsAttr != null) {
            try {
                int colsNum = Integer.parseInt(colsAttr.getTextContent());
                try {
                    if (colsNum > target) {
                        if (colsNum >= 6 && colsNum <= 30) {
                            setBoardNumCols(colsNum);
                        }
                        else {
                            throw new SettingsFileException("number of columns is out of valid range: " + colsNum);
                        }
                    }
                    else {
                        throw new SettingsFileException("number of board columns is greater-equal to taget value");
                    }
                } catch (SettingsFileException e) {
                    throw e;
                }
            } catch (NumberFormatException e) {
                throw new SettingsFileException("xml: the value of columns attribute is not an int");
            }
        }
        else {
            throw new SettingsFileException("xml: Board element has no columns attribute");
        }
    }

    private void parseTargetAttr(Node gameNode) throws SettingsFileException {
        int target = 0;
        NamedNodeMap gameAttributes = gameNode.getAttributes();
        if (gameAttributes.getLength() != 0) {
            Node targetNode = gameAttributes.getNamedItem("target");
            if (targetNode != null) {
                try {
                    target = Integer.parseInt(targetNode.getTextContent());
                    this.target = target;
                } catch (NumberFormatException e) {
                    throw new SettingsFileException("xml: the value of target attribute is not an int");
                }
            }
            else {
                throw new SettingsFileException("xml: Game element has no target attribute");
            }
        }
        else {
            throw new SettingsFileException("xml: Game element has no attributes");
        }
    }

    private void parseGameTypeNode(Node gameTypeNode) throws SettingsFileException {
        if (gameTypeNode != null) {
            boolean isLegalGameTypeVal = false;
            NodeList gameTypeVal = gameTypeNode.getChildNodes();
            if (gameTypeVal != null) {
                String gType = gameTypeVal.item(0).getTextContent();
                GameType gameTypes[] = GameType.values();
                for (GameType gt : gameTypes) {
                    if (gt.name().equalsIgnoreCase(gType)) {
                        gameType = gt;
                        isLegalGameTypeVal = true;
                    }
                }
                if (!isLegalGameTypeVal) {
                    throw new SettingsFileException("xml: value of GameType is illegal");
                }
            }
            else {
                throw new SettingsFileException("xml: the GameType element has no value");
            }
        }
        else {
            throw new SettingsFileException("xml: GameDescriptor has no game type element");
        }
    }

    private Node getNodeWithNameAndType(NodeList nodeList, short nodeType, String nodeName) {
        Node res = null;

        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == nodeType && currentNode.getNodeName() == nodeName) {
                res = currentNode;
                break;
            }
        }

        return res;
    }

    public String toString() {
        String str = "target is " + target + "\n"
                + "rows num is " + boardNumRows +"\n"
                + "cols num is " + boardNumCols +"\n"
                + "game variant is " + gameVariant + "\n"
                + "game type is " + gameType;
        return str;
    }
}
