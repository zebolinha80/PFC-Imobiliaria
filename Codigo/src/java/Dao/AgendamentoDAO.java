/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dao;

import Modelo.Agendamento;
import Modelo.Usuario;
import Util.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Diego
 */
public class AgendamentoDAO {

    Date data = new Date();
    Timestamp timestamp = new Timestamp(data.getTime());

    PreparedStatement smt;
    ResultSet rs;

    public List<Usuario> listarCorretores() throws SQLException {

        String queryCorretores = "SELECT * FROM Usuario U "
                + " INNER JOIN Endereco E ON E.id_endereco = U.id_endereco"
                + " WHERE u.nivel_acesso = 'CORRETOR'";

        List<Usuario> corretores = new ArrayList<>();

        Connection connection = ConnectionFactory.getConexao();

        smt = connection.prepareStatement(queryCorretores);
        rs = smt.executeQuery();

        while (rs.next()) {

            Usuario usuario = new Usuario();

            usuario.setId_usuario(rs.getInt("id_usuario"));
            usuario.setNome(rs.getString("nome"));

            corretores.add(usuario);

        }

        smt.close();
        connection.close();

        return corretores;

    }

    public List<Agendamento> listarAgendamentos(Map parametros) throws SQLException {

        String queryAgendaImovel = "SELECT ag.id_agendamento, u.nome, uc.nome as corretor, ag.id_imovel, ag.dataagendamento, ag.datasolicitacao, ag.status\n"
                + " FROM agendamento ag\n"
                + " JOIN usuario u on u.id_usuario = ag.id_usuario\n"
                + " JOIN usuario uc on uc.id_usuario = ag.id_corretor\n";

        String modo = (String) parametros.get("modo");

        if (modo.equalsIgnoreCase("Corretor")) {
            queryAgendaImovel += "WHERE ag.id_corretor = ?";
        } else if (modo.equalsIgnoreCase("Usuario")) {
            queryAgendaImovel += "WHERE ag.id_usuario = ?";
        } else if (modo.equalsIgnoreCase("Imovel")) {
            queryAgendaImovel += "WHERE ag.id_imovel = ?";
        }

        queryAgendaImovel += "ORDER BY ag.dataagendamento ASC";

        List<Agendamento> listaAgenda = new ArrayList<>();

        Connection connection = ConnectionFactory.getConexao();

        smt = connection.prepareStatement(queryAgendaImovel);
        smt.setInt(1, (int) parametros.get("id"));
        rs = smt.executeQuery();

        while (rs.next()) {

            Agendamento ag = new Agendamento();

            ag.setId_agendamento(rs.getInt("id_agendamento"));
            ag.getUsuario().setNome(rs.getString("nome"));
            ag.getUsuarioCorretor().setNome(rs.getString("corretor"));
            ag.getImovel().setId_imovel(rs.getInt("id_imovel"));
            ag.setDataAgendamento(rs.getTimestamp("dataagendamento"));
            ag.setDataSolicitacao(rs.getTimestamp("datasolicitacao"));
            ag.setStatus(rs.getString("status"));

            listaAgenda.add(ag);

        }

        smt.close();
        connection.close();

        return listaAgenda;

    }

    public void Agendar(Agendamento agenda) throws SQLException {

        String insertAgendamento = "INSERT INTO Agendamento VALUES (DEFAULT,?,?,?,?,?,?,?)";

        Connection connection = ConnectionFactory.getConexao();

        smt = connection.prepareStatement(insertAgendamento);
        smt.setTimestamp(1, new Timestamp(agenda.getDataAgendamento().getTime()));
        smt.setTimestamp(2, timestamp);
        smt.setString(3, agenda.getStatus());
        smt.setString(4, agenda.getSituacao());
        smt.setInt(5, agenda.getUsuario().getId_usuario());
        smt.setInt(6, agenda.getImovel().getId_imovel());
        smt.setInt(7, agenda.getUsuarioCorretor().getId_usuario());
        smt.execute();

        smt.close();
        connection.close();

    }

    public int alocaCorretor(Agendamento agendamento) throws SQLException {

        int id = 0;

        String corretores = "SELECT id_usuario "
                + "FROM usuario "
                + "WHERE nivel_acesso = 'CORRETOR'";

        Connection connection = ConnectionFactory.getConexao();

        smt = connection.prepareStatement(corretores);
        ResultSet rsCorretor = smt.executeQuery();

        while (rsCorretor.next()) {

            String corretorDisponivel = "SELECT id_corretor FROM Agendamento WHERE id_corretor = ? AND dataagendamento = ?";

            smt = connection.prepareStatement(corretorDisponivel);
            smt.setInt(1, rsCorretor.getInt("id_usuario"));
            smt.setTimestamp(2, new Timestamp(agendamento.getDataAgendamento().getTime()));
            rs = smt.executeQuery();

            if (!rs.next()) {
                id = rsCorretor.getInt("id_usuario");
                break;
            }

        }

        smt.close();
        connection.close();

        return id;

    }

    public boolean disponibilidadeCorretor(Agendamento agendamento) throws SQLException {

        String disponibilidadeCorretor = "SELECT dataagendamento "
                + " FROM Agendamento "
                + "WHERE id_corretor  = ? "
                + "  AND dataagendamento = ?";

        Connection connection = ConnectionFactory.getConexao();

        smt = connection.prepareStatement(disponibilidadeCorretor);
        smt.setInt(1, agendamento.getUsuarioCorretor().getId_usuario());
        smt.setTimestamp(2, new Timestamp(agendamento.getDataAgendamento().getTime()));
        rs = smt.executeQuery();

        if (rs.next()) {
            smt.close();
            connection.close();
            return true;
        } else {
            smt.close();
            connection.close();
            return false;
        }

    }
}