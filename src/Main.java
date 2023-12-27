import modelo.Produto;

public final class Main {
    public static void main(String[] args) {
        // Testando a classe Produto

        // Cadastrando um novo produto
        boolean cadastroSucesso = Produto.cadastrarProduto("Notebook", 1999.99, 1, 2, 1);
        System.out.println("Cadastro bem-sucedido? " + cadastroSucesso);

        // Obtendo um produto por ID
        Produto produtoObtido = Produto.obterProdutoPorID(1);
        if (produtoObtido != null) {
            System.out.println("Produto obtido: " + produtoObtido);

            // Atualizando o produto
            boolean atualizacaoSucesso = produtoObtido.atualizarProduto("Laptop", 2199.99, 1, 2, 1);
            System.out.println("Atualização bem-sucedida? " + atualizacaoSucesso);

            // Obtendo novamente o produto para verificar a atualização
            Produto produtoAtualizado = Produto.obterProdutoPorID(1);
            System.out.println("Produto atualizado: " + produtoAtualizado);

            // Removendo o produto
            boolean remocaoSucesso = produtoAtualizado.removerProduto(1);
            System.out.println("Remoção bem-sucedida? " + remocaoSucesso);
        }
    }
}
