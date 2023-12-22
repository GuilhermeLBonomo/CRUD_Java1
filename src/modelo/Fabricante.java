package modelo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static modelo.Conexao.executarComandoSQL;
import static modelo.Conexao.executarConsultaSQL;
import static modelo.FuncoesUtilitarias.*;

public final class Fabricante {
    private Integer fabricanteID;
    private String nome;
    private String localizacao;
    private String descricao;

    public Fabricante(Integer fabricanteID, String nome, String localizacao, String descricao) {
        setFabricanteID(fabricanteID);
        setNome(nome);
        setLocalizacao(localizacao);
        setDescricao(descricao);
    }

    public Fabricante() {
    }

    public Integer getFabricanteID() {
        return fabricanteID;
    }

    public void setFabricanteID(Integer fabricanteID) {
        try {
            this.fabricanteID = validarID(fabricanteID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao criar o Fabricante: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        try {
            this.nome = validarNome(nome);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao criar o Fabricante: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        try {
            this.localizacao = validarStrings(localizacao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao criar o Fabricante: %s%n", e.getMessage());
            throw e;
        }
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao.substring(0, Math.min(descricao.length(), 255));
    }

    @Override
    public String toString() {
        return "Fabricante{fabricanteID=%d, nome='%s', localizacao='%s', descricao='%s'}".formatted(fabricanteID, nome, localizacao, descricao);
    }

    public boolean adicionarFabricante(final Integer fabricanteID, final String nome, final String localizacao, final String descricao) throws SQLException {
        try {
            Fabricante novoFabricante = new Fabricante(fabricanteID, nome, localizacao, descricao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao criar o Fabricante: %s%n", e.getMessage());
            return false;
        }
        String sql = "INSERT INTO Fabricante (FabricanteID, Nome, Localizacao, Descricao) VALUES (?, ?, ?, ?)";
        return executarComandoSQL("", "Fabricante", sql, fabricanteID, nome, localizacao, descricao);
    }

    public boolean removerFabricante(final Integer fabricanteID) throws SQLException {
        try {
            validarID(fabricanteID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao remover o Fabricante: %s%n", e.getMessage());
            return false;
        }
        final String sql = "DELETE FROM Fabricante WHERE FabricanteID = ?";
        return executarComandoSQL("", "Fabricante", sql, fabricanteID);
    }

    public boolean atualizarFabricante(final Integer fabricanteID, final String novoNome, final String novaLocalizacao, final String novaDescricao) throws SQLException {
        try {
            validarNome(novoNome);
            validarArgumentos(fabricanteID, novaLocalizacao, novaDescricao);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao atualizar o Fabricante: %s%n", e.getMessage());
            return false;
        }
        final String sql = "UPDATE Fabricante SET Nome=?, Localizacao=?, Descricao=? WHERE FabricanteID=?";
        return executarComandoSQL("", "Fabricante", sql, novoNome, novaLocalizacao, novaDescricao, fabricanteID);
    }


    public List<Fabricante> consultarFabricantesPorNome(String nome) throws SQLException {
        List<Fabricante> fabricantes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Fabricante WHERE Nome LIKE ? ORDER BY Nome";
            try (ResultSet rs = executarConsultaSQL("", "Fabricante", sql, "%" + nome + "%")) {

                while (rs.next()) {
                    Fabricante fabricante = new Fabricante();
                    fabricante.fabricanteID = rs.getInt("FabricanteID");
                    fabricante.nome = rs.getString("Nome");
                    fabricante.localizacao = rs.getString("Localizacao");
                    fabricante.descricao = rs.getString("Descricao");
                    fabricantes.add(fabricante);
                }
            }
        } catch (SQLException erro) {
            System.err.printf("Erro ao consultar os Fabricantes por nome: %s%n", erro.getMessage());
        }
        return fabricantes;
    }

    public Fabricante consultarFabricante(int fabricanteID) throws SQLException {
        try {
            String sql = "SELECT * FROM Fabricante WHERE FabricanteID=?";
            try (ResultSet rs = executarConsultaSQL("", "Fabricante", sql, fabricanteID)) {

                if (!rs.isBeforeFirst()) {
                    System.err.println("Fabricante não encontrado!");
                    return null;
                } else {
                    Fabricante fabricante = new Fabricante();
                    while (rs.next()) {
                        fabricante.fabricanteID = rs.getInt("FabricanteID");
                        fabricante.nome = rs.getString("Nome");
                        fabricante.localizacao = rs.getString("Localizacao");
                        fabricante.descricao = rs.getString("Descricao");
                    }
                    return fabricante;
                }
            }
        } catch (SQLException erro) {
            System.err.printf("Erro ao consultar o fabricante: %s%n", erro.getMessage());
            return null;
        }
    }
}
