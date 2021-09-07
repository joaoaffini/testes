package br.com.jp.servicos;

import static br.com.jp.utils.DataUtils.adicionarDias;

import java.util.Date;
import java.util.List;

import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;

public class LocacaoService {
	
	public String vPublica;
	protected String vProtegida;
	private String vPrivada;
	String vDefault;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException {
		
		if(usuario == null) {
			throw new LocadoraException("Usuario vazio");
		}
		
		Double precoLocacao = 0.0;
		
		if(filmes ==  null || filmes.size() == 0) {
			throw new LocadoraException("Filme vazio");
		}
		
		for (Filme f : filmes) {
			
			if(f.getEstoque() == 0) {
				throw new FilmeSemEstoqueException();
			}
			
			precoLocacao += f.getPrecoLocacao();
		}
		
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		locacao.setValor(precoLocacao);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		//TODO adicionar método para salvar
		
		return locacao;
	}

}