// PacienteDAO.java
package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO extends PessoaDAO<Paciente> {

    public PacienteDAO() {
        super("paciente");
    }

    public void inserirCompleto(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (nome, telefone, email, sexo, endereco) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paciente.getId_paciente());
            ps.setString(2, paciente.getNome());
            ps.setString(3, paciente.getTelefone());
            ps.setString(4, paciente.getEmail());
            ps.setString(5, paciente.getSexo());
            ps.setString(6, paciente.getEndereco());
            ps.executeUpdate();
        }
    }

    public List<Paciente> listarTodos() throws SQLException {
        List<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT * FROM paciente";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente(
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getInt("id_paciente"),
                        rs.getString("sexo"),
                        rs.getString("endereco")
                );
                pacientes.add(paciente);
            }
        }
        return pacientes;
    }
    public Paciente buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getInt("id_paciente"),
                            rs.getString("sexo"),
                            rs.getString("endereco")
                    );
                }
            }
        }
        return null;
    }

    public void deletar(int idPaciente) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id_paciente = ?";
        try (Connection conn = ConexaoPostgres.conectar(); // Conectando ao banco
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPaciente); // Definindo o ID do paciente para exclusão
            stmt.executeUpdate(); // Executando a atualização
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir o paciente", e); // Tratando a exceção
        }
    }
}
