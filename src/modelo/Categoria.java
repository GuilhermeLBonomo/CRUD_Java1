package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static modelo.Conexao.executarComandoSQL;
import static modelo.Conexao.executarConsultaSQL;
import static modelo.FuncoesUtilitarias.*;

public final class Categoria {
    private static final String DATABASE = Conexao.getDATABASE();
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
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir o ID da categoria: %s%n", e.getMessage());
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
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir nome da categoria: %s%n", e.getMessage());
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
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir status da categoria: %s%n", e.getMessage());
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
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir descrição da categoria: %s%n", e.getMessage());
            throw e;
        }
    }

    @Override
    public String toString() {
        return "Categoria{CategoriaID=%d, Nome='%s', Status='%s', Descricao='%s'}".formatted(CategoriaID, Nome, Status, Descricao);
    }

    public boolean cadastrarCategoria(final Integer categoriaID, final String nome, final String status, final String descricao) {
        try {
            String sql;
            if (categoriaID == null) {
                validarNome(nome);
                validarArgumentos(status, descricao);
                sql = "INSERT INTO %s (Nome, Status, Descricao) VALUES (?, ?, ?)".formatted(TABELA);
            } else {
                Categoria novaCategoria = new Categoria(categoriaID, nome, status, descricao);
                sql = "INSERT INTO %s (CategoriaID, Nome, Status, Descricao) VALUES (?, ?, ?, ?)".formatted(TABELA);
            }
            return executarComandoSQL(DATABASE, TABELA, sql, categoriaID, nome, status, descricao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir categoria: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir categoria: %s%n", e.getMessage());
        }
        return false;
    }

    public Categoria consultarCategoria(final Integer categoriaID) {
        String sql = "SELECT * FROM %s WHERE CategoriaId = ?".formatted(TABELA);
        try (ResultSet rs = executarConsultaSQL(DATABASE, TABELA, sql, categoriaID)) {
            validarID(categoriaID);
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

        try (ResultSet rs = executarConsultaSQL(DATABASE, TABELA, sql, "%" + nome + "%")) {
            validarNome(nome);
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
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao consultar os Fabricantes por nome: %s%n", e.getMessage());
        }

        return categorias;
    }


    public boolean atualizarCategoria(final Integer CategoriaId, final String Nome, final String Status, final String Descricao) {
        try {
            Categoria categoriaNova = new Categoria(CategoriaId, Nome, Status, Descricao);
            String sql = "UPDATE %s SET Nome = ?, Status = ?, Descricao = ? WHERE CategoriaId = ?".formatted(TABELA);
            return executarComandoSQL(DATABASE, TABELA, sql, categoriaNova.Nome, categoriaNova.Status, categoriaNova.Descricao, CategoriaId);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao atualizar a categoria: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao atualizar a categoria: %s%n", e.getMessage());
        }
        return false;
    }

    public boolean removerCategoria(final Integer CategoriaId) {
        try {
            final String sql = "DELETE FROM %s WHERE CategoriaId = ?".formatted(TABELA);
            return executarComandoSQL(DATABASE, TABELA, sql, validarID(CategoriaId));
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao remover a categoria: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao remover a categoria: %s%n", e.getMessage());
        }
        return false;
    }
}