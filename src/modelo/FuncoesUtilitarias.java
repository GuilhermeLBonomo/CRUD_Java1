package modelo;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class FuncoesUtilitarias {

    private static final int TAMANHO_MAXIMO_STRING = 256;
    private static final int TAMANHO_MAXIMO_NOME = 50;

    @Contract(value = "null -> fail", pure = true)
    public static @NotNull Integer validarID(final Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número inteiro positivo.");
        }
        return id;
    }

    @Contract("null -> fail")
    public static @NotNull String validarString(final String string) {
        final String regex = "^[\\p{L}0-9,.-]{1," + TAMANHO_MAXIMO_STRING + "}$";
        if (string == null || !string.matches(regex)) {
            throw new IllegalArgumentException("String inválida.");
        }
        return string.substring(0, Math.min(string.length(), TAMANHO_MAXIMO_STRING));
    }

    @Contract("null -> fail")
    public static @NotNull String validarNome(final String nome) {
        final String regex = "^[A-Za-z ]{1," + TAMANHO_MAXIMO_NOME + "}$";
        if (nome == null || !nome.matches(regex)) {
            throw new IllegalArgumentException("Nome inválido.");
        }
        return nome.substring(0, 1).toUpperCase() + nome.substring(1).toLowerCase();
    }

    @Contract(value = "null -> fail", pure = true)
    public static @NotNull Double validarPreco(final Double preco) {
        if (preco == null || preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser um número positivo.");
        }
        return preco;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void validarArgumentos(Object @NotNull ... args) {
        for (Object arg : args) {
            if (arg == null) {
                throw new IllegalArgumentException("Argumento não pode ser nulo.");
            }

            if (arg instanceof String) {
                validarString((String) arg);
            } else if (arg instanceof Integer) {
                validarID((Integer) arg);
            } else if (arg instanceof Double) {
                validarPreco((Double) arg);
            }
        }
    }
}
