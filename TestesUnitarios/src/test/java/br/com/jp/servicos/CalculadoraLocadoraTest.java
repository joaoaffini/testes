package br.com.jp.servicos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import br.com.jp.entidades.Filme;
import br.com.jp.entidades.Locacao;
import br.com.jp.entidades.Usuario;
import br.com.jp.exceptions.FilmeSemEstoqueException;
import br.com.jp.exceptions.LocadoraException;

@RunWith(Parameterized.class)
public class CalculadoraLocadoraTest {
	
	private LocacaoService service;
	
	private static Filme filme1 = new Filme("Filme 1", 5, 4.0);
	private static Filme filme2 = new Filme("Filme 2", 5, 4.0);
	private static Filme filme3 = new Filme("Filme 3", 5, 4.0);
	private static Filme filme4 = new Filme("Filme 4", 5, 4.0);
	private static Filme filme5 = new Filme("Filme 5", 5, 4.0);
	private static Filme filme6 = new Filme("Filme 6", 5, 4.0);
	
	@Parameter
	public List<Filme> filmes;
	
	//o value = 1 serve para indicar a ordem do parametro no filmes o value por default eh 0
	@Parameter(value = 1)
	public Double valorLocacao;
	
	@Parameter(value = 2)
	public String cenario;
	
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	//cada item no array dispara uma execucao dos testes que utilizam os parametros
	@Parameters(name = "{2}")
	public static Collection<Object[]> getParametros() {
		return Arrays.asList(new Object [][] {
			{Arrays.asList(filme1, filme2), 8.0, "2 Filmes Sem Desconto"},
			{Arrays.asList(filme1, filme2, filme3), 11.0, "3 Filmes 25%"},
			{Arrays.asList(filme1, filme2, filme3, filme4), 13.0, "4 Filmes 50%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5), 14.0, "5 Filmes 75%"},
			{Arrays.asList(filme1, filme2, filme3, filme4, filme5, filme6), 14.0, "6 Filmes 100%"}
		});
	}
	
	@Test()
	public void deveCalcularValorLocacaoConsiderandoDescontos() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		Usuario usuario = new Usuario("Usuario 1");
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//resultado
		assertThat(resultado.getValor(), is(valorLocacao));
	}
	
	@Test
	public void print() {
		System.out.println(valorLocacao);
	}
}
