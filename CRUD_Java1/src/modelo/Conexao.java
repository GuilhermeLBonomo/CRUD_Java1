package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class Conexao {
    private static final String URL = "jdbc:mariadb://localhost:3306/seu_banco_de_dados";
    private static final String USUARIO = "seu_usuario";
    private static final String SENHA = "sua_senha";

    public static Connection obterConexao() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver JDBC não encontrado", e);
        } catch (SQLException e) {
            throw new SQLException("Erro ao obter conexão. Verifique a URL, usuário e senha.", e);
        } catch (Exception e) {
            throw new SQLException("Erro desconhecido ao obter conexão.", e);
        }
    }

    public static void fecharConexao(final Connection conexao) throws SQLException {
        try {
            if (conexao != null && !conexao.isClosed()) {
                conexao.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Não foi possível fechar a conexão", e);
        } catch (Exception e) {
            throw new SQLException("Erro desconhecido ao fechar a conexão.", e);
        }
    }


    public static boolean executarComandoSQL(String sql, Object... parametros) throws SQLException {
        try (Connection conexao = obterConexao();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            System.err.printf("Erro SQL ao executar comando: %s%n", e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao executar comando: %s%n", e.getMessage());
            return false;
        }
    }


}