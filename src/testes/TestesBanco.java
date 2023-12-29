package testes;

import modelo.Produto;

public class TestesBanco {
    public static void main(String[] args) {
        // Testando a classe Produto:

        // Cadastrando um novo produto
        final int ID = 1;
        boolean cadastroSucesso = Produto.cadastrarProduto("Notebook", 1999.99, 1, 2, ID);
        System.out.printf("Cadastro bem-sucedido? %b%n", cadastroSucesso);

        // Obtendo um produto por ID
        Produto produtoObtido = Produto.obterProdutoPorID(ID);
        System.out.println(produtoObtido);
        if (produtoObtido != null) {
            System.out.printf("Produto obtido: %s%n", produtoObtido);

            // Atualizando o produto
            boolean atualizacaoSucesso = produtoObtido.atualizarProduto("Laptop", 2199.99, 1, 2, ID);
            System.out.printf("Atualização bem-sucedida? %b%n", atualizacaoSucesso);

            // Obtendo novamente o produto para verificar a atualização
            Produto produtoAtualizado = Produto.obterProdutoPorID(ID);
            System.out.printf("Produto atualizado: %s%n", produtoAtualizado);

            // Removendo o produto
            boolean remocaoSucesso = produtoAtualizado.removerProduto(ID);
            System.out.printf("Remoção bem-sucedida? %b%n", remocaoSucesso);
        } else {
            System.out.println("Produto não encontrado.");
        }
    }
}

