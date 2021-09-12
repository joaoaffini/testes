package br.com.jp.dao;

import java.util.List;

import br.com.jp.entidades.Locacao;

public interface LocacaoDAO {
	
	public void salvar(Locacao locacao);

	public List<Locacao> obterLocacoesPendentes();

}
