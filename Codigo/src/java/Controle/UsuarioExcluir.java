/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Dao.UsuarioDAO;
import Modelo.Usuario;

/**
 *
 * @author Diieg
 */
public class UsuarioExcluir implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) {

        Usuario usuario = new Usuario();
        usuario.setId_usuario(Integer.parseInt(request.getParameter("id")));
        usuario.setSituacao("Inativo");

        UsuarioDAO dao = new UsuarioDAO();

        try {
            dao.excluir(usuario);
            request.setAttribute("msg", "Usuário removido com sucesso!");
            return "index.jsp";
        } catch (SQLException ex) {
            request.setAttribute("msgerro", ex.getMessage());
            System.err.println(ex.getMessage());
            return "index.jsp";
        }
    }
}
