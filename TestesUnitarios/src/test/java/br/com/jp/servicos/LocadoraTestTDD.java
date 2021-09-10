package br.com.jp.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;
import br.com.jp.servicos.builders.FilmeBuilder;
import br.com.jp.servicos.builders.UsuarioBuilder;
import br.com.jp.servicos.matchers.MatchersProprios;
import br.com.jp.utils.DataUtils;

public class LocadoraTestTDD {
	
	private LocacaoService service;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Before
	public void setup() {
		
		service = new LocacaoService();
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		//Se o dia da semana for sabado, este teste nao sera executado
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Filme filme = FilmeBuilder.umFilme().comValor(5.0).agora();
		List<Filme> filmes = new ArrayList<Filme>();
		filmes.add(filme);
		
		//acao
		Locacao locacao;
		locacao = service.alugarFilme(usuario, filmes);
			
		//verificacao
			
		//ErrorCollector
		error.checkThat(locacao.getValor(), is(equalTo(5.0)));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
	}
	
	@Test
	public void devePagar75PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(11.00));
	}
	
	@Test
	public void devePagar50PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		Filme filme4 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3, filme4);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(13.00));
	}
	
	@Test
	public void devePagar25PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		Filme filme4 = FilmeBuilder.umFilme().agora();
		Filme filme5 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario  = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3, filme4, filme5);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.00));
	}
	
	@Test
	public void devePagar0PcFilme() throws FilmeSemEstoqueException, LocadoraException {
		
		Filme filme1 = FilmeBuilder.umFilme().agora();
		Filme filme2 = FilmeBuilder.umFilme().agora();
		Filme filme3 = FilmeBuilder.umFilme().agora();
		Filme filme4 = FilmeBuilder.umFilme().agora();
		Filme filme5 = FilmeBuilder.umFilme().agora();
		Filme filme6 = FilmeBuilder.umFilme().agora();
		
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6);
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(14.00));
	}
	
	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws FilmeSemEstoqueException, LocadoraException {
		
		//Se o dia da semana for sabado, este teste SERA executado
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Filme filme1 = FilmeBuilder.umFilme().agora();
		List<Filme> filmes = Arrays.asList(filme1);
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		//acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificacao
		
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
		
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEmUmaSegunda());
		
	}

}
