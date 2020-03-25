
package filtro;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Usuario;
import modelo.Perfil;

public class AcessoFunc implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpSession usuarioLogado = ((HttpServletRequest) request).getSession();
        Usuario usuario = (Usuario) usuarioLogado.getAttribute("usuarioLogado");

        if (usuario != null && usuario.getLogin().getNivel().equals(Perfil.FUNCIONARIO)) {
            chain.doFilter(request, response);
        } else {
            ((HttpServletResponse) response).sendRedirect(((HttpServletRequest)request).getContextPath()+"/acessonegado.jsp");
        }
    }

    @Override
    public void destroy() {

    }
    
}