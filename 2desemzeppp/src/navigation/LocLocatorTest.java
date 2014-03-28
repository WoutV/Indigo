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
}
