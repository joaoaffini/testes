package br.com.jp.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.jp.dao.LocacaoDAO;
import br.com.jp.dao.LocacaoDAOFake;
import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;
import br.com.jp.servicos.builders.FilmeBuilder;
import br.com.jp.servicos.builders.LocacaoBuilder;
import br.com.jp.servicos.builders.UsuarioBuilder;
import br.com.jp.servicos.matchers.MatchersProprios;
import br.com.jp.utils.DataUtils;

public class LocadoraTestTDD {
	
	@InjectMocks
	private LocacaoService service;
	
	
	@Mock
	private SPCService spcService;
	@Mock
	private LocacaoDAO dao;
	@Mock
	private EmailService emailService;
	
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Before
	public void setup() {
		
		MockitoAnnotations.initMocks(this);
		
//		service = new LocacaoService();
//		dao = Mockito.mock(LocacaoDAO.class);
//		//LocacaoDAO dao = new LocacaoDAOFake();
//		service.setLocacaoDao(dao);
//		spcService = Mockito.mock(SPCService.class);
//		service.setSPCService(spcService);
//		emailService = Mockito.mock(EmailService.class);
//		service.setEmailService(emailService);
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
	
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		//Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario 2").agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//Mockito.any(Usuario.class) = significa que o mock retorna true para qualquer usuario
		Mockito.when(spcService.possuiNegativacao(Mockito.any(Usuario.class))).thenReturn(true);
		
		//retirado este codigo para tratar com try catch e poder continuar a execucao do teste apos o erro lancado
		//expectedException.expect(LocadoraException.class);
		//expectedException.expectMessage("Usuário Negativado");
		
		//acao
		try {
			service.alugarFilme(usuario, filmes);
			//caso o teste nao lance uma exception o teste deve falhar e nao retornar um falso positivo
			Assert.fail();
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário Negativado"));
		}
		
		//verficacao
		Mockito.verify(spcService).possuiNegativacao(usuario);
	}
	
	@Test
	public void deveEnviarEmailLocacoesAtrasadas() {
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Usuario3").agora();
		List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario).agora(),
											   LocacaoBuilder.umaLocacao().comUsuario(usuario2).agora(),
											   LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario3).agora(),
											   LocacaoBuilder.umaLocacao().atrasado().comUsuario(usuario3).agora());
		
		//Quando o metodo dao.obterLocacoesPendentes() ele vai retornar a list locacoes
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		//Verifica se foi chamado 3 vezes para um usuario
		Mockito.verify(emailService, times(3)).notificarAtraso(Mockito.any(Usuario.class));
		Mockito.verify(emailService).notificarAtraso(usuario);
		Mockito.verify(emailService, Mockito.atLeastOnce()).notificarAtraso(usuario3);
		Mockito.verify(emailService, never()).notificarAtraso(usuario2);
		
		//significa que se algum usuario que nao estiver no verify acima entrou no metodo para enviar email
		//ele vai dar erro nos testes
		Mockito.verifyNoMoreInteractions(emailService);
		
	}

}
