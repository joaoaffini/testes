package br.com.jp.servicos;

import br.com.jp.entidades.Usuario;

public interface EmailService {
	
	public void notificarAtraso(Usuario usuario);

}
