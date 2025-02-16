import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Stack;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, forneça uma URL.");
            return;
        }

        String url = args[0];

        try {
            URL htmlUrl = new URL(url);
            BufferedReader reader = new BufferedReader(new InputStreamReader(htmlUrl.openStream()));

            int depth = 0; // Define o nível de profundidade atual
            String deepestText = ""; // Texto do nível mais profundo
            int maxDepth = -1; // Profundidade máxima encontrada
            Stack<String> tagStack = new Stack<>(); 

            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim(); // Remove espaços iniciais e finais

                if (line.isEmpty()) {
                    continue; // Ignora linhas em branco
                }

                if (line.startsWith("<") && line.endsWith(">")) {
                    // É uma tag
                    if (line.startsWith("</")) {
                        // Tag de fechamento
                        depth--;
                        if (!tagStack.isEmpty() && tagStack.peek().equals(line.substring(2, line.length() - 1))) {
                            tagStack.pop(); // Remove a tag da pilha
                        } else {
                            // HTML malformado
                            System.out.println("malformed HTML");
                            return;
                        }
                    } else {
                        // Tag de abertura
                        depth++;
                        tagStack.push(line.substring(1, line.length() - 1)); // Adiciona a tag à pilha
                    }
                } else {
                    if (depth > maxDepth) {
                        maxDepth = depth;
                        deepestText = line;
                    }
                }
            }

            // Verifica se todas as tags foram fechadas da forma correta
            if (!tagStack.isEmpty()) {
                System.out.println("malformed HTML");
                return;
            }

            // Imprime o texto mais profundo
            System.out.println(deepestText);

            reader.close();
        } catch (Exception e) {
            System.out.println("URL connection error"); // em caso de problema de conexão imprime essa mensagem
        }
    }
}