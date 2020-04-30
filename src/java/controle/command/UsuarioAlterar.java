package controle.command;

import controle.Command;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Usuario;
import modelo.DAO.UsuarioDAO;
import modelo.Perfil;
import modelo.Sessao;

public class UsuarioAlterar implements Command {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) {
        boolean atualizado = false;
        HttpSession usuarioLogado = request.getSession();
        Sessao sessao = (Sessao) usuarioLogado.getAttribute("usuarioLogado");
        int id = Integer.parseInt(request.getParameter("id"));
        boolean autorizado = false;

        if (sessao.getNivel().equals(Perfil.ADMINISTRADOR)) {
            autorizado = true;
        }
        if (!sessao.getNivel().equals(Perfil.ADMINISTRADOR) && sessao.getId_usuario() == id) {
            autorizado = true;
        }

        if (!autorizado) {
            request.setAttribute("msgerro", "Você não tem permissão para editar este Usuário");
            return "index.jsp";
        } else {
            try {
                //Login
                String mail = request.getParameter("mail");

                String nome = request.getParameter("name");
                String tel_celular = request.getParameter("tel_celular");
                String tel_residencial = request.getParameter("tel_residencial");
                String tppessoa = request.getParameter("inputtp");

                Usuario usuario = new Usuario();
                usuario.setId_usuario(id);
                usuario.setNome(nome);
                usuario.setTipoPessoa(tppessoa);
                usuario.setTel_celular(tel_celular);
                usuario.setTel_residencial(tel_residencial);
                usuario.getLogin().setEmail(mail);

                String logradouro = request.getParameter("logradouro");
                int numero = Integer.parseInt(request.getParameter("numero"));
                String complemento = request.getParameter("complemento");
                String cidade = request.getParameter("cidade");
                String estado = request.getParameter("estado");
                String cep = request.getParameter("cep");
                String bairro = request.getParameter("bairro");

                usuario.getEndereco().setId_endereco(Integer.parseInt(request.getParameter("ide")));
                usuario.getEndereco().setLogradouro(logradouro);
                usuario.getEndereco().setNumero(numero);
                usuario.getEndereco().setComplemento(complemento);
                usuario.getEndereco().setCidade(cidade);
                usuario.getEndereco().setEstado(estado);
                usuario.getEndereco().setCep(cep);
                usuario.getEndereco().setBairro(bairro);

                UsuarioDAO dao = new UsuarioDAO();
                atualizado = dao.alterar(usuario);

                if (atualizado) {
                    request.setAttribute("msg", "Dados alterados com sucesso!");
                    return "index.jsp";
                } else {
                    request.setAttribute("msgerro", "Ocorreu um erro ao tentar atualizar os Dados");
                    return "index.jsp";
                }
            } catch (NumberFormatException ex) {
                Logger.getLogger(UsuarioAlterar.class.getName()).log(Level.SEVERE, null, ex);
                request.setAttribute("msgerro", ex.getMessage());
                return "index.jsp";
            }
        }

    }

}
