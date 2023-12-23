package modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static modelo.Conexao.executarComandoSQL;
import static modelo.Conexao.executarConsultaSQL;
import static modelo.FuncoesUtilitarias.*;

public final class Categoria {
    private static final String TABELA = "Categoria";
    private Integer CategoriaID;
    private String Nome;
    private String Status;
    private String Descricao;

    public Categoria() {
    }

    Categoria(final Integer categoriaID, final String nome, final String status, final String descricao) {
        this.setCategoriaID(categoriaID);
        this.setNome(nome);
        this.setStatus(status);
        this.setDescricao(descricao);
    }

    public int getCategoriaID() {
        return this.CategoriaID;
    }

    public void setCategoriaID(final Integer categoriaID) {
        try {
            this.CategoriaID = validarID(categoriaID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir o ID da categoria: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getNome() {
        return this.Nome;
    }

    public void setNome(final String nome) {
        try {
            this.Nome = validarNome(nome);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir nome  da categoria: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(final String status) {
        try {
            this.Status = validarString(status);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir status da categoria: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getDescricao() {
        return this.Descricao;
    }

    public void setDescricao(final String descricao) {
        try {
            this.Descricao = validarString(descricao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir descrição da categoria: %s%n", e.getMessage());
            throw e;
        }
    }

    @Override
    public String toString() {
        return "Categoria{CategoriaID=%d, Nome='%s', Status='%s', Descricao='%s'}".formatted(CategoriaID, Nome, Status, Descricao);
    }

    public boolean cadastrarCategoria(final Integer categoriaID, final String nome, final String status, final String descricao) {
        Connection conexao = null;
        try {
            Categoria categoria = new Categoria(categoriaID, nome, status, descricao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir categoria: %s%n", e.getMessage());
            return false;
        }
        String sql = "INSERT INTO %s (CategoriaID, Nome, Status, Descricao) VALUES (?, ?, ?, ?)".formatted(TABELA);
        return executarComandoSQL("", TABELA, sql, String.valueOf(categoriaID), nome, status, descricao);
    }

    public Categoria consultarCategoria(final Integer categoriaID) {
        String sql = "SELECT * FROM %s WHERE CategoriaId = ?".formatted(TABELA);
        try (ResultSet rs = executarConsultaSQL("", TABELA, sql, categoriaID)) {
            if (!rs.isBeforeFirst()) {
                System.err.println("Categoria não encontrada!");
            } else {
                Categoria categoria = new Categoria();
                while (rs.next()) {
                    categoria.setCategoriaID(rs.getInt("CategoriaID"));
                    categoria.setNome(rs.getString("Nome"));
                    categoria.setStatus(rs.getString("Status"));
                    categoria.setDescricao(rs.getString("Descricao"));
                }
                return categoria;
            }
        } catch (SQLException e) {
            System.err.printf("Erro ao consultar a categoria: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao consultar a categoria: %s%n", e.getMessage());
        }
        return null;
    }

    public List<Categoria> consultarCategoriasPorNome(final String nome) {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT * FROM %s WHERE Nome LIKE ? ORDER BY Nome".formatted(TABELA);

        try (ResultSet rs = executarConsultaSQL("", TABELA, sql, "%" + nome + "%")) {
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setCategoriaID(rs.getInt("CategoriaID"));
                categoria.setNome(rs.getString("Nome"));
                categoria.setStatus(rs.getString("Status"));
                categoria.setDescricao(rs.getString("Descricao"));
                categorias.add(categoria);
            }
        } catch (SQLException erro) {
            System.err.printf("Erro ao consultar os Fabricantes por nome: %s%n", erro.getMessage());
        }

        return categorias;
    }


    public boolean atualizarCategoria(final Integer CategoriaId, final String Nome, final String Status, final String Descricao) {
        try {
            validarID(CategoriaId);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao atualizar a categoria: %s%n", e.getMessage());
            return false;
        }
        final String sql = "UPDATE %s SET Nome = ?, Status = ?, Descricao = ? WHERE CategoriaId = ?".formatted(TABELA);
        return executarComandoSQL("", TABELA, sql, Nome, Status, Descricao, CategoriaId);
    }

    public boolean removerCategoria(final Integer CategoriaId) {
        try {
            validarID(CategoriaId);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao remover a categoria: %s%n", e.getMessage());
            return false;
        }
        final String sql = "DELETE FROM %s WHERE CategoriaId = ?".formatted(TABELA);
        return executarComandoSQL("", TABELA, sql, CategoriaId);
    }
}