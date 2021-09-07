package br.com.jp.servicos;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * 
 * @author joaop
 * Preciso garantir a ordem de execucao dos testes porem o junit nao consegue garantir isso automaticamente
 * com a anotacao @FixMethodOrder(MethodSorters.NAME_ASCENDING) = os metodos sao executados em ordem alfabetica
 * de acordo com o nome do metodo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrdemTest {
	
	private static int contador = 0;
	
	@Test
	public void t1_inicia() {
		contador = 1;
	}
	
	@Test
	public void t2_verifica() {
		Assert.assertEquals(1, contador);
	}
	
	/**
	 * Uma forma de resolver o problema seria essa
	 * os metodos inicia e verifica deixam de estar anotados com @Test
	 * e sao chamados em um outro metodo que chama os metodos na ordem desejada
	 * desta forma perde a rastreabilidade do teste
	 */
	
//	@Test
//	public void testeGeral() {
//		inicia();
//		verifica();
//	}
}
