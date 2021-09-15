package br.com.jp.servicos;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import br.com.jp.dao.LocacaoDAO;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({LocacaoService.class})
public class LocadoraTestTDDPowerMock {
	
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
		service = PowerMockito.spy(service);
		
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
		//Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//resolvendo o problema com powermock
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(28, 4, 2017));
		
		//Mockando metodo estatico
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 28);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		
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
		error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), DataUtils.obterData(28, 4, 2017)), is(true));
		error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterData(29, 4, 2017)), is(true));
		
		//verificar quantidade de chamadas em metodos estaticos
		PowerMockito.verifyStatic(Mockito.times(2));
		Calendar.getInstance();
	}
	
	@Test
	public void naoDeveDevolverFilmeNoDomingo() throws Exception {
		
		//Se o dia da semana for sabado, este teste SERA executado
		//Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//utilizando o powermock para setar sempre sabado no date qdo executar este teste
		//PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(DataUtils.obterData(29, 4, 2017));
		
		//cenario
		Filme filme1 = FilmeBuilder.umFilme().agora();
		List<Filme> filmes = Arrays.asList(filme1);
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 29);
		calendar.set(Calendar.MONTH, Calendar.APRIL);
		calendar.set(Calendar.YEAR, 2017);
		
		PowerMockito.mockStatic(Calendar.class);
		PowerMockito.when(Calendar.getInstance()).thenReturn(calendar);
		//acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificacao
		
		
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
		
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEm(Calendar.MONDAY));
		assertThat(retorno.getDataRetorno(), MatchersProprios.caiEmUmaSegunda());
		
	}
	
	
	@Test
	public void deveTratarErroSPC() throws Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());

		Mockito.when(spcService.possuiNegativacao(usuario)).thenThrow(new Exception("Falha Catastrófica"));
		
		//verificacao
		expectedException.expect(LocadoraException.class);
		expectedException.expectMessage("Problemas com SPC, tente novamente");
		
		//acao
		service.alugarFilme(usuario, filmes);
		
		
	}
	
	@Test
	public void deveAlugarFilmeSemCalcularValor() throws Exception {
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//mock de metodo privado utilizando o spy do powermock
		//no metodo setup esta configurado o spy para a classe service
		PowerMockito.doReturn(1.0).when(service, "calcularValorLocacao", filmes);
		
		
		//acao
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		//verificacao
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(1.0));
		
		//verificar se um metodo privado foi chamado
		PowerMockito.verifyPrivate(service).invoke("calcularValorLocacao", filmes);
	}
	
	@Test
	public void deveCalcularValorLocacao() throws Exception {
		//cenario
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//acao
		//executando metodo privado atraves do powermock
		//lembrando que o mockito tbm possui o Whitebox, mas apenas o do powermock consegue 
		//executar um metodo privado
		Double valorLocacao = (Double) Whitebox.invokeMethod(service, "calcularValorLocacao", filmes);
		
		//verificacao
		Assert.assertThat(valorLocacao, CoreMatchers.is(4.0));
		
	}

}
