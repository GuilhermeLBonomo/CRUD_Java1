package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static modelo.Conexao.executarComandoSQL;
import static modelo.Conexao.executarConsultaSQL;
import static modelo.FuncoesUtilitarias.*;


public final class Produto {
    private static final String DATABASE = Conexao.getDATABASE();
    private static final String TABELA = "Produto";
    private Integer ProdutoID;
    private String Nome;
    private Double Preco;
    private Integer FabricanteID;
    private Integer CategoriaID;

    public Produto() {
    }

    public Produto(final Integer produtoID, final String nome, final Double preco, final Integer fabricanteID, final Integer categoriaID) {
        this.setProdutoID(produtoID);
        this.setNome(nome);
        this.setPreco(preco);
        this.setFabricanteID(fabricanteID);
        this.setCategoriaID(categoriaID);
    }

    public static boolean cadastrarProduto(final String nome, final Double preco, final Integer fabricanteID, final Integer categoriaID, final Integer produtoId) {
        try {
            String sql;
            if (produtoId == null) {
                validarNome(nome);
                validarArgumentos(preco, fabricanteID, categoriaID);
                sql = "INSERT INTO %s (Nome, Preco, FabricanteID, CategoriaID) VALUES (?, ?, ?, ?);".formatted(TABELA);
            } else {
                Produto produto = new Produto(produtoId, nome, preco, fabricanteID, categoriaID);
                sql = "UPDATE %s SET Nome=?, Preco=?, FabricanteID=?, CategoriaID=? WHERE ProdutoID=?".formatted(TABELA);
            }
            return executarComandoSQL(DATABASE, TABELA, sql, nome, preco, fabricanteID, categoriaID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao cadastrar o produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao cadastrar o produto: %s%n", e.getMessage());
        }
        return false;
    }

    public static Produto obterProdutoPorID(final Integer produtoID) {
        String sql = "SELECT * FROM %s WHERE ProdutoID = ?".formatted(TABELA);
        try (ResultSet rs = executarConsultaSQL(DATABASE, TABELA, sql, produtoID)) {
            validarID(produtoID);
            if (rs.next()) {
                int produtoId = rs.getInt("ProdutoID");
                String nome = rs.getString("Nome");
                double preco = rs.getDouble("Preco");
                int fabricanteID = rs.getInt("FabricanteID");
                int categoriaID = rs.getInt("CategoriaID");
                return new Produto(produtoId, nome, preco, fabricanteID, categoriaID);
            }
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao obter o produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao obter o produto: %s%n", e.getMessage());
        }
        return null;
    }

    public static List<Produto> obterProdutosOrdenados() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM %s ORDER BY Nome".formatted(TABELA);
        try (ResultSet rs = executarConsultaSQL(DATABASE, TABELA, sql)) {
            while (rs.next()) {
                int produtoId = rs.getInt("ProdutoID");
                String nome = rs.getString("Nome");
                double preco = rs.getDouble("Preco");
                int fabricanteID = rs.getInt("FabricanteID");
                int categoriaID = rs.getInt("CategoriaID");
                produtos.add(new Produto(produtoId, nome, preco, fabricanteID, categoriaID));
            }
        } catch (SQLException e) {
            System.err.printf("Erro ao obter os produtos: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao obter os produtos: %s%n", e.getMessage());
        }
        return produtos;
    }

    public Integer getProdutoID() {
        return this.ProdutoID;
    }

    public void setProdutoID(final Integer produtoID) {
        try {
            validarID(produtoID);
            this.ProdutoID = produtoID;
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir o ID do produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir o ID do produto: %s%n", e.getMessage());
        }
    }

    public String getNome() {
        return this.Nome;
    }

    public void setNome(final String nome) {
        try {
            this.Nome = validarNome(nome);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir o nome do produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir o nome do produto: %s%n", e.getMessage());
        }
    }

    public Double getPreco() {
        return this.Preco;
    }

    public void setPreco(final Double preco) {
        try {
            this.Preco = validarPreco(preco);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir o preço do produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir o preço do produto: %s%n", e.getMessage());
        }
    }

    public Integer getFabricanteID() {
        return this.FabricanteID;
    }

    public void setFabricanteID(final Integer fabricanteID) {
        try {
            this.FabricanteID = validarID(fabricanteID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir o ID do fabricante: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir o ID do fabricante: %s%n", e.getMessage());
        }
    }

    public Integer getCategoriaID() {
        return this.CategoriaID;
    }

    public void setCategoriaID(final Integer categoriaID) {
        try {
            this.CategoriaID = validarID(categoriaID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir o ID da categoria: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir o ID da categoria: %s%n", e.getMessage());
        }
    }

    public String obterNomeFabricante() {
        String sql = "SELECT Nome FROM Fabricante WHERE FabricanteID = ?";
        try (ResultSet rs = Conexao.executarConsultaSQL(DATABASE, TABELA, sql, this.FabricanteID)) {
            if (rs.next()) {
                return rs.getString("Nome");
            }
        } catch (SQLException e) {
            System.err.printf("Erro ao obter nome do fabricante: %s%n", e.getMessage());
        }
        return null;
    }

    public String obterNomeCategoria() {
        String sql = "SELECT Nome FROM Categoria WHERE CategoriaID = ?";
        try (ResultSet rs = Conexao.executarConsultaSQL(DATABASE, TABELA, sql, this.CategoriaID)) {
            if (rs.next()) {
                return rs.getString("Nome");
            }
        } catch (SQLException e) {
            System.err.printf("Erro ao obter nome da categoria: %s%n", e.getMessage());
        }
        return null;
    }


    @Override
    public String toString() {
        return "Produto{ProdutoID=%d, Nome='%s', Preco=%.2f, FabricanteID=%d, CategoriaID=%d}".formatted(ProdutoID, Nome, Preco, FabricanteID, CategoriaID);
    }

    public boolean atualizarProduto(final String nome, final Double preco, final Integer fabricanteID, final Integer categoriaID, final Integer produtoId) {
        try {
            Produto produto = new Produto(produtoId, nome, preco, fabricanteID, categoriaID);
            String sql = "UPDATE %s SET Nome=?, Preco=?, FabricanteID=?, CategoriaID=? WHERE ProdutoID=?".formatted(TABELA);
            return executarComandoSQL(DATABASE, TABELA, sql, produto.Nome, produto.Preco, produto.FabricanteID, produto.CategoriaID, produtoId);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao atualizar o produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao atualizar o produto: %s%n", e.getMessage());
        }
        return false;
    }

    public boolean consultarProdutoPorID(final Integer produtoID) {
        try {
            String sql = "SELECT * FROM %s WHERE ProdutoID = ?".formatted(TABELA);
            return executarComandoSQL(DATABASE, TABELA, sql, validarID(produtoID));
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao consultar o produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao consultar o produto: %s%n", e.getMessage());
        }
        return false;

    }

    public boolean removerProduto(final Integer ProdutoID) {
        try {
            String sql = "DELETE FROM %s WHERE ProdutoID = ?".formatted(TABELA);
            return executarComandoSQL(DATABASE, TABELA, sql, validarID(ProdutoID));
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao remover o produto: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao remover o produto: %s%n", e.getMessage());
        }
        return false;
    }
}