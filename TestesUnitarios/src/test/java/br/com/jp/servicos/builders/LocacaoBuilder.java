package br.com.jp.servicos.builders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.utils.DataUtils;

public class LocacaoBuilder {
	
	private Locacao locacao;
	
	public static LocacaoBuilder umaLocacao() {
		
		LocacaoBuilder builder = new LocacaoBuilder();
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		double valorLocacao = 0d;
		
		for (Filme filme : filmes) {
			valorLocacao += filme.getPrecoLocacao();
		}
		
		builder.locacao = new Locacao(usuario, filmes, new Date(), DataUtils.obterDataComDiferencaDias(1), valorLocacao);
		
		return builder;
	}
	
	public LocacaoBuilder comDataRetorno(Date dataReorno) {
		locacao.setDataRetorno(dataReorno);
		return this;
	}
	
	
	public LocacaoBuilder atrasado() {
		locacao.setDataLocacao(DataUtils.obterDataComDiferencaDias(-4));
		locacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(-2));
		
		return this;
	}
	
	public LocacaoBuilder comUsuario(Usuario usuario) {
		locacao.setUsuario(usuario);
		return this;
	}
	
	public Locacao agora() {
		
		return locacao;
	}

}
