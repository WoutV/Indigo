package navigation;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import map.Map;
import map.Symbol;

import org.junit.Test;

public class LocLocatorTest {

	@Test
	public void alphaminus60() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("RS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("WR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("WS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(-60,alpha,1);
	}
	
	@Test
	public void alpha0() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("BR");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("BR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("RS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(0,alpha,1);
	}
	
	@Test
	public void alpha60() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("WS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("YR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("RR");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(60,alpha,1);
	}
	
	@Test
	public void alpha120() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("RR");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("WS");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("WS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(120,alpha,1);
	}
	
	@Test
	public void alphaminus120() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("WS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("WS");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("RR");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(-120,alpha,1);
	}
	
	@Test
	public void alpha180() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("YR");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("WS");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("BS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(180,alpha,1);
	}
	
	@Test
	public void alpha60outlier() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("WS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("YR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("RR");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		
		Symbol s21 = new Symbol("RS");
		s21.setX(40);s21.setY(32);
		list2.add(s21);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double x = loc0[0];
		assertEquals(84,x,1);
		double y = loc0[1];
		assertEquals(73,y,1);
		double alpha = loc0[2];
		assertEquals(60,alpha,1);
	}
	
	@Test
	public void with4Symbols() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("RS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("WR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("WS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		Symbol s21 = new Symbol("YR");
		s21.setX(70);s21.setY(50);
		list2.add(s21);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double x = loc0[0];
		assertEquals(63,x,1);
		double y = loc0[1];
		assertEquals(182,y,1);
		double alpha = loc0[2];
		assertEquals(-60,alpha,1);
	}
	
	@Test
	public void alphaminus56() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("RS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("WR");
		s10.setX(38);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("WS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(-56,alpha,1);
	}
	
	@Test
	public void with5Symbols() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("RS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("WR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("WS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		Symbol s21 = new Symbol("YR");
		s21.setX(70);s21.setY(50);
		list2.add(s21);
		Symbol s22 = new Symbol("RS");
		s22.setX(60);s21.setY(32);
		list2.add(s22);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double x = loc0[0];
		assertEquals(63,x,1);
		double y = loc0[1];
		assertEquals(182,y,1);
		double alpha = loc0[2];
		assertEquals(-60,alpha,1);
	}
	
	@Test
	public void alpha90() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("BR");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("BR");
		s10.setX(68);s10.setY(60);
		list2.add(s10);
		Symbol s20 = new Symbol("RS");
		s20.setX(68);s20.setY(40);
		list2.add(s20);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double alpha = loc0[2];
		assertEquals(90,alpha,1);
	}
	
	@Test
	public void with7Symbols() {
		List<Symbol> list2 = new LinkedList<>();
		Symbol center0 = new Symbol("WS");
		center0.setX(50);center0.setY(50);
		list2.add(center0);
		Symbol s10 = new Symbol("YR");
		s10.setX(40);s10.setY(68);
		list2.add(s10);
		Symbol s20 = new Symbol("RS");
		s20.setX(60);s20.setY(68);
		list2.add(s20);
		Symbol s21 = new Symbol("WR");
		s21.setX(70);s21.setY(50);
		list2.add(s21);
		Symbol s22 = new Symbol("BS");
		s22.setX(60);s22.setY(32);
		list2.add(s22);
		Symbol s23 = new Symbol("YR");
		s23.setX(40);s23.setY(32);
		list2.add(s23);
		Symbol s24 = new Symbol("BR");
		s24.setX(30);s24.setY(50);
		list2.add(s24);
		LocationLocator locator = new LocationLocator(new Map("/shapesDemo.csv"),null,null,null);
		double[] loc0 = locator.locate(list2);
		double x = loc0[0];
		assertEquals(42,x,1);
		double y = loc0[1];
		assertEquals(145,y,1);
		double alpha = loc0[2];
		assertEquals(120,alpha,1);
	}
}
