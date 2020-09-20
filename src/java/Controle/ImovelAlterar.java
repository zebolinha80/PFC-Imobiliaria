/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controle;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Dao.ImovelDAO;
import Modelo.Imovel;

/**
 *
 * @author Diego
 */
public class ImovelAlterar implements ICommand {

    @Override
    public String executar(HttpServletRequest request, HttpServletResponse response) {

        try {
            Imovel imovel = new Imovel();

            imovel.setId_imovel(Integer.parseInt(request.getParameter("id")));
            imovel.setArea_edificada(Double.parseDouble(request.getParameter("areaedificada").replaceAll("[^0-9]", "")));
            imovel.setArea_total(Double.parseDouble(request.getParameter("areatotal").replaceAll("[^0-9]", "")));
            imovel.setBanheiros(Integer.parseInt(request.getParameter("banheiro").replaceAll("[^0-9]", "")));
            imovel.setComodos(Integer.parseInt(request.getParameter("comodos").replaceAll("[^0-9]", "")));
            imovel.setDescricao(request.getParameter("descricao"));
            imovel.setTipo_imovel(request.getParameter("tpimovel"));
            imovel.setTitulo(request.getParameter("titulo"));
            imovel.setVagas_garagem(Integer.parseInt(request.getParameter("garagem").replaceAll("[^0-9]", "")));
            imovel.setValor(Double.parseDouble(request.getParameter("valorimovel").replaceAll("[^0-9]", "")));

            imovel.getEndereco().setId_endereco(Integer.parseInt(request.getParameter("ide")));
            imovel.getEndereco().setLogradouro(request.getParameter("logradouro"));
            imovel.getEndereco().setNumero(Integer.parseInt(request.getParameter("numero").replaceAll("[^0-9]", "")));
            imovel.getEndereco().setComplemento(request.getParameter("complemento"));
            imovel.getEndereco().setCidade(request.getParameter("cidade"));
            imovel.getEndereco().setEstado(request.getParameter("estado"));
            imovel.getEndereco().setCep(request.getParameter("cep"));
            imovel.getEndereco().setBairro(request.getParameter("bairro"));

            ImovelDAO dao = new ImovelDAO();

            dao.alterar(imovel);
            request.setAttribute("msg", "Dados atualizados com sucesso!");
            return "index.jsp";

        } catch (NumberFormatException | SQLException ex) {
            request.setAttribute("msgerro", ex.getMessage());
            System.err.println(ex.getMessage());
            return "index.jsp";
        }
    }

}
