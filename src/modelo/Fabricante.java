package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        return Conexao.executarComandoSQL(sql, fabricanteID, nome, localizacao, descricao);
    }

    public boolean removerFabricante(final Integer fabricanteID) throws SQLException {
        try {
            validarID(fabricanteID);
        } catch (IllegalArgumentException e) {
            System.err.printf("Erro ao remover o Fabricante: %s%n", e.getMessage());
            return false;
        }
        final String sql = "DELETE FROM Fabricante WHERE FabricanteID = ?";
        return Conexao.executarComandoSQL(sql, fabricanteID);
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
        return Conexao.executarComandoSQL(sql, novoNome, novaLocalizacao, novaDescricao, fabricanteID);
    }

    public Fabricante consultarFabricantePorNome(String nome) throws SQLException {
        Connection conexao = null;
        PreparedStatement ps;
        ResultSet rs;

        try {
            conexao = Conexao.obterConexao();
            String sql = "SELECT * FROM Fabricante WHERE Nome = ?";
            ps = conexao.prepareStatement(sql);
            ps.setString(1, nome);

            rs = ps.executeQuery();

            if (rs.next()) {
                Fabricante fabricante = new Fabricante();
                fabricante.fabricanteID = rs.getInt("FabricanteID");
                fabricante.nome = rs.getString("Nome");
                fabricante.localizacao = rs.getString("Localizacao");
                fabricante.descricao = rs.getString("Descricao");
                return fabricante;
            } else {
                return null;
            }
        } catch (SQLException erro) {
            System.err.printf("Erro ao consultar o Fabricante: %s%n", erro.getMessage());
            return null;
        } finally {
            Conexao.fecharConexao(conexao);
        }
    }

    public boolean consultarFabricante(int fabricanteID) throws SQLException {
        Connection conexao = null;

        try {
            conexao = Conexao.obterConexao();
            String sql = "SELECT * FROM Fabricante WHERE FabricanteID=?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setInt(1, fabricanteID);
            ResultSet rs = ps.executeQuery();

            if (!rs.isBeforeFirst()) {
                System.err.println("Fabricante não encontrado!");
                return false;
            } else {
                while (rs.next()) {
                    this.fabricanteID = rs.getInt("FabricanteID");
                    this.nome = rs.getString("Nome");
                    this.localizacao = rs.getString("Localizacao");
                    this.descricao = rs.getString("Descricao");
                }
                return true;
            }
        } catch (SQLException erro) {
            System.err.printf("Erro ao consultar o fabricante: %s%n", erro.getMessage());
            return false;
        } finally {
            Conexao.fecharConexao(conexao);
        }
    }

    public boolean atualizarFabricante(String nome) throws SQLException {
        if (this.fabricanteID == null || this.fabricanteID <= 0) {
            System.err.println("Erro: FabricanteID inválido para atualização.");
            return false;
        }

        Connection conexao = null;
        try {
            conexao = Conexao.obterConexao();
            String sql = "UPDATE Fabricante SET Nome=? WHERE FabricanteID=?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setInt(2, this.fabricanteID);
            int totalRegistrosAfetados = ps.executeUpdate();
            if (totalRegistrosAfetados == 0) {
                System.err.println("Não foi feita a atualização!");
            } else {
                System.err.println("Atualização realizada!");
            }
            return true;
        } catch (SQLException erro) {
            System.err.printf("Erro ao atualizar o fabricante: %s%n", erro.getMessage());
            return false;
        } finally {
            Conexao.fecharConexao(conexao);
        }
    }
}
