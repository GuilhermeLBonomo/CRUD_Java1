package modelo;

import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Properties;

import static modelo.FuncoesUtilitarias.validarArgumentos;

public final class Conexao {
    private static final Properties properties = new Properties();

    static {
        try (java.io.InputStream input = Conexao.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        } catch (Exception e) {
            System.err.printf("Erro ao carregar o arquivo de configurações: %s%n", e.getMessage());
        }
    }

    private static final String USUARIO = properties.getProperty("usuario");
    private static final String SENHA = properties.getProperty("senha");
    private static final String DATABASE = properties.getProperty("database");
    private static final String SERVER = properties.getProperty("server");
    private static final String PORTA = properties.getProperty("porta");
    private static final String URL = "jdbc:mariadb://" + SERVER + ":" + PORTA + "/" + DATABASE;


    public static boolean executarComandoSQL(final String database, final String tabela, final String sql, final Object... parametros) {
        Connection conexao = null;

        try {
            String database_out = checarDatabase(database);
            checarTabela(database_out, tabela);
            validarArgumentos(parametros);
            conexao = obterConexao();
            PreparedStatement ps = conexao.prepareStatement(sql);

            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }

            int linhasAfetadas = ps.executeUpdate();
            fecharConexao(conexao);
            return linhasAfetadas > 0;
        } catch (SQLException e) {
            System.err.printf("Erro SQL ao executar comando: %s%n", e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao executar comando: %s%n", e.getMessage());
            return false;
        }
    }

    public static @NotNull String checarDatabase(final String database) throws SQLException {
        String database_out = (database == null || database.isEmpty()) ? Conexao.DATABASE : database;

        Connection conexao = null;

        try {
            conexao = obterConexao();
            String sql = "SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, database_out);
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.next()) {
                System.err.println("O banco de dados '" + database_out + "' não existe.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao checar o banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao executar comando: %s%n", e.getMessage());
        } finally {
            fecharConexao(conexao);
        }

        return database_out;
    }

    public static void checarTabela(final String database, final String tabela) throws SQLException {
        Connection conexao = null;

        try {
            conexao = obterConexao();
            String database_out = checarDatabase(database);
            String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ? AND TABLE_NAME = ?";
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, database_out);
            ps.setString(2, tabela);
            ResultSet resultSet = ps.executeQuery();

            if (!resultSet.next()) {
                System.err.println("A tabela '" + tabela + "' não existe no banco de dados '" + database_out + "'.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao checar a tabela: " + e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao executar comando: %s%n", e.getMessage());
        } finally {
            fecharConexao(conexao);
        }
    }

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

    public static ResultSet executarConsultaSQL(final String database, final String tabela, final String sql, final Object... parametros) throws SQLException {
        Connection conexao;
        ResultSet resultSet = null;

        try {
            conexao = obterConexao();
            checarDatabase(database);
            checarTabela(database, tabela);

            PreparedStatement ps = conexao.prepareStatement(sql);

            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }

            resultSet = ps.executeQuery();
            fecharConexao(conexao);
        } catch (SQLException e) {
            System.err.printf("Erro SQL ao executar consulta: %s%n", e.getMessage());
        } catch (Exception e) {
            System.err.printf("Erro desconhecido ao executar consulta: %s%n", e.getMessage());
        }

        return resultSet;
    }

    public static String getDATABASE() {
        return DATABASE;
    }
}