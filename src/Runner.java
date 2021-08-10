import java.util.*;

//По данной непустой строке s длины не более 10^4, состоящей из строчных букв латинского алфавита, постройте оптимальный беспрефиксный код. 
//В первой строке выведите количество различных букв k, встречающихся в строке, и размер получившейся закодированной строки. 
//В следующих k строках запишите коды букв в формате "letter: code". В последней строке выведите закодированную строку.

//TestInput1: a
//Output: 
//1 1
//a: 0
//0

//TestInput2: abacabad
//4 14
//a: 0
//b: 10
//c: 110
//d: 111
//01001100100111

class Runner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.next();

        Map<Character, Integer> symbolMap = new HashMap<>();

        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            if (symbolMap.containsKey(currentChar)) {
                symbolMap.put(currentChar, symbolMap.get(currentChar) + 1);
            } else {
                symbolMap.put(currentChar, 1);
            }
        }

        PriorityQueue<HuffmanTree.Node> nodePriorityQueue = new PriorityQueue<>(Comparator.comparingInt(HuffmanTree.Node::getNumberOfInputs));
        for (Map.Entry<Character, Integer> entry : symbolMap.entrySet()) {
            nodePriorityQueue.add(new HuffmanTree.Leave(entry.getKey(), entry.getValue()));
        }

        int length = 0;
        while (nodePriorityQueue.size() >= 2){
            HuffmanTree.Node currentVertex = new HuffmanTree.Vertex(nodePriorityQueue.poll(), nodePriorityQueue.poll());
            length += currentVertex.numberOfInputs;
            nodePriorityQueue.add(currentVertex);
        }

        if (length == 0){
            length = symbolMap.get(inputString.charAt(0));
        }
        System.out.println(symbolMap.size()+ " " + length);

        HuffmanTree.Node head = nodePriorityQueue.poll();
        head.buildKey("");

        for (int i = 0; i < inputString.length(); i++) {
            System.out.print(head.searchKey(inputString.charAt(i)));
        }
    }
}

class HuffmanTree{

    static abstract class Node{
        int numberOfInputs;

        String key;

        public int getNumberOfInputs() {
            return numberOfInputs;
        }

        public abstract void buildKey(String key);

        public abstract String searchKey(char symbol);
    }

    static class Leave extends Node{
        char symbol;

        public Leave(char symbol, int numberOfInputs) {
            this.symbol = symbol;
            this.numberOfInputs = numberOfInputs;
        }

        @Override
        public void buildKey(String key) {
            if(key.equals("")){
                key = "0";
            }
            this.key = key;
            System.out.println(symbol + ": " + this.key);
        }

        @Override
        public String searchKey(char symbol) {
            return this.symbol == symbol ? this.key : null;
        }
    }

    static class Vertex extends Node{
        Node leftSon;
        Node rightSon;

        public Vertex(Node leftSon, Node rightSon) {
            this.leftSon = leftSon;
            this.rightSon = rightSon;
            this.numberOfInputs = leftSon.numberOfInputs + rightSon.numberOfInputs;
        }

        @Override
        public void buildKey(String key) {
            this.key = key;
            leftSon.buildKey(key + '0');
            rightSon.buildKey(key + '1');
        }

        @Override
        public String searchKey(char symbol) {
            String leftSonSearch = leftSon.searchKey(symbol);
            String rightSonSearch = rightSon.searchKey(symbol);

            if (leftSonSearch != null){
                return leftSonSearch;
            }
            return rightSonSearch;
        }
    }
}
