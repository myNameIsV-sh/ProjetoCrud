// MedicoDAO.java
package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicoDAO extends PessoaDAO<Medico> {

    public MedicoDAO() {
        super("medico");
    }

    public void inserirCompleto(Medico medico) throws SQLException {
        String sql = "INSERT INTO medico (nome, telefone, email, especialidade, crm) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getTelefone());
            ps.setString(3, medico.getEmail());
            ps.setString(4, medico.getEspecialidade());
            ps.setString(5, medico.getCrm());

            ps.executeUpdate();  // O id_medico será gerado automaticamente pelo banco

        } catch (SQLException e) {
            throw new SQLException("Erro ao inserir médico", e);
        }
    }


    public List<Medico> listarTodos() throws SQLException {
        List<Medico> medicos = new ArrayList<>();
        String sql = "SELECT * FROM medico";

        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medico medico = new Medico(
                        rs.getString("nome"),
                        rs.getString("telefone"),
                        rs.getString("email"),
                        rs.getInt("id_medico"),
                        rs.getString("especialidade"),
                        rs.getString("crm")
                );
                medicos.add(medico);
            }
        }
        return medicos;
    }

    public Medico buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM medico WHERE id_medico = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Medico(
                            rs.getString("nome"),
                            rs.getString("telefone"),
                            rs.getString("email"),
                            rs.getInt("id_medico"),
                            rs.getString("especialidade"),
                            rs.getString("crm")
                    );
                }
            }
        }
        return null;
    }

    public void atualizar(Medico medico) throws SQLException {
        String sql = "UPDATE medico SET nome = ?, telefone = ?, email = ?, especialidade = ?, crm = ? WHERE id_medico = ?";
        try (Connection conn = ConexaoPostgres.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, medico.getNome());
            ps.setString(2, medico.getTelefone());
            ps.setString(3, medico.getEmail());
            ps.setString(4, medico.getEspecialidade());
            ps.setString(5, medico.getCrm());
            ps.setInt(6, medico.getId_medico());
            ps.executeUpdate();
        }
    }
    // O método agora usa a classe ConexaoPostgres para conectar ao banco de dados.
    public void deletar(int idMedico) throws SQLException {
        // Obtendo a conexão usando a classe ConexaoPostgres
        String sql = "DELETE FROM medico WHERE id_medico = ?";
        try (Connection conn = ConexaoPostgres.conectar(); // Aqui estamos usando a conexão que sua classe ConexaoPostgres fornece.
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMedico);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir o médico", e);
        }
    }

}