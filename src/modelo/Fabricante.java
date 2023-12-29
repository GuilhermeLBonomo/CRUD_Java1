package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static modelo.Conexao.executarComandoSQL;
import static modelo.Conexao.executarConsultaSQL;
import static modelo.FuncoesUtilitarias.*;

public final class Fabricante {
    private static final String DATABASE = Conexao.getDATABASE();
    private final static String TABELA = "Fabricante";
    private Integer FabricanteID;
    private String Nome;
    private String Localizacao;
    private String Descricao;

    public Fabricante(Integer fabricanteID, String nome, String localizacao, String descricao) {
        this.setFabricanteID(fabricanteID);
        this.setNome(nome);
        this.setLocalizacao(localizacao);
        this.setDescricao(descricao);
    }

    public Fabricante() {
    }

    public Integer getFabricanteID() {
        return this.FabricanteID;
    }

    public void setFabricanteID(final Integer fabricanteID) {
        try {
            this.FabricanteID = validarID(fabricanteID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir ID do fabricante: %s%n", e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir ID do fabricante: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getNome() {
        return this.Nome;
    }

    public void setNome(String nome) {
        try {
            this.Nome = validarNome(nome);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir nome do Fabricante: %s%n", e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir nome do Fabricante: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getLocalizacao() {
        return this.Localizacao;
    }

    public void setLocalizacao(String localizacao) {
        try {
            this.Localizacao = validarString(localizacao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir localização do Fabricante: %s%n", e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir localização do Fabricante: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getDescricao() {
        return this.Descricao;
    }

    public void setDescricao(String descricao) {
        try {
            this.Descricao = validarString(descricao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao definir descrição do Fabricante: %s%n", e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao definir descrição do Fabricante: %s%n", e.getMessage());
            throw e;
        }
    }

    @Override
    public String toString() {
        return "Fabricante{fabricanteID=%d, nome='%s', localizacao='%s', descricao='%s'}".formatted(FabricanteID, Nome, Localizacao, Descricao);
    }

    public boolean cadastrarFabricante(final Integer fabricanteID, final String nome, final String localizacao, final String descricao) {
        try {
            String sql;
            if (fabricanteID == null) {
                validarNome(nome);
                validarArgumentos(localizacao, descricao);
                sql = "INSERT INTO %s (Nome, Localizacao, Descricao) VALUES (?, ?, ?)".formatted(TABELA);
            } else {
                Fabricante novoFabricante = new Fabricante(fabricanteID, nome, localizacao, descricao);
                sql = "INSERT INTO %s (FabricanteID, Nome, Localizacao, Descricao) VALUES (?, ?, ?, ?)".formatted(TABELA);
            }
            return executarComandoSQL(DATABASE, TABELA, sql, fabricanteID, nome, localizacao, descricao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao criar o Fabricante: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao criar o Fabricante: %s%n", e.getMessage());
        }
        return false;
    }

    public boolean removerFabricante(final Integer fabricanteID) {
        try {
            String sql = "DELETE FROM %s WHERE FabricanteID = ?".formatted(TABELA);
            return executarComandoSQL(DATABASE, TABELA, sql, validarID(fabricanteID));
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao remover o Fabricante: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao remover o Fabricante: %s%n", e.getMessage());
        }
        return false;

    }

    public boolean atualizarFabricante(final Integer fabricanteID, final String novoNome, final String novaLocalizacao, final String novaDescricao) {
        try {
            Fabricante fabricanteNovo = new Fabricante(fabricanteID, novoNome, novaLocalizacao, novaDescricao);
            String sql = "UPDATE %s SET Nome=?, Localizacao=?, Descricao=? WHERE FabricanteID=?".formatted(TABELA);
            return executarComandoSQL(DATABASE, TABELA, sql, fabricanteNovo.Nome, fabricanteNovo.Localizacao, fabricanteNovo.Descricao, fabricanteID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao atualizar o Fabricante: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao atualizar o Fabricante: %s%n", e.getMessage());
        }
        return false;
    }


    public List<Fabricante> consultarFabricantesPorNome(final String nome) {
        List<Fabricante> fabricantes = new ArrayList<>();
        String sql = "SELECT * FROM %s WHERE Nome LIKE ? ORDER BY Nome".formatted(TABELA);

        try (ResultSet rs = executarConsultaSQL(DATABASE, TABELA, sql, "%" + nome + "%")) {
            validarNome(nome);
            while (rs.next()) {
                Fabricante fabricante = new Fabricante();
                fabricante.FabricanteID = rs.getInt("FabricanteID");
                fabricante.Nome = rs.getString("Nome");
                fabricante.Localizacao = rs.getString("Localizacao");
                fabricante.Descricao = rs.getString("Descricao");
                fabricantes.add(fabricante);
            }
        } catch (SQLException erro) {
            System.err.printf("Erro ao consultar os Fabricantes por nome: %s%n", erro.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao consultar os Fabricantes por nome: %s%n", e.getMessage());
        }
        return fabricantes;
    }

    public Fabricante consultarFabricante(final Integer fabricanteID) {
        String sql = "SELECT * FROM %s WHERE FabricanteID=?".formatted(TABELA);
        try (ResultSet rs = executarConsultaSQL(DATABASE, TABELA, sql, fabricanteID)) {
            validarID(fabricanteID);
            if (!rs.isBeforeFirst()) {
                System.err.println("Fabricante não encontrado!");
            } else {
                Fabricante fabricante = new Fabricante();
                while (rs.next()) {
                    fabricante.FabricanteID = rs.getInt("FabricanteID");
                    fabricante.Nome = rs.getString("Nome");
                    fabricante.Localizacao = rs.getString("Localizacao");
                    fabricante.Descricao = rs.getString("Descricao");
                }
                return fabricante;
            }
        } catch (SQLException erro) {
            System.err.printf("Erro ao consultar o fabricante: %s%n", erro.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao consultar o fabricante: %s%n", e.getMessage());
        }
        return null;
    }

}
