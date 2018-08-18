package engine;

import common.GameType;
import common.GameVariant;
import common.PlayersTypes;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameSettings implements Serializable {
    final int maxNumOfPlayers = 6;
    final int minNumOfPlayers = 2;
    private int target;
    private int boardNumRows;
    private int boardNumCols;
    private GameVariant gameVariant;
    private GameType gameType;
    private String settingsFilePath;
    private List<Player> players;
    private boolean isDynamicPlayers;
    private int numOfPlayers;
    private String gameTitle;

    GameSettings() {
        this.boardNumRows = 6;
        this.boardNumCols = 7;
        this.players = new ArrayList<Player>(maxNumOfPlayers);
        this.isDynamicPlayers = true;
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

    public boolean isDynamicPlayers() {
        return isDynamicPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void initGameSettings(String settingsFilePath) throws Exception {
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
            parseGameSettingFile(doc);
        } catch (SettingsFileException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    private void parseGameSettingFile(Node doc) throws SettingsFileException {
        if (doc.getNodeType() == Node.DOCUMENT_NODE) {
            if (doc.getChildNodes().getLength() == 0) {
                throw new SettingsFileException("xml: xml file has no elements");
            }
            else if (doc.getChildNodes().getLength() > 1) {
                throw new SettingsFileException("xml: xml file has more than a single root element");
            }
            else {
                Node rootElement = doc.getChildNodes().item(0);
                if (rootElement.getNodeType() == Node.ELEMENT_NODE && rootElement.getNodeName() == "GameDescriptor") {
                    NodeList mainElements = rootElement.getChildNodes();
                    if (mainElements != null) {
                        try {
                            List<Node> gameTypeNodes = getNodesWithNameAndType(mainElements, Node.ELEMENT_NODE, "GameType");
                            parseGameTypeNode(gameTypeNodes);

                            List<Node> gameNodes = getNodesWithNameAndType(mainElements, Node.ELEMENT_NODE, "Game");
                            parseGameNode(gameNodes);

                            // parsing DynampicPlayers before Players, to validate number of players
                            List<Node> dynamicPlayersNodes = getNodesWithNameAndType(mainElements, Node.ELEMENT_NODE, "DynamicPlayers");
                            parseDynamicPlayersNode(dynamicPlayersNodes);

                            List<Node> playersNodes = getNodesWithNameAndType(mainElements, Node.ELEMENT_NODE, "Players");
                            parsePlayersParentNode(playersNodes);

                            if (players.size() != numOfPlayers) {
                                throw new SettingsFileException("xml: the number of Players in the Players list doen's match the number defined in the DynamicPlayers node");
                            }

                        }
                        catch (SettingsFileException e) {
                            throw e;
                        }
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

    private void parseDynamicPlayersNode(List<Node> dynamicPlayersNodes) throws SettingsFileException {
        if (dynamicPlayersNodes.size() != 0) {
            Node dynamicPlayersNode = dynamicPlayersNodes.get(0);
            NamedNodeMap dynamicPlayersAttributes = dynamicPlayersNode.getAttributes();

            if (dynamicPlayersAttributes.getLength() != 0) {
                try {
                    Node gameTitle = dynamicPlayersAttributes.getNamedItem("game-title");
                    parseGameTitle(gameTitle);

                    Node numPlayers = dynamicPlayersAttributes.getNamedItem("total-players");
                    parseNumPlayers(numPlayers);
                }
                catch (SettingsFileException e) {
                    throw e;
                }

            }
            else {
                throw new SettingsFileException("xml: DynamicPlayers element has no attributes");
            }
        }
        else {
            this.isDynamicPlayers = false;
            numOfPlayers = 2;
        }
    }

    private void parseNumPlayers(Node numPlayers) throws SettingsFileException {
        if (numPlayers != null) {
            try {
                int numPlayersVal  = Integer.parseInt(numPlayers.getChildNodes().item(0).getTextContent());

                if (numPlayersVal >= minNumOfPlayers && numPlayersVal <= maxNumOfPlayers) {
                    this.numOfPlayers = numPlayersVal;
                }
                else {
                    throw new SettingsFileException("xml: defined number of players is " + numPlayersVal);
                }
            }
            catch (NumberFormatException e) {
                throw new SettingsFileException("xml: the value of total-players attribute is not an int");
            }
        }
        else {
            throw new SettingsFileException("xml: DynamicPlayers has no total-players attribute");
        }
    }

    private void parseGameTitle(Node gameTitle) throws SettingsFileException{
        if (gameTitle != null) {
            this.gameTitle = gameTitle.getChildNodes().item(0).getTextContent();
        }
        else {
            throw new SettingsFileException("xml: DynamicPlayers has no game-title attribute");
        }
    }

    private void parsePlayersParentNode(List<Node> playersParent) throws SettingsFileException {
        if (playersParent.size() != 0 && this.isDynamicPlayers) {
            NodeList PlayersChildren = playersParent.get(0).getChildNodes();
            List<Node> playersNodes = getNodesWithNameAndType(PlayersChildren, Node.ELEMENT_NODE, "Player");
            parsePlayersNodeList(playersNodes);
        }
        else {
            throw new SettingsFileException("xml: number of players in Players list and DynamicPlayers does not match");
        }
    }

    private void parsePlayersNodeList(List<Node> playersNode) throws SettingsFileException{
        for (Node player : playersNode) {
            try {
                parseSinglePlayerNode(player);
            }
            catch (SettingsFileException e) {
                throw e;
            }
        }
    }

    private void parseSinglePlayerNode(Node playerNode) throws SettingsFileException {
        NamedNodeMap playerAttributes = playerNode.getAttributes();

        if (playerAttributes.getLength() != 0) {
            Node idAttr = playerAttributes.getNamedItem("id");

            if (idAttr != null) {
                try {
                    int playerId = Integer.parseInt(idAttr.getNodeValue());

                    for (Player p : players) {
                        if (p.getId() == playerId) {
                            throw new SettingsFileException("xml: Players list has more than one player with the id: " + playerId);
                        }
                    }
                    parsePlayerElement(playerNode, playerId);
                }
                catch (NumberFormatException e) {
                    throw new SettingsFileException("xml: the value of player id attribute is not an int");
                }
                catch (SettingsFileException e) {
                    throw e;
                }
            }
            else {
                throw new SettingsFileException("xml: Player element has no id attribute");
            }
        }
        else {
            throw new SettingsFileException("xml: Player element has no attributes");
        }

    }

    private void parsePlayerElement(Node playerNode, int playerId) throws SettingsFileException {
        String actualPlayerName = "";
        PlayersTypes actualPlayerType = PlayersTypes.HUMAN;

        NodeList playerElements = playerNode.getChildNodes();

        if (playerElements.getLength() != 0) {
            List<Node> playerNames = getNodesWithNameAndType(playerElements, Node.ELEMENT_NODE, "Name");
            if (playerNames.size() != 0) {
                try {
                    actualPlayerName = parsePlayerName(playerNames.get(0), playerId);
                }
                catch (SettingsFileException e) {
                    throw e;
                }
            }
            else {
                throw  new SettingsFileException("xml: player " + playerId + "has no name element");
            }

            List<Node> playerTypes = getNodesWithNameAndType(playerElements, Node.ELEMENT_NODE, "Type");
            if (playerTypes.size() != 0) {
                try {
                    actualPlayerType = parsePlayerType(playerTypes.get(0), playerId);
                }
                catch (SettingsFileException e) {
                    throw e;
                }
            }
            else {
                throw  new SettingsFileException("xml: player " + playerId + "has no type element");
            }
        }
        else {
            throw new SettingsFileException("xml: player " + playerId + "has no elemetns");
        }

        players.add(new Player(playerId, actualPlayerType, actualPlayerName));
    }

    private PlayersTypes parsePlayerType(Node playerType, int playerID) throws SettingsFileException{
        boolean isLegalPlayerType = false;
        NodeList playerTypeVal = playerType.getChildNodes();
        PlayersTypes actualPlayerType = PlayersTypes.HUMAN;

        if (playerTypeVal.getLength() != 0) {
            String playerTypeStr = playerTypeVal.item(0).getTextContent();
            PlayersTypes playerTypes[] = PlayersTypes.values();

            for (PlayersTypes pt : playerTypes) {
                if (pt.name().equalsIgnoreCase(playerTypeStr)) {
                    isLegalPlayerType = true;
                    actualPlayerType = pt;
                    break;
                }
            }

            if (!isLegalPlayerType) {
                throw new SettingsFileException("xml: the value of player type for player " + playerID + "is illegal");
            }
        }
        else {
            throw new SettingsFileException("xml: the player type element for player " + playerID + "has no value");
        }

        return actualPlayerType;
    }

    private String parsePlayerName(Node playerName, int playerID) throws SettingsFileException {
        NodeList playerNameVal = playerName.getChildNodes();
        String actualName = "";

        if (playerNameVal.getLength() != 0) {
            actualName = playerNameVal.item(0).getTextContent();
        }
        else {
            throw new SettingsFileException("xml: the player type element for player " + playerID  + "has no value");
        }

        return actualName;
    }

    private void parseGameNode(List<Node> gameNodes) throws SettingsFileException {
        if (gameNodes.size() != 0) {
            Node gameNode = gameNodes.get(0);
            try {
                // validating board's rows and columns number against the input target
                parseTargetAttr(gameNode);
                parseGameNodeChildElements(gameNode);
            } catch (SettingsFileException e) {
                throw e;
            }
        }
        else {
            throw new SettingsFileException("xml: GameDescriptor has no Game element");
        }
    }

    private void parseGameNodeChildElements(Node gameNode) throws SettingsFileException {
        NodeList gameNodeChildNodes = gameNode.getChildNodes();

        List<Node> boardNodes = getNodesWithNameAndType(gameNodeChildNodes, Node.ELEMENT_NODE, "Board");
        if (boardNodes.size() != 0) {
            parseBoardNode(boardNodes.get(0));
        }
        else {
            throw new SettingsFileException("xml: the Game element has no Board element");
        }

        List<Node> variantNodes = getNodesWithNameAndType(gameNodeChildNodes, Node.ELEMENT_NODE, "Variant");
        if (boardNodes.size() != 0) {
            parseVariantNode(variantNodes.get(0));
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
                            boardNumRows = rowsNum;
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
                            boardNumCols = colsNum;
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

    private void parseGameTypeNode(List<Node> gameTypeNodes) throws SettingsFileException {
        if (gameTypeNodes.size() != 0) {
            Node gameTypeNode = gameTypeNodes.get(0);
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

    private List<Node> getNodesWithNameAndType(NodeList nodeList, short nodeType, String nodeName) {
        List<Node> res = new ArrayList<Node>();

        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node currentNode = nodeList.item(i);
            if (currentNode.getNodeType() == nodeType && currentNode.getNodeName() == nodeName) {
                res.add(currentNode);
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
